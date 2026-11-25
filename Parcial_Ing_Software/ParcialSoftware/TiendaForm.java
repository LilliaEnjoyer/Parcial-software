import javax.swing.*;
import java.awt.*;

public class TiendaForm extends javax.swing.JFrame {

    private final TiendaService tienda;

    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnModificarCant;
    private javax.swing.JButton btnEliminarItem;
    private javax.swing.JButton btnVaciarCarrito;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnReponer;
    private javax.swing.JButton btnSuscribir;
    private javax.swing.JComboBox<String> cbDescuento;
    private javax.swing.JComboBox<String> cbPago;
    private javax.swing.JComboBox<String> cbProductos;
    private javax.swing.JSpinner spCantidad;
    private javax.swing.JCheckBox chkExpress;
    private javax.swing.JCheckBox chkGarantia;
    private javax.swing.JCheckBox chkRegalo;
    private javax.swing.JLabel lblDescuento;
    private javax.swing.JLabel lblExtras;
    private javax.swing.JLabel lblPago;
    private javax.swing.JLabel lblProducto;
    private javax.swing.JLabel lblCant;
    private javax.swing.JScrollPane scrollFactura;
    private javax.swing.JTextArea txtAreaSalida;

    public TiendaForm() {
        this.tienda = new TiendaService();
        initComponents();
        cargarProductosEnCombo();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        lblProducto = new javax.swing.JLabel("Producto:");
        cbProductos = new javax.swing.JComboBox<>();
        lblCant = new javax.swing.JLabel("Cant:");
        spCantidad = new javax.swing.JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        btnAgregar = new javax.swing.JButton("Agregar");
        btnSuscribir = new javax.swing.JButton("Avisar Stock");
        btnReponer = new javax.swing.JButton("Reponer +5");

        lblExtras = new javax.swing.JLabel("Extras:");
        chkGarantia = new javax.swing.JCheckBox("Garantía (+$45)");
        chkRegalo = new javax.swing.JCheckBox("Regalo (+$8.50)");
        chkExpress = new javax.swing.JCheckBox("Express (+$15)");
        btnModificarCant = new javax.swing.JButton("Modificar Cantidad");
        btnEliminarItem = new javax.swing.JButton("Eliminar Ítem");
        btnVaciarCarrito = new javax.swing.JButton("Vaciar Carrito");

        lblDescuento = new javax.swing.JLabel("Descuento:");
        cbDescuento = new javax.swing.JComboBox<>(new String[]{
            "Sin descuento", "Temporada (-15%)", "Cliente Frecuente (-10%)", "Cupón (-$20)", "Por Cantidad (-12% >=3 uds)"
        });

        lblPago = new javax.swing.JLabel("Pago:");
        cbPago = new javax.swing.JComboBox<>(new String[]{
            "Tarjeta de Crédito", "Transferencia Bancaria", "Contra Entrega"
        });

        btnPagar = new javax.swing.JButton("Finalizar Compra");
        btnHistorial = new javax.swing.JButton("Ver Historial");
        txtAreaSalida = new javax.swing.JTextArea(18, 72);
        scrollFactura = new javax.swing.JScrollPane(txtAreaSalida);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tienda en Línea - Formulario");
        setLayout(new BorderLayout(10, 10));

        JPanel panelControles = new JPanel(new GridLayout(3, 1, 5, 5));
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila1.add(lblProducto);
        fila1.add(cbProductos);
        fila1.add(lblCant);
        fila1.add(spCantidad);
        fila1.add(btnAgregar);
        fila1.add(btnSuscribir);
        fila1.add(btnReponer);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila2.add(lblExtras);
        fila2.add(chkGarantia);
        fila2.add(chkRegalo);
        fila2.add(chkExpress);
        fila2.add(btnModificarCant);
        fila2.add(btnEliminarItem);
        fila2.add(btnVaciarCarrito);

        JPanel fila3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila3.add(lblDescuento);
        fila3.add(cbDescuento);
        fila3.add(lblPago);
        fila3.add(cbPago);
        fila3.add(btnPagar);
        fila3.add(btnHistorial);

        panelControles.add(fila1);
        panelControles.add(fila2);
        panelControles.add(fila3);

        txtAreaSalida.setEditable(false);
        txtAreaSalida.setFont(new Font("Monospaced", Font.PLAIN, 12));

        add(panelControles, BorderLayout.NORTH);
        add(scrollFactura, BorderLayout.CENTER);

        btnAgregar.addActionListener(evt -> btnAgregarActionPerformed());
        btnModificarCant.addActionListener(evt -> btnModificarCantActionPerformed());
        btnEliminarItem.addActionListener(evt -> btnEliminarItemActionPerformed());
        btnVaciarCarrito.addActionListener(evt -> btnVaciarCarritoActionPerformed());
        btnSuscribir.addActionListener(evt -> btnSuscribirActionPerformed());
        btnReponer.addActionListener(evt -> btnReponerActionPerformed());
        btnPagar.addActionListener(evt -> btnPagarActionPerformed());
        btnHistorial.addActionListener(evt -> txtAreaSalida.setText(tienda.consultarHistorialPedidos()));

        pack();
    }

    private void btnAgregarActionPerformed() {
        String seleccionado = (String) cbProductos.getSelectedItem();
        if (seleccionado == null) return;
        String id = seleccionado.split(" - ")[0];
        int cantidad = (Integer) spCantidad.getValue();

        boolean agregado = tienda.agregarAlCarrito(
                id,
                cantidad,
                chkGarantia.isSelected(),
                chkRegalo.isSelected(),
                chkExpress.isSelected()
        );

        if (agregado) {
            cargarProductosEnCombo();
            actualizarVistaCarrito();
        } else {
            JOptionPane.showMessageDialog(this, "Stock insuficiente para la cantidad solicitada.");
        }
    }

    private void btnModificarCantActionPerformed() {
        if (tienda.getCarrito().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.");
            return;
        }

        String[] opciones = new String[tienda.getCarrito().size()];
        for (int i = 0; i < tienda.getCarrito().size(); i++) {
            ItemCarrito ic = tienda.getCarrito().get(i);
            opciones[i] = (i + 1) + ". " + ic.getDescripcionCompleta();
        }

        String elegido = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el producto cuya cantidad desea cambiar:",
                "Modificar Cantidad",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (elegido != null) {
            int indice = Integer.parseInt(elegido.split("\\.")[0]) - 1;
            String inputCant = JOptionPane.showInputDialog(this, "Ingrese la nueva cantidad (0 para eliminar):");
            if (inputCant != null && !inputCant.isBlank()) {
                try {
                    int nuevaCant = Integer.parseInt(inputCant);
                    if (tienda.modificarCantidadCarrito(indice, nuevaCant)) {
                        cargarProductosEnCombo();
                        actualizarVistaCarrito();
                    } else {
                        JOptionPane.showMessageDialog(this, "No hay suficiente stock para aumentar a esa cantidad.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida.");
                }
            }
        }
    }

    private void btnEliminarItemActionPerformed() {
        if (tienda.getCarrito().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito ya está vacío.");
            return;
        }

        String[] opciones = new String[tienda.getCarrito().size()];
        for (int i = 0; i < tienda.getCarrito().size(); i++) {
            ItemCarrito ic = tienda.getCarrito().get(i);
            opciones[i] = (i + 1) + ". " + ic.getDescripcionCompleta() + " ($" + ic.getSubtotalItem() + ")";
        }

        String elegido = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el producto que desea quitar:",
                "Eliminar del Carrito",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (elegido != null) {
            int indice = Integer.parseInt(elegido.split("\\.")[0]) - 1;
            if (tienda.eliminarDelCarrito(indice)) {
                cargarProductosEnCombo();
                actualizarVistaCarrito();
            }
        }
    }

    private void btnVaciarCarritoActionPerformed() {
        if (tienda.getCarrito().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito ya está vacío.");
            return;
        }
        tienda.vaciarCarrito();
        cargarProductosEnCombo();
        actualizarVistaCarrito();
        JOptionPane.showMessageDialog(this, "Carrito vaciado y stock restaurado.");
    }

    private void btnSuscribirActionPerformed() {
        String seleccionado = (String) cbProductos.getSelectedItem();
        if (seleccionado == null) return;
        String id = seleccionado.split(" - ")[0];

        String nombre = JOptionPane.showInputDialog(this, "Ingresa tu nombre para la notificación:");
        if (nombre != null && !nombre.isBlank()) {
            tienda.suscribirAProducto(id, new ClienteObservador(nombre, nombre.toLowerCase() + "@correo.com") {
                @Override
                public void actualizar(String nombreProducto, int nuevoStock) {
                    super.actualizar(nombreProducto, nuevoStock);
                    JOptionPane.showMessageDialog(TiendaForm.this,
                            "¡Aviso para " + nombre + "!\nEl producto '" + nombreProducto + "' ya tiene stock (" + nuevoStock + ").");
                }
            });
            JOptionPane.showMessageDialog(this, "Suscripción registrada correctamente.");
        }
    }

    private void btnReponerActionPerformed() {
        String seleccionado = (String) cbProductos.getSelectedItem();
        if (seleccionado == null) return;
        String id = seleccionado.split(" - ")[0];
        tienda.reabastecerProducto(id, 5);
        cargarProductosEnCombo();
    }

    private void btnPagarActionPerformed() {
        tienda.limpiarDescuentos();
        switch (cbDescuento.getSelectedIndex()) {
            case 1 -> tienda.agregarDescuento(new DescuentoTemporada());
            case 2 -> tienda.agregarDescuento(new DescuentoClienteFrecuente());
            case 3 -> tienda.agregarDescuento(new DescuentoCupon(20.0));
            case 4 -> tienda.agregarDescuento(new DescuentoCantidad(tienda.getTotalUnidadesCarrito()));
        }

        EstrategiaPago pago = switch (cbPago.getSelectedIndex()) {
            case 0 -> new PagoTarjeta("4532999988881234");
            case 1 -> new PagoTransferencia("Bancolombia");
            default -> new PagoContraEntrega("Dirección registrada");
        };

        String factura = tienda.cerrarPedido(pago);
        txtAreaSalida.setText(factura);
    }

    private void cargarProductosEnCombo() {
        cbProductos.removeAllItems();
        for (Producto p : tienda.getCatalogo()) {
            cbProductos.addItem(p.getId() + " - " + p.getNombre() + " ($" + p.getPrecio() + ") [Stock: " + p.getStock() + "]");
        }
    }

    private void actualizarVistaCarrito() {
        if (tienda.getCarrito().isEmpty()) {
            txtAreaSalida.setText("=== CARRITO DE COMPRAS VACÍO ===");
            return;
        }
        StringBuilder sb = new StringBuilder("=== CARRITO DE COMPRAS ===\n");
        for (int i = 0; i < tienda.getCarrito().size(); i++) {
            ItemCarrito ic = tienda.getCarrito().get(i);
            sb.append(String.format("%d. %s -> $%.2f\n", i + 1, ic.getDescripcionCompleta(), ic.getSubtotalItem()));
        }
        sb.append(String.format("\nTotal unidades: %d | Subtotal actual: $%.2f",
                tienda.getTotalUnidadesCarrito(), tienda.calcularSubtotal()));
        txtAreaSalida.setText(sb.toString());
    }
}