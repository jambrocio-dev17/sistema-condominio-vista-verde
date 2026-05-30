package logic;

import javax.swing.JOptionPane;
import ui.Login;
import ui.menuPrincipal;

public class LoginController {

    private static final String USUARIO_VALIDO  = "iusr_vistaverde";
    private static final String PASSWORD_VALIDO = "R3sidencial2026%";

    /**
     * Valida las credenciales.
     * Si son correctas cierra la ventana de login y abre el menú principal.
     */
    public void procesarIngreso(String usuario, String password, Login loginVista) {
        if (usuario.equals(USUARIO_VALIDO) && password.equals(PASSWORD_VALIDO)) {
            menuPrincipal menu = new menuPrincipal();
            menu.setVisible(true);
            loginVista.dispose();
        } else {
            JOptionPane.showMessageDialog(loginVista,
                "Credenciales incorrectas. Por favor, intente de nuevo.",
                "Error de Acceso",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}