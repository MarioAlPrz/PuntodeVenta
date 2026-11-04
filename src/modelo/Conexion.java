import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase Conexión - Patrón Singleton
 * Gestiona la conexión única a la base de datos MySQL
 */
public class Conexion {

    private static Conexion instancia;
    private Connection conexion;

    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_punto_venta?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Mexico_City";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    private Conexion() {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("✅ Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }

    /**
     * Obtiene la instancia única de Conexión (Singleton thread-safe)
     */
    public static synchronized Conexion getInstancia() {
        if (instancia == null || instancia.conexion == null) {
            instancia = new Conexion();
        }
        try {
            if (instancia.conexion.isClosed()) {
                instancia = new Conexion();
            }
        } catch (SQLException e) {
            instancia = new Conexion();
        }
        return instancia;
    }

    /**
     * Retorna el objeto Connection de JDBC
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔌 Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
