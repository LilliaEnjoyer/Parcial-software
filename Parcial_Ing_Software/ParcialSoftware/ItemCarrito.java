public class ItemCarrito {
    private final Vendible item;
    private int cantidad;

    public ItemCarrito(Vendible item, int cantidad) {
        this.item = item;
        this.cantidad = cantidad;
    }

    public Vendible getItem() {
        return item;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotalItem() {
        return item.getPrecio() * cantidad;
    }

    public String getDescripcionCompleta() {
        return String.format("%s (x%d)", item.getDescripcion(), cantidad);
    }
}