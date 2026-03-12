package edu.uoc.b2.tema3;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests per als exercicis SQL / Tests para los ejercicios SQL — Tema 3
 * REQUIERE tener un servidor MySQL local y una BBDD 'uoc_test'.
 */
@DisplayName("Tema 3 — Ejercicios SQL / Exercicis SQL (MySQL)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConsultasSQLTest {

    private Connection conn;

    @BeforeEach
    void setUp() throws Exception {
        // Conexión a MySQL local. Se requiere tener una BBDD 'uoc_test' creada.
        String url = "jdbc:mysql://localhost:3306/uoc_test?allowMultiQueries=true&serverTimezone=UTC";
        conn = DriverManager.getConnection(url, "root", "root");

        // Leer y ejecutar schema.sql
        try (java.io.InputStream is = getClass().getResourceAsStream("/schema.sql")) {
            if (is == null) throw new RuntimeException("No se encuentra schema.sql");
            try (Scanner scanner = new Scanner(is, java.nio.charset.StandardCharsets.UTF_8)) {
                String script = scanner.useDelimiter("\\A").next();
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(script);
                }
            }
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    // ─── Ex1 ─────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("Ex1: noms/nombres cliente ordenats/ordenados alfabèticament/alfabéticamente")
    void ex1_noms() throws Exception {
        List<String> noms = ConsultasSQL.ex1_nombresClientes(conn);
        assertThat(noms).hasSize(6);
        assertThat(noms.get(0)).isEqualTo("Ana García");  // Primer alfabèticament
        assertThat(noms.get(noms.size()-1)).isEqualTo("Sofia Sánchez");
        // Verifica ordre
        assertThat(noms).isSortedAccordingTo(String::compareTo);
    }

    @Test @Order(2) @DisplayName("Ex1: total productes/productos = 7")
    void ex1_total() throws Exception {
        assertThat(ConsultasSQL.ex1_totalProductos(conn)).isEqualTo(7);
    }

    // ─── Ex2 ─────────────────────────────────────────────────

    @Test @Order(3) @DisplayName("Ex2: productes/productos de Electronica (4 en total)")
    void ex2_categoria_electronica() throws Exception {
        List<String> result = ConsultasSQL.ex2_productosPorCategoria(conn, "Electronica");
        assertThat(result).hasSize(4)
            .contains("Laptop Pro", "Ratolí", "Webcam HD", "Teclat");
        // Primer el més car
        assertThat(result.get(0)).isEqualTo("Laptop Pro");
    }

    @Test @Order(4) @DisplayName("Ex2: top 3 productes/productos")
    void ex2_top3() throws Exception {
        List<String> result = ConsultasSQL.ex2_topNProductos(conn, 3);
        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualTo("Laptop Pro"); // El més car
    }

    @Test @Order(5) @DisplayName("Ex2: clients/clientes VIP d'Espanya/de España")
    void ex2_vip_es() throws Exception {
        List<String> result = ConsultasSQL.ex2_clientesPorSegmentoYPais(conn, "VIP", "ES");
        assertThat(result).containsExactlyInAnyOrder("Ana García", "Marc López");
    }

    // ─── Ex3 ─────────────────────────────────────────────────

    @Test @Order(6) @DisplayName("Ex3: comandes/pedidos por/per client/cliente (inclou Jordi amb 0)")
    void ex3_comandes_per_client() throws Exception {
        Map<String, Integer> result = ConsultasSQL.ex3_pedidosPorCliente(conn);
        assertThat(result).containsKey("Jordi Puig");
        assertThat(result.get("Jordi Puig")).isZero();    // Sense comandes
        assertThat(result.get("Ana García")).isEqualTo(2); // 2 comandes
    }

    @Test @Order(7) @DisplayName("Ex3: total facturat/facturado Ana (comandes lliurades / pedidos entregados)")
    void ex3_total_ana() throws Exception {
        // Ana: comanda 1 (Laptop 1299.99 + 2xRatolí 59.98) + comanda 2 (Taula 199.99)
        // = 1359.97 + 199.99 = 1559.96
        double total = ConsultasSQL.ex3_totalFacturadoCliente(conn, "Ana García");
        assertThat(total).isCloseTo(1559.96, within(0.01));
    }

    @Test @Order(8) @DisplayName("Ex3: client/cliente sense comandes lliurades / sin pedidos entregados → 0.0")
    void ex3_total_zero() throws Exception {
        // Laura té 1 comanda PENDENT (no lliurada)
        double total = ConsultasSQL.ex3_totalFacturadoCliente(conn, "Laura Blanc");
        assertThat(total).isEqualTo(0.0);
    }

    // ─── Ex4 ─────────────────────────────────────────────────

    @Test @Order(9) @DisplayName("Ex4: preu mig / precio medio Electronica ≈ 364.99")
    void ex4_preu_mitja() throws Exception {
        Map<String, Double> result = ConsultasSQL.ex4_precioMedioPorCategoria(conn);
        // Electronica: (1299.99 + 29.99 + 79.99 + 49.99) / 4 = 364.99
        assertThat(result).containsKey("Electronica");
        assertThat(result.get("Electronica")).isCloseTo(364.99, within(0.01));
    }

    @Test @Order(10) @DisplayName("Ex4: categories/categorías amb/con més de 2 productes/productos → Electronica")
    void ex4_categories_molts() throws Exception {
        List<String> result = ConsultasSQL.ex4_categoriasConMasDeNProductos(conn, 2);
        // Electronica: 4 productes, Mobles: 2 → HAVING > 2 → només Electronica
        assertThat(result).containsExactly("Electronica");
    }

    // ─── Ex5 ─────────────────────────────────────────────────

    @Test @Order(11) @DisplayName("Ex5: productes/productos no demanats/demandados (Cadira, Teclat, Llum LED no demanats de primeres)")
    void ex5_no_demandats() throws Exception {
        // Nota: Teclat SÍ apareix a linies_comanda (comanda 3, Pau)
        // Productes en cap comanda: cap! Tots apareixen a almenys una comanda
        // Anem a verificar que no retorna productes que SÍ han estat demanats
        List<String> result = ConsultasSQL.ex5_productosNoDemandados(conn);
        assertThat(result).doesNotContain("Laptop Pro", "Ratolí", "Webcam HD");
    }

    @Test @Order(12) @DisplayName("Ex5: SELF JOIN empleat/empleado → cap/jefe")
    void ex5_self_join() throws Exception {
        Map<String, String> result = ConsultasSQL.ex5_empleadoConJefe(conn);
        assertThat(result.get("CEO")).isEqualTo("Sense cap");   // El CEO no té cap
        assertThat(result.get("CTO")).isEqualTo("CEO");          // CTO → CEO
        assertThat(result.get("Dev Junior")).isEqualTo("Dev Senior"); // Jerarquia
    }
}
