package modelo;

/**
 * Modelo Venta - POJO
 * Representa el encabezado de una venta realizada
 */
public class Venta {

    private int id;
    private String folio;
    private String fecha;
    private int usuarioId;
    private double subtotal;
    private double iva;
    private double total;
    private String observaciones;
    private String estatus;

    public Venta() {
    }

    public Venta(int id, String folio, String fecha, int usuarioId,
            double subtotal, double iva, double total,
            String observaciones, String estatus) {
        this.id = id;
        this.folio = folio;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.observaciones = observaciones;
        this.estatus = estatus;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @Override
    public String toString() {
        return "Venta{folio='" + folio + "', total=" + total + ", estatus='" + estatus + "'}";
    }
}
