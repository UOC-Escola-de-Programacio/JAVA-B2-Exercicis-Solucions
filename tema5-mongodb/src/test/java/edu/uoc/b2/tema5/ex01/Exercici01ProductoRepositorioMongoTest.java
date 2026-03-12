package edu.uoc.b2.tema5.ex01;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests per als exercicis MongoDB / Tests para los ejercicios MongoDB — Tema 5,
 * Ex 01
 * Usa Testcontainers per aixecar / usa Testcontainers para levantar
 * un MongoDB real a memòria durant els tests / un MongoDB real en memoria
 * durante los tests
 */
@DisplayName("Tema 5 — Exercici 01 MongoDB (Testcontainers)")
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Exercici01ProductoRepositorioMongoTest {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0"));

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private ProductoRepositorioMongo repo;

    @BeforeAll
    static void initContainer() {
        String connectionString = mongoDBContainer.getReplicaSetUrl();
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase("test_bdd");
    }

    @AfterAll
    static void closeContainer() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @BeforeEach
    void setUp() {
        // Netejem la BD abans de cada test / Limpiamos la BD antes de cada test
        database.getCollection("productos").drop();
        repo = new ProductoRepositorioMongo(database);

        // Inserir dades base / Insertar datos base
        repo.insertar("Laptop Pro", "Electronica", 1299.99, 15);
        repo.insertar("Ratón Gamer", "Electronica", 79.99, 50);
        repo.insertar("Taula Ergonòmica", "Mobles", 399.99, 8);
        repo.insertar("Llum LED", "Llum", 24.99, 200);
        repo.insertar("Laptop Bàsic", "Electronica", 599.99, 20);
    }

    @Test
    @Order(1)
    @DisplayName("insertar: afegeix i retorna ObjectId vàlid / añade y retorna ObjectId válido")
    void probarInsertar() {
        ObjectId newId = repo.insertar("Monitor 4K", "Electronica", 250.00, 10);
        assertThat(newId).isNotNull();

        // Comprovar que s'ha inserit (6 productes ara)
        long count = database.getCollection("productos").countDocuments();
        assertThat(count).isEqualTo(6);
    }

    @Test
    @Order(2)
    @DisplayName("buscarPorNombre: cerca textual funcional / búsqueda textual funcional")
    void probarBuscador() {
        List<Document> docs = repo.buscarPorNombre("Laptop");
        assertThat(docs).isNotEmpty().hasSize(2);
        assertThat(docs.get(0).getString("nombre")).contains("Laptop");
    }

    @Test
    @Order(3)
    @DisplayName("listar: paginació / paginación (offset i limit)")
    void probarListado() {
        List<Document> page1 = repo.listar(1, 2, "precio");
        assertThat(page1).hasSize(2); // Dos más baratos
        assertThat(page1.get(0).getDouble("precio")).isEqualTo(24.99); // Llum LED

        List<Document> page2 = repo.listar(2, 2, "precio");
        assertThat(page2).hasSize(2); // Los siguientes dos
        assertThat(page2.get(0).getDouble("precio")).isEqualTo(399.99); // Taula
    }

    @Test
    @Order(4)
    @DisplayName("actualizarPrecio: canvia el preu del document / cambia el precio del documento")
    void probarActualizacion() {
        ObjectId pId = repo.insertar("Test Producto", "Test", 10.0, 1);
        boolean updated = repo.actualizarPrecio(pId, 25.0);
        assertThat(updated).isTrue();

        Document modificado = repo.listar(1, 10, "precio")
                .stream()
                .filter(doc -> doc.getObjectId("_id").equals(pId))
                .findFirst()
                .orElse(null);
        assertThat(modificado).isNotNull();
        assertThat(modificado.getDouble("precio")).isEqualTo(25.0);
    }

    @Test
    @Order(5)
    @DisplayName("estadisticasPorCategoria: agrupa i calcula sumes / agrupa y calcula sumas")
    void probarAgregaciones() {
        Map<String, Object> stats = repo.estadisticasPorCategoria();

        // Comprovem categories / Comprobamos categorías clave
        assertThat(stats).containsKey("Electronica").containsKey("Mobles").containsKey("Llum");

        // Objectes anidats com a Maps de la sortida
        @SuppressWarnings("unchecked")
        Map<String, Double> electrStats = (Map<String, Double>) stats.get("Electronica");

        assertThat(electrStats).isNotNull();
        // 3 productes ("Laptop Pro", "Ratón Gamer", "Laptop Bàsic")
        assertThat(electrStats.get("numProductes")).isEqualTo(3.0);
    }

}
