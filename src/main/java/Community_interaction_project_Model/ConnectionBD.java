package Community_interaction_project_Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionBD {
    private static final String URL = "jdbc:mysql://ies9021.edu.ar:3306/ies9021_coco"; // URL de la BD
    private static final String USER = "ies9021_userdb";  // Usuario de la BD
    private static final String PASSWORD = "Xsw23edc.127";  // Contraseña de la BD
    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(ConnectionBD.class.getName());

    public ConnectionBD() {
        // Constructor vacío para inicialización opcional
    }

    // Conecta con la base de datos
    public Connection conectar() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                LOGGER.log(Level.INFO, "Conexión exitosa a la base de datos");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al conectar a la base de datos", e);
            }
        }
        return connection;
    }

    // Desconecta de la base de datos
    public void desconectar() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    LOGGER.log(Level.INFO, "Conexión cerrada");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión a la base de datos", e);
            }
        }
    }
}
