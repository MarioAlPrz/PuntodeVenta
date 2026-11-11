package modelo;

/**
 * Modelo DetalleVenta - POJO
 * Representa una línea/producto dentro de una venta
 */
public class DetalleVenta {

    private int id;
    private int ventaId;
    private int productoId;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    // Campo auxiliar para mostrar en tablas (no se persiste)
    private String productoNombre;
    private String productoCodigo;

    // Constructor vacío
    public DetalleVenta() {}

    // Constructor completo
    public DetalleVenta(int id, int ventaId, int productoId, int cantidad,
                        double precioUnitario, double subtotal) {
        this.id = id;
        this.ventaId = ventaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVentaId() { return ventaId; }
    public void setVentaId(int ventaId) { this.ventaId = ventaId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public String getProductoCodigo() { return productoCodigo; }
    public void setProductoCodigo(String productoCodigo) { this.productoCodigo = productoCodigo; }

    @Override
    public String toString() {
        return "DetalleVenta{productoId=" + productoId + ", cantidad=" + cantidad + ", subtotal=" + subtotal + "}";
    }
}
