package modelo;

public class ResultadoVenta {
    private double subtotal;
    private double descuento;
    private double iva;
    private double total;

    public ResultadoVenta(double subtotal, double descuento, double iva, double total) {
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.iva = iva;
        this.total = total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getIva() {
        return iva;
    }

    public double getTotal() {
        return total;
    }
}
