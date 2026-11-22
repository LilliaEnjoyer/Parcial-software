public class DescuentoTemporada implements EstrategiaDescuento {
    @Override
    public double aplicarDescuento(double subtotal) {
        return subtotal * 0.85;
    }

    @Override
    public String getNombreRegla() {
        return "Descuento de Temporada (-15%)";
    }
}
