package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TiendaService {
    private ArrayList<Producto> productos = new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();

    public TiendaService() {
        precargarDatos();
    }

    private void precargarDatos() {
        productos.add(new LibroPapel("LP-001", "Cien años de soledad", 28.50, "Gabriel García Márquez", 8));
        productos.add(new LibroPapel("LP-002", "El amor en los tiempos del cólera", 24.00, "Gabriel García Márquez", 5));
        productos.add(new LibroDigital("LD-001", "La insoportable levedad del ser", 12.00, "Milan Kundera", "PDF"));
        productos.add(new LibroDigital("LD-002", "Rayuela", 15.50, "Julio Cortázar", "EPUB"));
        productos.add(new ArticuloPapeleria("PA-001", "Cuaderno universitario", 4.50, "Norma", 20));
        productos.add(new ArticuloPapeleria("PA-002", "Esfero azul", 1.20, "Faber-Castell", 30));

        clientes.add(new Cliente("Ana Torres", true));
        clientes.add(new Cliente("Luis Pérez", false));
    }

    public void registrarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("Debe indicar el producto.");
        }
        for (Producto existente : productos) {
            if (existente.getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un producto con el código " + producto.getCodigo() + ".");
            }
        }
        productos.add(producto);
    }

    public List<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

    public List<Cliente> getClientes() {
        return new ArrayList<>(clientes);
    }

    public ArrayList<Producto> buscarPorNombre(String texto) {
        ArrayList<Producto> encontrados = new ArrayList<>();
        String filtro = texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase(Locale.ROOT).contains(filtro)) {
                encontrados.add(producto);
            }
        }
        return encontrados;
    }

    public ResultadoVenta registrarVenta(Cliente cliente, List<LineaVenta> lineas) {
        if (cliente == null) {
            throw new IllegalArgumentException("Debe seleccionar un cliente.");
        }
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un producto.");
        }

        Map<String, Integer> cantidadesPorCodigo = new HashMap<>();
        for (LineaVenta linea : lineas) {
            if (linea == null) {
                throw new IllegalArgumentException("La venta contiene una línea de producto inválida.");
            }
            Producto producto = linea.getProducto();
            int cantidadActual = cantidadesPorCodigo.getOrDefault(producto.getCodigo(), 0);
            cantidadesPorCodigo.put(producto.getCodigo(), cantidadActual + linea.getCantidad());
        }
        for (LineaVenta linea : lineas) {
            Producto producto = linea.getProducto();
            int cantidadTotal = cantidadesPorCodigo.get(producto.getCodigo());
            if (!producto.hayStock(cantidadTotal)) {
                throw new IllegalArgumentException("No hay suficiente stock de " + producto.getNombre() + ".");
            }
        }

        double subtotal = 0;
        double descuento = 0;
        double iva = 0;
        for (LineaVenta linea : lineas) {
            Producto producto = linea.getProducto();
            double lineaSubtotal = linea.getSubtotal();
            subtotal += lineaSubtotal;
            if (cliente.esSocio() && producto.aplicaDescuentoSocio()) {
                descuento += lineaSubtotal * 0.10;
            }
            iva += producto.calcularIva(linea.getCantidad());
        }

        for (LineaVenta linea : lineas) {
            linea.getProducto().descontarStock(linea.getCantidad());
        }
        double total = subtotal - descuento + iva;
        return new ResultadoVenta(subtotal, descuento, iva, total);
    }
}
