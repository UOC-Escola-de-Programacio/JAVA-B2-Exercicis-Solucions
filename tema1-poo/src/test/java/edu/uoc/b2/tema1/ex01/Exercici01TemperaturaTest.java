package edu.uoc.b2.tema1.ex01;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests de l'exercici 01 — Temperatura immutable
 * NO MODIFIQUES ESTE ARCHIVO / NO MODIFIQUIS AQUEST FITXER
 */
@DisplayName("Ex01 — Temperatura Immutable")
class Exercici01TemperaturaTest {

    @Test
    @DisplayName("Constructor: crea temperatura correctament")
    void constructor_valid() {
        Temperatura t = new Temperatura(25.0);
        assertThat(t.getGrados()).isEqualTo(25.0);
    }

    @Test
    @DisplayName("Constructor: llança excepció per sota del zero absolut")
    void constructor_zeroAbsolut() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Temperatura(-300.0))
                .withMessageContaining("-300");
    }

    @Test
    @DisplayName("Constructor: zero absolut exacte és vàlid (-273.15)")
    void constructor_zeroAbsolutExacte() {
        assertThatNoException().isThrownBy(() -> new Temperatura(-273.15));
    }

    @Test
    @DisplayName("toFahrenheit: 0°C → 32°F")
    void toFahrenheit_zero() {
        assertThat(new Temperatura(0.0).toFahrenheit()).isEqualTo(32.0);
    }

    @Test
    @DisplayName("toFahrenheit: 100°C → 212°F")
    void toFahrenheit_cent() {
        assertThat(new Temperatura(100.0).toFahrenheit()).isEqualTo(212.0);
    }

    @Test
    @DisplayName("toFahrenheit: -40°C → -40°F (punt d'intersecció)")
    void toFahrenheit_interseccio() {
        assertThat(new Temperatura(-40.0).toFahrenheit()).isEqualTo(-40.0);
    }

    @Test
    @DisplayName("toKelvin: 0°C → 273.15 K")
    void toKelvin_zero() {
        assertThat(new Temperatura(0.0).toKelvin()).isEqualTo(273.15);
    }

    @Test
    @DisplayName("toKelvin: 100°C → 373.15 K")
    void toKelvin_cent() {
        assertThat(new Temperatura(100.0).toKelvin()).isEqualTo(373.15);
    }

    @Test
    @DisplayName("sumar: retorna NOVA temperatura (immutabilitat)")
    void sumar_retorna_nova_instancia() {
        Temperatura original = new Temperatura(20.0);
        Temperatura nova = original.sumar(10.0);

        // L'original NO s'ha modificat
        assertThat(original.getGrados()).isEqualTo(20.0);
        // La nova té la suma
        assertThat(nova.getGrados()).isEqualTo(30.0);
        // Són objectes DIFERENTS
        assertThat(nova).isNotSameAs(original);
    }

    @Test
    @DisplayName("restar: retorna NOVA temperatura")
    void restar_retorna_nova_instancia() {
        Temperatura original = new Temperatura(20.0);
        Temperatura nova = original.restar(5.0);

        assertThat(original.getGrados()).isEqualTo(20.0); // No canviat
        assertThat(nova.getGrados()).isEqualTo(15.0);
    }

    @Test
    @DisplayName("restar: llança excepció si baixa del zero absolut")
    void restar_baix_zeroAbsolut() {
        Temperatura t = new Temperatura(0.0);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> t.restar(300.0));
    }

    @Test
    @DisplayName("Encadenament d'operacions (method chaining)")
    void encadenament() {
        Temperatura resultat = new Temperatura(10.0)
                .sumar(20.0) // 30
                .sumar(5.0) // 35
                .restar(15.0); // 20

        assertThat(resultat.getGrados()).isEqualTo(20.0);
    }

    @Test
    @DisplayName("equals: mateixes temperatures son iguals")
    void equals_iguals() {
        assertThat(new Temperatura(25.0)).isEqualTo(new Temperatura(25.0));
    }

    @Test
    @DisplayName("equals: temperatures diferents NO son iguals")
    void equals_diferent() {
        assertThat(new Temperatura(25.0)).isNotEqualTo(new Temperatura(26.0));
    }

    @Test
    @DisplayName("toString: format '25.00 °C'")
    void toString_format() {
        assertThat(new Temperatura(25.0).toString()).isEqualTo("25.00 °C");
    }

    @Test
    @DisplayName("toString: valor decimal '18.50 °C'")
    void toString_decimal() {
        assertThat(new Temperatura(18.5).toString()).isEqualTo("18.50 °C");
    }

    @Test
    @DisplayName("toString: temperatura negativa '-10.00 °C'")
    void toString_negatiu() {
        assertThat(new Temperatura(-10.0).toString()).isEqualTo("-10.00 °C");
    }
}
