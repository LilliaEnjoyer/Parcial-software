public class CreadorTecnologia extends CreadorProducto {
    @Override
    public Producto crearProducto(String id, String nombre, double precio, int stock, String mesesGarantia) {
        return new ProductoTecnologia(id, nombre, precio, stock, Integer.parseInt(mesesGarantia));
    }
}
