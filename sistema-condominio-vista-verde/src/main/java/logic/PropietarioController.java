package logic;

import DAO.PropietarioDAO;
import model.PropietarioModel;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class PropietarioController {

    private final PropietarioDAO dao = new PropietarioDAO();

    public boolean guardar(String nombre, String dpi, String telefono,
                           String correo, int numeroCasa) {
        if (nombre == null || nombre.isBlank() ||
            dpi    == null || dpi.isBlank()    ||
            numeroCasa <= 0) {
            JOptionPane.showMessageDialog(null,
                "Nombre completo, DPI y número de casa son obligatorios.",
                "Campos requeridos", JOptionPane.WARNING_MESSAGE);
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
