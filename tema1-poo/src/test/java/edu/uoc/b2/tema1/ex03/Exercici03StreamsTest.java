package edu.uoc.b2.tema1.ex03;

import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static edu.uoc.b2.tema1.ex03.ProcesadorProductos.*;

@DisplayName("Ex03 — Streams API")
class Exercici03StreamsTest {

    // Datos de test / Dades de test 
    private static List<Producto> PRODUCTOS;

    @BeforeAll
    static void setup() {
        PRODUCTOS = List.of(
                new Producto(1, "Laptop", "Electronica", 1299.99, 10, true),
                new Producto(2, "Ratolí", "Electronica", 29.99, 50, true),
                new Producto(3, "Taula", "Mobles", 199.99, 5, true),
                new Producto(4, "Cadira", "Mobles", 89.99, 20, false), // INACTIU
                new Producto(5, "Llamp USB", "Electronica", 12.99, 0, true), // Estoc 0
                new Producto(6, "Libreria", "Mobles", 149.99, 8, true),
                new Producto(7, "Webcam", "Electronica", 59.99, 15, false) // INACTIU
        );
    }

    @Test
    @DisplayName("activos: excluye productos inactivos (Cadira, Webcam)")
    void actius_exclou_inactius() {
        List<Producto> result = activos(PRODUCTOS);
        assertThat(result).hasSize(5);
        assertThat(result).noneMatch(p -> p.nombre().equals("Cadira") || p.nombre().equals("Webcam"));
    }

    @Test
    @DisplayName("activos: ordenados por precio ascendente")
    void actius_ordenats() {
        List<Producto> result = activos(PRODUCTOS);
        // Preus en ordre: 12.99, 29.99, 149.99, 199.99, 1299.99
        assertThat(result.get(0).precio()).isEqualTo(12.99);
        assertThat(result.get(result.size() - 1).precio()).isEqualTo(1299.99);
    }

    @Test
    @DisplayName("nombresPorCategoria: Electronica (activos)")
    void noms_electronica() {
        List<String> result = nombresPorCategoria(PRODUCTOS, "Electronica");
        // Actius d'Electronica: Laptop, Ratolí, Llamp USB (Webcam és inactiu → NO)
        // NOTA: el mètode filtra per categoria però NO per actiu → tots els de la cat.
        assertThat(result).contains("Laptop", "Ratolí", "Llamp USB");
        assertThat(result).isSortedAccordingTo(String::compareTo);
    }

    @Test
    @DisplayName("nombresPorCategoria: categoría inexistente → vacío")
    void noms_categoria_inexistent() {
        assertThat(nombresPorCategoria(PRODUCTOS, "Aliments")).isEmpty();
    }

    @Test
    @DisplayName("totalFacturacion: suma activos (precio × stock)")
    void total_faturacio() {
        // Actius: Laptop(1299.99×10) + Ratolí(29.99×50) + Taula(199.99×5) +
        // Llamp(12.99×0) + Libreria(149.99×8)
        // = 12999.90 + 1499.50 + 999.95 + 0 + 1199.92 = 16699.27
        double total = totalFacturacion(PRODUCTOS);
        assertThat(total).isCloseTo(16699.27, within(0.01));
    }

    @Test
    @DisplayName("productoMasCaro: devuelve el Laptop")
    void producte_mes_car() {
        Optional<Producto> result = productoMasCaro(PRODUCTOS);
        assertThat(result).isPresent();
        assertThat(result.get().nombre()).isEqualTo("Laptop");
        assertThat(result.get().precio()).isEqualTo(1299.99);
    }

    @Test
    @DisplayName("productoMasCaro: lista vacía → Optional.empty")
    void producte_mes_car_buit() {
        assertThat(productoMasCaro(List.of())).isEmpty();
    }

    @Test
    @DisplayName("contarPorCategoria: cuenta correctamente")
    void comptar_per_categoria() {
        Map<String, Long> result = contarPorCategoria(PRODUCTOS);
        // Electronica: Laptop, Ratolí, Llamp USB, Webcam = 4 (actius i inactius)
        assertThat(result).containsKey("Electronica");
        assertThat(result.get("Electronica")).isEqualTo(4L);
        assertThat(result.get("Mobles")).isEqualTo(3L);
    }

    @Test
    @DisplayName("precioMedio: Mobles activos = (199.99 + 149.99) / 2")
    void preu_mitja_mobles() {
        // Mobles actius: Taula(199.99) + Libreria(149.99) [Cadira és inactiu]
        double mitja = precioMedio(PRODUCTOS, "Mobles");
        assertThat(mitja).isCloseTo((199.99 + 149.99) / 2, within(0.01));
    }

    @Test
    @DisplayName("precioMedio: categoria inexistente → 0.0")
    void preu_mitja_inexistent() {
        assertThat(precioMedio(PRODUCTOS, "Aliments")).isEqualTo(0.0);
    }

    @Test
    @DisplayName("hayStockBajo: detecta stock 0")
    void hi_ha_estoc_baix() {
        assertThat(hayStockBajo(PRODUCTOS, 5)).isTrue(); // Llamp USB estoc = 0
    }

    @Test
    @DisplayName("filtrarPorStock: entre 5 y 15 (activos)")
    void filtrar_per_estoc() {
        List<Producto> result = filtrarPorStock(PRODUCTOS, 5, 15);
        // Actius amb estoc entre 5 i 15: Taula(5), Laptop(10), Webcam és inactiu,
        // Libreria(8)
        assertThat(result)
                .extracting(Producto::nombre)
                .contains("Taula", "Laptop", "Libreria");
        assertThat(result).noneMatch(p -> p.nombre().equals("Webcam")); // Inactiu!
        // Ordenats per estoc: 5 (Taula), 8 (Libreria), 10 (Laptop)
        assertThat(result.get(0).stock()).isLessThanOrEqualTo(result.get(1).stock());
    }
}
