public class PagoTransferencia implements EstrategiaPago {
    private final String banco;

    public PagoTransferencia(String banco) {
        this.banco = banco;
    }

    @Override
    public String procesarPago(double montoTotal) {
        return String.format("Transferencia validada vía %s (Sin comisión). Total transferido: $%.2f", 
                banco, montoTotal);
    }

    @Override
    public String getNombreMetodo() {
        return "Transferencia Bancaria (" + banco + ")";
    }
}
