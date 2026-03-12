-- ============================================================
-- Schema H2 per als exercicis de SQL (Tema 3)
-- S'executa automàticament abans de cada test
-- Compatible amb H2 mode MySQL (MODE=MySQL en la URL de connexió)
-- ============================================================

-- Neteja prèvia (per si s'executa més d'una vegada)
DROP TABLE IF EXISTS linies_comanda;
DROP TABLE IF EXISTS comandes;
DROP TABLE IF EXISTS productes;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS empleats;

-- ─── Taula clients ───────────────────────────────────────────
CREATE TABLE clients (
    id      INT PRIMARY KEY AUTO_INCREMENT,
    nom     VARCHAR(100) NOT NULL,
    email   VARCHAR(200) NOT NULL UNIQUE,
    pais    VARCHAR(50)  DEFAULT 'ES',
    segment VARCHAR(20)  DEFAULT 'STANDARD'  -- STANDARD, PREMIUM, VIP
);

-- ─── Taula productes ─────────────────────────────────────────
CREATE TABLE productes (
    id        INT PRIMARY KEY AUTO_INCREMENT,
    nom       VARCHAR(200) NOT NULL,
    categoria VARCHAR(100),
    preu      DECIMAL(10,2) NOT NULL CHECK (preu > 0),
    estoc     INT NOT NULL DEFAULT 0
);

-- ─── Taula comandes ──────────────────────────────────────────
CREATE TABLE comandes (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    client_id  INT NOT NULL,
    estat      VARCHAR(20) DEFAULT 'PENDENT',   -- PENDENT, ENVIAT, LLIURAT, CANCEL·LAT
    creat_el   DATE DEFAULT CURRENT_DATE,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- ─── Taula linies_comanda ────────────────────────────────────
CREATE TABLE linies_comanda (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    comanda_id   INT NOT NULL,
    producte_id  INT NOT NULL,
    quantitat    INT NOT NULL CHECK (quantitat > 0),
    preu_unitari DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (comanda_id)  REFERENCES comandes(id),
    FOREIGN KEY (producte_id) REFERENCES productes(id)
);

-- ─── Taula empleats (para SELF JOIN) ─────────────────────────
CREATE TABLE empleats (
    id         INT PRIMARY KEY,
    nom        VARCHAR(100) NOT NULL,
    cap_id     INT,
    departament VARCHAR(100),
    salari     DECIMAL(10,2),
    FOREIGN KEY (cap_id) REFERENCES empleats(id)
);

-- ─── Dades de prova ──────────────────────────────────────────
INSERT INTO clients (nom, email, pais, segment) VALUES
    ('Ana García',    'ana@test.com',    'ES', 'VIP'),
    ('Pau Martí',     'pau@test.com',    'ES', 'PREMIUM'),
    ('Laura Blanc',   'laura@test.com',  'CA', 'STANDARD'),
    ('Marc López',    'marc@test.com',   'ES', 'VIP'),
    ('Sofia Sánchez', 'sofia@test.com',  'MX', 'STANDARD'),
    ('Jordi Puig',    'jordi@test.com',  'ES', 'STANDARD');  -- Sense comandes

INSERT INTO productes (nom, categoria, preu, estoc) VALUES
    ('Laptop Pro',   'Electronica', 1299.99, 10),
    ('Ratolí',       'Electronica',   29.99, 50),
    ('Taula',        'Mobles',       199.99,  5),
    ('Cadira',       'Mobles',        89.99, 20),
    ('Llum LED',     'Llum',          15.99,100),
    ('Webcam HD',    'Electronica',   79.99, 30),
    ('Teclat',       'Electronica',   49.99, 40);

INSERT INTO comandes (client_id, estat, creat_el) VALUES
    (1, 'LLIURAT',    '2024-01-10'),
    (1, 'LLIURAT',    '2024-02-15'),
    (2, 'ENVIAT',     '2024-03-01'),
    (3, 'PENDENT',    '2024-03-10'),
    (4, 'LLIURAT',    '2024-01-20'),
    (5, 'CANCEL·LAT', '2024-02-05');

INSERT INTO linies_comanda (comanda_id, producte_id, quantitat, preu_unitari) VALUES
    (1, 1, 1, 1299.99),  -- Ana: Laptop
    (1, 2, 2,   29.99),  -- Ana: 2x Ratolí
    (2, 3, 1,  199.99),  -- Ana: Taula
    (3, 6, 1,   79.99),  -- Pau: Webcam
    (3, 7, 2,   49.99),  -- Pau: 2x Teclat
    (4, 5, 3,   15.99),  -- Laura: 3x Llum
    (5, 1, 1, 1299.99),  -- Marc: Laptop
    (5, 4, 2,   89.99);  -- Marc: 2x Cadira

INSERT INTO empleats (id, nom, cap_id, departament, salari) VALUES
    (1, 'CEO',         NULL, 'Direcció',   8000.00),
    (2, 'CTO',            1, 'Tecnologia', 7000.00),
    (3, 'CFO',            1, 'Finances',   7000.00),
    (4, 'Dev Senior',     2, 'Tecnologia', 5000.00),
    (5, 'Dev Junior',     4, 'Tecnologia', 3000.00),
    (6, 'Comptable',      3, 'Finances',   4000.00);
