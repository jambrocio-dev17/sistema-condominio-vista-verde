package logic;

import DAO.CasasMorosasDAO;
import DAO.ConfiguracionCuotaDAO;
import model.CasaMorosaModel;
import ui.CasasMorosas;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class CasasMorosasController {

    private static final String[] COLUMNAS = {"No. de Casa", "Propietario", "Teléfono", "Meses de Deuda"};

    private final CasasMorosasDAO       dao;
    private final ConfiguracionCuotaDAO cuotaDAO;
    private final CasasMorosas          vista;

    // Datos del último filtro aplicado (para exportar PDF)
    private ArrayList<CasaMorosaModel> ultimaLista  = new ArrayList<>();
    private int                        ultimoMes    = 0;
    private int                        ultimoAnio   = 0;
    private double                     ultimoMonto  = 0;

    public CasasMorosasController(CasasMorosas vista) {
        this.dao      = new CasasMorosasDAO();
        this.cuotaDAO = new ConfiguracionCuotaDAO();
        this.vista    = vista;
    }

    public ArrayList<CasaMorosaModel> getUltimaLista()  { return ultimaLista; }
    public int                        getUltimoMes()    { return ultimoMes; }
    public int                        getUltimoAnio()   { return ultimoAnio; }
    public double                     getUltimoMonto()  { return ultimoMonto; }

    /**
     * Carga las casas morosas del mes/año indicados y actualiza
     * la tabla, el contador y el monto pendiente en la vista.
     */
    public void cargar(int mes, int anio) {
        ArrayList<CasaMorosaModel> lista = dao.listarMorosas(mes, anio);
        int conteo = lista.size();
        double cuota = cuotaDAO.obtenerCuotaActual();
        double montoTotal = conteo * cuota;

        // Guardar para exportar PDF después
        this.ultimaLista = lista;
        this.ultimoMes   = mes;
        this.ultimoAnio  = anio;
        this.ultimoMonto = montoTotal;

        DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (CasaMorosaModel c : lista) {
            modelo.addRow(new Object[]{
                "Casa " + c.getNumeroCasa(),
                c.getPropietario(),
                c.getTelefono(),
                c.getMesesDeuda()
            });
        }
        vista.getTableMorosas().setModel(modelo);
        vista.getLblConteo().setText(String.valueOf(conteo));
        vista.getLblMonto().setText(String.format("Q %,.2f", montoTotal));
        vista.getLblMensaje().setText(
            conteo + " casa(s) no han pagado la cuota del mes seleccionado"
        );
    }
}
