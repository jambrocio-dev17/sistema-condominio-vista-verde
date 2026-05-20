package DAO;


import configuration.Conexion;
import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import logic.ConfiguracionCuotaController;

public class ConfiguracionCuotaDAO {
    public boolean registrar(ConfiguracionCuotaController confiCuota) {
        String sql = "INSERT INTO residentes (nombre, apartamento) VALUES (?, ?)";

            // El try-with-resources cierra automáticamente la conexión y el statement
            /*try (Connection con = Conexion.getConnection().conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                //ps.setInt(1, confiCuota.getId());
                //ps.setInt(2, confiCuota.getMonto());

                ps.execute();
                return true;

            } catch (SQLException e) {
                System.err.println("Error al registrar residente: " + e.getMessage());
                return false;
            }*/
            return false;
    }

        /*public List<ConfiguracionCuotaController> listar() {
            List<ConfiguracionCuotaController> lista = new ArrayList<>();
            String sql = "SELECT * FROM residentes ORDER BY id ASC";

            try (Connection con = Conexion.getInstancia().conectar();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    ConfiguracionCuotaController res = new ConfiguracionCuotaController();
                    res.setId(rs.getInt("id"));
                    res.setNombre(rs.getString("nombre"));
                    res.setApartamento(rs.getString("apartamento"));
                    lista.add(res);
                }

            } catch (SQLException e) {
                System.err.println("Error al listar residentes: " + e.getMessage());
            }

            return lista;
        }*/

        public boolean actualizar(ConfiguracionCuotaController confiCuota) {
            // Lógica similar al registrar, pero con UPDATE
            return false; 
        }

        public boolean eliminar(int id) {
            // Lógica similar al registrar, pero con DELETE
            return false;
        }

}