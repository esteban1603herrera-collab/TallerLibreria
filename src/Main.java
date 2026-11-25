import modelo.TiendaService;
import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TiendaService tiendaService = new TiendaService();
            VentanaPrincipal ventana = new VentanaPrincipal(tiendaService);
            ventana.setVisible(true);
        });
    }
}