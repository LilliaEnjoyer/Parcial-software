import java.util.ArrayList;
import java.util.List;

public abstract class Producto implements Vendible {
    protected String id;
    protected String nombre;
    protected double precioBase;
    protected int stock;
    private final List<ObservadorStock> observadores = new ArrayList<>();

    public Producto(String id, String nombre, double precioBase, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
    }

    public void suscribir(ObservadorStock observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void reponerStock(int cantidad) {
        boolean estabaAgotado = (this.stock == 0);
        this.stock += cantidad;
        if (estabaAgotado && this.stock > 0) {
            notificarObservadores();
        }
    }

    public boolean descontarStock(int cantidad) {
        if (this.stock >= cantidad) {
            this.stock -= cantidad;
            return true;
        }
        return false;
    }

    private void notificarObservadores() {
        for (ObservadorStock obs : observadores) {
            obs.actualizar(this.nombre, this.stock);
        }
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public int getStock() { return stock; }

    @Override
    public double getPrecio() {
        return precioBase;
    }
}