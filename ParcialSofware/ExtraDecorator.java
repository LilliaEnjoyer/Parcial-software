public abstract class ExtraDecorator implements Vendible {
    protected final Vendible productoEnvuelto;

    public ExtraDecorator(Vendible productoEnvuelto) {
        this.productoEnvuelto = productoEnvuelto;
    }
}
