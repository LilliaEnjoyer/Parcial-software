import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TiendaService {
    private final List<Producto> catalogo = new ArrayList<>();
    private final List<ItemCarrito> carrito = new ArrayList<>();
    private final List<EstrategiaDescuento> descuentosActivos = new ArrayList<>();
    private final List<String> historialFacturas = new ArrayList<>();

    public TiendaService() {
        cargarCatalogoInicial();
    }

    private void cargarCatalogoInicial() {
        CreadorProducto fabricaTech = new CreadorTecnologia();
        CreadorProducto fabricaRopa = new CreadorRopa();
        CreadorProducto fabricaAlimento = new CreadorAlimento();

        catalogo.add(fabricaTech.crearProducto("P01", "Laptop Gamer RTX", 1200.00, 5, "24"));
        catalogo.add(fabricaTech.crearProducto("P02", "Mouse Inalámbrico", 60.00, 0, "12"));
        catalogo.add(fabricaRopa.crearProducto("P03", "Chaqueta Impermeable", 85.00, 8, "L"));
        catalogo.add(fabricaAlimento.crearProducto("P04", "Café Especial Origen 500g", 18.00, 15, "2027-06-30"));
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

    private Producto obtenerProductoBase(Vendible item) {
        Vendible actual = item;
        while (actual instanceof ExtraDecorator decorador) {
            actual = decorador.productoEnvuelto;
        }
        return (actual instanceof Producto prod) ? prod : null;
    }

    public boolean agregarAlCarrito(String productoId, int cantidad, boolean garantia, boolean regalo, boolean express) {
        if (cantidad <= 0) return false;
        Producto base = buscarProductoPorId(productoId);
        if (base == null || !base.descontarStock(cantidad)) {
            return false;
        }

        Vendible item = base;
        if (garantia) item = new GarantiaExtendidaDecorator(item);
        if (regalo) item = new EmpaqueRegaloDecorator(item);
        if (express) item = new EnvioExpressDecorator(item);

        // Si ya existe exactamente la misma configuración en el carrito, suma la cantidad
        for (ItemCarrito ic : carrito) {
            if (ic.getItem().getDescripcion().equals(item.getDescripcion())) {
                ic.setCantidad(ic.getCantidad() + cantidad);
                return true;
            }
        }

        carrito.add(new ItemCarrito(item, cantidad));
        return true;
    }

    public boolean modificarCantidadCarrito(int indice, int nuevaCantidad) {
        if (indice < 0 || indice >= carrito.size()) return false;
        if (nuevaCantidad <= 0) {
            return eliminarDelCarrito(indice);
        }

        ItemCarrito ic = carrito.get(indice);
        Producto base = obtenerProductoBase(ic.getItem());
        if (base == null) return false;

        int diferencia = nuevaCantidad - ic.getCantidad();
        if (diferencia > 0) {
            if (!base.descontarStock(diferencia)) {
                return false; // No alcanza el stock para aumentar
            }
        } else if (diferencia < 0) {
            base.reponerStock(Math.abs(diferencia)); // Devuelve unidades al stock
        }

        ic.setCantidad(nuevaCantidad);
        return true;
    }

    public boolean eliminarDelCarrito(int indice) {
        if (indice < 0 || indice >= carrito.size()) return false;
        ItemCarrito removido = carrito.remove(indice);
        Producto base = obtenerProductoBase(removido.getItem());
        if (base != null) {
            base.reponerStock(removido.getCantidad());
        }
        return true;
    }

    public void vaciarCarrito() {
        for (ItemCarrito ic : carrito) {
            Producto base = obtenerProductoBase(ic.getItem());
            if (base != null) {
                base.reponerStock(ic.getCantidad());
            }
        }
        carrito.clear();
    }

    public List<ItemCarrito> getCarrito() {
        return carrito;
    }

    public int getTotalUnidadesCarrito() {
        return carrito.stream().mapToInt(ItemCarrito::getCantidad).sum();
    }

    public void agregarDescuento(EstrategiaDescuento descuento) {
        descuentosActivos.add(descuento);
    }

    public void limpiarDescuentos() {
        descuentosActivos.clear();
    }

    public double calcularSubtotal() {
        return carrito.stream().mapToDouble(ItemCarrito::getSubtotalItem).sum();
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
        for (ItemCarrito ic : carrito) {
            factura.append(String.format("- %s : $%.2f\n", ic.getDescripcionCompleta(), ic.getSubtotalItem()));
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

    public String consultarHistorialPedidos() {
        if (historialFacturas.isEmpty()) {
            return "No hay pedidos registrados en el historial todavía.";
        }
        StringBuilder sb = new StringBuilder("=== HISTORIAL DE PEDIDOS (" + historialFacturas.size() + ") ===\n\n");
        for (int i = 0; i < historialFacturas.size(); i++) {
            sb.append("PEDIDO #").append(i + 1).append("\n");
            sb.append(historialFacturas.get(i)).append("\n");
        }
        return sb.toString();
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
}