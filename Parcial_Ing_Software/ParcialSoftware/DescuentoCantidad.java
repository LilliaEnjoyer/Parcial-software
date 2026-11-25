public class DescuentoCantidad implements EstrategiaDescuento {
    private final int cantidadTotalUnidades;

    public DescuentoCantidad(int cantidadTotalUnidades) {
        this.cantidadTotalUnidades = cantidadTotalUnidades;
    }

    @Override
    public double aplicarDescuento(double subtotal) {
        if (cantidadTotalUnidades >= 3) {
            return subtotal * 0.88; // 12% de descuento por llevar 3 o más unidades
        }
        return subtotal;
    }

    @Override
    public String getNombreRegla() {
        return (cantidadTotalUnidades >= 3)
                ? "Descuento por Cantidad (-12% por llevar " + cantidadTotalUnidades + " uds)"
                : "Descuento por Cantidad (No aplica: requiere mín. 3 uds)";
    }
}