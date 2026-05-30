package logic;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
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

                String htmlCuerpo = String.format("""
                    <!DOCTYPE html>
                    <html lang="es">
                    <head>
                      <meta charset="UTF-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <style>
                        body { margin:0; padding:0; background-color:#f0ffea; font-family:Arial,Helvetica,sans-serif; }
                        .wrapper { padding:30px 15px; background-color:#f0ffea; }
                        .container { max-width:600px; margin:0 auto; background:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 4px 18px rgba(26,58,10,0.15); }
                        .header { background-color:#1a3a0a; padding:32px 40px; text-align:center; }
                        .header-icon { width:54px; height:54px; background:#2d5a1e; border-radius:50%%; display:inline-flex; align-items:center; justify-content:center; margin-bottom:14px; }
                        .header h1 { color:#8fcc6f; margin:0 0 6px; font-size:22px; letter-spacing:1px; }
                        .header p { color:#a8d888; margin:0; font-size:13px; }
                        .ribbon { background:linear-gradient(90deg,#3a7a1a,#8fcc6f,#3a7a1a); height:5px; }
                        .body { padding:32px 40px; }
                        .greeting { color:#1a3a0a; font-size:16px; margin:0 0 14px; }
                        .intro { color:#555; font-size:14px; line-height:1.65; margin:0 0 26px; }
                        .amount-badge { background:#eaf6e0; border:2px solid #3a7a1a; border-radius:10px; padding:22px 20px; text-align:center; margin-bottom:26px; }
                        .amount-badge .label { color:#5a7a45; font-size:12px; text-transform:uppercase; letter-spacing:1px; margin:0 0 6px; }
                        .amount-badge .amount { font-size:40px; font-weight:bold; color:#1a3a0a; margin:0 0 10px; }
                        .status-pill { background:#3a7a1a; color:#ffffff; font-size:13px; font-weight:bold; padding:5px 22px; border-radius:20px; display:inline-block; letter-spacing:1px; }
                        .details-table { width:100%%; border-collapse:collapse; margin-bottom:26px; border-radius:8px; overflow:hidden; border:1px solid #dff0d0; }
                        .details-table td { padding:11px 16px; font-size:14px; border-bottom:1px solid #e8f5dc; }
                        .details-table tr:last-child td { border-bottom:none; }
                        .details-table td.key { color:#3a7a1a; font-weight:bold; width:38%%; background:#f4fbee; }
                        .details-table td.val { color:#222; }
                        .closing { color:#555; font-size:13px; line-height:1.65; margin:0 0 10px; }
                        .footer { background:#1a3a0a; color:#8fcc6f; text-align:center; padding:22px 40px; font-size:12px; line-height:1.7; }
                        .footer strong { color:#ffffff; font-size:13px; }
                        .footer .divider { color:#2d5a1e; margin:8px 0; }
                      </style>
                    </head>
                    <body>
                      <div class="wrapper">
                        <div class="container">
                          <div class="header">
                            <div class="header-icon">
                              <svg width="28" height="28" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M12 2L3 7v10l9 5 9-5V7L12 2z" stroke="#8fcc6f" stroke-width="1.8" fill="none"/>
                                <path d="M12 7v5M12 15h.01" stroke="#8fcc6f" stroke-width="1.8" stroke-linecap="round"/>
                              </svg>
                            </div>
                            <h1>Condominio Vista Verde</h1>
                            <p>Sistema de Administración de Cuotas</p>
                          </div>
                          <div class="ribbon"></div>
                          <div class="body">
                            <p class="greeting">Estimado/a <strong>%s</strong>,</p>
                            <p class="intro">
                              Nos complace informarle que su pago de cuota mensual ha sido
                              registrado exitosamente en nuestro sistema. A continuación encontrará
                              el resumen de su transacción.
                            </p>
                            <div class="amount-badge">
                              <p class="label">Monto pagado</p>
                              <p class="amount">Q %,.2f</p>
                              <span class="status-pill">&#10003;&nbsp; PAGADO</span>
                            </div>
                            <table class="details-table">
                              <tr><td class="key">N.° de Casa</td><td class="val">Casa %d</td></tr>
                              <tr><td class="key">Período</td><td class="val">%s %d</td></tr>
                              <tr><td class="key">Concepto</td><td class="val">Cuota de mantenimiento</td></tr>
                              <tr><td class="key">Estado</td><td class="val" style="color:#3a7a1a;font-weight:bold;">Pagado</td></tr>
                            </table>
                            <p class="closing">
                              Agradecemos su puntualidad. Si tiene alguna consulta sobre este comprobante,
                              comuníquese directamente con la administración del condominio.
                            </p>
                          </div>
                          <div class="footer">
                            <strong>Condominio Vista Verde</strong>
                            <div class="divider">&#9135;&#9135;&#9135;&#9135;&#9135;&#9135;&#9135;&#9135;&#9135;</div>
                            Administración &bull; Sistema de Cuotas<br>
                            <span style="color:#6faa55;font-size:11px;">
                              Este es un mensaje automático, por favor no responda a este correo.
                            </span>
                          </div>
                        </div>
                      </div>
                    </body>
                    </html>
                    """,
                    nombrePropietario, monto, numeroCasa, meses[mes], anio
                );

                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(htmlCuerpo, "text/html; charset=UTF-8");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(htmlPart);
                msg.setContent(multipart);

                Transport.send(msg);
                System.out.println("Correo de confirmación enviado a: " + destinatario);
            } catch (Exception e) {
                System.err.println("No se pudo enviar el correo de confirmación: " + e.getMessage());
            }
        }, "email-sender").start();
    }
}