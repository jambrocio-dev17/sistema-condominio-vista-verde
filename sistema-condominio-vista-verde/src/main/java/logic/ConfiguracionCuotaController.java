package logic;

import model.ConfiguracionCuotaModel;
import DAO.ConfiguracionCuotaDAO;
import ui.ConfiguracionCuota;

import javax.swing.JOptionPane;

public class ConfiguracionCuotaController {
    
    private ConfiguracionCuotaModel modelo;
    private ConfiguracionCuotaDAO dao;
    private ConfiguracionCuota vista;

    public ConfiguracionCuotaController(ConfiguracionCuotaModel modelo, ConfiguracionCuotaDAO dao, ConfiguracionCuota vista) {
        this.modelo = modelo;
        this.dao = dao;
        this.vista = vista;
        
    }
    
    public float getCuotaActual() {
        // Usamos el método que creamos en el DAO
        return (float) dao.obtenerCuotaActual(); 
    }

    public float calcularRecaudacionEsperada(float cuota) {
        return cuota * 30; 
    }

    public void GuardarDatos(int id, float monto){
        // 1. Actualizamos nuestro modelo con los datos que llegaron de la vista
        modelo.setId(id);
        modelo.setMonto(monto);
        
        // 2. Le pasamos el modelo al DAO para que haga el UPDATE en Postgres
        boolean exito = dao.actualizar(modelo);
        
        // 3. Mostramos el resultado al usuario
        if (exito) {
            JOptionPane.showMessageDialog(vista, "¡La cuota se actualizó correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar la cuota. Revisa la consola.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método auxiliar para limpiar las cajas de texto
    private void limpiarCampos() {
        // vista.txtId.setText("");
        // vista.txtMonto.setText("");
    }
    //its not working now
}