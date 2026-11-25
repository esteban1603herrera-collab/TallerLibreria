package modelo;

public class Cliente {
    private String nombre;
    private boolean socio;

    public Cliente(String nombre, boolean socio) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        this.nombre = nombre.trim();
        this.socio = socio;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean esSocio() {
        return socio;
    }

    @Override
    public String toString() {
        return nombre + (socio ? " (Socio)" : "");
    }
}
