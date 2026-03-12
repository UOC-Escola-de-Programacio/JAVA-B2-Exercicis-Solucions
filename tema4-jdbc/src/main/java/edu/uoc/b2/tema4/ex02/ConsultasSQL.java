package edu.uoc.b2.tema4.ex02;

import java.sql.*;
import java.util.*;

/**
 * ============================================================
 * EXERCICIO 02 JDBC / EXERCICI 02 JDBC — Consultas / Consultes ⭐⭐⭐
 * ============================================================
 *
 * [CAT] INSTRUCCIONS:
 * - Implementa tant les consultes SQL com el codi JDBC complet.
 * - USA PreparedStatement per a qualsevol consulta amb paràmetres.
 * - Llegeix els resultats dels ResultSet i emplena les dades corresponents.
 *
 * [ES] INSTRUCCIONES:
 * - Implementa tanto las consultas SQL como el código JDBC completo.
 * - USA PreparedStatement para cualquier consulta con parámetros.
 * - Lee los resultados de los ResultSet y llena los datos correspondientes.
 */
public class ConsultasSQL {

    public static List<String> ex1_nombresClientes(Connection conn) throws SQLException {
        String sql = "SELECT nom FROM clients ORDER BY nom ASC";
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        }
        return list;
    }

    public static int ex1_totalProductos(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM productes";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public static List<String> ex2_productosPorCategoria(Connection conn, String categoria) throws SQLException {
        String sql = "SELECT nom FROM productes WHERE categoria = ? ORDER BY preu DESC";
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString(1));
                }
            }
        }
        return list;
    }

    public static List<String> ex2_topNProductos(Connection conn, int n) throws SQLException {
        String sql = "SELECT nom FROM productes ORDER BY preu DESC LIMIT ?";
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, n);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString(1));
                }
            }
        }
        return list;
    }

    public static List<String> ex2_clientesPorSegmentoYPais(Connection conn, String segmento, String pais)
            throws SQLException {
        String sql = "SELECT nom FROM clients WHERE segment = ? AND pais = ? ORDER BY nom ASC";
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, segmento);
            stmt.setString(2, pais);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString(1));
                }
            }
        }
        return list;
    }

    public static Map<String, Integer> ex3_pedidosPorCliente(Connection conn) throws SQLException {
        String sql = "SELECT c.nom, COUNT(o.id) FROM clients c LEFT JOIN comandes o ON c.id = o.client_id GROUP BY c.id, c.nom";
        Map<String, Integer> map = new LinkedHashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        }
        return map;
    }

    public static double ex3_totalFacturadoCliente(Connection conn, String nombreCliente) throws SQLException {
        String sql = "SELECT SUM(lc.quantitat * lc.preu_unitari) FROM clients c JOIN comandes o ON c.id = o.client_id JOIN linies_comanda lc ON o.id = lc.comanda_id WHERE c.nom = ? AND o.estat = 'LLIURAT'";
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

    public static Map<String, Double> ex4_precioMedioPorCategoria(Connection conn) throws SQLException {
        String sql = "SELECT categoria, ROUND(AVG(preu), 2) FROM productes GROUP BY categoria";
        Map<String, Double> map = new LinkedHashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString(1), rs.getDouble(2));
            }
        }
        return map;
    }

    public static List<String> ex4_categoriasConMasDeNProductos(Connection conn, int minim) throws SQLException {
        String sql = "SELECT categoria FROM productes GROUP BY categoria HAVING COUNT(*) > ?";
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

    public static List<String> ex5_productosNoDemandados(Connection conn) throws SQLException {
        String sql = "SELECT nom FROM productes WHERE id NOT IN (SELECT producte_id FROM linies_comanda)";
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        }
        return list;
    }

    public static Map<String, String> ex5_empleadoConJefe(Connection conn) throws SQLException {
        String sql = "SELECT e.nom, COALESCE(c.nom, 'Sense cap') FROM empleats e LEFT JOIN empleats c ON e.cap_id = c.id";
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
