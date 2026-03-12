package edu.uoc.b2.tema5.ex02;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================
 * EXERCICI 02 MongoDB / EJERCICIO 02 MongoDB — Migración / Migració SQL a
 * MongoDB ⭐⭐⭐
 * ============================================================
 *
 * [CAT] OBJECTIU: Implementa el migrador llegint dades agrupades de MySQL via
 * JDBC (DataSource)
 * per inserir-les estructurades a MongoDB.
 * [ES] OBJETIVO: Implementa el migrador leyendo datos agrupados de MySQL vía
 * JDBC (DataSource)
 * para insertarlos estructurados en MongoDB.
 *
 * ESTRUCTURA DESEADA MONGODB / ESTRUCTURA DESITJADA MONGODB:
 * {
 * "cliente_sql_id": 1,
 * "nombre": "Ana García", "email": "ana@test.com", "pais": "ES",
 * "pedidos": [
 * { "pedido_sql_id": 1, "estado": "LLIURAT",
 * "lineas": [
 * {"producto_sql_id": 1, "nombre_prod": "Laptop Pro", "cantidad": 1,
 * "precio_unitario": 1299.99}
 * ]
 * }
 * ]
 * }
 */
public class MigradorSQLMongo {

    private final DataSource mysqlDS;
    private final MongoDatabase mongoDB;

    public MigradorSQLMongo(DataSource mysql, MongoDatabase mongo) {
        this.mysqlDS = mysql;
        this.mongoDB = mongo;
    }

    public void migrar() throws SQLException {
        MongoCollection<Document> clientesMongo = mongoDB.getCollection("clientes_migrados");
        clientesMongo.drop(); // Limpia antes de migrar / Neteja abans de migrar

        Map<Integer, Document> clientesDocs = new HashMap<>();

        try (Connection conn = mysqlDS.getConnection()) {

            // 1. CARREGAR CLIENTS / CARGAR CLIENTES:
            String sqlClients = "SELECT id, nom, email, pais FROM clients";
            try (PreparedStatement stmt = conn.prepareStatement(sqlClients);
                    ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Document doc = new Document("cliente_sql_id", rs.getInt("id"))
                            .append("nombre", rs.getString("nom"))
                            .append("email", rs.getString("email"))
                            .append("pais", rs.getString("pais"))
                            .append("pedidos", new ArrayList<Document>());
                    clientesDocs.put(rs.getInt("id"), doc);
                }
            }

            // 2. CARREGAR COMANDES I LÍNIES / CARGAR PEDIDOS Y LÍNEAS:
            String sqlOrders = "SELECT o.id as comanda_id, o.client_id, o.estat, o.creat_el, " +
                    "lc.producte_id, p.nom AS prod_nom, lc.quantitat, lc.preu_unitari " +
                    "FROM comandes o " +
                    "JOIN linies_comanda lc ON o.id = lc.comanda_id " +
                    "JOIN productes p ON lc.producte_id = p.id";
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrders);
                    ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int clientId = rs.getInt("client_id");
                    Document clientDoc = clientesDocs.get(clientId);
                    if (clientDoc != null) {
                        List<Document> pedidos = clientDoc.getList("pedidos", Document.class);
                        int orderId = rs.getInt("comanda_id");

                        Document pedido = null;
                        for (Document pDoc : pedidos) {
                            if (pDoc.getInteger("pedido_sql_id") == orderId) {
                                pedido = pDoc;
                                break;
                            }
                        }

                        if (pedido == null) {
                            pedido = new Document("pedido_sql_id", orderId)
                                    .append("estado", rs.getString("estat"))
                                    .append("lineas", new ArrayList<Document>());
                            pedidos.add(pedido);
                        }

                        List<Document> lineas = pedido.getList("lineas", Document.class);
                        lineas.add(new Document("producto_sql_id", rs.getInt("producte_id"))
                                .append("nombre_prod", rs.getString("prod_nom"))
                                .append("cantidad", rs.getInt("quantitat"))
                                .append("precio_unitario", rs.getDouble("preu_unitari")));
                    }
                }
            }
        }

        // 3. INSERIR A MONGODB / INSERTAR EN MONGODB
        if (!clientesDocs.isEmpty()) {
            clientesMongo.insertMany(new ArrayList<>(clientesDocs.values()));
        }
    }
}
