package logic;

import model.ConfiguracionCuotaModel;
import DAO.ConfiguracionCuotaDAO;
import ui.ConfiguracionCuota;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ConfiguracionCuotaController {
    
    
    private ConfiguracionCuotaModel modelo;
    private ConfiguracionCuotaDAO dao;
    private ConfiguracionCuota vista;

    public ConfiguracionCuotaController(ConfiguracionCuotaModel modelo, ConfiguracionCuotaDAO dao, ConfiguracionCuota vista) {
        this.modelo = modelo;
        this.dao = dao;
        this.vista = vista;
        
        // Aquí le decimos a los botones de la vista que este controlador escuchará sus clics
        // this.vista.btnGuardar.addActionListener(this);
        // this.vista.btnActualizar.addActionListener(this);
    }

   public void GuardarDatos(int id, float monto){
       
   }
    
    // Método auxiliar para limpiar las cajas de texto
    private void limpiarCampos() {
        // vista.txtId.setText("");
        // vista.txtMonto.setText("");
    }
}