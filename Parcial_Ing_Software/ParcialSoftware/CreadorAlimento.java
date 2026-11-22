public class CreadorAlimento extends CreadorProducto {
    @Override
    public Producto crearProducto(String id, String nombre, double precio, int stock, String fechaVencimiento) {
        return new ProductoAlimento(id, nombre, precio, stock, fechaVencimiento);
    }
}
