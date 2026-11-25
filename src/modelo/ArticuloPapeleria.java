package modelo;

public class ArticuloPapeleria extends Producto {
    private String marca;
    private int stock;

    public ArticuloPapeleria(String codigo, String nombre, double precio, String marca, int stock) {
        super(codigo, nombre, precio);
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.marca = marca.trim();
        this.stock = stock;
    }

    public String getMarca() {
        return marca;
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
        return getPrecio() * cantidad * 0.15;
    }

    @Override
    public boolean aplicaDescuentoSocio() {
        return false;
    }

    @Override
    public String getTipo() {
        return "Papelería";
    }
}
