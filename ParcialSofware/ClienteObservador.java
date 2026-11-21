import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

public class ClienteObservador implements ObservadorStock {
    private final String nombre;
    private final String email;
    private final List<String> notificacionesRecibidas = new ArrayList<>();

    public ClienteObservador(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    @Override
    public void actualizar(String nombreProducto, int nuevoStock) {
        String mensaje = "[ALERTA PARA " + nombre + " (" + email + ")]: El producto '" 
                         + nombreProducto + "' vuelve a estar disponible (Stock: " + nuevoStock + ").";
        notificacionesRecibidas.add(mensaje);
        System.out.println(mensaje);
    }

    @Override
    public String getNombreCliente() {
        return nombre;
    }

    public List<String> getNotificacionesRecibidas() {
        return notificacionesRecibidas;
    }
}