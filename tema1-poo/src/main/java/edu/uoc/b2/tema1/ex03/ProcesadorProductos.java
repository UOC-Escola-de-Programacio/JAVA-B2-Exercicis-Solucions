package edu.uoc.b2.tema1.ex03;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ============================================================
 * EXERCICI 03 — Streams API ⭐⭐⭐ (Mitjà)
 * ============================================================
 *
 * [CAT] OBJECTIU: Implementa operacions sobre col·leccions usant la Streams
 * API.
 * Tots els mètodes han d'usar Streams (NO bucles for/while/forEach imperatiu).
 *
 * [ES] OBJETIVO: Implementa operaciones sobre colecciones usando la Streams
 * API.
 * Todos los métodos deben usar Streams (NO bucles for/while/forEach
 * imperativo).
 *
 * CONTEXT: Gestionem una llista de productes d'una botiga online. / Gestionamos
 * una lista de productos de una tienda online.
 */
public class ProcesadorProductos {

    public record Producto(
            int id,
            String nombre,
            String categoria,
            double precio,
            int stock,
            boolean activo) {
    }

    /**
     * [CAT] Devuelve/Retorna tots els productes actius, ordenats per preu
     * ascendent.
     * [ES] Devuelve todos los productos activos, ordenados por precio ascendente.
     */
    public static List<Producto> activos(List<Producto> productos) {
        return productos.stream()
                .filter(Producto::activo)
                .sorted(Comparator.comparingDouble(Producto::precio))
                .collect(Collectors.toList());
    }

    /**
     * [CAT] Devuelve/Retorna els noms (String) de tots els productes de la
     * categoria donada. La llista ha d'estar ordenada alfabèticament.
     * [ES] Devuelve los nombres (String) de todos los productos de la categoría
     * dada. La lista debe estar ordenada alfabéticamente.
     */
    public static List<String> nombresPorCategoria(List<Producto> productos, String categoria) {
        return productos.stream()
                .filter(p -> p.categoria().equals(categoria))
                .map(Producto::nombre)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * [CAT] Calcula el total de la facturació potencial (preu × estoc) dels
     * productes actius.
     * [ES] Calcula el total de la facturación potencial (precio × stock) de los
     * productos activos.
     */
    public static double totalFacturacion(List<Producto> productos) {
        return productos.stream()
                .filter(Producto::activo)
                .mapToDouble(p -> p.precio() * p.stock())
                .sum();
    }

    /**
     * [CAT] Devuelve/Retorna el producte més car entre els actius.
     * Si la llista és buida o cap és actiu, Devuelve/Retorna Optional.empty().
     * [ES] Devuelve el producto más caro entre los activos.
     * Si la lista está vacía o ninguno es activo, Devuelve Optional.empty().
     */
    public static Optional<Producto> productoMasCaro(List<Producto> productos) {
        return productos.stream()
                .filter(Producto::activo)
                .max(Comparator.comparingDouble(Producto::precio));
    }

    /**
     * [CAT] Devuelve/Retorna un Map<String, Long> amb el nombre de productes per
     * cada categoria.
     * Exemple: {"Electronica" → 3, "Mobles" → 2}
     * [ES] Devuelve un Map<String, Long> con el número de productos por cada
     * categoría.
     * Ejemplo: {"Electronica" → 3, "Muebles" → 2}
     */
    public static Map<String, Long> contarPorCategoria(List<Producto> productos) {
        return productos.stream()
                .collect(Collectors.groupingBy(Producto::categoria, Collectors.counting()));
    }

    /**
     * [CAT] Devuelve/Retorna el preu mitjà dels productes actius de la categoria
     * donada.
     * Si no hi ha productes que compleixin, Devuelve/Retorna 0.0.
     * [ES] Devuelve el precio medio de los productos activos de la categoría dada.
     * Si no hay productos que cumplan, devuelve 0.0.
     */
    public static double precioMedio(List<Producto> productos, String categoria) {
        return productos.stream()
                .filter(Producto::activo)
                .filter(p -> p.categoria().equals(categoria))
                .mapToDouble(Producto::precio)
                .average()
                .orElse(0.0);
    }

    /**
     * [CAT] Devuelve/Retorna true si ALGUN producte actiu té estoc per sota de
     * {@code minim}.
     * [ES] Devuelve true si ALGÚN producto activo tiene stock por debajo de
     * {@code minim}.
     */
    public static boolean hayStockBajo(List<Producto> productos, int minim) {
        return productos.stream()
                .filter(Producto::activo)
                .anyMatch(p -> p.stock() < minim);
    }

    /**
     * [CAT] Devuelve/Retorna els productes actius amb estoc entre minim i maxim
     * (inclusiu).
     * Ordenats per estoc ascendent.
     * [ES] Devuelve los productos activos con stock entre minim y maxim
     * (inclusivo).
     * Ordenados por stock ascendente.
     */
    public static List<Producto> filtrarPorStock(List<Producto> productos, int minim, int maxim) {
        return productos.stream()
                .filter(Producto::activo)
                .filter(p -> p.stock() >= minim && p.stock() <= maxim)
                .sorted(Comparator.comparingInt(Producto::stock))
                .collect(Collectors.toList());
    }
}
