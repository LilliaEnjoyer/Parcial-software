public class ProductoFactory {
    public static Producto crearProducto(String categoria, String id, String nombre, double precio, int stock, String atributoExtra) {
        return switch (categoria.toUpperCase()) {
            case "TECNOLOGIA" -> new ProductoTecnologia(id, nombre, precio, stock, Integer.parseInt(atributoExtra));
            case "ROPA" -> new ProductoRopa(id, nombre, precio, stock, atributoExtra);
            case "ALIMENTO" -> new ProductoAlimento(id, nombre, precio, stock, atributoExtra);
            default -> throw new IllegalArgumentException("Categoría no soportada: " + categoria);
        };
    }
}
