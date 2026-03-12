package edu.uoc.b2.tema5.ex01;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * ============================================================
 * EXERCICI 01 MongoDB / EJERCICIO 01 MongoDB — Operaciones CRUD / Operacions
 * CRUD ⭐⭐
 * ============================================================
 *
 * [CAT] OBJECTIU: Implementa un repositori MongoDB per a la gestió de Productes
 * (documents BSON).
 * [ES] OBJETIVO: Implementa un repositorio MongoDB para la gestión de Productos
 * (documentos BSON).
 *
 * REGLES / REGLAS:
 * - Omple la classe amb la gestió requerida usant el controlador natiu
 * (MongoCollection).
 * - Llena la clase con la gestión requerida usando el controlador nativo
 * (MongoCollection).
 */
public class ProductoRepositorioMongo {

    private final MongoCollection<Document> col;

    public ProductoRepositorioMongo(MongoDatabase db) {
        this.col = db.getCollection("productos");
        this.col.createIndex(Indexes.text("nombre"));
        this.col.createIndex(Indexes.ascending("categoria", "precio"));
    }

    /**
     * [CAT] Insereix un producte al MongoDB i retorna el seu id generat (ObjectId).
     * [ES] Inserta un producto en MongoDB y retorna su id generado (ObjectId).
     */
    public ObjectId insertar(String nombre, String categoria, double precio, int stock) {
        if (nombre == null || nombre.trim().isEmpty() || precio <= 0 || stock < 0) {
            throw new IllegalArgumentException("Datos inválidos");
        }
        Document doc = new Document("nombre", nombre)
                .append("categoria", categoria)
                .append("precio", precio)
                .append("stock", stock)
                .append("activo", true)
                .append("creado", new Date());
        this.col.insertOne(doc);
        return doc.getObjectId("_id");
    }

    /**
     * [CAT] Cerca aplicant "text search" (cerca textual pel nom).
     * [ES] Búsqueda aplicando "text search" (búsqueda textual por el nombre).
     */
    public List<Document> buscarPorNombre(String textBusqueda) {
        return this.col.find(Filters.text(textBusqueda)).into(new ArrayList<>());
    }

    /**
     * [CAT] Llista productes actius (activo=true), ordenats i paginats.
     * [ES] Lista productos activos (activo=true), ordenados y paginados.
     */
    public List<Document> listar(int pagina, int tamañoPagina, String ordenarPorCampo) {
        int limit = Math.max(tamañoPagina, 10);
        int skip = (pagina - 1) * limit;
        if (skip < 0)
            skip = 0;
        return this.col.find(Filters.eq("activo", true))
                .sort(Sorts.ascending(ordenarPorCampo))
                .skip(skip)
                .limit(limit)
                .into(new ArrayList<>());
    }

    /**
     * [CAT] Actualitza el preu d'un producte segons ObjectId.
     * [ES] Actualiza el precio de un producto según ObjectId.
     */
    public boolean actualizarPrecio(ObjectId id, double nuevoPrecio) {
        if (nuevoPrecio <= 0) {
            throw new IllegalArgumentException("Precio inválido");
        }
        return this.col.updateOne(Filters.eq("_id", id), Updates.set("precio", nuevoPrecio)).getModifiedCount() > 0;
    }

    /**
     * [CAT] Genera estadístiques agrupant mitjançant Pipeline Aggregation.
     * [ES] Genera estadísticas agrupando mediante Pipeline Aggregation.
     */
    public Map<String, Object> estadisticasPorCategoria() {
        List<Document> stats = this.col.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("activo", true)),
                Aggregates.group("$categoria", Accumulators.sum("count", 1), Accumulators.avg("avg", "$precio"))))
                .into(new ArrayList<>());

        Map<String, Object> result = new HashMap<>();
        for (Document doc : stats) {
            String categoria = doc.getString("_id");
            Map<String, Object> innerMap = new HashMap<>();
            innerMap.put("count", doc.getInteger("count"));
            innerMap.put("avg", doc.getDouble("avg"));
            result.put(categoria, innerMap);
        }
        return result;
    }
}
