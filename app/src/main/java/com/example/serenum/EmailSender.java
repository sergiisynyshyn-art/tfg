package com.example.serenum;

import android.os.Handler;
import android.os.Looper;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * EmailSender permite enviar correos electrónicos a través de Gmail usando JavaMail API.
 * Los correos se envían de forma asincrónica en un thread separado para no bloquear la UI.
 */
public class EmailSender {

    // Credenciales de Gmail (CONFIGURAR CON TUS CREDENCIALES REALES)
    // IMPORTANTE: Reemplaza estos valores con tu email y contraseña de aplicación de Gmail
    private static final String EMAIL_FROM = "TU_EMAIL@gmail.com";  // ← CAMBIA ESTO
    private static final String EMAIL_PASSWORD = "TU_CONTRASEÑA_APP"; // ← CAMBIA ESTO

    // Propiedades SMTP para Gmail
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    /**
     * Interfaz para escuchar los resultados del envío de correo.
     */
    public interface EmailCallback {
        /**
         * Se llama cuando el correo se envía correctamente.
         */
        void onSuccess();

        /**
         * Se llama cuando hay un error al enviar el correo.
         *
         * @param error Mensaje de error
         */
        void onError(String error);
    }

    /**
     * Envía un correo de bienvenida al usuario.
     * El envío se realiza de forma asincrónica en un thread separado.
     *
     * @param usuarioEmail Email del usuario destinatario
     * @param usuarioNombre Nombre del usuario para personalizar el mensaje
     * @param callback Callback para recibir el resultado del envío
     */
    public static void enviarCorreoBienvenida(String usuarioEmail, String usuarioNombre, EmailCallback callback) {
        // Crear un thread separado para no bloquear la UI
        new Thread(() -> {
            try {
                // Configurar las propiedades SMTP
                Properties props = new Properties();
                props.put("mail.smtp.host", SMTP_HOST);
                props.put("mail.smtp.port", SMTP_PORT);
                props.put("mail.smtp.auth", "true"); // Se requiere autenticación
                props.put("mail.smtp.starttls.enable", "true"); // Usar TLS
                props.put("mail.smtp.starttls.required", "true");
                props.put("mail.smtp.connectiontimeout", "5000"); // Timeout de conexión
                props.put("mail.smtp.timeout", "5000"); // Timeout de envío

                // Crear sesión SMTP con autenticación
                Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                    @Override
                    protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new jakarta.mail.PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
                    }
                });

                // Crear el mensaje de correo
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL_FROM));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(usuarioEmail));

                // Configurar el asunto del correo
                message.setSubject("Bienvenido a Serenum");

                // Crear el contenido del correo en HTML para mejor formato
                String contenidoHTML = "<html>" +
                        "<body style='font-family: Arial, sans-serif;'>" +
                        "<h2 style='color: #333;'>¡Bienvenido a Serenum, " + usuarioNombre + "!</h2>" +
                        "<p style='color: #666; font-size: 14px;'>" +
                        "Tu cuenta de Google se ha conectado correctamente con Serenum." +
                        "</p>" +
                        "<p style='color: #666; font-size: 14px;'>" +
                        "Ya puedes comenzar a utilizar todas las funcionalidades de nuestra aplicación." +
                        "</p>" +
                        "<p style='margin-top: 20px; color: #999; font-size: 12px;'>" +
                        "Si no fuiste tú quien realizó este registro, ignora este correo." +
                        "</p>" +
                        "</body>" +
                        "</html>";

                // Establecer el contenido del correo como HTML
                message.setContent(contenidoHTML, "text/html; charset=utf-8");

                // Enviar el correo
                Transport.send(message);

                // Ejecutar el callback de éxito en el thread principal
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(callback::onSuccess);
                }

            } catch (MessagingException e) {
                // Ejecutar el callback de error en el thread principal
                String errorMessage = "Error al enviar correo: " + e.getMessage();
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onError(errorMessage));
                }
            }
        }).start();
    }

    /**
     * Actualiza las credenciales de Gmail.
     * IMPORTANTE: Esto debe hacerse de forma segura, preferiblemente leyendo desde una configuración encriptada.
     *
     * @param email Email de la cuenta Gmail
     * @param password Contraseña de aplicación de Gmail
     */
    public static void actualizarCredenciales(String email, String password) {
        // Esta es una solución simple. En producción, se debe encriptar estas credenciales
        // Por ejemplo, usando SharedPreferences encriptado o AndroidKeyStore
    }
}
