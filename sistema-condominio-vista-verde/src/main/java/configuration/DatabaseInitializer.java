package configuration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void inicializar() {
        // Obtenemos la conexión única a través del Singleton
        Connection con = Conexion.getInstance().getConnection();

        // ATENCIÓN: Solo ponemos el Statement en el try-with-resources
        // No ponemos la Connection para evitar que se cierre automáticamente
        try (Statement st = con.createStatement()) {

            st.execute("CREATE TABLE IF NOT EXISTS Casa ("
                    + "numero_casa INTEGER PRIMARY KEY "
                    + "CHECK(numero_casa >= 1 AND numero_casa <= 30))");

            st.execute("INSERT INTO Casa(numero_casa) "
                    + "SELECT gs FROM generate_series(1,30) gs "
                    + "ON CONFLICT DO NOTHING");

            st.execute("CREATE TABLE IF NOT EXISTS Propietario ("
                    + "id_propietario SERIAL PRIMARY KEY,"
                    + "nombre_completo VARCHAR(150) NOT NULL,"
                    + "dpi VARCHAR(20),"
                    + "telefono VARCHAR(20),"
                    + "correo VARCHAR(100),"
                    + "numero_casa INTEGER UNIQUE)");

            st.execute("CREATE TABLE IF NOT EXISTS Pago ("
                    + "id_pago SERIAL PRIMARY KEY,"
                    + "numero_casa INTEGER NOT NULL,"
                    + "mes INTEGER NOT NULL CHECK(mes >= 1 AND mes <= 12),"
                    + "anio INTEGER NOT NULL,"
                    + "monto DECIMAL(10,2) NOT NULL,"
                    + "estado VARCHAR(20) DEFAULT 'Pagado',"
                    + "UNIQUE(numero_casa, mes, anio))");

            st.execute("CREATE TABLE IF NOT EXISTS Configuracion ("
                    + "id_config SERIAL PRIMARY KEY,"
                    + "cuota_actual DECIMAL(10,2) NOT NULL)");

            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Configuracion")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    st.execute("INSERT INTO Configuracion(cuota_actual) VALUES(1500.00)");
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error inicializando la base de datos", ex);
        }
    }
}