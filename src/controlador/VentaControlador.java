package controlador;

import dao.ProductoDAO;
import dao.VentaDAO;
import modelo.DetalleVenta;
import modelo.Producto;
import modelo.Venta;
import vista.VentaPOSVista;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class VentaControlador {

    private VentaPOSVista vista;
    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;
    private List<Producto> productosDisponibles;

    public VentaControlador(VentaPOSVista vista) {
        this.vista = vista;
        this.productoDAO = new ProductoDAO();
        this.ventaDAO = new VentaDAO();
        this.productosDisponibles = new ArrayList<>();

        initEventos();
        cargarProductos();
        generarNuevoFolio();
    }

    private void initEventos() {
        vista.getTxtCodigoBarras().addActionListener(e -> buscarYAgregar());

        vista.getBtnVoidItem().addActionListener(e -> eliminarProductoSeleccionado());
        vista.getBtnQuantity().addActionListener(e -> cambiarCantidad());
        vista.getBtnLimpiar().addActionListener(e -> limpiarVenta());
        vista.getBtnCancelar().addActionListener(e -> cancelarVenta());
        vista.getBtnPay().addActionListener(e -> finalizarVenta());

        vista.getBtnHold().addActionListener(e -> {
            JOptionPane.showMessageDialog(vista,
                "La venta actual ha sido puesta en espera.\n(Funcionalidad pendiente)",
                "Hold", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void cargarProductos() {
        productosDisponibles = productoDAO.listarActivos();
    }

    private void generarNuevoFolio() {
        String folio = ventaDAO.generarFolio();
        vista.setFolio(folio);
    }

    private void buscarYAgregar() {
        String busqueda = vista.getTxtCodigoBarras().getText().trim();
        if (busqueda.isEmpty()) return;

        Producto producto = null;
        for (Producto p : productosDisponibles) {
            if (p.getCodigo().equalsIgnoreCase(busqueda)) {
                producto = p;
                break;
            }
        }

        if (producto == null) {
            List<Producto> encontrados = new ArrayList<>();
            for (Producto p : productosDisponibles) {
                if (p.getNombre().toLowerCase().contains(busqueda.toLowerCase())) {
                    encontrados.add(p);
                }
            }

            if (encontrados.isEmpty()) {
                JOptionPane.showMessageDialog(vista,
                    "No se encontro el producto: " + busqueda,
                    "Producto no encontrado",
                    JOptionPane.WARNING_MESSAGE);
                vista.getTxtCodigoBarras().selectAll();
                return;
            } else if (encontrados.size() == 1) {
                producto = encontrados.get(0);
            } else {
                String[] opciones = new String[encontrados.size()];
                for (int i = 0; i < encontrados.size(); i++) {
                    opciones[i] = encontrados.get(i).getCodigo() + " - " + encontrados.get(i).getNombre()
                                 + " ($" + String.format("%.2f", encontrados.get(i).getPrecioUnitario()) + ")";
                }
                String seleccion = (String) JOptionPane.showInputDialog(vista,
                    "Se encontraron varios productos. Selecciona uno:",
                    "Seleccionar Producto",
                    JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
                if (seleccion == null) {
                    vista.getTxtCodigoBarras().selectAll();
                    return;
                }
                String codigoSel = seleccion.split(" - ")[0];
                for (Producto p : encontrados) {
                    if (p.getCodigo().equals(codigoSel)) {
                        producto = p;
                        break;
                    }
                }
            }
        }

        if (producto == null) return;

        int filaExistente = buscarProductoEnTabla(producto.getCodigo());
        if (filaExistente >= 0) {
            int cantActual = Integer.parseInt(
                vista.getModeloDetalle().getValueAt(filaExistente, 1).toString());
            int nuevaCant = cantActual + 1;

            if (nuevaCant > producto.getStock()) {
                JOptionPane.showMessageDialog(vista,
                    "Stock insuficiente. Disponible: " + producto.getStock(),
                    "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                vista.getTxtCodigoBarras().selectAll();
                return;
            }

            actualizarFilaProducto(filaExistente, producto, nuevaCant);
        } else {
            if (producto.getStock() <= 0) {
                JOptionPane.showMessageDialog(vista,
                    "Producto sin stock disponible",
                    "Stock agotado", JOptionPane.WARNING_MESSAGE);
                vista.getTxtCodigoBarras().selectAll();
                return;
            }

            agregarFilaProducto(producto, 1);
        }

        calcularTotales();

        vista.getTxtCodigoBarras().setText("");
        vista.getTxtCodigoBarras().requestFocusInWindow();
    }

    private int buscarProductoEnTabla(String codigo) {
        for (int i = 0; i < vista.getModeloDetalle().getRowCount(); i++) {
            for (Producto p : productosDisponibles) {
                if (p.getCodigo().equals(codigo)) {
                    String nombreTabla = vista.getModeloDetalle().getValueAt(i, 0).toString();
                    if (nombreTabla.startsWith(p.getNombre())) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private void agregarFilaProducto(Producto p, int cantidad) {
        double subtotal = p.getPrecioUnitario() * cantidad;
        double iva = subtotal * 0.16;
        String displayName = p.getNombre() + " x " + cantidad;

        vista.getModeloDetalle().addRow(new Object[]{
            displayName,
            cantidad,
            String.format("%.2f", p.getPrecioUnitario()),
            String.format("%.2f", subtotal),
            String.format("%.2f", iva)
        });
    }

    private void actualizarFilaProducto(int fila, Producto p, int nuevaCantidad) {
        double subtotal = p.getPrecioUnitario() * nuevaCantidad;
        double iva = subtotal * 0.16;
        String displayName = p.getNombre() + " x " + nuevaCantidad;

        vista.getModeloDetalle().setValueAt(displayName, fila, 0);
        vista.getModeloDetalle().setValueAt(nuevaCantidad, fila, 1);
        vista.getModeloDetalle().setValueAt(String.format("%.2f", p.getPrecioUnitario()), fila, 2);
        vista.getModeloDetalle().setValueAt(String.format("%.2f", subtotal), fila, 3);
        vista.getModeloDetalle().setValueAt(String.format("%.2f", iva), fila, 4);
    }

    private void eliminarProductoSeleccionado() {
        int fila = vista.getTablaDetalle().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista,
                "Selecciona un producto de la tabla para eliminarlo",
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vista,
            "¿Eliminar '" + vista.getModeloDetalle().getValueAt(fila, 0) + "' de la venta?",
            "Confirmar eliminacion",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            vista.getModeloDetalle().removeRow(fila);
            calcularTotales();
        }
    }

    private void cambiarCantidad() {
        int fila = vista.getTablaDetalle().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista,
                "Selecciona un producto de la tabla para cambiar la cantidad",
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String productoDisplay = vista.getModeloDetalle().getValueAt(fila, 0).toString();
        String nombreBase = productoDisplay.contains(" x ") ?
            productoDisplay.substring(0, productoDisplay.lastIndexOf(" x ")) : productoDisplay;

        Producto producto = null;
        for (Producto p : productosDisponibles) {
            if (p.getNombre().equals(nombreBase)) {
                producto = p;
                break;
            }
        }

        if (producto == null) {
            JOptionPane.showMessageDialog(vista,
                "No se pudo identificar el producto",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(vista,
            "Nueva cantidad para: " + producto.getNombre() + "\n(Stock disponible: " + producto.getStock() + ")",
            "Cambiar Cantidad",
            JOptionPane.QUESTION_MESSAGE);

        if (input == null) return;

        try {
            int nuevaCantidad = Integer.parseInt(input.trim());
            if (nuevaCantidad <= 0) {
                vista.getModeloDetalle().removeRow(fila);
            } else if (nuevaCantidad > producto.getStock()) {
                JOptionPane.showMessageDialog(vista,
                    "Stock insuficiente. Disponible: " + producto.getStock(),
                    "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                actualizarFilaProducto(fila, producto, nuevaCantidad);
            }
            calcularTotales();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista,
                "Ingresa un numero valido",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularTotales() {
        double subtotalVenta = 0;
        double ivaVenta = 0;

        for (int i = 0; i < vista.getModeloDetalle().getRowCount(); i++) {
            String subtotalStr = vista.getModeloDetalle().getValueAt(i, 3).toString()
                .replace("$", "").replace(",", "");
            String ivaStr = vista.getModeloDetalle().getValueAt(i, 4).toString()
                .replace("$", "").replace(",", "");
            subtotalVenta += Double.parseDouble(subtotalStr);
            ivaVenta += Double.parseDouble(ivaStr);
        }

        double total = subtotalVenta + ivaVenta;

        vista.getLblSubtotal().setText(String.format("$%,.2f", subtotalVenta));
        vista.getLblIva().setText(String.format("$%,.2f", ivaVenta));
        vista.getLblTotal().setText(String.format("$%,.2f", total));

        vista.getBtnPay().repaint();
    }

    private void limpiarVenta() {
        vista.getModeloDetalle().setRowCount(0);
        vista.getLblSubtotal().setText("$0.00");
        vista.getLblIva().setText("$0.00");
        vista.getLblTotal().setText("$0.00");
        vista.getTxtObservaciones().setText("");
        vista.getTxtCodigoBarras().setText("");
        vista.getTxtCodigoBarras().requestFocusInWindow();
        generarNuevoFolio();
        cargarProductos();
        vista.getBtnPay().repaint();
    }

    private void cancelarVenta() {
        if (vista.getModeloDetalle().getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista,
                "No hay venta que cancelar",
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vista,
            "¿Estas seguro de cancelar la venta actual?\nSe perderan todos los productos agregados.",
            "Cancelar Venta",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            limpiarVenta();
            JOptionPane.showMessageDialog(vista,
                "Venta cancelada",
                "Cancelada", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void finalizarVenta() {
        if (vista.getModeloDetalle().getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista,
                "Agrega al menos un producto para finalizar la venta",
                "Venta vacia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vista,
            "¿Finalizar y registrar esta venta?\n\nTotal: " + vista.getLblTotal().getText(),
            "Finalizar Venta",
            JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        Venta venta = new Venta();
        venta.setFolio(vista.getFolio());
        venta.setUsuarioId(vista.getUsuarioActual().getId());
        venta.setObservaciones(vista.getTxtObservaciones().getText());

        String subStr = vista.getLblSubtotal().getText().replace("$", "").replace(",", "");
        String ivaStr = vista.getLblIva().getText().replace("$", "").replace(",", "");
        String totalStr = vista.getLblTotal().getText().replace("$", "").replace(",", "");
        venta.setSubtotal(Double.parseDouble(subStr));
        venta.setIva(Double.parseDouble(ivaStr));
        venta.setTotal(Double.parseDouble(totalStr));

        List<DetalleVenta> detalles = new ArrayList<>();
        for (int i = 0; i < vista.getModeloDetalle().getRowCount(); i++) {
            String productoDisplay = vista.getModeloDetalle().getValueAt(i, 0).toString();
            String nombreBase = productoDisplay.contains(" x ") ?
                productoDisplay.substring(0, productoDisplay.lastIndexOf(" x ")) : productoDisplay;

            Producto prod = null;
            for (Producto p : productosDisponibles) {
                if (p.getNombre().equals(nombreBase)) {
                    prod = p;
                    break;
                }
            }

            if (prod != null) {
                DetalleVenta det = new DetalleVenta();
                det.setProductoId(prod.getId());
                det.setCantidad(Integer.parseInt(vista.getModeloDetalle().getValueAt(i, 1).toString()));
                det.setPrecioUnitario(prod.getPrecioUnitario());
                String detSubStr = vista.getModeloDetalle().getValueAt(i, 3).toString()
                    .replace("$", "").replace(",", "");
                det.setSubtotal(Double.parseDouble(detSubStr));
                detalles.add(det);
            }
        }

        boolean exito = ventaDAO.registrarVenta(venta, detalles);

        if (exito) {
            JOptionPane.showMessageDialog(vista,
                "Venta registrada exitosamente\n" +
                "Folio: " + venta.getFolio() + "\n" +
                "Total: " + vista.getLblTotal().getText(),
                "Venta Completada",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarVenta();
        } else {
            JOptionPane.showMessageDialog(vista,
                "Error al registrar la venta. Intenta de nuevo.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
