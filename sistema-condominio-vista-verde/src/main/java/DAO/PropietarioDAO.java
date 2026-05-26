/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import configuration.Conexion;
import model.PropietarioModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author yeimy
 */
public class PropietarioDAO {
    
    public boolean registrar(PropietarioModel p) {
        String sql = "INSERT INTO Propietario "
                + "(nombre_completo, dpi, telefono, correo, numero_casa) "
                + "VALUES (?, ?, ?, ?, ?)";

        // 1. Conexión fuera del try
        Connection con = Conexion.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombreCompleto());
            ps.setString(2, p.getDpi());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getCorreo());
            ps.setInt(5, p.getNumeroCasa());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar propietario: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<PropietarioModel> listar() {
        ArrayList<PropietarioModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM Propietario ORDER BY id_propietario ASC";

        Connection con = Conexion.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar propietarios: " + e.getMessage());
        }
        return lista;
    }

    public PropietarioModel buscarPorCasa(int numeroCasa) {
        String sql = "SELECT * FROM Propietario WHERE numero_casa = ?";

        Connection con = Conexion.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroCasa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar propietario: " + e.getMessage());
        }
        return null;
    }
    
    public boolean actualizar(PropietarioModel p) {
        String sql = "UPDATE Propietario "
                + "SET nombre_completo=?, dpi=?, telefono=?, correo=? "
                + "WHERE numero_casa=?";

        Connection con = Conexion.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombreCompleto());
            ps.setString(2, p.getDpi());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getCorreo());
            ps.setInt(5, p.getNumeroCasa());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar propietario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Propietario WHERE id_propietario = ?";

        Connection con = Conexion.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar propietario: " + e.getMessage());
            return false;
        }
    }

    private PropietarioModel mapear(ResultSet rs) throws SQLException {
        PropietarioModel p = new PropietarioModel();
        p.setId(rs.getInt("id_propietario"));
        p.setNombreCompleto(rs.getString("nombre_completo"));
        p.setDpi(rs.getString("dpi"));
        p.setTelefono(rs.getString("telefono"));
        p.setCorreo(rs.getString("correo"));
        p.setNumeroCasa(rs.getInt("numero_casa"));
        return p;
    }
}