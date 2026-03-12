package edu.uoc.b2.tema6.ex02;

/**
 * ============================================================
 * EXERCICI 02 Excepcions / Excepciones — Custom exception wrapping ⭐⭐⭐
 * ============================================================
 *
 * [CAT] OBJECTIU: Crear un procés de validació simple que embolcalli (wrap)
 * errors estranys
 * i utilitzi una Checked Exception personalitzada `UsuarioInvalidoException`.
 * [ES] OBJETIVO: Crear un proceso de validación simple que envuelva (wrap)
 * errores extraños
 * y utilice una Checked Exception personalizada `UsuarioInvalidoException`.
 */
public class ValidadorUsuario {

    /**
     * [CAT] Valida i retorna l'edat extreta des d'un camp formatat de text.
     * [ES] Valida y retorna la edad extraída desde un campo formateado de texto.
     *
     * @param infoUsuario ex: "Nom,18" o "Maria,"
     * @throws UsuarioInvalidoException si l'string està corrupte o no té edat / si
     *                                  el string está corrupto o no tiene edad
     */
    public static int validarYExtraerEdad(String infoUsuario) throws UsuarioInvalidoException {
        if (infoUsuario == null || infoUsuario.isEmpty()) {
            throw new UsuarioInvalidoException("Usuario inválido por cadena vacía");
        }
        String[] parts = infoUsuario.split(",");
        if (parts.length != 2) {
            throw new UsuarioInvalidoException("Usuario inválido");
        }
        int edad;
        try {
            edad = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new UsuarioInvalidoException("Edad inválida", e);
        }
        if (edad < 18) {
            throw new UsuarioInvalidoException("Menor de edad");
        }
        return edad;
    }
}
