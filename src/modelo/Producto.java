package modelo;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private double precio;

    protected Producto(String codigo, String nombre, double precio) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException("El precio debe ser un número válido y no puede ser negativo.");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " ($" + String.format("%.2f", precio) + ")";
    }

    public abstract boolean hayStock(int cantidad);

    public abstract void descontarStock(int cantidad);

    public abstract double calcularIva(int cantidad);

    public abstract boolean aplicaDescuentoSocio();

    public abstract String getTipo();
}
