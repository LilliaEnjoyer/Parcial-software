public class CreadorRopa extends CreadorProducto {
    @Override
    public Producto crearProducto(String id, String nombre, double precio, int stock, String talla) {
        return new ProductoRopa(id, nombre, precio, stock, talla);
    }
}
