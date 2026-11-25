package modelo;

public class LibroDigital extends Producto {
    private String autor;
    private String formato;

    public LibroDigital(String codigo, String nombre, double precio, String autor, String formato) {
        super(codigo, nombre, precio);
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        if (!"PDF".equalsIgnoreCase(formato) && !"EPUB".equalsIgnoreCase(formato)) {
            throw new IllegalArgumentException("El formato debe ser PDF o EPUB.");
        }
        this.autor = autor.trim();
        this.formato = formato.trim().toUpperCase();
    }

    public String getAutor() {
        return autor;
    }

    public String getFormato() {
        return formato;
    }

    @Override
    public boolean hayStock(int cantidad) {
        return cantidad > 0;
    }

    @Override
    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
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
        return "Libro digital";
    }
}
