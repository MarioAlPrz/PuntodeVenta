import controlador.LoginControlador;
import vista.LoginVista;
import javax.swing.*;

/**
 * Main - Punto de entrada de la aplicación
 * Configura el Look & Feel y lanza el login
 */
public class Main {

    public static void main(String[] args) {
        // Configurar FlatLaf como Look & Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("TitlePane.unifiedBackground", true);
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo configurar FlatLaf: " + e.getMessage());
            System.err.println("Usando Look & Feel por defecto.");
        }

        // Lanzar aplicación en el hilo de UI
        SwingUtilities.invokeLater(() -> {
            System.out.println("🚀 Iniciando Punto de Venta - Edición Estudiante");
            LoginVista login = new LoginVista();
            new LoginControlador(login);
            login.setVisible(true);
        });
    }
}
