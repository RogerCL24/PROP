package main.domain.controllers;

import main.domain.classes.*;
import main.domain.classes.algorism.AlgoritmoVoraz;
import main.domain.classes.algorism.Aproximation_Kruskal_ILS;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Controlador que gestiona las operaciones relacionadas con las distribuciones de productos.
 * Proporciona métodos para agregar, eliminar, editar, y exportar distribuciones,
 * así como para aplicar algoritmos de distribución.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */

public class distributionController {

    private static distributionController instance;
    private CjtDistribuciones cjtD;

    /**
     * Constructor del controlador de distribuciones.
     * Es privado para implementar el patrón Singleton.
     */
    private distributionController() {}

    /**
     * Obtiene la instancia única del controlador de distribuciones.
     *
     * @return Instancia única de la clase distributionController.
     */
    public static distributionController getInstance() {
        if (instance == null) {
            instance = new distributionController();
        }
        return instance;
    }

    /**
     * Asigna un conjunto de distribuciones al controlador.
     *
     * @param cjtD Conjunto de distribuciones a asociar.
     */
    public void setCjtD(CjtDistribuciones cjtD) {
        this.cjtD = cjtD;
    }

    /**
     * Obtiene una distribución por su nombre.
     * @param nombre El nombre de la distribución.
     * @return La distribución correspondiente al nombre dado.
     */
    public Distribucion getDistribucion(String nombre) {
        return cjtD.obtenerDistribucion(nombre);
    }


    /**
     * Cambia el nombre de una distribución, siempre que el nuevo nombre no exista.
     * Si el nuevo nombre ya existe, no realiza el cambio.
     * @param nombreDist El nombre actual de la distribución.
     * @param scanner El scanner para leer la entrada del usuario.
     * @return true si el cambio de nombre fue exitoso; false en caso contrario.
     */
    public boolean setNomDistribucion(String nombreDist, Scanner scanner) {
        String nouNom = scanner.nextLine();
        if (!cjtD.contiene(nouNom)) {
            Distribucion d = getDistribucion(nombreDist);
            d.setNom(nouNom);
            guardarDistribucion(d, nouNom);
            cjtD.eliminarDistribucionDelConjunto(nombreDist);
            return true;
        }
        else {
            System.out.println("Ya existe una distribucion con ese nombre.");
            return false;
        }
    }

    /**
     * Verifica si una distribución existe en el conjunto.
     * @param nombre El nombre de la distribución a verificar.
     * @return true si la distribución existe, false en caso contrario.
     */
    public boolean exists (String nombre) {
        return cjtD.contiene(nombre);
    }

    /**
     * Guarda una nueva distribución en el conjunto.
     * Si ya existe una distribución con el mismo nombre, no la guarda.
     * @param dist La distribución a guardar.
     * @param name El nombre de la distribución.
     * @return true si la distribución se guardó correctamente, false si ya existe.
     */
    public boolean guardarDistribucion(Distribucion dist, String name) {
        if (cjtD.contiene(name)) {
            System.out.println("Ya existe una distribucion con ese nombre.");
            return false;
        } else if (name.isEmpty()) {
            System.out.println("El nombre no puede estar vacio");
            return false;
        } else {
            dist.setNom(name);
            cjtD.agregarDistribucion(name, dist);
            System.out.println("Distribucion '" + name + "' agregada con exito.");
            return true;
        }
    }

    /**
     * Muestra todas las distribuciones guardadas en el conjunto.
     * @return true si hay distribuciones guardadas, false si el conjunto está vacío.
     */
    public boolean verDistribuciones() {
        if (cjtD.isEmpty()) {
            System.out.println("No hay distribuciones guardadas.");
            return false;
        } else {
            System.out.println("Distribuciones guardadas:");
            for (String nombreDistribucion : cjtD.getNames()) {
                System.out.println("- " + nombreDistribucion);
            }
            return true;
        }
    }

    /**
     * Elimina una distribución del conjunto.
     *
     * @param nombre El nombre de la distribución a eliminar.
     * @param scanner Objeto Scanner para leer la confirmación del usuario.
     * @return true si era la última distribución y se eliminó, false en caso contrario.
     */
    public boolean eliminarDistribucion (String nombre, Scanner scanner) {
        boolean keep_in = true;
        boolean lastOne = false;
        String opt = "";
        while (keep_in) {
            System.out.println("Esta seguro que quiere eliminar la distribucion '" + nombre + "' ?");
            if (cjtD.getSize() == 1) {
                System.out.println("\n******************************************************");
                System.out.println("Le recordamos que '" + nombre + "' es su ultima distribucion");
                System.out.println("******************************************************\n");
                lastOne = true;
            }
            System.out.println("1. Si");
            System.out.println("2. No");
            opt = scanner.nextLine().trim();
            if (opt.equals("1") || opt.equals("2")) keep_in = false;
            else System.out.println("Opcion no valida");
        }

        switch (opt) {
            case "1":
                cjtD.eliminarDistribucionDelConjunto(nombre);
                System.out.println("Distribucion '" + nombre + "' eliminada con exito.");
                return lastOne;
            case "2":
                return false;
        }
        return false;
    }

    /**
     * Crea una nueva distribución con las dimensiones dadas.
     * @param h Altura de la distribución.
     * @param w Ancho de la distribución.
     * @return La nueva distribución creada.
     */
    public Distribucion createDistribution(int h, int w) {
        return new Distribucion(h,w);
    }


    /**
     * Permite editar la distribución cambiando la relación entre dos productos.
     * @param nombreDist El nombre de la distribución a editar.
     */
    public void editarDistribucion(String nombreDist, Scanner scanner) {
        boolean keep_in = true;
        while (keep_in) {
            System.out.println("Escriba un producto, presione ENTER, luego escriba otro producto y presione ENTER. ");
            String prod1 = scanner.nextLine();
            String prod2 = scanner.nextLine();
            Distribucion d = getDistribucion(nombreDist);
            if (!d.isEmpty() && d.contieneProducto(prod1) && d.contieneProducto(prod2)) {
                keep_in = false;
                d.editarDist(prod1,prod2);
                cjtD.agregarDistribucion(nombreDist, d);
                System.out.println("Distribucion editada correctamente.");
                imprimirDistribucion(d);
            }
            else {
                System.out.println("Algun producto no existe o no ha introducido los productos en el formato correcto.");
            }
        }
    }

    /**
     * Elimina un producto de una distribución
     * @param scanner Scanner para leer entrada de texto.
     * @param nombreDist El nombre de la distribución de la que se eliminará el producto.
     */
    public boolean eliminarProducteDistribucion(Scanner scanner, String nombreDist) {;
        boolean change = false;
        String input = "";

        Set<String> productosDist = new HashSet<>(cjtD.obtenerDistribucion(nombreDist).getDist());
        boolean keep_in = true;

        while (keep_in) {
            System.out.println("Introduce los productos en una linea separados por espacios, y usa ENTER para indicar el final:");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) System.out.println("El producto debe contener almenos 1 caracter/simbolo, no puede estar vacio.");
            else keep_in = false;
        }

        String[] productos = input.split("\\s+");
        Set<String> productosAEliminar = new HashSet<>(Arrays.asList(productos));

        if (productosAEliminar.containsAll(productosDist)) {
            System.out.println("CUIDADO! No puedes eliminar todos los productos de la distribucion '" + nombreDist + "'. La distribucion no puede quedar vacia.");
            keep_in = true;
            while (keep_in) {
                System.out.println("Le gustaria eliminar la distribucion por completo?");
                System.out.println("1. Si");
                System.out.println("2. No");
                input = scanner.nextLine().trim();
                if (input.equals("1") || input.equals("2")) keep_in = false;
                else System.out.println("Opcion invalida.");
            }
            if (input.equals("1")) eliminarDistribucion(nombreDist, scanner);
        }
        else {
            Distribucion d = getDistribucion(nombreDist);
            for (String prod : productos) {
                if (!d.isEmpty() && d.contieneProducto(prod)) {
                    change = true;
                    d.eliminarProducte(prod);
                    cjtD.agregarDistribucion(nombreDist, d);
                    System.out.println("Producto " +  prod + " eliminado correctamente.");
                }
                else {
                    System.out.println(prod + " no existe en esta distribucion.");
                }
            }
        }
        return change;
    }


    /**
     * Exporta una distribución a un archivo de texto.
     * El archivo se guarda en una carpeta de salida configurada.
     * @param nombre El nombre de la distribución a exportar.
     * @param username El nombre de usuario asociado con la exportación.
     */
    public void exportarDistribucion(String nombre, String username) {
        if (!cjtD.isEmpty() && cjtD.contiene(nombre)) {
            Distribucion dist = cjtD.obtenerDistribucion(nombre);
            String rutaBase = System.getProperty("user.dir");
            String rutaFichero;

            if (rutaBase.endsWith("/src")) {
                rutaBase = rutaBase.replace("/src", "");
            }

            String nameInPath = nombre.concat('(' + username + ')' );
            rutaFichero = rutaBase + "/src/test/outputs/" + nameInPath + ".txt";

            try (FileWriter writer = new FileWriter(rutaFichero)) {
                writer.write("Distribucion generada con el algoritmo " + dist.getAlgoritme() + "\n");
                writer.write(formatoDistribucion(nombre));

                System.out.println("Distribucion exportada con éxito a " + rutaFichero + ".");
            } catch (IOException e) {
                System.out.println("Error al exportar la distribucion: " + e.getMessage());
            }
        } else {
            System.out.println("No existe una distribucion con ese nombre.");
        }
    }

    /**
     * Genera una cadena con el formato adecuado para exportar la distribución a un archivo de texto.
     * @param nombre El nombre de la distribución a formatear.
     * @return Una cadena con el formato de la distribución.
     */
    public String formatoDistribucion(String nombre) {
        StringBuilder sb = new StringBuilder();
        Distribucion dist = cjtD.obtenerDistribucion(nombre);
        List<String> Dist = dist.getDist();
        int numFilas = dist.getPrestage().getFirst();
        int numColumnas = dist.getPrestage().getSecond();
        int numProductos = Dist.size();

        boolean turn = false;
        int productoIndex = 0;

        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                if (!turn && productoIndex < numProductos && Dist.get(productoIndex) != null) {
                    sb.append(Dist.get(productoIndex)).append(" ");
                    productoIndex++;
                } else if (turn && productoIndex < numProductos) {
                    int reverseIndex = i * numColumnas + (numColumnas - j - 1);
                    if (reverseIndex < numProductos && Dist.get(reverseIndex) != null) {
                        sb.append(Dist.get(reverseIndex)).append(" ");
                        productoIndex++;
                    } else {
                        sb.append("/*/ ");
                    }
                } else {
                    sb.append("/*/ ");
                }
                
            }
            sb.append("\n");
            turn = !turn;
        }

        return sb.toString();
    }
    /**
     *
     * Procesa la lectura de una dimensión para la estantería.
     * Valida que la dimensión ingresada sea un número entero positivo.
     * @param mensaje El mensaje que se mostrará al usuario al solicitar la entrada.
     * @return La dimensión ingresada por el usuario (un número entero positivo).
     */

    public static int leerDimension(String mensaje, Scanner scanner) {
        int dimension = -1;
        while (dimension < 0) {
            System.out.print(mensaje);

            // Verifica si el próximo token es un entero
            if (scanner.hasNextInt()) {
                dimension = Integer.parseInt(scanner.nextLine());

                // Verifica que la dimensión sea positiva
                if (dimension <= 0) {
                    System.out.println("Error: La dimension debe ser un numero entero positivo.");
                    dimension = -1;
                }
            } else {
                // Elimina el token erróneo y muestra un mensaje de error
                System.out.println("Error: Ingrese un numero entero valido.");
                scanner.next();
            }
        }
        return dimension;
    }


    /**
     * Gestiona las distribuciones existentes, permitiendo al usuario consultar, modificar o eliminar distribuciones.
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     */
    public void gestionarDistribuciones(Scanner scanner) {
        boolean salir = false;
        boolean first = true;
        while (!salir) {
            if (!first) {
                verDistribuciones();
            }
            first = false;
            boolean keep_in = true;
            String opcion = "";
            while (keep_in) {
                System.out.println("\n--------------------------------------------");
                System.out.println("¿Que desea hacer?");
                System.out.println("1. Consultar una distribución.");
                System.out.println("2. Modificar una distribución.");
                System.out.println("3. Eliminar una distribución.");
                System.out.println("0. Volver al menu principal");
                opcion = scanner.nextLine();
                if (opcion.equals("1") || opcion.equals("2") || opcion.equals("0") || opcion.equals("3")) keep_in = false;
                else System.out.println("Opcion no valida.");
            }

            String name = "";
            keep_in = true;
            if (!opcion.equals("0")) {
                while (keep_in) {
                    System.out.println("Ingrese el nombre de la distribucion:");
                    name = scanner.nextLine();
                    if (exists(name)) keep_in = false;
                    else System.out.println("No se encontro ninguna distribucion con el nombre '" + name + "'.");
                }
            }
            keep_in = true;
            switch (opcion) {
                case "1":
                    imprimirDistribucion(getDistribucion(name));

                    break;
                case "2":

                    System.out.println("¿Que desea modificar de la distribucion?");
                    System.out.println("1. Mover productos.");
                    System.out.println("2. Eliminar productos.");
                    System.out.println("3. Cambiar el nombre de la distribucion.");
                    System.out.println("0. Volver.");

                    String opcion1 = scanner.nextLine();

                    switch (opcion1) {
                        case "1":
                            imprimirDistribucion(getDistribucion(name));
                            editarDistribucion(name, scanner);
                            break;

                        case "2":
                            imprimirDistribucion(getDistribucion(name));
                            while (keep_in) {
                                if (eliminarProducteDistribucion(scanner, name)) {
                                    keep_in = false;
                                    imprimirDistribucion(getDistribucion(name));
                                }
                                else System.out.println("Vuelva a intentarlo");

                            }

                            break;

                        case "3":
                            while (keep_in) {
                                System.out.println("Escriba el nuevo nombre de la distribucion, luego presione ENTER.");
                                if (setNomDistribucion(name, scanner)) keep_in = false;
                            }

                            break;


                        case "0":
                            break;

                        default:
                            System.out.println("Opcion no valida.");
                            break;
                    }

                    break;

                case "3":
                    if (eliminarDistribucion(name, scanner)) salir = true;

                    break;

                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida.");
                    break;
            }
        }
    }

    /**
     * Gestiona la creación de una nueva distribución de productos según las listas de productos existentes.
     * Permite elegir entre ajustar automáticamente las dimensiones de la estantería o especificarlas manualmente.
     * Además, permite elegir el algoritmo de distribución (Voraz o Aproximación).
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     * @param LlistaGdS Mapa que contiene la lista de productos y sus características para la distribución.
     * @param userName Nombre del usuario que realiza la acción.
     */
    public void generarDistribucion(Scanner scanner, Map<String,Map<String,Float>> LlistaGdS, String userName) {
        int h,w;
        h = w = 0;

        String opt = "0";
        boolean keep_in = true;
        while (keep_in) {
            System.out.println("Desea escribir las dimensiones de la estanteria o usar ajuste automatico: ");
            System.out.println("1. Escribir dimensiones");
            System.out.println("2. Ajuste automatico");
            opt = scanner.nextLine();
            if (opt.equals("1") || opt.equals("2")) keep_in = false;
            else System.out.println("Opcion no valida.");
        }

        switch (opt) {
            case "1":
                System.out.println("Que dimensiones desea que tenga la estanteria?");
                keep_in = true;
                while (keep_in) {

                    h = leerDimension("Ingrese la altura de la estantería (numero de estantes): ", scanner);
                    w = leerDimension("Ingrese el ancho de la estantería (numero de productos por estante): ",scanner);

                    if (h * w < LlistaGdS.size()) {
                        System.out.println("ERROR: Estanteria demasiado pequeña para el tamaño de la lista que ha elegido\n " +
                                "vuelva a introducir nuevas dimensiones para la estanteria");
                        // O una nueva lista...
                    } else keep_in = false;
                }
                break;
            case "2":
                h = w = (int) Math.ceil(Math.sqrt(LlistaGdS.size()));
                break;
            default:
                System.out.println("Opcion no valida.");
                break;
        }

        Distribucion d = createDistribution(h, w);
        String opcion = "";
        keep_in = true;
        while (keep_in) {
            System.out.println("Por ultimo, que algoritmo desea ejecutar?");
            System.out.println("1. Voraz (Backtracking).");
            System.out.println("2. Aproximacion (Kruskal).");
            System.out.println("0. Cancelar.");
            opcion = scanner.nextLine().trim();
            if (opcion.equals("1") || opcion.equals("2") || opcion.equals("0")) keep_in = false;
            else System.out.println("Opcion no valida.");
        }

        boolean introducida = false;
        switch (opcion) {
            case "1":
                System.out.println("A continuacion se generara la distribucion con el algoritmo Voraz...");
                executeAlgorithm(d, opcion, LlistaGdS);
                introducida = true;
                break;
            case "2":
                System.out.println("A continuacion se generara la distribucion con el algoritmo de Aproximacion...");
                executeAlgorithm(d, opcion, LlistaGdS);
                introducida = true;
                break;
            case "0":
                break;
            default:
                System.out.println("Opcion no valida.");
                break;
        }
        if (introducida) distribucionIntroducida(d, userName, scanner);
    }


    /**
     * Muestra y gestiona las opciones después de que una distribución haya sido generada.
     * Permite guardar la distribución, exportarla a un archivo o descartarla.
     *
     * @param dist La distribución generada.
     * @param userName username del usuario que ha introducido la distribucion
     * @param scanner El scanner para leer la entrada del usuario.
     */
    public void distribucionIntroducida(Distribucion dist, String userName, Scanner scanner) {
        imprimirDistribucion(dist);
        boolean keep_in = true;
        String opcion = "";
        System.out.println("\nDistribucion generada.");
        System.out.println("\n-------------------------------------------------");
        while (keep_in) {
            System.out.println("Menu de opciones:");
            System.out.println("1. Guardar distribucion");
            System.out.println("2. Guardar distribucion y exportarla a un fichero .txt");
            System.out.println("0. Descartar y volver al menu principal");
            opcion = scanner.nextLine().trim();
            if (opcion.equals("1") || opcion.equals("2") || opcion.equals("0")) keep_in = false;
            else System.out.println("Opcion no valida.");
        }

        keep_in = true;
        switch (opcion){
            case "1":
                while (keep_in) {
                    System.out.println("\n-------------------------------------------------");
                    System.out.println("Introduzca un nombre para la nueva distribucion:");
                    String name = scanner.nextLine().trim();
                    if (guardarDistribucion(dist, name)) {
                        keep_in = false;
                    }
                }
                break;
            case "2":
                while (keep_in) {
                    System.out.println("\n-------------------------------------------------");
                    System.out.println("Introduzca un nombre para la nueva distribucion:");
                    String name2 = scanner.nextLine().trim();
                    if (guardarDistribucion(dist, name2)) {
                        exportarDistribucion(name2, userName);
                        keep_in = false;
                    }
                }

                break;
            case "0":
                System.out.println("Regresando al menú principal...");
                break;
            default:
                System.out.println("Opcion no valida.");
                break;

        }
    }

    /**
     * Imprime una distribución en la consola con el formato adecuado.
     * @param dist La distribución a imprimir.
     */
    public void imprimirDistribucion(Distribucion dist) {
        System.out.println("Algoritmo usado " + dist.getAlgoritme());
        List<String> Dist = dist.getDist();
        int numFilas = dist.getPrestage().getFirst();
        int numColumnas = dist.getPrestage().getSecond();
        int numProductos = Dist.size();
        boolean turn = false;
        int productoIndex = 0;

        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {

                if (!turn && productoIndex < numProductos && Dist.get(productoIndex) != null) {
                    // Imprimir de izquierda a derecha
                    System.out.print(Dist.get(productoIndex) + " ");
                    productoIndex++;
                } else if (turn && productoIndex < numProductos) {
                    // Imprimir de derecha a izquierda
                    int reverseIndex = i * numColumnas + (numColumnas - j - 1);
                    if (reverseIndex < numProductos && Dist.get(reverseIndex) != null) {
                        System.out.print(Dist.get(reverseIndex) + " ");
                        productoIndex++;
                    }
                    else System.out.print("/*/ ");
                } else {
                    System.out.print("/*/ ");
                }
              
            }
            System.out.println();
            turn = !turn;
        }
    }

    /**
     * Ofrece al usuario aceptar o rechazar los cambios de la distribucion.
     *
     * @return Indica si se ha podido guardar la distribución
     * @param scanner El scanner para leer la entrada del usuario.
     */
    public boolean confirmarDistribucion(Scanner scanner) {
        boolean keep_in = true;
        String opt = "";
        while (keep_in) {
            System.out.println("Desea guardar la distribucion seleccionada?");
            System.out.println("1. Si");
            System.out.println("2. No");
            opt = scanner.nextLine().trim();
            if (opt.equals("1") || opt.equals("2")) keep_in = false;
            else System.out.println("Opcion no valida");
        }
        return opt.equals("1");
    }

    /**
     * Ejecuta un algoritmo de distribución en una distribución dada.
     * El algoritmo se selecciona en función de la opción proporcionada.
     * @param distribution La distribución sobre la que se ejecutará el algoritmo.
     * @param option La opción seleccionada para el algoritmo.
     * @param LlistaProductes El mapa de productos para generar la distribución.
     */
    public void executeAlgorithm(Distribucion distribution, String option, Map<String, Map<String,Float>> LlistaProductes) {
        Estrategia estrategia;
        if (Objects.equals(option, "1")) {
            estrategia = new AlgoritmoVoraz();
        }
        else {
            estrategia = new Aproximation_Kruskal_ILS();

        }
        distribution.setAlgoritmo(estrategia);
        distribution.generateDist(LlistaProductes);
    }

}


