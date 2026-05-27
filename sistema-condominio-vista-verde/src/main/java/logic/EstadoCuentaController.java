package logic;

import DAO.EstadoCuentaDAO;
import DAO.PropietarioDAO;
import model.PropietarioModel;
import ui.estadoCuenta;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EstadoCuentaController {

    private static final String[] COL_PAGADOS    = {"Mes", "Estado", "Monto"};
    private static final String[] COL_PENDIENTES = {"Mes", "Estado", "Monto"};

    private final EstadoCuentaDAO dao;
    private final PropietarioDAO  propietarioDAO;
    private final estadoCuenta    vista;

    // Datos de la última consulta (para exportar PDF)
    private int                 ultimoNumeroCasa  = 0;
    private String              ultimoPropietario = "";
    private ArrayList<Object[]> ultimosPagados    = new ArrayList<>();
    private ArrayList<Object[]> ultimosPendientes = new ArrayList<>();
    private int                 ultimoAnio        = 0;

    public EstadoCuentaController(estadoCuenta vista) {
        this.dao           = new EstadoCuentaDAO();
        this.propietarioDAO = new PropietarioDAO();
        this.vista         = vista;
    }

    public int                 getUltimoNumeroCasa()   { return ultimoNumeroCasa; }
    public String              getUltimoPropietario()  { return ultimoPropietario; }
    public ArrayList<Object[]> getUltimosPagados()    { return ultimosPagados; }
    public ArrayList<Object[]> getUltimosPendientes() { return ultimosPendientes; }
    public int                 getUltimoAnio()         { return ultimoAnio; }

    /**
     * Carga los meses pagados y pendientes de la casa seleccionada
     * en el año indicado y actualiza las tablas de la vista.
     */
    public void consultar(int numeroCasa, int anio) {
        if (numeroCasa < 1 || numeroCasa > 30) {
            javax.swing.JOptionPane.showMessageDialog(
                vista,
                "Seleccione un número de casa válido (1-30).",
                "Validación",
                javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        ArrayList<Object[]> pagados    = dao.listarPagados(numeroCasa, anio);
        ArrayList<Object[]> pendientes = dao.listarPendientes(numeroCasa, anio);

        // Obtener nombre del propietario para el PDF
        PropietarioModel prop = propietarioDAO.buscarPorCasa(numeroCasa);
        String nombreProp = (prop != null) ? prop.getNombreCompleto() : "Sin propietario";

        // Guardar para exportar PDF después
        this.ultimoNumeroCasa  = numeroCasa;
        this.ultimoPropietario = nombreProp;
        this.ultimosPagados    = pagados;
        this.ultimosPendientes = pendientes;
        this.ultimoAnio        = anio;

        DefaultTableModel modeloPagados = new DefaultTableModel(COL_PAGADOS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] fila : pagados) modeloPagados.addRow(fila);
        vista.getTablePagados().setModel(modeloPagados);

        DefaultTableModel modeloPendientes = new DefaultTableModel(COL_PENDIENTES, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] fila : pendientes) modeloPendientes.addRow(fila);
        vista.getTablePendientes().setModel(modeloPendientes);

        // Actualizar panel del propietario
        actualizarPanelPropietario(numeroCasa, prop, pagados, anio);
    }

    private void actualizarPanelPropietario(int numeroCasa, PropietarioModel prop,
                                             ArrayList<Object[]> pagados, int anio) {
        String nombre   = (prop != null) ? prop.getNombreCompleto() : "Sin propietario";
        String telefono = (prop != null && prop.getTelefono() != null) ? prop.getTelefono() : "-";
        String correo   = (prop != null && prop.getCorreo()   != null) ? prop.getCorreo()   : "-";

        vista.getLblInicialesPersonaBuscada().setText(iniciales(nombre));
        vista.getLblDatosBuscados().setText(
            "<html><b style='color:white; font-size:11px;'>" + nombre + "</b>" +
            "<br><span style='color:#8fcc6f; font-size:9px;'>Casa No. " + numeroCasa +
            " - " + telefono + " - " + correo + "</span></html>");

        double total = 0;
        for (Object[] fila : pagados) {
            if (fila.length > 2 && fila[2] != null) {
                try {
                    String s = fila[2].toString().replace("Q", "").replace(",", "").trim();
                    total += Double.parseDouble(s);
                } catch (NumberFormatException ignored) {}
            }
        }
        vista.getLblCantidadPersona().setText(String.format("Q%,.2f", total));
    }

    private String iniciales(String nombre) {
        if (nombre == null || nombre.isBlank()) return "?";
        String[] partes = nombre.trim().split("\\s+");
        if (partes.length == 1) return partes[0].substring(0, 1).toUpperCase();
        return (partes[0].substring(0, 1) + partes[1].substring(0, 1)).toUpperCase();
    }
}
