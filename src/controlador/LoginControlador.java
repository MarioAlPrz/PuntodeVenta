package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import vista.LoginVista;
import vista.VentaPOSVista;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

public class LoginControlador {

    private LoginVista vista;
    private UsuarioDAO usuarioDAO;

    public LoginControlador(LoginVista vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();
        initEventos();
    }

    private void initEventos() {
        // Click en botón log
        // in
        vista.getBtnLogin().addActionListener(e -> login());

        // Enter en campo de contraseña
        vista.getTxtContrasena().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        // Enter en campo de usuario -> pasar a contraseña
        vista.getTxtUsuario().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    vista.getTxtContrasena().requestFocus();
                }
            }
        });
    }

    private void login() {
        String usuario = vista.getTxtUsuario().getText().trim();
        String contrasena = new String(vista.getTxtContrasena().getPassword());

        // Validar campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            vista.mostrarError("Por favor completa todos los campos");
            return;
        }

        // Validar credenciales
        Usuario user = usuarioDAO.validarLogin(usuario, contrasena);

        if (user != null) {
            vista.mostrarExito("¡Bienvenido(a) " + user.getNombre() + "!");

            // Abrir ventana principal POS
            VentaPOSVista pos = new VentaPOSVista();
            pos.setUsuarioActual(user);
            new VentaControlador(pos);
            pos.setVisible(true);

            // Cerrar login
            vista.dispose();
        } else {
            vista.mostrarError("Usuario o contraseña incorrectos");
            vista.limpiarCampos();
        }
    }
}
