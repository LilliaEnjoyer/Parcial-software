public class PagoContraEntrega implements EstrategiaPago {
    private final String direccion;

    public PagoContraEntrega(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String procesarPago(double montoTotal) {
        double cargoServicio = 5.00;
        return String.format("Pedido programado Contra Entrega en '%s'. Total a pagar en efectivo (+$5.00 envío): $%.2f", 
                direccion, montoTotal + cargoServicio);
    }

    @Override
    public String getNombreMetodo() {
        return "Pago Contra Entrega";
    }
}