package edu.uoc.b2.tema6.ex01;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Ex01 — CalculadoraSegura")
class Exercici01CalculadoraTest {

    @Test
    @DisplayName("sumar: resultat correcte / resultado correcto")
    void sumar() {
        assertThat(CalculadoraSegura.sumar(3, 4)).isEqualTo(7);
    }

    @Test
    @DisplayName("dividir: 10 / 4 = 2.5")
    void dividir_ok() {
        assertThat(CalculadoraSegura.dividir(10, 4)).isEqualTo(2.5);
    }

    @Test
    @DisplayName("dividir: / 0 → ArithmeticException amb 'dividend' al missatge / con 'dividendo' en el mensaje")
    void dividir_zero() {
        assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> CalculadoraSegura.dividir(5, 0))
                .withMessageContaining("5");
    }

    @Test
    @DisplayName("raizCuadrada: √4 = 2.0")
    void arrel_ok() {
        assertThat(CalculadoraSegura.raizCuadrada(4)).isEqualTo(2.0);
    }

    @Test
    @DisplayName("raizCuadrada: √0 = 0.0")
    void arrel_zero() {
        assertThat(CalculadoraSegura.raizCuadrada(0)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("raizCuadrada: negatiu / negativo → IllegalArgumentException")
    void arrel_negatiu() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> CalculadoraSegura.raizCuadrada(-4))
                .withMessageContaining("-4");
    }

    @Test
    @DisplayName("factorial: 0! = 1")
    void factorial_zero() {
        assertThat(CalculadoraSegura.factorial(0)).isEqualTo(1L);
    }

    @Test
    @DisplayName("factorial: 5! = 120")
    void factorial_5() {
        assertThat(CalculadoraSegura.factorial(5)).isEqualTo(120L);
    }

    @Test
    @DisplayName("factorial: 10! = 3628800")
    void factorial_10() {
        assertThat(CalculadoraSegura.factorial(10)).isEqualTo(3628800L);
    }

    @Test
    @DisplayName("factorial: negatiu / negativo → IllegalArgumentException")
    void factorial_negatiu() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> CalculadoraSegura.factorial(-1))
                .withMessageContaining("-1");
    }

    @Test
    @DisplayName("factorial: > 20 → ArithmeticException")
    void factorial_massa_gran() {
        assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> CalculadoraSegura.factorial(21))
                .withMessageContaining("21");
    }

    @Test
    @DisplayName("parsearEntero: '42' → 42")
    void parsear_ok() {
        assertThat(CalculadoraSegura.parsearEntero("42")).isEqualTo(42);
    }

    @Test
    @DisplayName("parsearEntero: 'abc' → IllegalArgumentException amb 'abc' al missatge / con 'abc' en el mensaje")
    void parsear_invalid() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> CalculadoraSegura.parsearEntero("abc"))
                .withMessageContaining("abc");
    }

    @Test
    @DisplayName("parsearEntero: causa original (NumberFormatException) preservada")
    void parsear_causa() {
        try {
            CalculadoraSegura.parsearEntero("xyz");
            fail("Ha d'haver llançat excepció / Debe haber lanzado excepción");
        } catch (IllegalArgumentException e) {
            assertThat(e.getCause()).isInstanceOf(NumberFormatException.class);
        }
    }
}
