package logic;

import javax.swing.JOptionPane;
import ui.Login;
import ui.menuPrincipal;

public class LoginController {
    
    // Credenciales requeridas por el proyecto 
    private final String USUARIO_VALIDO = "iusr_vistaverde";
    private final String PASSWORD_VALIDO = "R3sidencial2026%";
    
    private Login vistaLogin;
    private menuPrincipal mPrincipal = new menuPrincipal();

    /**
     * Procesa el intento de ingreso al sistema
     * @param usuario Texto ingresado en el campo de usuario
     * @param password Texto ingresado en el campo de contraseña
     */
    public void procesarIngreso(String usuario, String password) {
        // Validación lógica [cite: 34]
        if (usuario.equals(USUARIO_VALIDO) && password.equals(PASSWORD_VALIDO)) {
            //abrirMenuPrincipal();
            JOptionPane.showMessageDialog(
                null,
                "Login Exitoso",
                "Información",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            mPrincipal.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(vistaLogin, 
                "Credenciales incorrectas. Por favor, intente de nuevo.", 
                "Error de Acceso", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /*private void abrirMenuPrincipal() {
        // Instancia la Pantalla 2 - Inicio [cite: 36]
        VistaInicio inicio = new VistaInicio();
        inicio.setVisible(true);
        
        // Cierra la pantalla de login
        vistaLogin.dispose();
    }*/
}