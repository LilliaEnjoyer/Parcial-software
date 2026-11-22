import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TiendaService {
    private final List<Producto> catalogo = new ArrayList<>();
    private final List<Vendible> carrito = new ArrayList<>();
    private final List<EstrategiaDescuento> descuentosActivos = new ArrayList<>();
    private final List<String> historialFacturas = new ArrayList<>();

    public TiendaService() {
        cargarCatalogoInicial();
    }

    private void cargarCatalogoInicial() {
        CreadorProducto fabricaTech = new CreadorTecnologia();
        CreadorProducto fabricaRopa = new CreadorRopa();
        CreadorProducto fabricaAlimento = new CreadorAlimento();

        catalogo.add(fabricaTech.crearProducto("P01", "Laptop Gamer RTX", 1200.00, 3, "24"));
        catalogo.add(fabricaTech.crearProducto("P02", "Mouse Inalámbrico", 60.00, 0, "12"));
        catalogo.add(fabricaRopa.crearProducto("P03", "Chaqueta Impermeable", 85.00, 5, "L"));
        catalogo.add(fabricaAlimento.crearProducto("P04", "Café Especial Origen 500g", 18.00, 10, "2027-06-30"));
    }

    public List<Producto> getCatalogo() {
        return catalogo;
    }

    public Producto buscarProductoPorId(String id) {
        return catalogo.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public boolean agregarAlCarrito(String productoId, boolean garantia, boolean regalo, boolean express) {
        Producto base = buscarProductoPorId(productoId);
        if (base == null || !base.descontarStock(1)) {
            return false;
        }

        Vendible item = base;
        if (garantia) item = new GarantiaExtendidaDecorator(item);
        if (regalo) item = new EmpaqueRegaloDecorator(item);
        if (express) item = new EnvioExpressDecorator(item);

        carrito.add(item);
        return true;
    }

    public List<Vendible> getCarrito() {
        return carrito;
    }

    public void agregarDescuento(EstrategiaDescuento descuento) {
        descuentosActivos.add(descuento);
    }

    public void limpiarDescuentos() {
        descuentosActivos.clear();
    }

    public double calcularSubtotal() {
        return carrito.stream().mapToDouble(Vendible::getPrecio).sum();
    }

    public double calcularTotalConDescuentos() {
        double total = calcularSubtotal();
        for (EstrategiaDescuento estrategia : descuentosActivos) {
            total = estrategia.aplicarDescuento(total);
        }
        return total;
    }

    public String cerrarPedido(EstrategiaPago metodoPago) {
        if (carrito.isEmpty()) {
            return "El carrito está vacío. No se puede generar factura.";
        }

        double subtotal = calcularSubtotal();
        double totalFinal = calcularTotalConDescuentos();
        String resultadoPago = metodoPago.procesarPago(totalFinal);

        StringBuilder factura = new StringBuilder();
        factura.append("==========================================\n");
        factura.append("           FACTURA DE COMPRA              \n");
        factura.append("Fecha: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        factura.append("------------------------------------------\n");
        for (Vendible item : carrito) {
            factura.append(String.format("- %s : $%.2f\n", item.getDescripcion(), item.getPrecio()));
        }
        factura.append("------------------------------------------\n");
        factura.append(String.format("Subtotal: $%.2f\n", subtotal));
        if (descuentosActivos.isEmpty()) {
            factura.append("Descuentos: Ninguno\n");
        } else {
            for (EstrategiaDescuento d : descuentosActivos) {
                factura.append("Aplicado: ").append(d.getNombreRegla()).append("\n");
            }
        }
        factura.append(String.format("Total tras descuentos: $%.2f\n", totalFinal));
        factura.append("Medio de pago: ").append(metodoPago.getNombreMetodo()).append("\n");
        factura.append("Estado: ").append(resultadoPago).append("\n");
        factura.append("==========================================\n");

        String textoFactura = factura.toString();
        historialFacturas.add(textoFactura);
        carrito.clear();
        descuentosActivos.clear();
        return textoFactura;
    }

    public void suscribirAProducto(String productoId, ObservadorStock cliente) {
        Producto p = buscarProductoPorId(productoId);
        if (p != null) {
            p.suscribir(cliente);
        }
    }

    public void reabastecerProducto(String productoId, int cantidad) {
        Producto p = buscarProductoPorId(productoId);
        if (p != null) {
            p.reponerStock(cantidad);
        }
    }

    public List<String> getHistorialFacturas() {
        return historialFacturas;
    }
}
