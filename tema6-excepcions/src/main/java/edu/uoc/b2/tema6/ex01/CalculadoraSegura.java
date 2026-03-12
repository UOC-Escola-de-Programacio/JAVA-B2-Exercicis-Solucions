package edu.uoc.b2.tema6.ex01;

/**
 * ============================================================
 * EXERCICIO 01 / EXERCICI 01 Excepciones / Excepcions — Calculadora Segura ⭐
 * (Fácil / Fàcil)
 * ============================================================
 *
 * [CAT] OBJECTIU: Implementa una calculadora que gestioni correctament les
 * excepcions en lloc de deixar que es propaguin sense informació útil.
 * [ES] OBJETIVO: Implementa una calculadora que gestione correctamente las
 * excepciones en lugar de dejar que se propaguen sin información útil.
 *
 * REGLES / REGLAS:
 * - Llança/Lanza IllegalArgumentException per arguments invàlids / por
 * argumentos inválidos
 * - Llança/Lanza ArithmeticException per operacions / por operaciones
 * matemàticament/matemáticamente impossibles/imposibles
 * - Els missatges d'excepció / Los mensajes de excepción han de ser descriptius
 * / descriptivos (incloure els valors / incluir los valores)
 */
public class CalculadoraSegura {

    /**
     * [CAT] Suma dos enters. No llança cap excepció.
     * [ES] Suma dos enteros. No lanza ninguna excepción.
     */
    public static int sumar(int a, int b) {
        // [ES] TODO — devuelve a + b (¡sencillo!)
        // [CAT] TODO — retorna a + b (senzill!)
        return a + b;
    }

    /**
     * [CAT] Divideix 'dividendo' entre 'divisor'.
     * [ES] Divide 'dividendo' entre 'divisor'.
     *
     * @throws ArithmeticException si divisor == 0,
     *                             [CAT] missatge: "Divisió per zero: [dividendo] /
     *                             0"
     *                             [ES] mensaje: "División por cero: [dividendo] /
     *                             0"
     */
    public static double dividir(double dividendo, double divisor) {
        // [ES] TODO — comprueba divisor == 0 → throw new ArithmeticException(...)
        // [CAT] TODO — comprova divisor == 0 → throw new ArithmeticException(...)
        // Pista: String.format("División por cero / Divisió per zero: %.1f / 0",
        // dividendo)
        if (divisor == 0.0) {
            throw new ArithmeticException(String.format("División por cero: %.1f / 0", dividendo));
        }
        return dividendo / divisor;
    }

    /**
     * [CAT] Calcula l'arrel quadrada d'un nombre.
     * [ES] Calcula la raíz cuadrada de un número.
     *
     * @throws IllegalArgumentException si n < 0
     *                                  [CAT] missatge: "No es pot calcular l'arrel
     *                                  quadrada d'un nombre negatiu: [n]"
     *                                  [ES] mensaje: "No se puede calcular la raíz
     *                                  cuadrada de un número negativo: [n]"
     */
    public static double raizCuadrada(double n) {
        // [ES] TODO — comprueba n < 0 → throw new IllegalArgumentException(...)
        // [CAT] TODO — comprova n < 0 → throw new IllegalArgumentException(...)
        // Pista: Math.sqrt(n)
        if (n < 0) {
            throw new IllegalArgumentException("No se puede calcular la raíz cuadrada de un número negativo: " + n);
        }
        return Math.sqrt(n);
    }

    /**
     * [CAT] Calcula el factorial d'un nombre enter no negatiu.
     * [ES] Calcula el factorial de un número entero no negativo.
     *
     * @throws IllegalArgumentException si n < 0
     * @throws ArithmeticException      si n > 20 (desbordaría un long)
     */
    public static long factorial(int n) {
        // [ES] TODO:
        // 1. Comprueba n < 0 → IllegalArgumentException
        // 2. Comprueba n > 20 → ArithmeticException
        // 3. Caso base: n == 0 o n == 1 → devuelve 1
        // 4. Caso recursivo: devuelve n * factorial(n - 1)
        // [CAT] TODO:
        // 1. Comprova n < 0 → IllegalArgumentException
        // 2. Comprova n > 20 → ArithmeticException
        // 3. Cas base: n == 0 o n == 1 → retorna 1
        // 4. Cas recursiu: retorna n * factorial(n - 1)
        if (n < 0) {
            throw new IllegalArgumentException("Factorial de numero negativo");
        }
        if (n > 20) {
            throw new ArithmeticException("Desbordamiento de long");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    /**
     * [CAT] Parseja un String a enter de forma segura.
     * [ES] Parsea un String a entero de forma segura.
     *
     * @throws IllegalArgumentException si el String no és un nombre enter / si el
     *                                  String no es un número entero vàlid / válido
     *                                  [CAT] missatge: "Format de nombre invàlid:
     *                                  '[text]'"
     *                                  [ES] mensaje: "Formato de número inválido:
     *                                  '[text]'"
     */
    public static int parsearEntero(String text) {
        // [ES] TODO — Usa Integer.parseInt(text) dentro de un
        // try-catch(NumberFormatException)
        // y relanza como: throw new IllegalArgumentException("Formato de número
        // inválido: '" + text + "'", e)
        // [CAT] TODO — Usa Integer.parseInt(text) dins d'un
        // try-catch(NumberFormatException)
        // i rellança com: throw new IllegalArgumentException("Format de nombre invàlid:
        // '" + text + "'", e)
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de número inválido: '" + text + "'", e);
        }
    }
}
