/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistema.condominio.vista.verde;
import ui.Login;
/**
 *
 * @author Josue Ambrocio
 */
public class SistemaCondominioVistaVerde {

    public static void main(String[] args) {
        logic.ThemeManager.aplicarTema();
        configuration.DatabaseInitializer.inicializar();
        java.awt.EventQueue.invokeLater(() -> {
            Login login = new Login();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
        });
    }
}