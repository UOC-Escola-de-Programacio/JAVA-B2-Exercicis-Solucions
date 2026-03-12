package edu.uoc.b2.tema4.ex01;

import java.sql.*;
import java.util.*;

/**
 * ============================================================
 * EXERCICIO 01 JDBC / EXERCICI 01 JDBC — CRUD DAO bàsic / básico ⭐⭐ (Fácil /
 * Fàcil)
 * ============================================================
 *
 * [CAT] OBJECTIU: Implementa un DAO (Data Access Object) complet per a la
 * entitat Producte.
 * [ES] OBJETIVO: Implementa un DAO (Data Access Object) completo para la
 * entidad Producto.
 *
 * SCHEMA (ja creat pels tests / ya creado por los tests):
 * CREATE TABLE productos (
 * id INT PRIMARY KEY AUTO_INCREMENT,
 * nombre VARCHAR(200) NOT NULL,
 * precio DECIMAL(10,2) NOT NULL,
 * stock INT DEFAULT 0
 * );
 */
public class ProductoDAO {

    private final Connection conn;

    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    // Record que representa un Producto
    public record Producto(Integer id, String nombre, double precio, int stock) {
    }

    /**
     * [CAT] Insereix un nou producte i retorna el seu ID generat automàticament.
     * [ES] Inserta un nuevo producto y devuelve su ID generado automáticamente.
     *
     * @param nombre nom del producte / nombre del producto
     * @param precio preu / precio (> 0)
     * @param stock  estoc inicial / stock inicial (>= 0)
     * @return l'ID generat per la base de dades / el ID generado por la base de
     *         datos
     * @throws SQLException si hi ha un error de BD / si hay un error de BD
     */
    public int insertar(String nombre, double precio, int stock) throws SQLException {
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.setInt(3, stock);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    /**
     * [CAT] Cerca un producte per ID.
     * [ES] Busca un producto por ID.
     *
     * @param id l'ID del producte / el ID del producto
     * @return Optional amb el Producto si existeix, o empty() si no / Optional con
     *         el Producto si existe, o empty() si no
     */
    public Optional<Producto> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"),
                            rs.getInt("stock")));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * [CAT] Retorna tots els productes ordenats per nom.
     * [ES] Devuelve todos los productos ordenados por nombre.
     */
    public List<Producto> listarTodos() throws SQLException {
        String sql = "SELECT * FROM productos ORDER BY nombre";
        List<Producto> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"),
                        rs.getInt("stock")));
            }
        }
        return list;
    }

    /**
     * [CAT] Actualitza el preu d'un producte existent.
     * [ES] Actualiza el precio de un producto existente.
     *
     * @return true si s'ha actualitzat (hi havia fila afectada), false si l'ID no
     *         existia / true si se ha actualizado, false si no
     */
    public boolean actualizarPrecio(int id, double nuevoPrecio) throws SQLException {
        String sql = "UPDATE productos SET precio = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, nuevoPrecio);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * [CAT] Elimina un producte per ID.
     * [ES] Elimina un producto por ID.
     *
     * @return true si s'ha eliminat, false si no existia / true si se ha eliminado,
     *         false si no existía
     */
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * [CAT] Retorna el nombre total de productes a la taula.
     * [ES] Devuelve el número total de productos en la tabla.
     */
    public int contar() throws SQLException {
        String sql = "SELECT COUNT(*) FROM productos";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
