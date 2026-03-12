package edu.uoc.b2.tema1.ex05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ============================================================
 * EXERCICIO 05 / EXERCICI 05 — Patrón Observer / Patró Observer ⭐⭐⭐⭐⭐ (Difícil)
 * ============================================================
 *
 * [CAT] OBJECTIU: Implementa el patró Observer per a un sistema de
 * notificacions.
 * Un "canal" pot tenir múltiples "subscriptors". Quan el canal
 * publica un missatge, TOTS els subscriptors el reben.
 *
 * [ES] OBJETIVO: Implementa el patrón Observer para un sistema de
 * notificaciones.
 * Un "canal" puede tener múltiples "suscriptores". Cuando el canal
 * publica un mensaje, TODOS los suscriptores lo reciben.
 *
 * PARTES A IMPLEMENTAR / PARTS A IMPLEMENTAR:
 * 1. Intefaz / Interfície Suscriptor (Observer)
 * 2. Clase CanalNotificacion (Subject / Observable)
 * 3. Implementación / Implementació LogSuscriptor
 * 4. Implementación / Implementació EmailSuscriptor
 * 5. Implementación / Implementació FiltroSuscriptor (decorador — filtra
 * mensajes/missatges per palabra clave / paraula clau)
 *
 * DIAGRAMA:
 *
 * «interface» CanalNotificacion
 * Suscriptor ─────────────────
 * ────────────── ┌──────► suscriptores: List<Suscriptor>
 * + recibir(msg: String) │ + suscribir(s: Suscriptor)
 * ▲ │ + desvincular(s: Suscriptor)
 * │ │ + publicar(msg: String)
 * ┌─────┴──────────────┐ │
 * │ │ │
 * LogSuscriptor EmailSuscriptor FiltroSuscriptor (wraps another)
 * mensajes:List<String> emails:List<String> filtro:String + destino:Suscriptor
 */
public class SistemaNotificaciones {

    /**
     * [CAT] Interfície Observer — tot subscriptor ha d'implementar-la.
     * [ES] Interfaz Observer — todo suscriptor debe implementarla.
     */
    public interface Suscriptor {
        /**
         * [CAT] Rep un missatge del canal al qual està subscrit.
         * [ES] Recibe un mensaje del canal al que está suscrito.
         */
        void recibir(String mensaje);
    }

    /**
     * [CAT] Subject (Observable) — manté la llista de subscriptors i els notifica.
     * [ES] Subject (Observable) — mantiene la lista de suscriptores y los notifica.
     */
    public static class CanalNotificacion {

        private final String nombre;
        private final List<Suscriptor> suscriptores;

        public CanalNotificacion(String nombre) {
            this.nombre = nombre;
            this.suscriptores = new ArrayList<>();
        }

        public String getNombre() {
            return nombre;
        }

        /**
         * [CAT] Afegeix un subscriptor. Si ja existeix, no l'afegeix de nou.
         * [ES] Añade un suscriptor. Si ya existe, no lo añade de nuevo.
         */
        public void suscribir(Suscriptor suscriptor) {
            if (!this.suscriptores.contains(suscriptor)) {
                this.suscriptores.add(suscriptor);
            }
        }

        /**
         * [CAT] Elimina un subscriptor. Si no existeix, no fa res.
         * [ES] Elimina un suscriptor. Si no existe, no hace nada.
         */
        public void desvincular(Suscriptor suscriptor) {
            this.suscriptores.remove(suscriptor);
        }

        /**
         * [CAT] Notifica TOTS els subscriptors amb el missatge.
         * Format del missatge que rep cada subscriptor: "[NomCanal] missatge"
         * Exemple: "[Esports] El Barça ha guanyat!"
         * [ES] Notifica TODOS los suscriptores con el mensaje.
         * Formato del mensaje que recibe cada suscriptor: "[NombreCanal] mensaje"
         * Ejemplo: "[Deportes] ¡El Barça ha ganado!"
         */
        public void publicar(String mensaje) {
            String msg = "[" + nombre + "] " + mensaje;
            for (Suscriptor s : suscriptores) {
                s.recibir(msg);
            }
        }

        /**
         * [CAT] Retorna el nombre de subscriptors actuals.
         * [ES] Devuelve el número de suscriptores actuales.
         */
        public int numeroSuscriptores() {
            return this.suscriptores.size();
        }
    }

    /**
     * [CAT] Subscriptor que guarda tots els missatges rebuts en una llista interna.
     * Útil per a logs i tests.
     * [ES] Suscriptor que guarda todos los mensajes recibidos en una lista interna.
     * Útil para logs y tests.
     */
    public static class LogSuscriptor implements Suscriptor {

        private final List<String> mensajes = new ArrayList<>();

        @Override
        public void recibir(String mensaje) {
            this.mensajes.add(mensaje);
        }

        /**
         * [CAT] Retorna tots els missatges rebuts (en ordre d'arribada).
         * [ES] Devuelve todos los mensajes recibidos (en orden de llegada).
         */
        public List<String> getMensajesRecibidos() {
            return Collections.unmodifiableList(this.mensajes);
        }

        /**
         * [CAT] Retorna el nombre de missatges rebuts.
         * [ES] Devuelve el número de mensajes recibidos.
         */
        public int contarMensajes() {
            return this.mensajes.size();
        }
    }

    /**
     * [CAT] Subscriptor que simula enviar un email (guarda una llista de "emails
     * enviats").
     * [ES] Suscriptor que simula enviar un email (guarda una lista de "emails
     * enviados").
     */
    public static class EmailSuscriptor implements Suscriptor {

        private final String direccion;
        private final List<String> emailsEnviados;

        public EmailSuscriptor(String direccion) {
            this.direccion = direccion;
            this.emailsEnviados = new ArrayList<>();
        }

        public String getDireccion() {
            return direccion;
        }

        @Override
        public void recibir(String mensaje) {
            this.emailsEnviados.add("To: " + direccion + " | " + mensaje);
        }

        public List<String> getEmailsEnviados() {
            return Collections.unmodifiableList(this.emailsEnviados);
        }
    }

    /**
     * [CAT] Subscriptor decorador que FILTRA missatges per paraula clau.
     * Només reenvia al subscriptor destí els missatges que CONTINGUIN la paraula
     * clau (case-insensitive).
     * [ES] Suscriptor decorador que FILTRA mensajes por palabra clave.
     * Solo reenvía al suscriptor destino los mensajes que CONTENGAN la palabra
     * clave (case-insensitive).
     */
    public static class FiltroSuscriptor implements Suscriptor {

        private final String filtro;
        private final Suscriptor destino;

        public FiltroSuscriptor(String filtro, Suscriptor destino) {
            this.filtro = filtro;
            this.destino = destino;
        }

        @Override
        public void recibir(String mensaje) {
            if (mensaje.toLowerCase().contains(filtro.toLowerCase())) {
                destino.recibir(mensaje);
            }
        }
    }
}
