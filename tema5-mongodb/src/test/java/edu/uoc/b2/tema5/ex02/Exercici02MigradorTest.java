package edu.uoc.b2.tema5.ex02;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.bson.Document;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tema 5 — Exercici 02 Migració SQL a MongoDB (H2 in memory simulat)")
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Exercici02MigradorTest {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0"));

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDB;

    // Simula origen MySQL
    private MysqlDataSource mysqlDS;
    private Connection sqlConn;

    @BeforeAll
    static void initContainer() {
        mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl());
        mongoDB = mongoClient.getDatabase("migracion_db");
    }

    @AfterAll
    static void closeContainer() {
        if (mongoClient != null)
            mongoClient.close();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Configurarem connexió local a MySQL (simulada)
        // NOTA: Es requereix MySQL local instal·lat per al DataSource (ja fet servir en
        // T4).
        String mysqlUrl = "jdbc:mysql://localhost:3306/uoc_test?allowMultiQueries=true&serverTimezone=UTC";
        sqlConn = DriverManager.getConnection(mysqlUrl, "root", "root");

        // Executa script base
        try (java.io.InputStream is = getClass().getResourceAsStream("/schema_ex02.sql")) {
            if (is != null) {
                try (Scanner scanner = new Scanner(is, java.nio.charset.StandardCharsets.UTF_8)) {
                    String script = scanner.useDelimiter("\\A").next();
                    try (Statement stmt = sqlConn.createStatement()) {
                        stmt.execute(script);
                    }
                }
            }
        }

        mysqlDS = new MysqlDataSource();
        mysqlDS.setURL(mysqlUrl);
        mysqlDS.setUser("root");
        mysqlDS.setPassword("root");
    }

    @AfterEach
    void tearDown() throws Exception {
        if (sqlConn != null)
            sqlConn.close();
    }

    @Test
    @DisplayName("migrar: Transforma clientes, pedidos y líneas a coleccion Mongo")
    void comprobarMigracion() throws Exception {
        MigradorSQLMongo migrador = new MigradorSQLMongo(mysqlDS, mongoDB);
        migrador.migrar();

        MongoCollection<Document> clientesMongo = mongoDB.getCollection("clientes_migrados");

        // S'haurien d'haver migrat 6 clients de l'esquema
        assertThat(clientesMongo.countDocuments()).isEqualTo(6);

        // Ana García la coneixem perquè té ID 1 a schema.sql
        Document anaDocs = clientesMongo.find(new Document("cliente_sql_id", 1)).first();
        assertThat(anaDocs).isNotNull();
        assertThat(anaDocs.getString("nombre")).isEqualTo("Ana García");

        // Ana hauria de tenir 2 comandes (pedidos) segons schema.sql
        List<Document> pedidosAna = anaDocs.getList("pedidos", Document.class);
        assertThat(pedidosAna).isNotNull().hasSize(2);

        // Cada pedido hauria de portar les seves linies (productes comprats)
        List<Document> lineasPedido1 = pedidosAna.get(0).getList("lineas", Document.class);
        assertThat(lineasPedido1).isNotEmpty();
    }
}
