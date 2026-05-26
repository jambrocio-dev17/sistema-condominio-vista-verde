/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configuration;
import java.sql.*;
/**
 *
 * @author Josue Ambrocio
 */
public class Conexion {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/vista_verde";
    private static final String USER = "postgres"; // Tu usuario de Postgres
    private static final String PASSWORD = "Elfogg2006."; // Tu contraseña de Postgres
    
    public Conexion() {
        initDatabase();
    }
    
    
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
    }
    
    private void initDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS residents ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name VARCHAR(255) NOT NULL,"
                    + "apartment VARCHAR(50) NOT NULL)");

            statement.execute("CREATE TABLE IF NOT EXISTS propietarios ("
                    + "id SERIAL PRIMARY KEY,"
                    + "nombre_completo VARCHAR(150) NOT NULL,"
                    + "dpi VARCHAR(20) NOT NULL,"
                    + "telefono VARCHAR(20),"
                    + "correo VARCHAR(100),"
                    + "numero_casa INT NOT NULL)");

            // Tablas del sistema Vista Verde
            statement.execute("CREATE TABLE IF NOT EXISTS Casa ("
                    + "numero_casa INTEGER PRIMARY KEY CHECK(numero_casa >= 1 AND numero_casa <= 30))");

            statement.execute("INSERT INTO Casa(numero_casa) "
                    + "SELECT gs FROM generate_series(1,30) gs ON CONFLICT DO NOTHING");

            statement.execute("CREATE TABLE IF NOT EXISTS Propietario ("
                    + "id_propietario SERIAL PRIMARY KEY,"
                    + "nombre_completo VARCHAR(150) NOT NULL,"
                    + "dpi VARCHAR(20),"
                    + "telefono VARCHAR(20),"
                    + "correo VARCHAR(100),"
                    + "numero_casa INTEGER UNIQUE)");

            statement.execute("CREATE TABLE IF NOT EXISTS Pago ("
                    + "id_pago SERIAL PRIMARY KEY,"
                    + "numero_casa INTEGER NOT NULL,"
                    + "mes INTEGER NOT NULL CHECK(mes >= 1 AND mes <= 12),"
                    + "anio INTEGER NOT NULL,"
                    + "monto DECIMAL(10,2) NOT NULL,"
                    + "estado VARCHAR(20) DEFAULT 'Pagado',"
                    + "UNIQUE(numero_casa, mes, anio))");

            statement.execute("CREATE TABLE IF NOT EXISTS Configuracion ("
                    + "id_config SERIAL PRIMARY KEY,"
                    + "cuota_actual DECIMAL(10,2) NOT NULL)");

            try (ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Configuracion")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    statement.execute("INSERT INTO Configuracion(cuota_actual) VALUES(1500.00)");
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error inicializando la base de datos de Postgres", ex);
        }
    }
    
}
