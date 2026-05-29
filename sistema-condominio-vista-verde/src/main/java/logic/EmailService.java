package logic;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

public class EmailService {

    private static final String HOST     = "smtp.gmail.com";
    private static final int    PORT     = 587;
    private static final String FROM     = "adolfobank123@gmail.com";
    
    // AQUÍ ESTÁ EL CAMBIO: Leemos la contraseña del sistema, no del código.
    private static final String PASSWORD = System.getenv("EMAIL_PASSWORD");

    private static final String FORMATO_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    // ... (El resto de tu código queda EXACTAMENTE igual a partir de aquí)
    
    public static String validarCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return null;
        }
        if (!correo.matches(FORMATO_REGEX)) {
            return "El formato del correo electrónico no es válido.\nEjemplo: usuario@dominio.com";
        }
        String domain = correo.substring(correo.indexOf('@') + 1);
        if (!dominioTieneMX(domain)) {
            return "El dominio \"" + domain + "\" no existe o no puede recibir correos.\n"
                 + "Verifique que el correo sea correcto.";
        }
        return null;
    }

    private static boolean dominioTieneMX(String domain) {
        // ... (Tu código actual)
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            env.put("java.naming.provider.url", "dns:");
            env.put("com.sun.jndi.dns.timeout.initial", "2000");
            env.put("com.sun.jndi.dns.timeout.retries", "1");
            InitialDirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});
            Attribute mx = attrs.get("MX");
            return mx != null && mx.size() > 0;
        } catch (NamingException e) {
            return false;
        }
    }

    public static void enviarConfirmacionPago(String destinatario, String nombrePropietario,
                                              int numeroCasa, int mes, int anio, double monto) {
        

        String testPass = System.getenv("EMAIL_PASSWORD");
        if (testPass == null) {
            System.out.println("❌ ERROR: Java NO está leyendo la variable de entorno.");
        } else {
            System.out.println("✅ ÉXITO: Java sí leyó la variable de entorno.");
        }
                                                
        // Validación de seguridad adicional:
        if (PASSWORD == null || PASSWORD.isEmpty()) {
            System.err.println("ERROR CRÍTICO: La contraseña del correo no está configurada en las variables de entorno.");
            return;
        }

        if (destinatario == null || destinatario.isBlank()) return;

        new Thread(() -> {
            String[] meses = {
                "", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            };

            Properties props = new Properties();
            props.put("mail.smtp.auth",              "true");
            props.put("mail.smtp.starttls.enable",   "true");
            props.put("mail.smtp.host",              HOST);
            props.put("mail.smtp.port",              String.valueOf(PORT));
            props.put("mail.smtp.ssl.protocols",     "TLSv1.2");
            props.put("mail.smtp.connectiontimeout", "8000");
            props.put("mail.smtp.timeout",           "8000");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM, PASSWORD);
                }
            });

            try {
                MimeMessage msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(FROM, "Condominio Vista Verde", "UTF-8"));
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
                msg.setSubject("Confirmación de Pago — " + meses[mes] + " " + anio);

                String cuerpo = String.format(
                    "Estimado/a %s,%n%n"
                    + "Su pago ha sido registrado exitosamente en el sistema.%n%n"
                    + "Detalles del pago:%n"
                    + "  - Casa N°    : %d%n"
                    + "  - Período    : %s %d%n"
                    + "  - Monto      : Q %.2f%n%n"
                    + "Gracias por su pago puntual.%n%n"
                    + "Atentamente,%n"
                    + "Administración%n"
                    + "Condominio Vista Verde",
                    nombrePropietario, numeroCasa, meses[mes], anio, monto
                );

                msg.setText(cuerpo, "UTF-8");
                Transport.send(msg);
                System.out.println("Correo de confirmación enviado a: " + destinatario);
            } catch (Exception e) {
                System.err.println("No se pudo enviar el correo de confirmación: " + e.getMessage());
            }
        }, "email-sender").start();
    }
}