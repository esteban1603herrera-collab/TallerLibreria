package modelo;

public class LibroPapel extends Producto {
    private String autor;
    private int stock;

    public LibroPapel(String codigo, String nombre, double precio, String autor, int stock) {
        super(codigo, nombre, precio);
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.autor = autor.trim();
        this.stock = stock;
    }

    public String getAutor() {
        return autor;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public boolean hayStock(int cantidad) {
        return cantidad > 0 && stock >= cantidad;
    }

    @Override
    public void descontarStock(int cantidad) {
        if (!hayStock(cantidad)) {
            throw new IllegalArgumentException("No hay suficiente stock de " + getNombre() + ".");
        }
        stock -= cantidad;
    }

    @Override
    public double calcularIva(int cantidad) {
        return 0;
    }

    @Override
    public boolean aplicaDescuentoSocio() {
        return true;
    }

    @Override
    public String getTipo() {
        return "Libro de papel";
    }
}
