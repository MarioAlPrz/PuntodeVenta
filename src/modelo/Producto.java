package modelo;

/**
 * Modelo Producto - POJO
 * Representa un producto disponible en el punto de venta
 */
public class Producto {

    private int id;
    private String codigo;
    private String nombre;
    private double precioUnitario;
    private int stock;
    private boolean activo;

    // Constructor vacío
    public Producto() {}

    // Constructor completo
    public Producto(int id, String codigo, String nombre, double precioUnitario, int stock, boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
        this.activo = activo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
