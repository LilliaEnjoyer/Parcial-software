public class GarantiaExtendidaDecorator extends ExtraDecorator {
    public GarantiaExtendidaDecorator(Vendible productoEnvuelto) {
        super(productoEnvuelto);
    }

    @Override
    public String getDescripcion() {
        return productoEnvuelto.getDescripcion() + " + Garantía Extendida (+12 meses)";
    }

    @Override
    public double getPrecio() {
        return productoEnvuelto.getPrecio() + 45.00;
    }
}
