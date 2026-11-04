import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * LoginVista - Pantalla de inicio de sesión
 * Diseño moderno con gradientes y estilo estudiantil
 */
public class LoginVista extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnLogin;

    // Para hacer la ventana arrastrable
    private int mouseX, mouseY;

    public LoginVista() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Punto de Venta - Login");
        setSize(450, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setResizable(false);

        // Forma redondeada
        setShape(new RoundRectangle2D.Double(0, 0, 450, 620, 25, 25));

        // Panel principal con gradiente
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(26, 26, 46),
                    0, getHeight(), new Color(15, 52, 96)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Hacer la ventana arrastrable
        mainPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 40, 5, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === Botón cerrar (X) ===
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topBar.setOpaque(false);
        JButton btnCerrar = new JButton("✕");
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCerrar.setForeground(new Color(180, 180, 200));
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> System.exit(0));
        btnCerrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnCerrar.setForeground(Color.RED); }
            public void mouseExited(MouseEvent e) { btnCerrar.setForeground(new Color(180, 180, 200)); }
        });
        topBar.add(btnCerrar);
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 0, 10);
        mainPanel.add(topBar, gbc);

        // === Ícono de usuario (círculo pintado) ===
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 80;
                int x = (getWidth() - size) / 2;
                int y = 5;

                // Círculo de fondo
                g2d.setColor(new Color(0, 102, 255, 80));
                g2d.fillOval(x, y, size, size);

                // Borde del círculo
                g2d.setColor(new Color(0, 102, 255));
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawOval(x, y, size, size);

                // Silueta de persona (cabeza)
                g2d.setColor(new Color(200, 220, 255));
                int headSize = 22;
                g2d.fillOval(x + (size - headSize) / 2, y + 15, headSize, headSize);

                // Cuerpo (arco)
                g2d.fillArc(x + 15, y + 40, size - 30, 35, 0, 180);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 95);
            }
        };
        iconPanel.setOpaque(false);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 40, 0, 40);
        mainPanel.add(iconPanel, gbc);

        // === Título ===
        JLabel lblTitulo = new JLabel("\uD83D\uDCE6 Punto de Venta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 40, 0, 40);
        mainPanel.add(lblTitulo, gbc);

        // === Subtítulo ===
        JLabel lblSubtitulo = new JLabel("Edición Estudiante \uD83C\uDF93", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(150, 170, 210));
        gbc.gridy = 3;
        gbc.insets = new Insets(2, 40, 20, 40);
        mainPanel.add(lblSubtitulo, gbc);

        // === Panel del formulario (glassmorphism) ===
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo semi-transparente
                g2d.setColor(new Color(255, 255, 255, 25));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Borde sutil
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.gridx = 0;
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.insets = new Insets(8, 25, 8, 25);

        // --- Label Usuario ---
        JLabel lblUsuario = new JLabel("👤  Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsuario.setForeground(new Color(180, 195, 220));
        fgbc.gridy = 0;
        fgbc.insets = new Insets(20, 25, 2, 25);
        formPanel.add(lblUsuario, fgbc);

        // --- Campo Usuario ---
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUsuario.setPreferredSize(new Dimension(300, 42));
        txtUsuario.setBackground(new Color(30, 40, 65));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 80, 120), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fgbc.gridy = 1;
        fgbc.insets = new Insets(2, 25, 8, 25);
        formPanel.add(txtUsuario, fgbc);

        // --- Label Contraseña ---
        JLabel lblContrasena = new JLabel("🔒  Contraseña");
        lblContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblContrasena.setForeground(new Color(180, 195, 220));
        fgbc.gridy = 2;
        fgbc.insets = new Insets(8, 25, 2, 25);
        formPanel.add(lblContrasena, fgbc);

        // --- Campo Contraseña ---
        txtContrasena = new JPasswordField(20);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtContrasena.setPreferredSize(new Dimension(300, 42));
        txtContrasena.setBackground(new Color(30, 40, 65));
        txtContrasena.setForeground(Color.WHITE);
        txtContrasena.setCaretColor(Color.WHITE);
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 80, 120), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fgbc.gridy = 3;
        fgbc.insets = new Insets(2, 25, 15, 25);
        formPanel.add(txtContrasena, fgbc);

        // --- Botón Login ---
        btnLogin = new JButton("Iniciar Sesión") {
            private boolean hover = false;

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color c1 = hover ? new Color(51, 133, 255) : new Color(0, 102, 255);
                Color c2 = hover ? new Color(30, 110, 230) : new Color(0, 82, 204);

                GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(300, 45));
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fgbc.gridy = 4;
        fgbc.insets = new Insets(10, 25, 10, 25);
        formPanel.add(btnLogin, fgbc);

        // --- Link olvidaste contraseña ---
        JLabel lblOlvido = new JLabel("¿Olvidaste tu contraseña?", SwingConstants.CENTER);
        lblOlvido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblOlvido.setForeground(new Color(100, 150, 255));
        lblOlvido.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fgbc.gridy = 5;
        fgbc.insets = new Insets(5, 25, 20, 25);
        formPanel.add(lblOlvido, fgbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 20, 10, 20);
        mainPanel.add(formPanel, gbc);

        // === Footer ===
        JLabel lblFooter = new JLabel("Proyecto Escolar 2025 \uD83C\uDF92", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(100, 110, 140));
        gbc.gridy = 5;
        gbc.insets = new Insets(15, 40, 20, 40);
        mainPanel.add(lblFooter, gbc);

        setContentPane(mainPanel);
    }

    // === Métodos públicos ===

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JPasswordField getTxtContrasena() {
        return txtContrasena;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiarCampos() {
        txtUsuario.setText("");
        txtContrasena.setText("");
        txtUsuario.requestFocus();
    }
}
