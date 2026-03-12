package edu.uoc.b2.tema1.ex05;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static edu.uoc.b2.tema1.ex05.SistemaNotificaciones.*;

@DisplayName("Ex05 — Patrón Observer: Sistema de Notificaciones")
class Exercici05ObserverTest {

    private CanalNotificacion canal;
    private LogSuscriptor log1, log2;

    @BeforeEach
    void setup() {
        canal = new CanalNotificacion("Noticias");
        log1 = new LogSuscriptor();
        log2 = new LogSuscriptor();
    }

    @Test
    @DisplayName("Canal nuevo: 0 suscriptores")
    void canal_nou_sense_subscriptors() {
        assertThat(canal.numeroSuscriptores()).isZero();
    }

    @Test
    @DisplayName("suscribir: añade suscriptores correctamente")
    void subscriure_incrementa() {
        canal.suscribir(log1);
        assertThat(canal.numeroSuscriptores()).isEqualTo(1);
        canal.suscribir(log2);
        assertThat(canal.numeroSuscriptores()).isEqualTo(2);
    }

    @Test
    @DisplayName("suscribir: no permite duplicados")
    void subscriure_no_duplicats() {
        canal.suscribir(log1);
        canal.suscribir(log1); // Mismo objeto
        assertThat(canal.numeroSuscriptores()).isEqualTo(1);
    }

    @Test
    @DisplayName("desvincular: elimina el suscriptor")
    void desvincular() {
        canal.suscribir(log1);
        canal.suscribir(log2);
        canal.desvincular(log1);
        assertThat(canal.numeroSuscriptores()).isEqualTo(1);
    }

    @Test
    @DisplayName("desvincular: si no existe, no hace nada")
    void desvincular_inexistent() {
        canal.suscribir(log1);
        assertThatNoException().isThrownBy(() -> canal.desvincular(log2));
        assertThat(canal.numeroSuscriptores()).isEqualTo(1);
    }

    @Test
    @DisplayName("publicar: todos los suscriptores reciben el mensaje")
    void publicar_tots_reben() {
        canal.suscribir(log1);
        canal.suscribir(log2);
        canal.publicar("Breaking news!");

        assertThat(log1.contarMensajes()).isEqualTo(1);
        assertThat(log2.contarMensajes()).isEqualTo(1);
    }

    @Test
    @DisplayName("publicar: formato '[NombreCanal] mensaje'")
    void publicar_format() {
        canal.suscribir(log1);
        canal.publicar("Hola!");

        assertThat(log1.getMensajesRecibidos().get(0)).isEqualTo("[Noticias] Hola!");
    }

    @Test
    @DisplayName("publicar: múltiples mensajes en orden")
    void publicar_multiples() {
        canal.suscribir(log1);
        canal.publicar("M1");
        canal.publicar("M2");
        canal.publicar("M3");

        assertThat(log1.getMensajesRecibidos())
                .containsExactly("[Noticias] M1", "[Noticias] M2", "[Noticias] M3");
    }

    @Test
    @DisplayName("desvincular: el desvinculado NO recibe más mensajes")
    void desvincular_no_rep_mes() {
        canal.suscribir(log1);
        canal.suscribir(log2);
        canal.publicar("Primero");
        canal.desvincular(log1);
        canal.publicar("Segundo");

        assertThat(log1.contarMensajes()).isEqualTo(1); // Solo recibió "Primero"
        assertThat(log2.contarMensajes()).isEqualTo(2); // Recibe los dos
    }

    @Test
    @DisplayName("EmailSuscriptor: guarda el email enviado")
    void email_subscriptor() {
        EmailSuscriptor email = new EmailSuscriptor("user@test.com");
        canal.suscribir(email);
        canal.publicar("Aviso importante");

        assertThat(email.getEmailsEnviados()).hasSize(1);
        assertThat(email.getEmailsEnviados().get(0))
                .startsWith("To: user@test.com")
                .contains("[Noticias] Aviso importante");
    }

    @Test
    @DisplayName("FiltroSuscriptor: pasa mensajes con palabra clave")
    void filtre_subscriptor_passa() {
        LogSuscriptor destino = new LogSuscriptor();
        FiltroSuscriptor filtro = new FiltroSuscriptor("urgente", destino);
        canal.suscribir(filtro);

        canal.publicar("Mensaje urgente!"); // Contiene "urgente" → pasa
        canal.publicar("Mensaje normal"); // No contiene "urgente" → bloquea

        assertThat(destino.contarMensajes()).isEqualTo(1);
        assertThat(destino.getMensajesRecibidos().get(0)).contains("urgente");
    }

    @Test
    @DisplayName("FiltroSuscriptor: case-insensitive")
    void filtre_case_insensitive() {
        LogSuscriptor destino = new LogSuscriptor();
        FiltroSuscriptor filtro = new FiltroSuscriptor("URGENTE", destino);
        canal.suscribir(filtro);
        canal.publicar("Mensaje urgente recibido");
        assertThat(destino.contarMensajes()).isEqualTo(1);
    }

    @Test
    @DisplayName("Escenario completo: canal con 3 suscriptores y filtro")
    void escenari_complet() {
        LogSuscriptor adminLog = new LogSuscriptor();
        EmailSuscriptor email1 = new EmailSuscriptor("admin@test.com");
        LogSuscriptor filtradoLog = new LogSuscriptor();
        FiltroSuscriptor filtro = new FiltroSuscriptor("crítico", filtradoLog);

        canal.suscribir(adminLog);
        canal.suscribir(email1);
        canal.suscribir(filtro);
        assertThat(canal.numeroSuscriptores()).isEqualTo(3);

        canal.publicar("Error crítico en el servidor"); // Todos lo reciben; filtro lo deja pasar
        canal.publicar("Información general"); // Todos lo reciben; filtro lo bloquea

        assertThat(adminLog.contarMensajes()).isEqualTo(2);
        assertThat(email1.getEmailsEnviados()).hasSize(2);
        assertThat(filtradoLog.contarMensajes()).isEqualTo(1); // Solo el crítico
    }
}
