public class ProductoAlimento extends Producto {
    private final String fechaVencimiento;

    public ProductoAlimento(String id, String nombre, double precioBase, int stock, String fechaVencimiento) {
        super(id, nombre, precioBase, stock);
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String getDescripcion() {
        return nombre + " [Alimento - Vence: " + fechaVencimiento + "]";
    }
}
