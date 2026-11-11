package vista;

import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentaPOSVista extends JFrame {

    private JTextField txtCodigoBarras;
    private JTable tablaDetalle;
    private DefaultTableModel modeloDetalle;
    private JLabel lblSubtotal, lblIva, lblTotal;
    private JLabel lblVendedor, lblFecha, lblFolio;
    private JButton btnVoidItem, btnQuantity, btnHold, btnPay;
    private JButton btnLimpiar, btnCancelar;
    private JTextArea txtObservaciones;

    private Usuario usuarioActual;
    private Timer timerReloj;

    public VentaPOSVista() {
        setTitle("ducksuvenirs");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        armarUI();
        iniciarReloj();
        registrarAtajos();
    }

    private void armarUI() {
        setLayout(new BorderLayout());

        add(panelNorte(), BorderLayout.NORTH);
        add(panelCentro(), BorderLayout.CENTER);
        add(panelEste(), BorderLayout.EAST);
        add(panelSur(), BorderLayout.SOUTH);
    }

    private JPanel panelNorte() {
        JPanel norte = new JPanel(new BorderLayout());
        norte.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        norte.setBackground(new Color(30, 42, 58));

        JLabel etiquetaSucursal = new JLabel("ducksuvenirs");
        etiquetaSucursal.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaSucursal.setForeground(Color.WHITE);
        norte.add(etiquetaSucursal, BorderLayout.WEST);

        JPanel info = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        info.setOpaque(false);

        lblVendedor = new JLabel("Cajero: -");
        lblVendedor.setFont(new Font("Arial", Font.PLAIN, 13));
        lblVendedor.setForeground(new Color(200, 215, 235));
        info.add(lblVendedor);

        lblFolio = new JLabel("#0001");
        lblFolio.setFont(new Font("Arial", Font.BOLD, 13));
        lblFolio.setForeground(new Color(0, 200, 220));
        info.add(lblFolio);

        norte.add(info, BorderLayout.CENTER);

        lblFecha = new JLabel("");
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFecha.setForeground(new Color(180, 195, 215));
        norte.add(lblFecha, BorderLayout.EAST);

        // Barra de código de barras debajo del encabezado
        JPanel barraCodigo = new JPanel(new BorderLayout(10, 0));
        barraCodigo.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        barraCodigo.setBackground(new Color(235, 240, 248));

        JLabel etiquetaCodigo = new JLabel("Código:");
        etiquetaCodigo.setFont(new Font("Arial", Font.BOLD, 13));
        barraCodigo.add(etiquetaCodigo, BorderLayout.WEST);

        txtCodigoBarras = new JTextField();
        txtCodigoBarras.setFont(new Font("Arial", Font.PLAIN, 14));
        txtCodigoBarras.setToolTipText("Escanea o escribe el código y presiona Enter");
        barraCodigo.add(txtCodigoBarras, BorderLayout.CENTER);

        JLabel hint = new JLabel("  Enter: agregar  |  F3: enfocar  |  F8: limpiar  |  F12: pagar  ");
        hint.setFont(new Font("Arial", Font.ITALIC, 11));
        hint.setForeground(new Color(100, 110, 130));
        barraCodigo.add(hint, BorderLayout.EAST);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(norte, BorderLayout.NORTH);
        contenedor.add(barraCodigo, BorderLayout.SOUTH);
        return contenedor;
    }

    private JScrollPane panelCentro() {
        String[] columnas = {"PRODUCTO", "CANTIDAD", "P. UNITARIO", "SUBTOTAL", "IVA"};
        modeloDetalle = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.setFillsViewportHeight(true);
        tablaDetalle.setRowHeight(32);
        tablaDetalle.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaDetalle.setGridColor(new Color(220, 228, 238));
        tablaDetalle.setSelectionBackground(new Color(190, 215, 255));
        tablaDetalle.setSelectionForeground(Color.BLACK);

        tablaDetalle.getColumnModel().getColumn(0).setPreferredWidth(260);
        tablaDetalle.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaDetalle.getColumnModel().getColumn(2).setPreferredWidth(110);
        tablaDetalle.getColumnModel().getColumn(3).setPreferredWidth(110);
        tablaDetalle.getColumnModel().getColumn(4).setPreferredWidth(90);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < 5; i++) {
            tablaDetalle.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }

        JScrollPane scroll = new JScrollPane(tablaDetalle);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private JPanel panelEste() {
        JPanel este = new JPanel(new GridLayout(6, 1, 0, 10));
        este.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        este.setBackground(new Color(42, 55, 72));

        btnVoidItem  = new BotonRedondo("Void");
        btnQuantity  = new BotonRedondo("Quantity");
        btnHold      = new BotonRedondo("Hold");
        btnLimpiar   = new BotonRedondo("Limpiar");
        btnCancelar  = new BotonRedondo("Cancelar");
        btnPay       = new BotonRedondo("Pay");

        este.add(envolver(btnVoidItem));
        este.add(envolver(btnQuantity));
        este.add(envolver(btnHold));
        este.add(envolver(btnLimpiar));
        este.add(envolver(btnCancelar));
        este.add(envolver(btnPay));

        return este;
    }

    private JPanel panelSur() {
        JPanel sur = new JPanel(new BorderLayout(20, 0));
        sur.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 210, 225)),
            BorderFactory.createEmptyBorder(10, 15, 12, 15)
        ));
        sur.setBackground(new Color(248, 250, 255));

        // Observaciones a la izquierda
        JPanel obsPanel = new JPanel(new BorderLayout(5, 2));
        obsPanel.setOpaque(false);
        obsPanel.setPreferredSize(new Dimension(380, 65));
        JLabel lblObs = new JLabel("Observaciones:");
        lblObs.setFont(new Font("Arial", Font.BOLD, 11));
        obsPanel.add(lblObs, BorderLayout.NORTH);
        txtObservaciones = new JTextArea(2, 28);
        txtObservaciones.setFont(new Font("Arial", Font.PLAIN, 12));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(txtObservaciones);
        obsPanel.add(scrollObs, BorderLayout.CENTER);
        sur.add(obsPanel, BorderLayout.WEST);

        // Totales a la derecha
        JPanel totales = new JPanel(new GridLayout(3, 2, 10, 4));
        totales.setOpaque(false);

        Font fLabel = new Font("Arial", Font.PLAIN, 13);
        Font fValor  = new Font("Arial", Font.BOLD, 14);

        JLabel tSubtotal = new JLabel("Subtotal:");
        tSubtotal.setFont(fLabel);
        lblSubtotal = new JLabel("$0.00");
        lblSubtotal.setFont(fValor);
        lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel tIva = new JLabel("Impuesto:");
        tIva.setFont(fLabel);
        lblIva = new JLabel("$0.00");
        lblIva.setFont(fValor);
        lblIva.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel tTotal = new JLabel("Total a Pagar:");
        tTotal.setFont(new Font("Arial", Font.BOLD, 15));
        lblTotal = new JLabel("$0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(new Color(27, 120, 50));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        totales.add(tSubtotal);   totales.add(lblSubtotal);
        totales.add(tIva);        totales.add(lblIva);
        totales.add(tTotal);      totales.add(lblTotal);

        sur.add(totales, BorderLayout.EAST);
        return sur;
    }

    private JPanel envolver(JButton btn) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(btn, BorderLayout.CENTER);
        return p;
    }

    private void iniciarReloj() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
        timerReloj = new Timer(1000, e -> lblFecha.setText(sdf.format(new Date())));
        timerReloj.start();
        lblFecha.setText(sdf.format(new Date()));
    }

    private void registrarAtajos() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0),  "enfocar");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0),  "limpiar");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0),  "cancelar");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "pagar");

        am.put("enfocar",  new AbstractAction() { public void actionPerformed(ActionEvent e) { txtCodigoBarras.requestFocusInWindow(); } });
        am.put("limpiar",  new AbstractAction() { public void actionPerformed(ActionEvent e) { btnLimpiar.doClick(); } });
        am.put("cancelar", new AbstractAction() { public void actionPerformed(ActionEvent e) { btnCancelar.doClick(); } });
        am.put("pagar",    new AbstractAction() { public void actionPerformed(ActionEvent e) { btnPay.doClick(); } });
    }

    // Getters requeridos por VentaControlador
    public JTextField getTxtCodigoBarras()      { return txtCodigoBarras; }
    public JTable getTablaDetalle()             { return tablaDetalle; }
    public DefaultTableModel getModeloDetalle() { return modeloDetalle; }
    public JLabel getLblSubtotal()              { return lblSubtotal; }
    public JLabel getLblIva()                   { return lblIva; }
    public JLabel getLblTotal()                 { return lblTotal; }
    public JLabel getLblVendedor()              { return lblVendedor; }
    public JLabel getLblFecha()                 { return lblFecha; }
    public JLabel getLblFolio()                 { return lblFolio; }
    public JTextArea getTxtObservaciones()      { return txtObservaciones; }
    public JButton getBtnVoidItem()             { return btnVoidItem; }
    public JButton getBtnQuantity()             { return btnQuantity; }
    public JButton getBtnHold()                 { return btnHold; }
    public JButton getBtnPay()                  { return btnPay; }
    public JButton getBtnLimpiar()              { return btnLimpiar; }
    public JButton getBtnCancelar()             { return btnCancelar; }

    public Usuario getUsuarioActual() { return usuarioActual; }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        if (usuario != null) {
            lblVendedor.setText("Cajero: " + usuario.getNombre());
        }
    }

    public void setFolio(String folio) {
        lblFolio.setText("#" + folio);
    }

    public String getFolio() {
        String t = lblFolio.getText();
        return t.startsWith("#") ? t.substring(1) : t;
    }

    // Botón redondo reutilizable
    static class BotonRedondo extends JButton {
        public BotonRedondo(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setPreferredSize(new Dimension(90, 90));
            setFont(new Font("Arial", Font.BOLD, 12));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(new Color(180, 195, 215));
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(210, 225, 245));
            } else {
                g2.setColor(Color.WHITE);
            }

            g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
            g2.setColor(new Color(130, 145, 165));
            g2.draw(new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1));

            super.paintComponent(g);
            g2.dispose();
        }
    }
}
