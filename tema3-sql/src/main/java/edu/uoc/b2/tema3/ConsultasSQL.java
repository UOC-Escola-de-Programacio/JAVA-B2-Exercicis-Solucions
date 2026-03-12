package edu.uoc.b2.tema3;

import java.sql.*;
import java.util.*;

/**
 * ============================================================
 * EXERCICIS SQL / EJERCICIOS SQL — Tema 3
 * ============================================================
 *
 * [CAT] INSTRUCCIONS GENERALS:
 * - En aquest tema NOMÉS has d'escriure la consulta SQL dins de la variable
 * `sql`.
 * - El codi JDBC ja està implementat per tu, no el modifiquis.
 * - Les consultes han de funcionar amb H2 (compatibles amb MySQL).
 *
 * [ES] INSTRUCCIONES GENERALES:
 * - En este tema SOLO tienes que escribir la consulta SQL dentro de la variable
 * `sql`.
 * - El código JDBC ya está implementado por ti, no lo modifiques.
 * - Las consultas deben funcionar con H2 (compatibles con MySQL).
 */
public class ConsultasSQL {

    // ──────────────────────────────────────────────────────────
    // EJERCICIO 1 / EXERCICI 1 ⭐ — SELECT bàsic / básico
    // ──────────────────────────────────────────────────────────

    /**
     * [CAT] Retorna els noms de tots els clients, ordenats alfabèticament.
     * [ES] Devuelve los nombres de todos los clientes, ordenados alfabéticamente.
     */
    public static List<String> ex1_nombresClientes(Connection conn) throws SQLException {
        String sql = "SELECT nom FROM clients ORDER BY nom ASC";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("nom"));
            }
        }
        return list;
    }

    /**
     * [CAT] Retorna el nombre total de productes a la base de dades.
     * [ES] Devuelve el número total de productos en la base de datos.
     */
    public static int ex1_totalProductos(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM productes";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // ──────────────────────────────────────────────────────────
    // EJERCICIO 2 / EXERCICI 2 ⭐⭐ — WHERE, ORDER BY, LIMIT
    // ──────────────────────────────────────────────────────────

    /**
     * [CAT] Retorna els noms dels productes de la categoria donada, ordenats per
     * preu descendent.
     * [ES] Devuelve los nombres de los productos de la categoría dada, ordenados
     * por precio descendente.
     */
    public static List<String> ex2_productosPorCategoria(Connection conn, String categoria)
            throws SQLException {
        String sql = "SELECT nom FROM productes WHERE categoria = ? ORDER BY preu DESC";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("nom"));
                }
            }
        }
        return list;
    }

    /**
     * [CAT] Retorna els N productes més cars (top N per preu, ordre descendent).
     * [ES] Devuelve los N productos más caros (top N por precio, orden
     * descendente).
     */
    public static List<String> ex2_topNProductos(Connection conn, int n) throws SQLException {
        String sql = "SELECT nom FROM productes ORDER BY preu DESC LIMIT ?";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, n);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("nom"));
                }
            }
        }
        return list;
    }

    /**
     * [CAT] Retorna els clients del segment donat que siguin del país donat. Llista
     * de noms, ordenats alfabèticament.
     * [ES] Devuelve los clientes del segmento dado que sean del país dado. Lista de
     * nombres, ordenados alfabéticamente.
     */
    public static List<String> ex2_clientesPorSegmentoYPais(Connection conn, String segmento, String pais)
            throws SQLException {
        String sql = "SELECT nom FROM clients WHERE segment = ? AND pais = ? ORDER BY nom";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, segmento);
            stmt.setString(2, pais);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("nom"));
                }
            }
        }
        return list;
    }

    // ──────────────────────────────────────────────────────────
    // EJERCICIO 3 / EXERCICI 3 ⭐⭐⭐ — JOINs
    // ──────────────────────────────────────────────────────────

    /**
     * [CAT] Retorna Map<nomClient, nombreComandes> per a TOTS els clients (inclou
     * clients sense cap comanda, amb valor 0).
     * [ES] Devuelve Map<nomClient, numeroPedidos> para TODOS los clientes (incluye
     * clientes sin ningún pedido, con valor 0).
     */
    public static Map<String, Integer> ex3_pedidosPorCliente(Connection conn) throws SQLException {
        String sql = "SELECT c.nom, COUNT(o.id) FROM clients c LEFT JOIN comandes o ON c.id = o.client_id GROUP BY c.id, c.nom";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        Map<String, Integer> map = new LinkedHashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        }
        return map;
    }

    /**
     * [CAT] Retorna el total facturat de les comandes LLIURADES d'un client donat.
     * Retorna 0.0 si no té cap comanda lliurada.
     * [ES] Devuelve el total facturado de los pedidos ENTREGADOS (LLIURAT) de un
     * cliente dado. Devuelve 0.0 si no tiene ningún pedido entregado.
     */
    public static double ex3_totalFacturadoCliente(Connection conn, String nombreCliente)
            throws SQLException {
        String sql = "SELECT SUM(lc.quantitat * lc.preu_unitari) FROM clients c JOIN comandes o ON c.id = o.client_id JOIN linies_comanda lc ON o.id = lc.comanda_id WHERE c.nom = ? AND o.estat = 'LLIURAT'";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    // ──────────────────────────────────────────────────────────
    // EJERCICIO 4 / EXERCICI 4 ⭐⭐⭐⭐ — GROUP BY, HAVING
    // ──────────────────────────────────────────────────────────

    /**
     * [CAT] Retorna Map<categoria, preumitja> per a totes les categories. El preu
     * mitjà ha de tenir 2 decimals de precisió.
     * [ES] Devuelve Map<categoria, precioMedio> para todas las categorías. El
     * precio medio debe tener 2 decimales de precisión.
     */
    public static Map<String, Double> ex4_precioMedioPorCategoria(Connection conn)
            throws SQLException {
        String sql = "SELECT categoria, ROUND(AVG(preu), 2) FROM productes GROUP BY categoria";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        Map<String, Double> map = new LinkedHashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString(1), rs.getDouble(2));
            }
        }
        return map;
    }

    /**
     * [CAT] Retorna les categories que tinguin més de {@code minim} productes.
     * [ES] Devuelve las categorías que tengan más de {@code minim} productos.
     */
    public static List<String> ex4_categoriasConMasDeNProductos(Connection conn, int minim)
            throws SQLException {
        String sql = "SELECT categoria FROM productes GROUP BY categoria HAVING COUNT(*) > ?";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, minim);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString(1));
                }
            }
        }
        return list;
    }

    // ──────────────────────────────────────────────────────────
    // EJERCICIO 5 / EXERCICI 5 ⭐⭐⭐⭐⭐ — Subconsultas y SELF JOIN
    // ──────────────────────────────────────────────────────────

    /**
     * [CAT] Retorna els noms dels productes que MAI han estat en cap comanda.
     * [ES] Devuelve los nombres de los productos que NUNCA han estado en ningún
     * pedido.
     */
    public static List<String> ex5_productosNoDemandados(Connection conn) throws SQLException {
        String sql = "SELECT nom FROM productes WHERE id NOT IN (SELECT producte_id FROM linies_comanda)";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("nom"));
            }
        }
        return list;
    }

    /**
     * [CAT] Retorna Map<nomEmpleat, nomCap> per a cada empleat. Els empleats sense
     * cap (CEO) han de tenir valor "Sense cap".
     * [ES] Devuelve Map<nomEmpleado, nomJefe> para cada empleado. Los empleados sin
     * jefe (CEO) deben tener valor "Sense cap".
     */
    public static Map<String, String> ex5_empleadoConJefe(Connection conn) throws SQLException {
        String sql = "SELECT e.nom, COALESCE(c.nom, 'Sense cap') FROM empleats e LEFT JOIN empleats c ON e.cap_id = c.id";

        // --- NO MODIFICAR A PARTIR DE AQUÍ / A PARTIR D'AQUÍ ---
        Map<String, String> map = new LinkedHashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString(1), rs.getString(2));
            }
        }
        return map;
    }
}
