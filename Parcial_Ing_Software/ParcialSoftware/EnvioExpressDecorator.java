public class EnvioExpressDecorator extends ExtraDecorator {
    public EnvioExpressDecorator(Vendible productoEnvuelto) {
        super(productoEnvuelto);
    }

    @Override
    public String getDescripcion() {
        return productoEnvuelto.getDescripcion() + " + Envío Express 24h";
    }

    @Override
    public double getPrecio() {
        return productoEnvuelto.getPrecio() + 15.00;
    }
}
