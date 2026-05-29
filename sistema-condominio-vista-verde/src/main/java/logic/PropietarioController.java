package logic;

import DAO.PropietarioDAO;
import model.PropietarioModel;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class PropietarioController {

    private final PropietarioDAO dao = new PropietarioDAO();

    public boolean guardar(String nombre, String dpi, String telefono,
                           String correo, int numeroCasa) {
        if (nombre == null || nombre.isBlank() || nombre.trim().split("\\s+").length < 2) {
            JOptionPane.showMessageDialog(null,
                "El nombre debe contener al menos dos palabras.",
                "Nombre inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dpi == null || !dpi.matches("\\d{13}")) {
            JOptionPane.showMessageDialog(null,
                "El DPI debe contener exactamente 13 dígitos.",
                "DPI inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dao.existeDpi(dpi)) {
            JOptionPane.showMessageDialog(null,
                "El DPI ingresado ya está registrado.",
                "DPI duplicado", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (telefono == null || !telefono.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(null,
                "El teléfono debe contener exactamente 8 dígitos.",
                "Teléfono inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (numeroCasa <= 0) {
            JOptionPane.showMessageDialog(null,
                "Debe seleccionar un número de casa válido.",
                "Casa inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String errorCorreo = EmailService.validarCorreo(correo);
        if (errorCorreo != null) {
            JOptionPane.showMessageDialog(null, errorCorreo,
                "Correo inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        PropietarioModel p = new PropietarioModel(nombre, dpi, telefono, correo, numeroCasa);
        boolean ok = dao.registrar(p);
        if (ok) {
            JOptionPane.showMessageDialog(null,
                "Propietario registrado correctamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                "No se pudo registrar. Verifique que la casa no tenga propietario asignado.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return ok;
    }

    public boolean actualizar(String nombre, String dpi, String telefono,
                              String correo, int numeroCasa) {
        if (nombre == null || nombre.isBlank() || nombre.trim().split("\\s+").length < 2) {
            JOptionPane.showMessageDialog(null,
                "El nombre debe contener al menos dos palabras.",
                "Nombre inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dpi == null || !dpi.matches("\\d{13}")) {
            JOptionPane.showMessageDialog(null,
                "El DPI debe contener exactamente 13 dígitos.",
                "DPI inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (telefono == null || !telefono.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(null,
                "El teléfono debe contener exactamente 8 dígitos.",
                "Teléfono inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String errorCorreo = EmailService.validarCorreo(correo);
        if (errorCorreo != null) {
            JOptionPane.showMessageDialog(null, errorCorreo,
                "Correo inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        PropietarioModel p = new PropietarioModel(nombre, dpi, telefono, correo, numeroCasa);
        boolean ok = dao.actualizar(p);
        if (ok) {
            JOptionPane.showMessageDialog(null,
                "Propietario actualizado correctamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                "No se pudo actualizar el propietario.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return ok;
    }

    public boolean eliminar(int id) {
        boolean ok = dao.eliminar(id);
        if (!ok) JOptionPane.showMessageDialog(null,
            "No se pudo eliminar el propietario.",
            "Error", JOptionPane.ERROR_MESSAGE);
        return ok;
    }

    public ArrayList<PropietarioModel> listar() {
        return dao.listar();
    }

    public PropietarioModel buscarPorCasa(int numeroCasa) {
        return dao.buscarPorCasa(numeroCasa);
    }
}
