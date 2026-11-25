package modelo;

public class LineaVenta {
    private Producto producto;
    private int cantidad;

    public LineaVenta(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("Debe seleccionar un producto.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}
