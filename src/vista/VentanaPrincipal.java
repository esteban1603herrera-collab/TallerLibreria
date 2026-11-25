package vista;

import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private TiendaService tienda;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JComboBox<Producto> comboProductos;
    private JComboBox<Cliente> comboClientes;
    private ArrayList<LineaVenta> lineasVenta = new ArrayList<>();
    private DefaultTableModel modeloVenta;
    private JLabel resultadoVenta;

    public VentanaPrincipal(TiendaService tienda) {
        this.tienda = tienda;
        setTitle("Páginas Andinas");
        setSize(820, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Productos", crearPanelProductos());
        pestañas.addTab("Nueva venta", crearPanelVenta());
        add(pestañas, BorderLayout.CENTER);
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloTabla = new DefaultTableModel(
                new Object[]{"Código", "Tipo", "Nombre", "Precio", "Detalle", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        JPanel controles = new JPanel(new BorderLayout(5, 5));
        JTextField buscador = new JTextField();
        JButton buscar = new JButton("Buscar");
        JButton mostrarTodos = new JButton("Mostrar todos");
        JButton nuevo = new JButton("Registrar producto");

        JPanel busqueda = new JPanel(new BorderLayout(5, 5));
        busqueda.add(new JLabel("Nombre:"), BorderLayout.WEST);
        busqueda.add(buscador, BorderLayout.CENTER);
        busqueda.add(buscar, BorderLayout.EAST);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(mostrarTodos);
        botones.add(nuevo);
        controles.add(busqueda, BorderLayout.NORTH);
        controles.add(botones, BorderLayout.CENTER);
        panel.add(controles, BorderLayout.NORTH);

        buscar.addActionListener(e -> actualizarTabla(tienda.buscarPorNombre(buscador.getText())));
        mostrarTodos.addActionListener(e -> actualizarTabla(tienda.getProductos()));
        nuevo.addActionListener(e -> registrarProducto());

        actualizarTabla(tienda.getProductos());
        return panel;
    }

    private void actualizarTabla(List<Producto> productos) {
        modeloTabla.setRowCount(0);
        for (Producto producto : productos) {
            String detalle = "";
            String stock = "";
            if (producto instanceof LibroPapel) {
                LibroPapel libro = (LibroPapel) producto;
                detalle = libro.getAutor();
                stock = String.valueOf(libro.getStock());
            } else if (producto instanceof LibroDigital) {
                LibroDigital libro = (LibroDigital) producto;
                detalle = libro.getAutor() + " - " + libro.getFormato();
                stock = "Sin límite";
            } else if (producto instanceof ArticuloPapeleria) {
                ArticuloPapeleria articulo = (ArticuloPapeleria) producto;
                detalle = articulo.getMarca();
                stock = String.valueOf(articulo.getStock());
            }
            modeloTabla.addRow(new Object[]{
                    producto.getCodigo(), producto.getTipo(), producto.getNombre(),
                    String.format("%.2f", producto.getPrecio()), detalle, stock
            });
        }
    }

    private void registrarProducto() {
        String[] tipos = {"Libro de papel", "Libro digital", "Papelería"};
        JComboBox<String> tipo = new JComboBox<>(tipos);
        JTextField codigo = new JTextField();
        JTextField nombre = new JTextField();
        JTextField precio = new JTextField();
        JTextField extra1 = new JTextField();
        JTextField extra2 = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.add(new JLabel("Tipo")); panel.add(tipo);
        panel.add(new JLabel("Código")); panel.add(codigo);
        panel.add(new JLabel("Nombre")); panel.add(nombre);
        panel.add(new JLabel("Precio")); panel.add(precio);
        panel.add(new JLabel("Autor / Marca")); panel.add(extra1);
        panel.add(new JLabel("Stock / Formato")); panel.add(extra2);

        while (true) {
            int opcion = JOptionPane.showConfirmDialog(this, panel,
                    "Registrar producto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (opcion != JOptionPane.OK_OPTION) return;
            try {
                String codigoTexto = codigo.getText();
                String nombreTexto = nombre.getText();
                double precioNumero = Double.parseDouble(precio.getText().trim().replace(',', '.'));
                Producto producto;
                if (tipo.getSelectedIndex() == 0) {
                    producto = new LibroPapel(codigoTexto, nombreTexto, precioNumero,
                            extra1.getText(), Integer.parseInt(extra2.getText()));
                } else if (tipo.getSelectedIndex() == 1) {
                    producto = new LibroDigital(codigoTexto, nombreTexto, precioNumero,
                            extra1.getText(), extra2.getText());
                } else {
                    producto = new ArticuloPapeleria(codigoTexto, nombreTexto, precioNumero,
                            extra1.getText(), Integer.parseInt(extra2.getText()));
                }
                tienda.registrarProducto(producto);
                actualizarTabla(tienda.getProductos());
                actualizarProductosVenta();
                JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
                return;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Revise el precio y, si corresponde, el stock: deben ser números válidos.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private JPanel crearPanelVenta() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel superior = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        superior.add(new JLabel("Cliente:"), gbc);
        comboClientes = new JComboBox<>(tienda.getClientes().toArray(new Cliente[0]));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        superior.add(comboClientes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        superior.add(new JLabel("Producto:"), gbc);
        comboProductos = new JComboBox<>();
        actualizarProductosVenta();
        comboProductos.setPreferredSize(new Dimension(300, comboProductos.getPreferredSize().height));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        superior.add(comboProductos, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        superior.add(new JLabel("Cantidad:"), gbc);
        JSpinner cantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        gbc.gridx = 3;
        superior.add(cantidad, gbc);
        JButton agregar = new JButton("Ingresar producto");
        gbc.gridx = 4;
        superior.add(agregar, gbc);
        panel.add(superior, BorderLayout.NORTH);

        modeloVenta = new DefaultTableModel(
                new Object[]{"Código", "Producto", "Cantidad", "Precio", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaVenta = new JTable(modeloVenta);
        panel.add(new JScrollPane(tablaVenta), BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(8, 8));
        JPanel botonesVenta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton quitar = new JButton("Quitar línea");
        JButton finalizar = new JButton("Finalizar venta");
        botonesVenta.add(quitar);
        botonesVenta.add(finalizar);
        resultadoVenta = new JLabel("Seleccione un producto y pulse «Ingresar producto». Luego finalice la venta para cobrar.");
        resultadoVenta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        resultadoVenta.setOpaque(true);
        resultadoVenta.setBackground(new Color(245, 245, 245));
        inferior.add(resultadoVenta, BorderLayout.CENTER);
        inferior.add(botonesVenta, BorderLayout.SOUTH);
        panel.add(inferior, BorderLayout.SOUTH);

        agregar.addActionListener(e -> {
            try {
                Producto producto = (Producto) comboProductos.getSelectedItem();
                int unidades = (Integer) cantidad.getValue();
                LineaVenta linea = new LineaVenta(producto, unidades);
                lineasVenta.add(linea);
                modeloVenta.addRow(new Object[]{
                        producto.getCodigo(), producto.getNombre(), unidades,
                        String.format("%.2f", producto.getPrecio()),
                        String.format("%.2f", linea.getSubtotal())
                });
                int ultimaFila = modeloVenta.getRowCount() - 1;
                tablaVenta.setRowSelectionInterval(ultimaFila, ultimaFila);
                tablaVenta.scrollRectToVisible(tablaVenta.getCellRect(ultimaFila, 0, true));
                resultadoVenta.setText("Ingresado a la venta: " + producto.getNombre()
                        + " (" + unidades + "). Revise la línea en la tabla.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        quitar.addActionListener(e -> {
            int fila = tablaVenta.getSelectedRow();
            if (fila >= 0) {
                lineasVenta.remove(fila);
                modeloVenta.removeRow(fila);
                resultadoVenta.setText("Línea quitada. Revise el carrito antes de finalizar la venta.");
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una línea para quitar.");
            }
        });

        finalizar.addActionListener(e -> {
            try {
                ResultadoVenta resultado = tienda.registrarVenta(
                        (Cliente) comboClientes.getSelectedItem(), lineasVenta);
                String mensaje = String.format(
                        "Subtotal: $%.2f%nDescuento socio: $%.2f%nIVA: $%.2f%nTOTAL A PAGAR: $%.2f",
                        resultado.getSubtotal(), resultado.getDescuento(),
                        resultado.getIva(), resultado.getTotal());
                resultadoVenta.setText(String.format(
                        "<html><b>Venta registrada</b> — Subtotal: $%.2f &nbsp; Descuento socio: $%.2f"
                                + " &nbsp; IVA: $%.2f &nbsp; <b>Total a pagar: $%.2f</b></html>",
                        resultado.getSubtotal(), resultado.getDescuento(),
                        resultado.getIva(), resultado.getTotal()));
                JOptionPane.showMessageDialog(this, mensaje, "Resultado de la venta",
                        JOptionPane.INFORMATION_MESSAGE);
                lineasVenta.clear();
                modeloVenta.setRowCount(0);
                actualizarTabla(tienda.getProductos());
                actualizarProductosVenta();
            } catch (IllegalArgumentException ex) {
                resultadoVenta.setText("No se registró la venta. Corrija el problema indicado.");
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        return panel;
    }

    private void actualizarProductosVenta() {
        if (comboProductos != null) {
            comboProductos.removeAllItems();
            for (Producto producto : tienda.getProductos()) {
                comboProductos.addItem(producto);
            }
        }
    }
}
