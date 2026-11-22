public class PagoTarjeta implements EstrategiaPago {
    private final String ultimosDigitos;

    public PagoTarjeta(String numeroTarjeta) {
        this.ultimosDigitos = numeroTarjeta.length() >= 4 
                ? numeroTarjeta.substring(numeroTarjeta.length() - 4) 
                : "0000";
    }

    @Override
    public String procesarPago(double montoTotal) {
        double comision = montoTotal * 0.02;
        return String.format("Pago aprobado con Tarjeta ****%s. Total cobrado (incl. 2%% recargo): $%.2f", 
                ultimosDigitos, montoTotal + comision);
    }

    @Override
    public String getNombreMetodo() {
        return "Tarjeta de Crédito";
    }
}
