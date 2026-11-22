public class DescuentoClienteFrecuente implements EstrategiaDescuento {
    @Override
    public double aplicarDescuento(double subtotal) {
        return subtotal * 0.90;
    }

    @Override
    public String getNombreRegla() {
        return "Cliente Frecuente (-10%)";
    }
}
