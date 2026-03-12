package edu.uoc.b2.tema1.ex02;

import org.junit.jupiter.api.*;
import java.util.List;
import java.util.function.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Ex02 — Lambdas y Interfaces Funcionales")
class Exercici02FiltroTextosTest {

    @Test
    @DisplayName("predicadoLongitudMinima: rechaza texto corto")
    void predicat_longitud_curta() {
        assertThat(FiltroTextos.predicadoLongitudMinima(5).test("hola")).isFalse();
    }

    @Test
    @DisplayName("predicadoLongitudMinima: acepta texto largo")
    void predicat_longitud_llarga() {
        assertThat(FiltroTextos.predicadoLongitudMinima(5).test("hola!")).isTrue();
    }

    @Test
    @DisplayName("predicadoLongitudMinima: acepta texto de longitud exacta")
    void predicat_longitud_exacta() {
        assertThat(FiltroTextos.predicadoLongitudMinima(4).test("java")).isTrue();
    }

    @Test
    @DisplayName("predicadoEmpiezaPor: acepta prefijo correcto")
    void predicat_prefix_ok() {
        assertThat(FiltroTextos.predicadoEmpiezaPor("hol").test("hola")).isTrue();
    }

    @Test
    @DisplayName("predicadoEmpiezaPor: rechaza prefijo incorrecto")
    void predicat_prefix_ko() {
        assertThat(FiltroTextos.predicadoEmpiezaPor("adeu").test("hola")).isFalse();
    }

    @Test
    @DisplayName("predicadoEmpiezaPor: case-insensitive")
    void predicat_prefix_caseInsensitive() {
        assertThat(FiltroTextos.predicadoEmpiezaPor("JAVA").test("java B2")).isTrue();
    }

    @Test
    @DisplayName("transformadorNormalizar: trim + uppercase")
    void transformador_normalitzar() {
        assertThat(FiltroTextos.transformadorNormalizar().apply("  hola món  "))
                .isEqualTo("HOLA MÓN");
    }

    @Test
    @DisplayName("filtrar: devuelve lista correcta")
    void filtrar_basic() {
        List<String> llista = List.of("java", "c++", "python", "go");
        List<String> result = FiltroTextos.filtrar(llista,
                FiltroTextos.predicadoLongitudMinima(4));
        assertThat(result).containsExactlyInAnyOrder("java", "python");
    }

    @Test
    @DisplayName("filtrar: lista vacía → resultado vacío")
    void filtrar_buit() {
        assertThat(FiltroTextos.filtrar(List.of(), s -> true)).isEmpty();
    }

    @Test
    @DisplayName("filtrar: ningún elemento pasa el filtro")
    void filtrar_capPass() {
        List<String> llista = List.of("a", "bb", "ccc");
        assertThat(FiltroTextos.filtrar(llista, s -> s.length() > 10)).isEmpty();
    }

    @Test
    @DisplayName("transformar: aplica la función a todos")
    void transformar_tots() {
        List<String> result = FiltroTextos.transformar(
                List.of("hola", "adeu"),
                FiltroTextos.transformadorNormalizar());
        assertThat(result).containsExactly("HOLA", "ADEU");
    }

    @Test
    @DisplayName("filtrarYTransformar: filtra y aplica función")
    void filtrarTransformar() {
        List<String> result = FiltroTextos.filtrarYTransformar(
                List.of("hola", "hei", "adeu"),
                FiltroTextos.predicadoLongitudMinima(4),
                FiltroTextos.transformadorNormalizar());
        assertThat(result).containsExactlyInAnyOrder("HOLA", "ADEU");
    }

    @Test
    @DisplayName("combinarAnd: pasan ambos predicados")
    void combinarAnd_passaTots() {
        Predicate<String> p = FiltroTextos.combinarAnd(
                FiltroTextos.predicadoLongitudMinima(3),
                FiltroTextos.predicadoEmpiezaPor("ja"));
        assertThat(p.test("java")).isTrue(); // llargada >= 3 AND comença per "ja"
        assertThat(p.test("ja")).isFalse(); // llargada = 2 < 3
        assertThat(p.test("hola")).isFalse(); // No comença per "ja"
    }
}
