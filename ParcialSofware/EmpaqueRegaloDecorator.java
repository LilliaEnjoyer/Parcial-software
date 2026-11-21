public class EmpaqueRegaloDecorator extends ExtraDecorator {
    public EmpaqueRegaloDecorator(Vendible productoEnvuelto) {
        super(productoEnvuelto);
    }

    @Override
    public String getDescripcion() {
        return productoEnvuelto.getDescripcion() + " + Empaque de Regalo";
    }

    @Override
    public double getPrecio() {
        return productoEnvuelto.getPrecio() + 8.50;
    }
}
