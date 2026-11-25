import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("       SISTEMA DE TIENDA EN LÍNEA        ");
        System.out.println("=========================================");
        System.out.println("1. Iniciar Interfaz Gráfica (Formulario)");
        System.out.println("2. Iniciar Interfaz por Consola");
        System.out.print("Seleccione el modo de ejecución: ");

        String modo = sc.nextLine();

        if (modo.equals("1")) {
            SwingUtilities.invokeLater(() -> {
                TiendaForm form = new TiendaForm();
                form.setVisible(true);
            });
        } else if (modo.equals("2")) {
            ejecutarConsola(sc);
        } else {
            System.out.println("Opción no válida.");
        }
    }

    private static void ejecutarConsola(Scanner sc) {
        TiendaService tienda = new TiendaService();
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== TIENDA EN LÍNEA (CONSOLA) ===");
            System.out.println("1. Ver catálogo");
            System.out.println("2. Agregar producto al carrito (con cantidad y extras)");
            System.out.println("3. Ver carrito");
            System.out.println("4. Modificar cantidad de un producto en el carrito");
            System.out.println("5. Eliminar producto del carrito / Vaciar carrito");
            System.out.println("6. Aplicar descuento");
            System.out.println("7. Cerrar pedido y pagar");
            System.out.println("8. Consultar historial de pedidos");
            System.out.println("9. Suscribirse a producto agotado (Observer)");
            System.out.println("10. Reponer stock de producto (Admin)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = sc.nextLine();
            switch (opcion) {
                case "1" -> {
                    System.out.println("\n--- CATÁLOGO ---");
                    for (Producto p : tienda.getCatalogo()) {
                        System.out.printf("%s | %s | $%.2f | Stock: %d\n",
                                p.getId(), p.getDescripcion(), p.getPrecio(), p.getStock());
                    }
                }
                case "2" -> {
                    System.out.print("Ingrese ID del producto (ej. P01): ");
                    String id = sc.nextLine();
                    System.out.print("Cantidad a agregar: ");
                    int cant = Integer.parseInt(sc.nextLine());
                    System.out.print("¿Garantía extendida (+$45)? (s/n): ");
                    boolean g = sc.nextLine().equalsIgnoreCase("s");
                    System.out.print("¿Empaque regalo (+$8.50)? (s/n): ");
                    boolean r = sc.nextLine().equalsIgnoreCase("s");
                    System.out.print("¿Envío express (+$15)? (s/n): ");
                    boolean e = sc.nextLine().equalsIgnoreCase("s");

                    if (tienda.agregarAlCarrito(id, cant, g, r, e)) {
                        System.out.println("Producto agregado al carrito.");
                    } else {
                        System.out.println("No disponible o stock insuficiente.");
                    }
                }
                case "3" -> {
                    System.out.println("\n--- CARRITO ACTUAL ---");
                    for (int i = 0; i < tienda.getCarrito().size(); i++) {
                        ItemCarrito ic = tienda.getCarrito().get(i);
                        System.out.printf("%d. %s ($%.2f)\n", i + 1, ic.getDescripcionCompleta(), ic.getSubtotalItem());
                    }
                    System.out.printf("Unidades: %d | Subtotal: $%.2f | Total con descuentos: $%.2f\n",
                            tienda.getTotalUnidadesCarrito(), tienda.calcularSubtotal(), tienda.calcularTotalConDescuentos());
                }
                case "4" -> {
                    if (tienda.getCarrito().isEmpty()) {
                        System.out.println("El carrito está vacío.");
                        break;
                    }
                    for (int i = 0; i < tienda.getCarrito().size(); i++) {
                        ItemCarrito ic = tienda.getCarrito().get(i);
                        System.out.printf("%d. %s\n", i + 1, ic.getDescripcionCompleta());
                    }
                    System.out.print("Número del ítem a modificar: ");
                    int idx = Integer.parseInt(sc.nextLine()) - 1;
                    System.out.print("Nueva cantidad (0 para eliminar): ");
                    int nuevaCant = Integer.parseInt(sc.nextLine());
                    if (tienda.modificarCantidadCarrito(idx, nuevaCant)) {
                        System.out.println("Cantidad actualizada.");
                    } else {
                        System.out.println("No se pudo actualizar (índice inválido o stock insuficiente).");
                    }
                }
                case "5" -> {
                    if (tienda.getCarrito().isEmpty()) {
                        System.out.println("El carrito está vacío.");
                        break;
                    }
                    for (int i = 0; i < tienda.getCarrito().size(); i++) {
                        ItemCarrito ic = tienda.getCarrito().get(i);
                        System.out.printf("%d. %s ($%.2f)\n", i + 1, ic.getDescripcionCompleta(), ic.getSubtotalItem());
                    }
                    System.out.print("Ingrese el número del producto a eliminar (o 0 para vaciar todo): ");
                    int num = Integer.parseInt(sc.nextLine());
                    if (num == 0) {
                        tienda.vaciarCarrito();
                        System.out.println("Carrito vaciado.");
                    } else if (tienda.eliminarDelCarrito(num - 1)) {
                        System.out.println("Producto eliminado del carrito.");
                    } else {
                        System.out.println("Número inválido.");
                    }
                }
                case "6" -> {
                    System.out.println("1. Temporada (15%) | 2. Cliente Frecuente (10%) | 3. Cupón ($20) | 4. Por Cantidad (-12% >=3 uds)");
                    String desc = sc.nextLine();
                    switch (desc) {
                        case "1" -> tienda.agregarDescuento(new DescuentoTemporada());
                        case "2" -> tienda.agregarDescuento(new DescuentoClienteFrecuente());
                        case "3" -> tienda.agregarDescuento(new DescuentoCupon(20.0));
                        case "4" -> tienda.agregarDescuento(new DescuentoCantidad(tienda.getTotalUnidadesCarrito()));
                    }
                    System.out.println("Descuento aplicado.");
                }
                case "7" -> {
                    System.out.println("Seleccione pago: 1. Tarjeta | 2. Transferencia | 3. Contra Entrega");
                    String mp = sc.nextLine();
                    EstrategiaPago pago = switch (mp) {
                        case "1" -> new PagoTarjeta("4532123456789876");
                        case "2" -> new PagoTransferencia("Bancolombia");
                        default -> new PagoContraEntrega("Calle 100 # 15-20, Bogotá");
                    };
                    System.out.println("\n" + tienda.cerrarPedido(pago));
                }
                case "8" -> System.out.println("\n" + tienda.consultarHistorialPedidos());
                case "9" -> {
                    System.out.print("ID del producto agotado (ej. P02): ");
                    String idObs = sc.nextLine();
                    System.out.print("Su nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Su correo: ");
                    String correo = sc.nextLine();
                    tienda.suscribirAProducto(idObs, new ClienteObservador(nombre, correo));
                    System.out.println("Suscripción registrada.");
                }
                case "10" -> {
                    System.out.print("ID del producto a reponer: ");
                    String idRep = sc.nextLine();
                    System.out.print("Cantidad a ingresar: ");
                    int cant = Integer.parseInt(sc.nextLine());
                    tienda.reabastecerProducto(idRep, cant);
                }
                case "0" -> salir = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }
}