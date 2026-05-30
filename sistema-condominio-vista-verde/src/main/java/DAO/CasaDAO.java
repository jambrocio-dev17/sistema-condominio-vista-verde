/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import configuration.Conexion;
import model.CasaModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author mc296
 */
public class CasaDAO {

    public ArrayList<CasaModel> listar() {
        ArrayList<CasaModel> lista = new ArrayList<>();
        String sql = "SELECT numero_casa FROM Casa ORDER BY numero_casa ASC";
        
        // 1. Obtenemos la conexión única a través del Singleton (fuera del try)
        Connection con = Conexion.getInstance().getConnection();

        // 2. Solo el PreparedStatement y ResultSet van en el try-with-resources
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                lista.add(new CasaModel(rs.getInt("numero_casa")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar casas: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<CasaModel> listarSinPropietario() {
        ArrayList<CasaModel> lista = new ArrayList<>();
        String sql = "SELECT c.numero_casa FROM Casa c "
                   + "LEFT JOIN Propietario p ON c.numero_casa = p.numero_casa "
                   + "WHERE p.numero_casa IS NULL "
                   + "ORDER BY c.numero_casa ASC";
                   
        // 1. Obtenemos la conexión única (fuera del try)
        Connection con = Conexion.getInstance().getConnection();

        // 2. Ejecutamos los recursos que sí deben cerrarse
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                lista.add(new CasaModel(rs.getInt("numero_casa")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar casas sin propietario: " + e.getMessage());
        }
        return lista;
    }
}
