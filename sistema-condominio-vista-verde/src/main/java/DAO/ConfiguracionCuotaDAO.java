/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import configuration.Conexion;
import model.ConfiguracionCuotaModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author elena
 */
public class ConfiguracionCuotaDAO {
    
    // Método para guardar el nuevo monto en la base de datos
    public boolean actualizar(ConfiguracionCuotaModel modelo) {
        String sql = "UPDATE Configuracion SET cuota_actual = ? WHERE id_config = ?";

        Connection con = Conexion.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setFloat(1, modelo.getMonto()); 
            ps.setInt(2, modelo.getId()); 

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar la cuota: " + e.getMessage());
            return false;
        }
    }
    
}
