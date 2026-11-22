# Tienda en Línea - Proyecto Patrones de Diseño (Java)

Aplicación de comercio electrónico desarrollada en Java con un núcleo de negocio (`TiendaService`) compartido tanto por una interfaz de consola como por un formulario gráfico (`TiendaForm`).

## Patrones de Diseño Implementados
1. **Factory Method (`CreadorProducto`)**: Subclases creadoras (`CreadorTecnologia`, `CreadorRopa`, `CreadorAlimento`) instancian cada categoría de producto sin acoplar el código cliente.
2. **Decorator (`ExtraDecorator`)**: Añade dinámicamente extras (`GarantiaExtendidaDecorator`, `EmpaqueRegaloDecorator`, `EnvioExpressDecorator`) modificando precio y descripción.
3. **Strategy (`EstrategiaDescuento` y `EstrategiaPago`)**: Desacopla las reglas de descuentos y los medios de pago intercambiables.
4. **Observer (`ObservadorStock`)**: Notifica automáticamente a los clientes suscritos cuando un producto agotado vuelve a tener stock.

## Cómo ejecutar
1. Abrir la carpeta `ParcialSoftware` en Visual Studio Code (asegurándose de que no haya archivos `pom.xml` previos).
2. Abrir `Main.java` y presionar **Run Java**.
3. Elegir `1` para abrir el Formulario Gráfico o `2` para usar la Consola.
4. Para verificar las pruebas de los patrones, abrir `PruebasPatrones.java` y presionar **Run Java**.
