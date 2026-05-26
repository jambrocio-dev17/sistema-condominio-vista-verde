/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import configuration.Conexion;
import model.CasaMorosaModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.CasaMorosaModel;

/**
 *
 * @author yeimy
 */
public class CasasMorosasDAO {
    
    /**
     * Lista todas las casas que NO tienen pago registrado para el mes/año dado.
     * Incluye nombre del propietario, teléfono y cuántos meses del año no han pagado.
     */
    public ArrayList<CasaMorosaModel> listarMorosas(int mes, int anio) {
        ArrayList<CasaMorosaModel> lista = new ArrayList<>();
        String sql =
            "SELECT c.numero_casa, "
          + "       COALESCE(p.nombre_completo, 'Sin propietario') AS propietario, "
          + "       COALESCE(p.telefono, '-') AS telefono, "
          + "       ? - (SELECT COUNT(*) FROM Pago "
          + "             WHERE numero_casa = c.numero_casa "
          + "               AND anio = ? AND mes <= ?) AS meses_deuda "
          + "FROM Casa c "
          + "LEFT JOIN Propietario p ON c.numero_casa = p.numero_casa "
          + "WHERE c.numero_casa NOT IN ("
          + "    SELECT numero_casa FROM Pago WHERE mes = ? AND anio = ?"
          + ") "
          + "ORDER BY c.numero_casa ASC";

        Connection con = Conexion.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mes);
            ps.setInt(2, anio);
            ps.setInt(3, mes);
            ps.setInt(4, mes);
            ps.setInt(5, anio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new CasaMorosaModel(
                        rs.getInt("numero_casa"),
                        rs.getString("propietario"),
                        rs.getString("telefono"),
                        Math.max(0, rs.getInt("meses_deuda"))
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar casas morosas: " + e.getMessage());
        }
        return lista;
    }
    
    /** Cuenta cuántas casas no han pagado en el mes/año dado. */
    public int contarMorosas(int mes, int anio) {
        String sql = "SELECT COUNT(*) FROM Casa "
                   + "WHERE numero_casa NOT IN ("
                   + "    SELECT numero_casa FROM Pago WHERE mes = ? AND anio = ?"
                   + ")";
        Connection con = Conexion.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mes);
            ps.setInt(2, anio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar morosas: " + e.getMessage());
        }
        return 0;
    }
}