package edu.uoc.b2.tema1.ex04;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static edu.uoc.b2.tema1.ex04.GestorUsuarios.*;

@DisplayName("Ex04 — Optional<T>")
class Exercici04OptionalTest {

    private static List<Usuario> USUARIOS;

    @BeforeAll
    static void setup() {
        USUARIOS = List.of(
                new Usuario(1, "Ana", "ana@test.com", "600111222",
                        new Direccion("C/ Major 1", "Barcelona", "08001"), 0.15),
                new Usuario(2, "Pau", "pau@test.com", null,
                        new Direccion("Av. Diagonal", "Barcelona", "08010"), null),
                new Usuario(3, "Laura", "laura@test.com", "611333444",
                        null, 0.0),
                new Usuario(4, "Marc", "marc@test.com", null,
                        null, null));
    }

    @Test
    @DisplayName("buscarPorId: id existente → Present")
    void cercar_existent() {
        assertThat(buscarPorId(USUARIOS, 1)).isPresent()
                .hasValueSatisfying(u -> assertThat(u.nombre()).isEqualTo("Ana"));
    }

    @Test
    @DisplayName("buscarPorId: id inexistente → Empty")
    void cercar_inexistent() {
        assertThat(buscarPorId(USUARIOS, 99)).isEmpty();
    }

    @Test
    @DisplayName("getTelefono: usuario con teléfono → Present")
    void getTelefon_present() {
        assertThat(getTelefono(USUARIOS.get(0))).isPresent()
                .contains("600111222");
    }

    @Test
    @DisplayName("getTelefono: usuario sin teléfono → Empty")
    void getTelefon_empty() {
        assertThat(getTelefono(USUARIOS.get(1))).isEmpty(); // Pau no tiene teléfono
    }

    @Test
    @DisplayName("getCiudad: usuario con dirección devuelve su ciudad")
    void getCiutat_present() {
        assertThat(getCiudad(USUARIOS.get(0))).isEqualTo("Barcelona");
    }

    @Test
    @DisplayName("getCiudad: usuario sin dirección → 'Desconocida'")
    void getCiutat_absent() {
        assertThat(getCiudad(USUARIOS.get(2))).isEqualTo("Desconocida"); // Laura no tiene dirección
    }

    @Test
    @DisplayName("calcularPrecioFinal: con descuento 15% sobre 100€")
    void preu_amb_descompte() {
        assertThat(calcularPrecioFinal(USUARIOS.get(0), 100.0)).isCloseTo(85.0, within(0.001));
    }

    @Test
    @DisplayName("calcularPrecioFinal: sin descuento (null) → precio original")
    void preu_sense_descompte_null() {
        assertThat(calcularPrecioFinal(USUARIOS.get(1), 100.0)).isCloseTo(100.0, within(0.001));
    }

    @Test
    @DisplayName("calcularPrecioFinal: descuento 0.0 → precio original")
    void preu_descompte_zero() {
        assertThat(calcularPrecioFinal(USUARIOS.get(2), 100.0)).isCloseTo(100.0, within(0.001));
    }

    @Test
    @DisplayName("nombrePorEmail: email existente → nombre")
    void nom_per_email_existent() {
        assertThat(nombrePorEmail(USUARIOS, "laura@test.com")).isEqualTo("Laura");
    }

    @Test
    @DisplayName("nombrePorEmail: case-insensitive")
    void nom_per_email_case() {
        assertThat(nombrePorEmail(USUARIOS, "ANA@TEST.COM")).isEqualTo("Ana");
    }

    @Test
    @DisplayName("nombrePorEmail: email inexistente → NoSuchElementException")
    void nom_per_email_inexistent() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> nombrePorEmail(USUARIOS, "noexisteix@test.com"));
    }

    @Test
    @DisplayName("primeroConDescuento: devuelve Ana (15%)")
    void primer_amb_descompte() {
        Optional<Usuario> result = primeroConDescuento(USUARIOS);
        assertThat(result).isPresent();
        assertThat(result.get().nombre()).isEqualTo("Ana");
    }

    @Test
    @DisplayName("primeroConDescuento: lista sin ningún descuento → Empty")
    void primer_amb_descompte_buit() {
        List<Usuario> sense = List.of(
                new Usuario(10, "X", "x@x.com", null, null, null),
                new Usuario(11, "Y", "y@y.com", null, null, 0.0));
        assertThat(primeroConDescuento(sense)).isEmpty();
    }
}
