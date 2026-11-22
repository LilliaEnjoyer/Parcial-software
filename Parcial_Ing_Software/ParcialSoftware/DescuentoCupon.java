public class DescuentoCupon implements EstrategiaDescuento {
    private final double montoFijo;

    public DescuentoCupon(double montoFijo) {
        this.montoFijo = montoFijo;
    }

    @Override
    public double aplicarDescuento(double subtotal) {
        return Math.max(0, subtotal - montoFijo);
    }

    @Override
    public String getNombreRegla() {
        return "Cupón Promocional (-$" + String.format("%.2f", montoFijo) + ")";
    }
}
