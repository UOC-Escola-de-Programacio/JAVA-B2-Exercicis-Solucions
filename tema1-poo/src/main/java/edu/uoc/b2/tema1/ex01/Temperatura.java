package edu.uoc.b2.tema1.ex01;

/**
 * ============================================================
 * EXERCICIO 01 / EXERCICI 01 — Clase Inmutable: Temperatura ⭐ (Fácil / Fàcil)
 * ============================================================
 *
 * [CAT] OBJECTIU: Implementa la classe {@code Temperatura} seguint el patró
 * d'immutabilitat. La classe ha de ser immutable: sense setters, tots els
 * camps {@code final}, i cada operació retorna un NOU objecte.
 *
 * [ES] OBJETIVO: Implementa la clase {@code Temperatura} siguiendo el patrón
 * de inmutabilidad. La clase debe ser inmutable: sin setters, todos los
 * campos {@code final}, y cada operación devuelve un NUEVO objeto.
 *
 * MÉTODOS / MÈTODES:
 * Constructor: Temperatura(double grados) — valida >= -273.15
 * getGrados() : double — Devuelve/Retorna los grados/graus Celsius
 * toFahrenheit() : double — F = C × 9/5 + 32
 * toKelvin() : double — K = C + 273.15
 * sumar(double delta) : Temperatura — Devuelve/Retorna NUEVA/NOVA instància
 * restar(double delta) : Temperatura — Devuelve/Retorna NUEVA/NOVA instància
 * equals(Object) y hashCode(): basados en grados (tolerancia 0.001) / basats en
 * graus (tolerància 0.001)
 * toString() : "25.00 °C"
 *
 * EJEMPLO / EXEMPLE:
 * Temperatura t1 = new Temperatura(100.0);
 * Temperatura t2 = t1.sumar(20.0);
 * System.out.println(t1); // "100.00 °C" ← t1 NO ha cambiado / NO ha canviat
 * System.out.println(t2); // "120.00 °C"
 * System.out.println(t1.toFahrenheit()); // 212.0
 */
public final class Temperatura {

    private final double grados;

    public Temperatura(double grados) {
        if (grados < -273.15) {
            throw new IllegalArgumentException("Temperatura inferior al cero absoluto: " + grados);
        }
        this.grados = grados;
    }

    /**
     * [ES] Devuelve los grados Celsius de esta temperatura.
     * [CAT] Retorna els graus Celsius d'aquesta temperatura.
     */
    public double getGrados() {
        return this.grados;
    }

    /**
     * [ES] Convierte la temperatura a grados Fahrenheit. Fórmula: F = C × 9/5 + 32
     * [CAT] Converteix la temperatura a graus Fahrenheit. Fórmula: F = C × 9/5 + 32
     */
    public double toFahrenheit() {
        return this.grados * 9.0 / 5.0 + 32.0;
    }

    /**
     * [ES] Convierte la temperatura a Kelvin. Fórmula: K = C + 273.15
     * [CAT] Converteix la temperatura a Kelvin. Fórmula: K = C + 273.15
     */
    public double toKelvin() {
        return this.grados + 273.15;
    }

    /**
     * [ES] Devuelve una NUEVA temperatura con {@code delta} grados añadidos. Esta
     * temperatura NO se modifica.
     * [CAT] Retorna una NOVA temperatura amb {@code delta} graus afegits. Aquesta
     * temperatura NO es modifica.
     */
    public Temperatura sumar(double delta) {
        return new Temperatura(this.grados + delta);
    }

    /**
     * [ES] Devuelve una NUEVA temperatura con {@code delta} grados restados. Esta
     * temperatura NO se modifica.
     * [CAT] Retorna una NOVA temperatura amb {@code delta} graus restats. Aquesta
     * temperatura NO es modifica.
     *
     * @throws IllegalArgumentException si el resultado / resultat < -273.15
     */
    public Temperatura restar(double delta) {
        return new Temperatura(this.grados - delta);
    }

    /**
     * [ES] Dos temperaturas son iguales si difieren menos de 0.001 °C.
     * [CAT] Dues temperatures són iguals si difereixen menys de 0.001 °C.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        Temperatura that = (Temperatura) other;
        return Math.abs(this.grados - that.grados) < 0.001;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(Math.round(grados * 100.0) / 100.0);
    }

    /**
     * [ES] Formato: "25.00 °C"
     * [CAT] Format: "25.00 °C"
     */
    @Override
    public String toString() {
        return String.format("%.2f °C", grados).replace(",", ".");
    }
}
