public class ProductoRopa extends Producto {
    private final String talla;

    public ProductoRopa(String id, String nombre, double precioBase, int stock, String talla) {
        super(id, nombre, precioBase, stock);
        this.talla = talla;
    }

    @Override
    public String getDescripcion() {
        return nombre + " [Ropa - Talla: " + talla + "]";
    }
}
