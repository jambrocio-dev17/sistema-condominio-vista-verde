package configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL      = "jdbc:postgresql://localhost:5432/vista_verde";
    private static final String USUARIO  = "postgres";
    private static final String PASSWORD = "Elfogg2006.";

    private static volatile Conexion instance;
    
    //Objeto de conexión de JDBC
    private Connection connection;

    // Constructor privado: solo se ejecuta la primera vez
    private Conexion() {
        try {
            this.connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error al establecer la conexión con PostgreSQL", e);
        }
    }

    //Punto de acceso global (Double-Checked Locking)
    public static Conexion getInstance() {
        if (instance == null) {
            synchronized (Conexion.class) {
                if (instance == null) {
                    instance = new Conexion();
                }
            }
        }
        return instance;
    }

    //Método para obtener la conexión activa
    public Connection getConnection() {
        try {
            // Seguridad: Si la conexión se cerró accidentalmente, la reabrimos
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al reconectar a la base de datos", e);
        }
        return connection;
    }
}