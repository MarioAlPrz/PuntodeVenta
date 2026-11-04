import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * UsuarioDAO - Data Access Object para usuarios
 * Maneja la autenticación y consultas de usuarios
 */
public class UsuarioDAO {

    private Connection conexion;

    public UsuarioDAO() {
        this.conexion = Conexion.getInstancia().getConexion();
    }

    /**
     * Valida las credenciales de login
     * @return Usuario si las credenciales son válidas, null si no
     */
    public Usuario validarLogin(String usuario, String contrasena) {
        String hash = hashSHA256(contrasena);
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ? AND activo = TRUE";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, hash);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("usuario"),
                    rs.getString("contrasena"),
                    rs.getString("rol"),
                    rs.getBoolean("activo")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error en validación de login: " + e.getMessage());
        }
        return null;
    }

    /**
     * Genera hash SHA-256 de un texto (para contraseñas)
     */
    private String hashSHA256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }
}
