-- ============================================================
-- Schema para ej. 03 (traspasos) JDBC
-- ============================================================

DROP TABLE IF EXISTS cuentas;

CREATE TABLE cuentas (
    id    INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(200) NOT NULL,
    saldo DECIMAL(10,2) NOT NULL CHECK (saldo >= 0)
) ENGINE=InnoDB;

INSERT INTO cuentas (nombre, saldo) VALUES
    ('A. Einstein', 500.00),
    ('N. Tesla',     300.00),
    ('M. Curie',    1200.00);
