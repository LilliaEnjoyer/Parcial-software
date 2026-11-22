public class PruebasPatrones {
    public static void main(String[] args) {
        // 1. Prueba Factory Method
        CreadorProducto fabricaTech = new CreadorTecnologia();
        CreadorProducto fabricaRopa = new CreadorRopa();

        Producto tech = fabricaTech.crearProducto("T1", "Monitor", 300.0, 2, "12");
        Producto ropa = fabricaRopa.crearProducto("R1", "Camiseta", 25.0, 5, "M");

        if (!(tech instanceof ProductoTecnologia) || !(ropa instanceof ProductoRopa)) {
            throw new RuntimeException("Fallo en Factory Method");
        }

        // 2. Prueba Decorator
        Vendible item = new EnvioExpressDecorator(new EmpaqueRegaloDecorator(new GarantiaExtendidaDecorator(tech)));
        if (Math.abs(item.getPrecio() - 368.50) > 0.01) {
            throw new RuntimeException("Fallo en Decorator");
        }

        // 3. Prueba Strategy
        EstrategiaDescuento desc = new DescuentoTemporada();
        if (Math.abs(desc.aplicarDescuento(200.0) - 170.0) > 0.01) {
            throw new RuntimeException("Fallo en Strategy");
        }

        // 4. Prueba Observer
        Producto agotado = fabricaTech.crearProducto("T2", "Teclado", 100.0, 0, "12");
        ClienteObservador cli = new ClienteObservador("Jhosef", "jhosef@correo.com");
        agotado.suscribir(cli);
        agotado.reponerStock(3);
        if (cli.getNotificacionesRecibidas().size() != 1) {
            throw new RuntimeException("Fallo en Observer");
        }

        System.out.println("¡TODAS LAS PRUEBAS DE PATRONES PASARON EXITOSAMENTE!");
    }
}
