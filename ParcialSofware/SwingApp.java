import javax.swing.*;
import java.awt.*;

public class SwingApp extends JFrame {
    private final TiendaService tienda = new TiendaService();
    private final JComboBox<String> cbProductos = new JComboBox<>();
    private final JCheckBox chkGarantia = new JCheckBox("Garantía Ext. (+$45)");
    private final JCheckBox chkRegalo = new JCheckBox("Regalo (+$8.50)");
    private final JCheckBox chkExpress = new JCheckBox("Express (+$15)");
    private final JComboBox<String> cbDescuento = new JComboBox<>(new String[]{
            "Sin descuento", "Temporada (-15%)", "Cliente Frecuente (-10%)", "Cupón (-$20)"
    });
    private final JComboBox<String> cbPago = new JComboBox<>(new String[]{
            "Tarjeta de Crédito", "Transferencia Bancaria", "Contra Entrega"
    });
    private final JTextArea areaSalida = new JTextArea();

    public SwingApp() {
        setTitle("Tienda en Línea - Patrones de Diseño");
        setSize(720, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelTop = new JPanel(new GridLayout(3, 1, 5, 5));
        panelTop.setBorder(BorderFactory.createTitledBorder("Catálogo y Extras (Factory + Decorator)"));

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actualizarComboProductos();
        JButton btnAgregar = new JButton("Agregar al Carrito");
        JButton btnSuscribir = new JButton("Avisarme si hay stock (Observer)");
        JButton btnReponer = new JButton("Reponer +5 Stock");
        fila1.add(new JLabel("Producto:"));
        fila1.add(cbProductos);
        fila1.add(btnAgregar);
        fila1.add(btnSuscribir);
        fila1.add(btnReponer);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila2.add(new JLabel("Extras:"));
        fila2.add(chkGarantia);
        fila2.add(chkRegalo);
        fila2.add(chkExpress);

        JPanel fila3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnPagar = new JButton("Finalizar Compra y Facturar (Strategy)");
        fila3.add(new JLabel("Descuento:"));
        fila3.add(cbDescuento);
        fila3.add(new JLabel("Pago:"));
        fila3.add(cbPago);
        fila3.add(btnPagar);

        panelTop.add(fila1);
        panelTop.add(fila2);
        panelTop.add(fila3);
        add(panelTop, BorderLayout.NORTH);

        areaSalida.setEditable(false);
        areaSalida.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(areaSalida), BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> {
            String seleccionado = (String) cbProductos.getSelectedItem();
            if (seleccionado == null) return;
            String id = seleccionado.split(" - ")[0];
            boolean ok = tienda.agregarAlCarrito(id, chkGarantia.isSelected(), chkRegalo.isSelected(), chkExpress.isSelected());
            if (ok) {
                actualizarComboProductos();
                mostrarCarrito();
            } else {
                JOptionPane.showMessageDialog(this, "Producto agotado. Puedes suscribirte para recibir alerta.");
            }
        });

        btnSuscribir.addActionListener(e -> {
            String seleccionado = (String) cbProductos.getSelectedItem();
            if (seleccionado == null) return;
            String id = seleccionado.split(" - ")[0];
            String nombre = JOptionPane.showInputDialog(this, "Ingrese su nombre para la alerta:");
            if (nombre != null && !nombre.isBlank()) {
                tienda.suscribirAProducto(id, new ClienteObservador(nombre, nombre.toLowerCase() + "@correo.com") {
                    @Override
                    public void actualizar(String nombreProducto, int nuevoStock) {
                        super.actualizar(nombreProducto, nuevoStock);
                        JOptionPane.showMessageDialog(SwingApp.this,
                                "¡Notificación Observer para " + nombre + "!\n" + nombreProducto + " ya tiene stock (" + nuevoStock + ").");
                    }
                });
                JOptionPane.showMessageDialog(this, "Suscrito correctamente al producto " + id);
            }
        });

        btnReponer.addActionListener(e -> {
            String seleccionado = (String) cbProductos.getSelectedItem();
            if (seleccionado == null) return;
            String id = seleccionado.split(" - ")[0];
            tienda.reabastecerProducto(id, 5);
            actualizarComboProductos();
        });

        btnPagar.addActionListener(e -> {
            tienda.limpiarDescuentos();
            switch (cbDescuento.getSelectedIndex()) {
                case 1 -> tienda.agregarDescuento(new DescuentoTemporada());
                case 2 -> tienda.agregarDescuento(new DescuentoClienteFrecuente());
                case 3 -> tienda.agregarDescuento(new DescuentoCupon(20.0));
            }

            EstrategiaPago pago = switch (cbPago.getSelectedIndex()) {
                case 0 -> new PagoTarjeta("4532999988881234");
                case 1 -> new PagoTransferencia("Nequi / Bancolombia");
                default -> new PagoContraEntrega("Dirección registrada del cliente");
            };

            String factura = tienda.cerrarPedido(pago);
            areaSalida.setText(factura);
        });
    }

    private void actualizarComboProductos() {
        cbProductos.removeAllItems();
        for (Producto p : tienda.getCatalogo()) {
            cbProductos.addItem(p.getId() + " - " + p.getNombre() + " ($" + p.getPrecio() + ") [Stock: " + p.getStock() + "]");
        }
    }

    private void mostrarCarrito() {
        StringBuilder sb = new StringBuilder("=== CARRITO DE COMPRAS ===\n");
        for (Vendible v : tienda.getCarrito()) {
            sb.append(String.format("• %s -> $%.2f\n", v.getDescripcion(), v.getPrecio()));
        }
        sb.append(String.format("\nSubtotal actual: $%.2f", tienda.calcularSubtotal()));
        areaSalida.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingApp().setVisible(true));
    }
}