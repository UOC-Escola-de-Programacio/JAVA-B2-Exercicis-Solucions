package edu.uoc.b2.tema6.ex02;

/**
 * [CAT] Excepció personalitzada Checked (requereix throws).
 * [ES] Excepción personalizada Checked (requiere throws).
 */
public class UsuarioInvalidoException extends Exception {

    public UsuarioInvalidoException(String message) {
        // [ES] TODO — Lama al super constructor.
        // [CAT] TODO — Crida al super constructor.
        super(message);
    }

    public UsuarioInvalidoException(String message, Throwable cause) {
        // [ES] TODO — Lama al super constructor (wrapping).
        // [CAT] TODO — Crida al super constructor (wrapping).
        super(message, cause);
    }
}
