package edu.uoc.b2.tema4.ex03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ============================================================
 * EXERCICI 03 JDBC / EJERCICIO 03 JDBC — Transacciones / Transaccions ⭐⭐⭐⭐
 * ============================================================
 *
 * [CAT] OBJECTIU: Controlar l'execució d'un traspàs de diners mitjançant
 * transaccions manuals (commit/rollback).
 * [ES] OBJETIVO: Controlar la ejecución de un traspaso de dinero mediante
 * transacciones manuales (commit/rollback).
 */
public class TraspasoBancario {

    /**
     * [CAT] Realitza un traspàs bancari assecurant l'A de ACID (Atomicitat).
     * [ES] Realiza un traspaso bancario asegurando la A de ACID (Atomicidad).
     *
     * @param conn      Connection (s'ha d'establir a autoCommit=false manualment /
     *                  se debe establecer a autoCommit=false manualmente)
     * @param origenId  ID de la cuenta origen
     * @param destinoId ID de la cuenta destino
     * @param cantidad  Dinero a traspasar (> 0)
     * @throws SQLException si falla alguna cosa (incloent falta de saldo) / si
     *                      falla algo (incluyendo falta de saldo)
     */
    public static void traspasar(Connection conn, int origenId, int destinoId, double cantidad) throws SQLException {
        if (cantidad <= 0) {
            throw new SQLException("Cantidad debe ser mayor que cero");
        }

        boolean oldAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try {
            String sqlOut = "UPDATE cuentas SET saldo = saldo - ? WHERE id = ? AND saldo >= ?";
            try (PreparedStatement stmtOut = conn.prepareStatement(sqlOut)) {
                stmtOut.setDouble(1, cantidad);
                stmtOut.setInt(2, origenId);
                stmtOut.setDouble(3, cantidad);
                if (stmtOut.executeUpdate() == 0) {
                    throw new SQLException("No hi ha prou saldo / No hay saldo suficiente");
                }
            }

            String sqlIn = "UPDATE cuentas SET saldo = saldo + ? WHERE id = ?";
            try (PreparedStatement stmtIn = conn.prepareStatement(sqlIn)) {
                stmtIn.setDouble(1, cantidad);
                stmtIn.setInt(2, destinoId);
                if (stmtIn.executeUpdate() == 0) {
                    throw new SQLException("Cuenta destino no encontrada");
                }
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(oldAutoCommit);
        }
    }
}
