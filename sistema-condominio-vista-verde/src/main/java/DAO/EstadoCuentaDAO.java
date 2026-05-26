/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import configuration.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mc296
 */
public class EstadoCuentaDAO {

    private static final String[] NOMBRES_MES = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    /** Devuelve los meses pagados de una casa en un año dado. */
    public ArrayList<Object[]> listarPagados(int numeroCasa, int anio) {
        ArrayList<Object[]> lista = new ArrayList<>();
        String sql = "SELECT mes, monto FROM Pago "
                   + "WHERE numero_casa = ? AND anio = ? AND estado = 'Pagado' "
                   + "ORDER BY mes ASC";
        Connection con = Conexion.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroCasa);
            ps.setInt(2, anio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int mes = rs.getInt("mes");
                    double monto = rs.getDouble("monto");
                    lista.add(new Object[]{NOMBRES_MES[mes - 1], "Pagado", String.format("Q %.2f", monto)});
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pagados: " + e.getMessage());
        }
        return lista;
    }
