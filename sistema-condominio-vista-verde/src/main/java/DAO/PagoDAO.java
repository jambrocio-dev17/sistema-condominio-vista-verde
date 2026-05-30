/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import configuration.Conexion;
import model.PagoModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author elena
 */
public class PagoDAO {
    
        public boolean registrar(PagoModel p) {
        String sql = "INSERT INTO Pago (numero_casa, mes, anio, monto, estado) "
                   + "VALUES (?, ?, ?, ?, ?)";
                   
        // 1. Obtenemos la conexión fuera del try
        Connection con = Conexion.getInstance().getConnection();
        
        // 2. Solo el PreparedStatement va dentro
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getNumeroCasa());
            ps.setInt(2, p.getMes());
            ps.setInt(3, p.getAnio());
            ps.setDouble(4, p.getMonto());
            ps.setString(5, p.getEstado());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar pago: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<PagoModel> listar() {
        ArrayList<PagoModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago ORDER BY anio DESC, mes DESC";
        
        Connection con = Conexion.getInstance().getConnection();
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) lista.add(mapear(rs));
            
        } catch (SQLException e) {
            System.err.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<PagoModel> listarPorCasa(int numeroCasa) {
        ArrayList<PagoModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago WHERE numero_casa = ? ORDER BY anio DESC, mes DESC";
        
        Connection con = Conexion.getInstance().getConnection();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroCasa);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pagos por casa: " + e.getMessage());
        }
        return lista;
    }

    public boolean existePago(int numeroCasa, int mes, int anio) {
        String sql = "SELECT COUNT(*) FROM Pago WHERE numero_casa=? AND mes=? AND anio=?";
        
        Connection con = Conexion.getInstance().getConnection();
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroCasa);
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar pago: " + e.getMessage());
        }
        return false;
    }

    private PagoModel mapear(ResultSet rs) throws SQLException {
        PagoModel p = new PagoModel();
        p.setId(rs.getInt("id_pago"));
        p.setNumeroCasa(rs.getInt("numero_casa"));
        p.setMes(rs.getInt("mes"));
        p.setAnio(rs.getInt("anio"));
        p.setMonto(rs.getDouble("monto"));
        p.setEstado(rs.getString("estado"));
        return p;
    }
    
}