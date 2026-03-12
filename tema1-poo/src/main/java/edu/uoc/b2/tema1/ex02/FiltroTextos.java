package edu.uoc.b2.tema1.ex02;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ============================================================
 * EXERCICIO 02 / EXERCICI 02 — Lambdas y Interfaces Funcionales / Lambdas i
 * Interfícies Funcionals ⭐⭐ (Fácil-Medio / Fàcil-Mitjà)
 * ============================================================
 *
 * [CAT] OBJECTIU: Implementa els mètodes usant Lambdas, Predicate i Function
 * de java.util.function. CAP mètode pot usar bucles for/while.
 *
 * [ES] OBJETIVO: Implementa los métodos usando Lambdas, Predicate y Function
 * de java.util.function. NINGÚN método puede usar bucles for/while.
 *
 * TIP:
 * [CAT] Usa stream().filter(...).collect(Collectors.toList()) per als
 * filtradors
 * Usa stream().map(...).collect(Collectors.toList()) per als transformadors
 * [ES] Usa stream().filter(...).collect(Collectors.toList()) para los
 * filtradores
 * Usa stream().map(...).collect(Collectors.toList()) para los transformadores
 */
public class FiltroTextos {

    /**
     * [CAT] Retorna un Predicate que és cert si el text té almenys {@code longMin}
     * caràcters.
     * [ES] Devuelve un Predicate que es cierto si el texto tiene al menos
     * {@code longMin} caracteres.
     *
     * Ejemplo / Exemple: predicadoLongitudMinima(5).test("hola") → false (4 < 5)
     * predicadoLongitudMinima(5).test("hola!") → true (5 >= 5)
     */
    public static Predicate<String> predicadoLongitudMinima(int longMin) {
        return s -> s.length() >= longMin;
    }

    /**
     * [CAT] Retorna un Predicate que és cert si el text comença per {@code prefix}
     * (case-insensitive).
     * [ES] Devuelve un Predicate que es cierto si el texto empieza por
     * {@code prefix} (case-insensitive).
     */
    public static Predicate<String> predicadoEmpiezaPor(String prefix) {
        return s -> s.toLowerCase().startsWith(prefix.toLowerCase());
    }

    /**
     * [CAT] Retorna una Function que transforma el text a majúscules i elimina
     * espais de l'inici i el final.
     * [ES] Devuelve una Function que transforma el texto a mayúsculas y elimina
     * espacios del inicio y el final.
     */
    public static Function<String, String> transformadorNormalizar() {
        return s -> s.trim().toUpperCase();
    }

    /**
     * [CAT] Filtra la llista de textos retornant NOMÉS els que compleixen el
     * predicat.
     * - Ha d'usar el Predicate passat com a paràmetre (no hard-codi cap condició)
     * - NO pot usar bucles for/while — ha d'usar Streams
     *
     * [ES] Filtra la lista de textos devolviendo SOLO los que cumplen el predicado.
     * - Debe usar el Predicate pasado como parámetro (no hard-codee ninguna
     * condición)
     * - NO puede usar bucles for/while — debe usar Streams
     *
     * @param textos    la llista d'entrada / la lista de entrada
     * @param predicado la condició de filtre / la condición de filtro
     * @return nova llista amb només els texts que compleixen la condició / nueva
     *         lista con solo los textos que cumplen la condición
     */
    public static List<String> filtrar(List<String> textos, Predicate<String> predicado) {
        return textos.stream().filter(predicado).collect(Collectors.toList());
    }

    /**
     * [CAT] Transforma tots els textos de la llista aplicant la funció donada.
     * - Ha d'usar la Function passada com a paràmetre
     * - NO pot usar bucles for/while — ha d'usar Streams
     *
     * [ES] Transforma todos los textos de la lista aplicando la función dada.
     * - Debe usar la Function pasada como parámetro
     * - NO puede usar bucles for/while — debe usar Streams
     */
    public static List<String> transformar(List<String> textos, Function<String, String> funcion) {
        return textos.stream().map(funcion).collect(Collectors.toList());
    }

    /**
     * [CAT] Filtra I després transforma els textos.
     * Primer aplica el predicat, després aplica la funció als resultats filtrats.
     *
     * [ES] Filtra Y luego transforma los textos.
     * Primero aplica el predicado, luego aplica la función a los resultados
     * filtrados.
     *
     * Ejemplo / Exemple:
     * filtrarYTransformar(["hola", "hei", "adeu"], longitud >= 4, mayúsculas)
     * → ["HOLA", "ADEU"] (filtra "hei", transforma els altres a majúscules / a
     * mayúsculas)
     */
    public static List<String> filtrarYTransformar(
            List<String> textos,
            Predicate<String> predicado,
            Function<String, String> funcion) {
        return textos.stream().filter(predicado).map(funcion).collect(Collectors.toList());
    }

    /**
     * [CAT] Combina dos predicats amb AND. Un text passa el filtre si compleix TOTS
     * DOS.
     * NOTA: no uses && dins d'una lambda — usa el mètode .and() de Predicate.
     * 
     * [ES] Combina dos predicados con AND. Un texto pasa el filtro si cumple AMBOS.
     * NOTA: no uses && dentro de una lambda — usa el método .and() de Predicate.
     */
    public static Predicate<String> combinarAnd(Predicate<String> p1, Predicate<String> p2) {
        return p1.and(p2);
    }
}
