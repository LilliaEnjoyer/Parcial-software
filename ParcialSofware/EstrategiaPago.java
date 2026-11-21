public interface EstrategiaPago {
    String procesarPago(double montoTotal);
    String getNombreMetodo();
}