package edu.uoc.b2.tema4.ex01;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Ex01 JDBC — ProductoDAO CRUD (MySQL)")
class Exercici01ProductoDAOTest {

    private Connection conn;
    private ProductoDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        // Conexión a MySQL local. Se requiere tener una BBDD 'uoc_test' creada.
        String url = "jdbc:mysql://localhost:3306/uoc_test?allowMultiQueries=true&serverTimezone=UTC";
        conn = DriverManager.getConnection(url, "root", "root");

        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS productos");
            st.execute("""
                    CREATE TABLE productos (
                        id    INT PRIMARY KEY AUTO_INCREMENT,
                        nombre VARCHAR(200) NOT NULL,
                        precio DECIMAL(10,2) NOT NULL,
                        stock  INT DEFAULT 0
                    )""");
        }
        dao = new ProductoDAO(conn);
    }

    @AfterEach
    void tearDown() throws Exception {
        conn.close();
    }

    @Test
    @DisplayName("insertar: retorna un ID positivo")
    void inserir_retorna_id() throws Exception {
        int id = dao.insertar("Laptop", 999.99, 10);
        assertThat(id).isPositive();
    }

    @Test
    @DisplayName("insertar: IDs consecutivos para inserciones seguidas")
    void inserir_ids_consecutius() throws Exception {
        int id1 = dao.insertar("A", 10.0, 1);
        int id2 = dao.insertar("B", 20.0, 2);
        assertThat(id2).isGreaterThan(id1);
    }

    @Test
    @DisplayName("buscarPorId: producto existente → Present")
    void cercar_existent() throws Exception {
        int id = dao.insertar("Ratón", 29.99, 50);
        Optional<ProductoDAO.Producto> p = dao.buscarPorId(id);
        assertThat(p).isPresent();
        assertThat(p.get().nombre()).isEqualTo("Ratón");
        assertThat(p.get().precio()).isEqualTo(29.99);
        assertThat(p.get().stock()).isEqualTo(50);
    }

    @Test
    @DisplayName("buscarPorId: ID inexistente → Empty")
    void cercar_inexistent() throws Exception {
        assertThat(dao.buscarPorId(9999)).isEmpty();
    }

    @Test
    @DisplayName("listarTodos: retorna todos ordenados por nombre")
    void llistar_tots() throws Exception {
        dao.insertar("Zebra", 1.0, 1);
        dao.insertar("Alpha", 2.0, 2);
        dao.insertar("Mig", 3.0, 3);
        List<ProductoDAO.Producto> llista = dao.listarTodos();
        assertThat(llista).hasSize(3);
        assertThat(llista.get(0).nombre()).isEqualTo("Alpha");
        assertThat(llista.get(2).nombre()).isEqualTo("Zebra");
    }

    @Test
    @DisplayName("actualizarPrecio: precio cambiado correctamente")
    void actualitzar_preu() throws Exception {
        int id = dao.insertar("Test", 100.0, 1);
        boolean ok = dao.actualizarPrecio(id, 150.0);
        assertThat(ok).isTrue();
        assertThat(dao.buscarPorId(id).get().precio()).isEqualTo(150.0);
    }

    @Test
    @DisplayName("actualizarPrecio: ID inexistente → false")
    void actualitzar_inexistent() throws Exception {
        assertThat(dao.actualizarPrecio(9999, 50.0)).isFalse();
    }

    @Test
    @DisplayName("eliminar: producto borrado correctamente")
    void eliminar_ok() throws Exception {
        int id = dao.insertar("Temp", 5.0, 1);
        assertThat(dao.eliminar(id)).isTrue();
        assertThat(dao.buscarPorId(id)).isEmpty();
    }

    @Test
    @DisplayName("eliminar: ID inexistente → false")
    void eliminar_inexistent() throws Exception {
        assertThat(dao.eliminar(9999)).isFalse();
    }

    @Test
    @DisplayName("contar: refleja inserciones y eliminaciones")
    void comptar() throws Exception {
        assertThat(dao.contar()).isZero();
        dao.insertar("A", 1.0, 1);
        dao.insertar("B", 2.0, 2);
        assertThat(dao.contar()).isEqualTo(2);
        int id = dao.insertar("C", 3.0, 3);
        dao.eliminar(id);
        assertThat(dao.contar()).isEqualTo(2);
    }
}
