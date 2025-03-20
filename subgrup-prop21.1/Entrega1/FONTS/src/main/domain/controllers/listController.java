package main.domain.controllers;

import main.domain.classes.LlistaProductes;
import main.domain.classes.CjtLlistesProductes;
import main.domain.classes.exceptions.formatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Clase que gestiona un conjunto de listas de productos con sus respectivas
 * matrices de similitud.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */

public class listController {

    private static listController instance;
    private CjtLlistesProductes cjt;


    /**
     * Constructor del controlador de listas.
     */
    private listController() {}

    /**
     * Devuelve la instancia única de listController, creándola si aún no existe.
     * Este metodo implementa el patrón Singleton, asegurando que solo haya una instancia de listController.
     *
     * @return La instancia única de listController.
     */
    public static listController getInstance() {
        if (instance == null) {
            instance = new listController();
        }
        return instance;
    }

    /**
     * Establece el conjunto de listas de productos (CjtLlistesProductes) que será gestionado por el controlador.
     *
     * @param cjt El conjunto de listas de productos a asignar.
     */
    public void setCjt(CjtLlistesProductes cjt) {
        this.cjt = cjt;
    }


    /**
     * Obtiene la lista de productos por su nombre.
     *
     * @param nombre El nombre de la lista.
     * @return La lista de productos correspondiente al nombre.
     */
    public LlistaProductes obtenerLista(String nombre) {
        return cjt.obtenerLista(nombre);
    }

    /**
     * Obtiene la matriz de similitudes de productos de una lista dada.
     *
     * @param nombre El nombre de la lista.
     * @return La matriz de similitudes de productos.
     */
    public Map<String, Map<String, Float>> getSimilarityMatrix(String nombre){return obtenerLista(nombre).getSimilarityMatrix();}

    /**
     * Verifica si una lista con el nombre proporcionado existe en el conjunto de listas.
     *
     * @param name El nombre de la lista a buscar.
     * @return true si la lista existe, false en caso contrario.
     */
    public boolean exists (String name) {
        return cjt.contiene(name);
    }

    /**
     * Permite introducir una nueva lista de productos.
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     */
    public void introducirNuevaLista(Scanner scanner) {
        System.out.println("-------------------------------------------------");
        System.out.println("INTRODUCIR UNA NUEVA LISTA DE PRODUCTOS");
        System.out.println("-------------------------------------------------");

        LlistaProductes LP = new LlistaProductes();
        String opcion1 = "-1";
        boolean keep_in = true;
        while (keep_in) {
            System.out.println("Como quiere introducir la lista de productos:");
            System.out.println("1. Introducir productos y similitudes desde TERMINAL");
            System.out.println("2. Introducir productos y similitudes desde FICHERO");
            System.out.println("0. Salir");

            opcion1 = scanner.nextLine();
            if (!Objects.equals(opcion1, "1") && !Objects.equals(opcion1, "2") && !Objects.equals(opcion1, "0")) {
                System.out.println("Opcion " + opcion1 + " no valida.");
            }
            else keep_in = false;
        }


        switch (opcion1) {
            case "1":
                boolean oneProduct = !(introducirProductos(scanner, LP, 0));
                if (!oneProduct) introducirSimilitudes(scanner, LP);
                guardarOdescartar(scanner,LP, oneProduct);
                break;
            case "2":
                try {
                    if (leerProductosDesdeFichero(scanner, LP)) guardarOdescartar(scanner,LP, false);
                }
                catch (formatException e) {
                    System.err.println(e.getMessage());
                }

                break;
            case "0":
                System.out.println("Regresando al menu principal...");
                break;
            default:
                System.out.println("Opcion no valida");
                break;
        }
    }

    /**
     * Introduce productos a una lista desde la entrada estándar.
     * El usuario ingresa productos que son añadidos a la lista.
     * Dependiendo del modo (`mod`), los productos se insertan con una validación adicional.
     *
     * @param scanner El scanner utilizado para leer la entrada del usuario.
     * @param lista La lista de productos a la cual se añadirán los productos introducidos.
     * @param mod El modo de inserción:
     *            - 1: Inserta productos de forma individual con validación.
     *            - Otro valor (0): Inserta los producto de forma individual sin validacion (no hay print).
     * @return Retorna `true` si los productos fueron introducidos correctamente.
     *         En el modo 1, retorna `false` si solo se introduce un producto (lo cual no es válido en ese modo).
     */
    public boolean introducirProductos(Scanner scanner, LlistaProductes lista, int mod) {
        boolean keep_in = true;
        String input = "";
        while (keep_in) {
            System.out.println("Introduce los productos en una línea separados por espacios, y usa ENTER para indicar el final:");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) System.err.println("El producto debe contener almenos 1 caracter/simbolo, no puede estar vacio.");
            else keep_in = false;
        }

        String[] productos = input.split("\\s+");

        if (mod == 1) {
            for (String producto : productos) {
                if (lista.insertarProducte(producto)) System.out.println("Producto " + producto + " insertado correctamente");
                else System.out.println("Producto " + producto + " ya existe!");
            }
        }
        else {
            for (String producto : productos) {
                lista.insertarProducte(producto);
                if (productos.length == 1) return false;
            }
        }
        return true;
    }

    /**
     * Introduce las similitudes entre productos en la lista.
     *
     * @param scanner El scanner para leer la entrada.
     * @param lista La lista de productos.
     */
    public void introducirSimilitudes(Scanner scanner, LlistaProductes lista) {
        System.out.println("Introduce una pareja de productos con su GdS (formato: P1 P2 GdS). Escribe 'fin' para terminar:");

        while (true) {
            try {
                String[] result = leerSimilitud(scanner, lista, 1);
                if (result == null) break; // Salir si se introdujo "fin"

                // Añadir la similitud a la lista si la entrada es válida
                String prod1 = result[0];
                String prod2 = result[1];
                float GdS = Float.parseFloat(result[2]);
                lista.setSimilarity(prod1, prod2, GdS);
                System.out.println("Similitud entre " + prod1 + " y " + prod2 + " con GdS " + GdS + " añadida correctamente.");

            } catch (formatException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    /**
     * Permite guardar o descartar una lista de productos.
     * Muestra la lista de productos introducidos y ofrece la opción de guardarla con un nombre
     * o descartarla y volver al menú principal.
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     * @param LP Lista de productos que se desea guardar o descartar.
     * @param oneProduct Indica si solo se introdujo un producto en la lista. Si es verdadero, no se verifican similitudes.
     */
    public void guardarOdescartar(Scanner scanner, LlistaProductes LP, boolean oneProduct) {
        System.out.println("Lista de productos introducida.");
        for (String prod : LP.getList()) System.out.println(prod);
        if (!oneProduct) verSimilitudesLista(LP);

        boolean keep_in = true;
        String opcion2 = "";
        while (keep_in) {
            System.out.println("\n-------------------------------------------------");
            System.out.println("Menu de opciones:");
            System.out.println("1. Guardar lista");
            System.out.println("0. Descartar y volver al menu principal");
            opcion2 = scanner.nextLine();
            if (opcion2.equals("0") || opcion2.equals("1")) keep_in = false;
            else System.out.println("Opcion " + opcion2 + " no valida.");
        }

        switch (opcion2){
            case "1":
                keep_in = true;
                while (keep_in) {
                    System.out.println("\n-------------------------------------------------");
                    System.out.println("Introduzca un nombre para la nueva lista:");
                    String name = scanner.nextLine();
                    if (guardarLista(name, LP, 0)) keep_in = false;
                    else System.out.println("Vuelva a intentarlo");
                }

                break;
            case "0":
                System.out.println("Regresando al menu principal...");
                break;
            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    /**
     * Gestiona las listas de productos, permitiendo consultar, modificar o eliminar listas.
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     */
    public void gestionarListasProductos(Scanner scanner) {
        boolean salir = false;

        boolean first = true;
        while (!salir) {
            if (!first) verListas();

            first = false;
            System.out.println("\n--------------------------------------------");
            System.out.println("¿Que desea hacer?");
            System.out.println("1. Consultar una lista.");
            System.out.println("2. Modificar una lista.");
            System.out.println("3. Eliminar una lista.");
            System.out.println("0. Volver al menu principal");
            String opcion = scanner.nextLine();
            boolean keep_in = true;
            switch (opcion){
                case "1":
                    while (keep_in) {
                        System.out.println("Ingrese el nombre de la lista que quiere consultar: ");
                        String name = scanner.nextLine();
                        if (verProductosLista(name)) keep_in = false;
                    }

                    break;
                case "2":
                    while (keep_in) {
                        System.out.println("Ingrese el nombre de la lista que quiere modificar: ");
                        String name2 = scanner.nextLine();
                        if (!exists(name2)) System.out.println("No existe una lista con ese nombre..");
                        else {
                            System.out.println("¿Que desea modificar de la lista?");
                            System.out.println("1. Añadir productos.");
                            System.out.println("2. Eliminar productos.");
                            System.out.println("3. Modificar los grados de similitud de productos.");
                            System.out.println("4. Cambiar el nombre de la lista");
                            System.out.println("0. Volver");
                            String opcion1 = scanner.nextLine();
                            switch (opcion1){
                                case "1":
                                    verProductosLista(name2);
                                    LlistaProductes LP = obtenerLista(name2);
                                    introducirProductos(scanner,LP,1);
                                    break;

                                case "2":
                                    verProductosLista(name2);
                                    eliminarProductosDeLista(scanner, name2);
                                    break;

                                case "3":
                                    LlistaProductes lista = cjt.obtenerLista(name2);
                                    verSimilitudesLista(lista);
                                    modificarMultiplesSimilitudes(scanner, name2);
                                    break;

                                case "4":
                                    while (keep_in) {
                                        System.out.println("Introduzca el nombre nuevo de la lista " + name2);
                                        String newName = scanner.nextLine();
                                        if (newName.equals(name2)) System.out.println(newName + " es igual al nombre anterior, " +
                                                "ponga uno diferente");
                                        else {
                                            cambiarNombreLista(name2, newName);
                                            System.out.println("Cambio de nombre efectuado -> Antiguo: " + name2 +
                                                    " | Nuevo: " + newName);
                                            keep_in = false;
                                        }
                                    }

                                    break;
                                default:
                                    System.out.println("Opcion no valida.");
                                    break;
                            }
                            keep_in = false;
                        }
                    }
                    break;
                case "3":
                    while (keep_in) {
                        System.out.println("Ingrese el nombre de la lista que quiere eliminar: ");
                        String name1 = scanner.nextLine();
                        if (exists(name1)) {
                            keep_in = false;
                            if (eliminarLista(name1, scanner)) salir = true;
                        }
                        else System.out.println("No existe una lista con ese nombre..");
                    }
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
     * Lee productos y sus similitudes desde un fichero de texto y los introduce en la lista de productos.
     * Cada línea del archivo debe contener dos productos y su similitud.
     * Si el archivo no está vacío y los productos se leen correctamente, se agregan a la lista.
     * Si ocurre un error de formato en el archivo, se lanza una excepción personalizada `formatException`.
     *
     * @param scanner El scanner utilizado para leer la entrada del usuario.
     * @param lista La lista de productos a la que se agregarán los productos leídos desde el archivo.
     * @return true si los productos se leen correctamente y se agregan a la lista, false en caso contrario.
     * @throws formatException Si ocurre un error de formato al leer el archivo, como un problema de estructura en las líneas,
     * o si el archivo está vacío o no puede ser leído.
     */
    public boolean leerProductosDesdeFichero(Scanner scanner, LlistaProductes lista) throws formatException {
        System.out.println("Introduce el nombre del fichero de texto (ej: test1): ");
        String nombreFichero = scanner.nextLine();
        String rutaBase = System.getProperty("user.dir");

        if (rutaBase.endsWith("/src")) {
            rutaBase = rutaBase.replace("/src", "");
        }


        String rutaFichero = rutaBase + File.separator + "src" + File.separator + "test" + File.separator + "inputs" + File.separator + nombreFichero + ".txt";

        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea;
            boolean fileNotEmpty = false;
            int lineNumber = 0;

            while ((linea = br.readLine()) != null) {
                fileNotEmpty = true;
                lineNumber++;

                try {
                    String[] partes = leerSimilitud(new Scanner(linea), lista, 0);
                    if (partes != null) {
                        lista.insertarProducte(partes[0]);
                        lista.insertarProducte(partes[1]);
                        lista.setSimilarity(partes[0], partes[1], Float.parseFloat(partes[2]));
                    }
                } catch (formatException e) {
                    throw new formatException(nombreFichero, lineNumber, e.getMessage());
                }
            }

            if (!fileNotEmpty) {
                throw new formatException("El archivo esta vacio: " + nombreFichero);
            }
            return true;

        } catch (IOException e) {
            throw new formatException("Error al leer el archivo: " + e.getMessage(), e);
        }
    }

    /**
     * Lee y valida la entrada de similitud de productos desde el scanner.
     * Verifica que los productos existan en la lista si mod == 1.
     *
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @param lista La lista de productos en la cual buscar los productos.
     * @param mod Modo de verificación; si es 1, valida que los productos existan en la lista.
     * @return Un array de strings con [producto1, producto2, GdS] si la entrada es válida, o null si se ingresa "fin".
     * @throws formatException Si la entrada tiene un formato incorrecto, los productos no existen o el GdS no es válido.
     */
    public String[] leerSimilitud(Scanner scanner, LlistaProductes lista, int mod) throws formatException {
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("fin")) {
            return null; // Señal para terminar
        }

        String[] tokens = input.split("\\s+");
        if (tokens.length != 3) {
            throw new formatException("Error: Debes ingresar exactamente dos productos y un grado de similitud (GdS).");
        }

        String prod1 = tokens[0];
        String prod2 = tokens[1];
        try {
            float GdS = Float.parseFloat(tokens[2]);

            if (Objects.equals(prod1, prod2)) {
                throw new formatException("Error: No puedes introducir el grado de similitud entre dos productos si son el mismo");
            }

            // Validación del valor de GdS
            if (GdS < 0 || GdS > 1) {
                throw new formatException("Error: El grado de similitud debe estar entre 0 y 1. Valor ingresado: " + GdS);
            }

            // Verificación de que los productos existen
            if (mod == 1 && (!lista.productExists(prod1) || !lista.productExists(prod2))) {
                throw new formatException("Error: Uno o ambos productos no existen en la lista.");
            }

            // Retornar datos en formato [producto1, producto2, grado]
            return new String[]{prod1, prod2, tokens[2]};

        } catch (NumberFormatException e) {
            throw new formatException("Error: El grado de similitud (GdS) debe ser un valor numerico.");
        }
    }



    /**
     * Modifica los grados de similitud entre parejas de productos en una lista.
     *
     * @param scanner El scanner para leer la entrada.
     * @param nombreLista El nombre de la lista a modificar.
     */
    public void modificarMultiplesSimilitudes(Scanner scanner, String nombreLista) {
        System.out.println("Introduce una pareja de productos con su GdS (formato: P1 P2 GdS). Escribe 'fin' para terminar:");
        LlistaProductes lista = cjt.obtenerLista(nombreLista);
        List<String[]> modificadas = new ArrayList<>();

        while (true) {
            try {
                // Intentar leer la similitud
                String[] result = leerSimilitud(scanner, lista, 1);
                if (result == null) break; // Salir si se introdujo "fin"

                // Extraer productos y nuevo GdS
                String prod1 = result[0];
                String prod2 = result[1];
                float nuevoGdS = Float.parseFloat(result[2]);

                // Modificar el GdS en la lista
                Float gdsAntiguo = lista.getSimilarity(prod1, prod2);
                lista.setSimilarity(prod1, prod2, nuevoGdS);
                modificadas.add(new String[]{prod1, prod2, String.valueOf(gdsAntiguo), String.valueOf(nuevoGdS)});

            } catch (formatException e) {
                // Manejo de error: mostrar mensaje y continuar
                System.err.println(e.getMessage());
            }
        }

        // Mostrar resumen de modificaciones
        if (!modificadas.isEmpty()) {
            System.out.println("\nGrados de similitud modificados:");
            System.out.printf("%-15s %-15s %-15s %-15s%n", "Producto 1", "Producto 2", "GdS Antiguo", "GdS Nuevo");
            System.out.println("-------------------------------------------------------------");
            for (String[] mod : modificadas) {
                System.out.printf("%-15s %-15s %-15s %-15s%n", mod[0], mod[1], mod[2], mod[3]);
            }
        } else {
            System.out.println("No se han modificado grados de similitud.");
        }
    }


    /**
     * Muestra todas las listas de productos guardadas.
     *
     * @return true si hay listas, false si no hay listas guardadas.
     */
    public boolean verListas(){
        if (cjt.isEmpty()) {
            System.out.println("No hay listas de productos guardadas.");
            return false;
        } else {
            System.out.println("\nListas de productos guardadas:");
            for (String nombreLista : cjt.getListas()) {
                System.out.println("- " + nombreLista);
            }
            return true;
        }
    }


    /**
     * Muestra todos los productos de una lista.
     *
     * @param name El nombre de la lista.
     * @return true si se muestra correctamente, false en caso contrario.
     */
    public boolean verProductosLista(String name){
        if (!cjt.isEmpty() && cjt.contiene(name)) {
            LlistaProductes lista = cjt.obtenerLista(name);
            return verProductosLista2(lista);
        } else {
            if (cjt.isEmpty()) System.out.println("No hay listas de productos guardadas."); // No deberia saltar nunca
            else System.out.println("No existe una lista con ese nombre.");
            return false;
        }
    }

    /**
     * Muestra todos los productos de una lista.
     *
     * @param lista La lista
     * @return true si se muestra correctamente, false en caso contrario.
     */
    public boolean verProductosLista2(LlistaProductes lista) {
        System.out.println("Productos de la lista: ");
        for (String prod : lista.getList()) {
            System.out.println(prod);
        }
        verSimilitudesLista(lista);
        return true;
    }

    /**
     * Muestra en consola las parejas de productos con similitud mayor a 0 en la lista de productos.
     * Evita duplicados y organiza la salida de forma que solo se imprima cada par una vez.
     *
     * @param lista La lista de productos que contiene la matriz de similitud.
     */
    public void verSimilitudesLista(LlistaProductes lista) {

        Map<String, Map<String, Float>> SM = lista.getSimilarityMatrix();
        System.out.println("\nParejas de productos con similitud mayor a 0:");
        if (SM.keySet().size() == 1) System.out.println("No hay ningun grado de similitud mayor a 0 en ninguna pareja.\n");
        else {
            for (String prod1 : SM.keySet()) {
                for (Map.Entry<String, Float> entry : SM.get(prod1).entrySet()) {
                    String prod2 = entry.getKey();
                    float GdS = entry.getValue();

                    // Evitar imprimir duplicados
                    if (prod1.compareTo(prod2) < 0 && GdS > 0) {
                        System.out.println(prod1 + " - " + prod2 + " : " + GdS);
                    }
                }
            }
        }
    }

    /**
     * Guarda o modifica una lista de productos.
     *
     * @param name El nombre de la lista.
     * @param lista La lista de productos.
     * @param mod El modo (0 para guardar, 1 para modificar).
     * @return true si la operación se realiza correctamente, false en caso contrario.
     */
    public boolean guardarLista(String name, LlistaProductes lista, int mod) {
        if (cjt.contiene(name) && mod == 0) {
            System.out.println("Ya existe una lista con ese nombre.");
            return false;
        } else if (name.isEmpty()) {
            System.out.println("El nombre no puede estar vacio");
            return false;
        }else if (!cjt.contiene(name) && mod == 0){
            lista.setNom(name);
            cjt.agregarLista(name, lista);
            System.out.println("Lista '" + name + "' agregada con exito.");
            return true;
        }
        else if (mod == 1) {
            cjt.agregarLista(name, lista);
            System.out.println("Lista '" + name + "' modificada con exito.");
            return true;
        }
        return false;
    }


    /**
     * Elimina productos de una lista de productos.
     *
     * @param scanner El scanner para leer la entrada.
     * @param nombreLista El nombre de la lista.
     */
    public void eliminarProductosDeLista(Scanner scanner, String nombreLista) {
        if (!cjt.contiene(nombreLista)) {
            System.out.println("No existe una lista con ese nombre.");
        } else {
            LlistaProductes lista = cjt.obtenerLista(nombreLista);
            Set<String> productosLista = lista.getSimilarityMatrix().keySet();

            System.out.println("Introduce los productos a eliminar en una línea, separados por espacios. Usa ENTER para confirmar:");
            String input = scanner.nextLine().trim();
            String[] productos = input.split("\\s+");

            Set<String> productosAEliminar = new HashSet<>(Arrays.asList(productos));
            if (productosAEliminar.containsAll(productosLista)) {
                System.out.println("CUIDADO! No puedes eliminar todos los productos de la lista '" + nombreLista + "'. La lista no puede quedar vacia.");
                boolean keep_in = true;
                while (keep_in) {
                    System.out.println("Le gustaria eliminar la lista por completo?");
                    System.out.println("1. Si");
                    System.out.println("2. No");
                    input = scanner.nextLine().trim();
                    if (input.equals("1") || input.equals("2")) keep_in = false;
                    else System.out.println("Opcion invalida.");
                }
                if (input.equals("1")) eliminarLista(nombreLista, scanner);
            }
            else {
                // Lista para almacenar los productos eliminados correctamente
                StringBuilder productosEliminados = new StringBuilder();

                for (String producto : productos) {
                    if (lista.getSimilarityMatrix().containsKey(producto)) {
                        lista.eliminarProducte(producto);
                        if (!productosEliminados.isEmpty()) {
                            productosEliminados.append(", ");
                        }
                        productosEliminados.append(producto);
                    }
                }

                // Si hay productos eliminados, mostrar el mensaje de confirmación
                if (!productosEliminados.isEmpty()) {
                    System.out.println("Se han eliminado los productos " + productosEliminados + " de la lista '" + nombreLista + "'.");
                } else {
                    System.out.println("No se elimino ningun producto porque no se encontraron coincidencias en la lista '" + nombreLista + "'.");
                }
            }


        }
    }


    /**
     * Cambia el nombre de una lista existente.
     *
     * @param nombreAntiguo El nombre actual de la lista.
     * @param nombreNuevo El nuevo nombre para la lista.
     */
    public void cambiarNombreLista(String nombreAntiguo, String nombreNuevo) {

        if (cjt.contiene(nombreAntiguo)){
            LlistaProductes lista = cjt.obtenerLista(nombreAntiguo);
            cjt.eliminarListaDelConjunto(nombreAntiguo);
            if (cjt.contiene(nombreNuevo)) System.out.println("Ya existe la lista " + nombreNuevo);
            else {
                lista.setNom(nombreNuevo);
                cjt.agregarLista(nombreNuevo, lista);
            }

        }
        else {
            System.out.println("No se encontro ninguna lista con el nombre '" + nombreAntiguo + "'.");
        }
    }

    /**
     * Elimina una lista del conjunto, pidiendo confirmación al usuario.
     *
     * @param nombre El nombre de la lista a eliminar.
     * @param scanner El scanner para leer la entrada del usuario.
     * @return true si la lista eliminada era la última, false si no lo era.
     */
    public boolean eliminarLista(String nombre, Scanner scanner) {
        boolean keep_in = true;
        boolean lastOne = false;
        String opt = "";
        while (keep_in) {
            System.out.println("Esta seguro que quiere eliminar la lista '" + nombre + "' ?");
            if (cjt.getSize() == 1) {
                System.out.println("\n******************************************************");
                System.out.println("Le recordamos que '" + nombre + "' es su ultima lista");
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
                cjt.eliminarListaDelConjunto(nombre);
                System.out.println("Lista '" + nombre + "' eliminada con exito.");
                return lastOne;
            case "2":
                return false;
        }
        return false;
    }
    /**
     * Pregunta al usuario si desea guardar la lista seleccionada.
     * @param scanner El scanner para leer la entrada del usuario.
     * @return true si el usuario elige guardar la lista; false en caso contrario.
     */
    public boolean confirmarLista(Scanner scanner) {
        boolean keep_in = true;
        String opt = "";
        while (keep_in) {
            System.out.println("Desea guardar la lista seleccionada?");
            System.out.println("1. Si");
            System.out.println("2. No");
            opt = scanner.nextLine().trim();
            if (opt.equals("1") || opt.equals("2")) keep_in = false;
            else System.out.println("Opcion no valida");
        }
        return opt.equals("1");
    }
}
