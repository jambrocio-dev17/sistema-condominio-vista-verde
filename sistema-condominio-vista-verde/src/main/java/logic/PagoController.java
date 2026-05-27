package logic;

import DAO.ConfiguracionCuotaDAO; // Importación corregida
import DAO.PagoDAO;
import model.PagoModel;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class PagoController {

    private final PagoDAO pagoDAO = new PagoDAO();
    // Usamos el DAO que ya construimos juntos
    private final ConfiguracionCuotaDAO confDAO = new ConfiguracionCuotaDAO(); 

    public boolean registrar(int numeroCasa, int mes, int anio) {
        if (numeroCasa <= 0 || mes <= 0 || anio <= 0) {
            JOptionPane.showMessageDialog(null,
                "Debe seleccionar número de casa, mes y año.",
                "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (pagoDAO.existePago(numeroCasa, mes, anio)) {
            JOptionPane.showMessageDialog(null,
                "Esta casa ya tiene registrado el pago para el mes y año indicados.",
                "Pago duplicado", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Llamamos directamente al método que creamos, el cual devuelve un float/double
        double cuota = confDAO.obtenerCuotaActual(); 
        
        PagoModel p = new PagoModel(numeroCasa, mes, anio, cuota);
        boolean ok = pagoDAO.registrar(p);
        
        if (ok) {
            JOptionPane.showMessageDialog(null,
                "Pago registrado correctamente — Q " + String.format("%.2f", cuota),
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                "No se pudo registrar el pago.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return ok;
    }

    public ArrayList<PagoModel> listar() {
        return pagoDAO.listar();
    }

    public ArrayList<PagoModel> listarPorCasa(int numeroCasa) {
        return pagoDAO.listarPorCasa(numeroCasa);
    }
}