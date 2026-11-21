public class ProductoTecnologia extends Producto {
    private final int mesesGarantiaFabrica;

    public ProductoTecnologia(String id, String nombre, double precioBase, int stock, int mesesGarantiaFabrica) {
        super(id, nombre, precioBase, stock);
        this.mesesGarantiaFabrica = mesesGarantiaFabrica;
    }

    @Override
    public String getDescripcion() {
        return nombre + " [Tecnología - Garantía fábrica: " + mesesGarantiaFabrica + " meses]";
    }
}