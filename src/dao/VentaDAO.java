package dao;

import modelo.Conexion;
import modelo.DetalleVenta;
import modelo.Venta;
import java.sql.*;
import java.util.List;

/**
 * VentaDAO - Data Access Object para ventas
 * Maneja el registro de ventas y generación de folios
 */
public class VentaDAO {

    private Connection conexion;

    public VentaDAO() {
        this.conexion = Conexion.getInstancia().getConexion();
    }

    /**
     * Genera un folio único con formato VEN-XXXXXX
     */
    public String generarFolio() {
        String sql = "SELECT COUNT(*) + 1 AS siguiente FROM ventas";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int siguiente = rs.getInt("siguiente");
                return String.format("VEN-%06d", siguiente);
            }
        } catch (SQLException e) {
            System.err.println("Error al generar folio: " + e.getMessage());
        }
        return "VEN-000001";
    }

    /**
     * Registra una venta completa (encabezado + detalles) en una transacción
     * @return true si se registró exitosamente
     */
    public boolean registrarVenta(Venta venta, List<DetalleVenta> detalles) {
        String sqlVenta = "INSERT INTO ventas (folio, fecha, usuario_id, subtotal, iva, total, observaciones, estatus) " +
                          "VALUES (?, NOW(), ?, ?, ?, ?, ?, 'COMPLETADA')";
        String sqlDetalle = "INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal) " +
                            "VALUES (?, ?, ?, ?, ?)";

        try {
            conexion.setAutoCommit(false); // Iniciar transacción

            // Insertar encabezado de venta
            PreparedStatement psVenta = conexion.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setString(1, venta.getFolio());
            psVenta.setInt(2, venta.getUsuarioId());
            psVenta.setDouble(3, venta.getSubtotal());
            psVenta.setDouble(4, venta.getIva());
            psVenta.setDouble(5, venta.getTotal());
            psVenta.setString(6, venta.getObservaciones());
            psVenta.executeUpdate();

            // Obtener ID generado de la venta
            ResultSet keys = psVenta.getGeneratedKeys();
            int ventaId = 0;
            if (keys.next()) {
                ventaId = keys.getInt(1);
            }

            // Insertar cada detalle
            PreparedStatement psDetalle = conexion.prepareStatement(sqlDetalle);
            for (DetalleVenta detalle : detalles) {
                psDetalle.setInt(1, ventaId);
                psDetalle.setInt(2, detalle.getProductoId());
                psDetalle.setInt(3, detalle.getCantidad());
                psDetalle.setDouble(4, detalle.getPrecioUnitario());
                psDetalle.setDouble(5, detalle.getSubtotal());
                psDetalle.addBatch();

                // Actualizar stock
                actualizarStock(detalle.getProductoId(), detalle.getCantidad());
            }
            psDetalle.executeBatch();

            conexion.commit(); // Confirmar transacción
            System.out.println("✅ Venta registrada: " + venta.getFolio());
            return true;

        } catch (SQLException e) {
            try {
                conexion.rollback(); // Revertir en caso de error
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al registrar venta: " + e.getMessage());
            return false;
        } finally {
            try {
                conexion.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error restaurando auto-commit: " + e.getMessage());
            }
        }
    }

    /**
     * Actualiza (disminuye) el stock de un producto después de una venta
     */
    public void actualizarStock(int productoId, int cantidad) throws SQLException {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, productoId);
            ps.executeUpdate();
        }
    }
}
