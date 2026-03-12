package edu.uoc.b2.tema6.ex02;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Tema 6 — Exercici 02 Excepciones (Excepcion Checked Custom)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Exercici02ValidadorTest {

    @Test
    @DisplayName("validarYExtraerEdad: Caso bueno funciona correctamente")
    void testEdadCorrecta() throws Exception {
        int edad = ValidadorUsuario.validarYExtraerEdad("Juan,25");
        assertThat(edad).isEqualTo(25);
    }

    @Test
    @DisplayName("validarYExtraerEdad: Null o vacio lanza nuestra excepció")
    void testNuloOParaVacio() {
        assertThatExceptionOfType(UsuarioInvalidoException.class)
                .isThrownBy(() -> ValidadorUsuario.validarYExtraerEdad(""));

        assertThatExceptionOfType(UsuarioInvalidoException.class)
                .isThrownBy(() -> ValidadorUsuario.validarYExtraerEdad(null));
    }

    @Test
    @DisplayName("validarYExtraerEdad: Faltan variables lanza excepció")
    void testFormatoInvalido() {
        assertThatExceptionOfType(UsuarioInvalidoException.class)
                .isThrownBy(() -> ValidadorUsuario.validarYExtraerEdad("SoloNombre"));
    }

    @Test
    @DisplayName("validarYExtraerEdad: Letras en edad lanza excepció envolviendo (wrap) a NumberFormatException")
    void testLetrasEdad() {
        Throwable t = org.junit.jupiter.api.Assertions.assertThrows(UsuarioInvalidoException.class,
                () -> ValidadorUsuario.validarYExtraerEdad("Ana,edatVint"));

        assertThat(t.getCause()).isNotNull();
        assertThat(t.getCause()).isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("validarYExtraerEdad: Menor de 18 lanza excepció")
    void testMenor18() {
        assertThatExceptionOfType(UsuarioInvalidoException.class)
                .isThrownBy(() -> ValidadorUsuario.validarYExtraerEdad("Luis,17"));
    }
}
