package edu.uoc.b2.tema4.ex03;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Tema 4 — Exercici 03 JDBC Transaccions (MySQL)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Exercici03TraspasoTest {

    private Connection conn;

    @BeforeEach
    void setUp() throws Exception {
        String url = "jdbc:mysql://localhost:3306/uoc_test?allowMultiQueries=true&serverTimezone=UTC";
        conn = DriverManager.getConnection(url, "root", "root");

        try (java.io.InputStream is = getClass().getResourceAsStream("/schema_ex03.sql")) {
            if (is == null)
                throw new RuntimeException("No se encuentra schema_ex03.sql");
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

    private double getSaldo(int id) throws Exception {
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT saldo FROM cuentas WHERE id = " + id)) {
            if (rs.next())
                return rs.getDouble(1);
            return 0;
        }
    }

    @Test
    @DisplayName("traspasar: caso éxito (ambos saldos se actualizan y commit)")
    void traspaso_ok() throws Exception {
        // ID 1 -> 500, ID 2 -> 300
        TraspasoBancario.traspasar(conn, 1, 2, 100);

        assertThat(getSaldo(1)).isEqualTo(400);
        assertThat(getSaldo(2)).isEqualTo(400);
    }

    @Test
    @DisplayName("traspasar: falta saldo lanza excepcion y rollback")
    void traspaso_falta_saldo() throws Exception {
        // ID 1 -> 500
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            TraspasoBancario.traspasar(conn, 1, 2, 600);
        });

        // Asegurarse de que ROLLBACK funcionó - nadie cobró / pagó nada
        assertThat(getSaldo(1)).isEqualTo(500); // Intactos
        assertThat(getSaldo(2)).isEqualTo(300); // Intactos
    }

    @Test
    @DisplayName("traspasar: devuelve con autoCommit a true después de la llamada")
    void traspaso_restaura_autocommit() throws Exception {
        TraspasoBancario.traspasar(conn, 1, 2, 50);
        assertThat(conn.getAutoCommit()).isTrue();
    }
}
