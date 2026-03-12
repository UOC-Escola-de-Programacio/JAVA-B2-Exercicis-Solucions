package edu.uoc.b2.tema6.ex05;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static edu.uoc.b2.tema6.ex05.SistemaComandes.*;

@DisplayName("Ex05 — SistemaComandes: Jerarquia d'excepcions")
class Exercici05SistemaComandesTest {

    private SistemaComandes sistema;

    @BeforeEach
    void setUp() {
        sistema = new SistemaComandes();
        sistema.afegirProducte(new Producte(1, "Laptop", 999.99, 5));
        sistema.afegirProducte(new Producte(2, "Ratolí", 29.99, 50));
        sistema.afegirProducte(new Producte(3, "Taula", 199.99, 0)); // Estoc 0!
    }

    @Test
    @DisplayName("Jerarquia: ComandaException és RuntimeException")
    void jerarquia_unchecked() {
        assertThat(new ComandaException("test")).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Jerarquia: tots hereten de ComandaException")
    void jerarquia_herencia() {
        assertThat(new ProducteNoTrobatException(1)).isInstanceOf(ComandaException.class);
        assertThat(new EstocInsuficientException("X", 2, 5)).isInstanceOf(ComandaException.class);
        assertThat(new ComandaInvalidaException("test")).isInstanceOf(ComandaException.class);
    }

    @Test
    @DisplayName("ProducteNoTrobatException: guarda l'ID del producte")
    void producte_no_trobat_id() {
        var ex = new ProducteNoTrobatException(42);
        assertThat(ex.getProducteId()).isEqualTo(42);
        assertThat(ex.getMessage()).contains("42");
    }

    @Test
    @DisplayName("EstocInsuficientException: guarda tots els camps")
    void estoc_insuficient_camps() {
        var ex = new EstocInsuficientException("Laptop", 3, 10);
        assertThat(ex.getNomProducte()).isEqualTo("Laptop");
        assertThat(ex.getEstocDisponible()).isEqualTo(3);
        assertThat(ex.getEstocDemanat()).isEqualTo(10);
        assertThat(ex.getMessage()).contains("Laptop").contains("3").contains("10");
    }

    @Test
    @DisplayName("processarComanda: comanda buida → ComandaInvalidaException")
    void processar_buit() {
        assertThatExceptionOfType(ComandaInvalidaException.class)
                .isThrownBy(() -> sistema.processarComanda(List.of()));
    }

    @Test
    @DisplayName("processarComanda: quantitat zero → ComandaInvalidaException")
    void processar_quantitat_zero() {
        assertThatExceptionOfType(ComandaInvalidaException.class)
                .isThrownBy(() -> sistema.processarComanda(
                        List.of(new LineaComanda(1, 0))))
                .withMessageContaining("0");
    }

    @Test
    @DisplayName("processarComanda: producte inexistent → ProducteNoTrobatException")
    void processar_producte_inexistent() {
        assertThatExceptionOfType(ProducteNoTrobatException.class)
                .isThrownBy(() -> sistema.processarComanda(
                        List.of(new LineaComanda(999, 1))))
                .satisfies(e -> assertThat(e.getProducteId()).isEqualTo(999));
    }

    @Test
    @DisplayName("processarComanda: estoc insuficient → EstocInsuficientException")
    void processar_estoc_insuficient() {
        assertThatExceptionOfType(EstocInsuficientException.class)
                .isThrownBy(() -> sistema.processarComanda(
                        List.of(new LineaComanda(3, 1)) // Taula: estoc=0, demanat=1
                ))
                .satisfies(e -> {
                    assertThat(e.getNomProducte()).isEqualTo("Taula");
                    assertThat(e.getEstocDisponible()).isZero();
                    assertThat(e.getEstocDemanat()).isEqualTo(1);
                });
    }

    @Test
    @DisplayName("processarComanda: comanda vàlida retorna resultat")
    void processar_valid() {
        Resultat r = sistema.processarComanda(List.of(
                new LineaComanda(1, 2), // 2x Laptop = 1999.98
                new LineaComanda(2, 3) // 3x Ratolí = 89.97
        ));
        assertThat(r.codiComanda()).startsWith("CMD-");
        assertThat(r.total()).isCloseTo(2089.95, within(0.01));
        assertThat(r.detalls()).hasSize(2);
    }

    @Test
    @DisplayName("intentarProcessar: comanda errònia → empty + log d'error")
    void intentar_error() {
        Optional<Resultat> res = sistema.intentarProcessar(
                List.of(new LineaComanda(999, 1)));
        assertThat(res).isEmpty();
        assertThat(sistema.getLogErrors()).hasSize(1);
        assertThat(sistema.getLogErrors().get(0))
                .contains("ProducteNoTrobatException");
    }

    @Test
    @DisplayName("intentarProcessar: comanda vàlida → present, sense log d'error")
    void intentar_valid() {
        Optional<Resultat> res = sistema.intentarProcessar(
                List.of(new LineaComanda(2, 1)) // Ratolí x1
        );
        assertThat(res).isPresent();
        assertThat(sistema.getLogErrors()).isEmpty();
    }

    @Test
    @DisplayName("Múltiples comandes vàlides: codis incrementals")
    void codis_incrementals() {
        Resultat r1 = sistema.processarComanda(List.of(new LineaComanda(2, 1)));
        Resultat r2 = sistema.processarComanda(List.of(new LineaComanda(2, 1)));
        assertThat(r1.codiComanda()).isNotEqualTo(r2.codiComanda());
    }
}
