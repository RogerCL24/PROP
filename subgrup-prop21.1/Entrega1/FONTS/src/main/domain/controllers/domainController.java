package main.domain.controllers;


import main.domain.classes.*;
import main.domain.classes.exceptions.formatException;

import java.util.*;

/**
 * Clase encargada de gestionar el menu principal de la aplicacion y la navegacion entre las diferentes opciones
 * relacionadas con usuarios, listas de productos y distribuciones.
 *
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class domainController {

    private userController userController;  // Controlador de usarios
    private listController listController;  // Controlador de listas
    private distributionController distributionController; // Controlador de distribuciones
    private mensajeController mensajeController;
    private int newMessages;

    /**
     * Constructor de la clase domainontroller.
     * Inicializa el userController
     */
    public domainController() {
        this.userController = userController.getInstance();
        this.listController = listController.getInstance();
        this.distributionController = distributionController.getInstance();
        this.mensajeController = mensajeController.getInstance();
        newMessages = 0;
    }

    /**
     * Inicializa los controladores para listas de productos y distribuciones.
     */
    private void initializeControllers() {
        listController.setCjt(userController.getCjtLlistesProductes());
        distributionController.setCjtD(userController.getCjtDistribuciones());
        newMessages = userController.getMensajesNuevos();
    }

    /**
     * Muestra el menu principal y las opciones disponibles para el usuario.
     */
    public void menuPrincipal() {
        System.out.println("\nBIENVENIDO/A, " + userController.getCurrentUser().getName() + "!");
        System.out.println("1. Introducir una nueva lista de productos");
        System.out.println("2. Gestionar listas de productos");
        System.out.println("3. Generar una distribucion");
        System.out.println("4. Gestionar las distribuciones ya generadas");
        System.out.println("5. Mensajes: " + mensajesNuevos());
        System.out.println("6. Cerrar sesion");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
    }


    /**
     * Ejecuta la opcion seleccionada en el menu principal, llamando a los metodos adecuados del controlador de usuario.
     *
     * @param opcion La opcion seleccionada por el usuario.
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     * @return Devuelve `true` si el menu debe continuar, `false` si el usuario ha cerrado sesion.
     */
    public boolean ejecutarOpcionMenuPrincipal(String opcion, Scanner scanner) {
        switch (opcion) {
            case "1":
                nuevaLista(scanner);
                break;
            case "2":
                if (verListasProductos()) gestionarListasProductos(scanner);
                break;
            case "3":
                generarDistribucion(scanner);
                break;
            case "4":
                if (verDistribuciones()) gestionarDistribuciones(scanner);
                break;
            case "5":
                newMessages = 0;
                menuMensajes(scanner);
                break;
            case "6":
                System.out.println("Sesion cerrada exitosamente.\n");
                return false;
            case "0":
                System.out.println("Saliendo del programa...");
                System.exit(0);
            default:
                System.out.println("Opcion '" + opcion + "' no valida. Por favor, seleccione una opcion del menu.");
        }
        return true;
    }


/**
     * Registra un nuevo usuario.
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registerUser(Scanner scanner) {
        boolean res = userController.registerUser(scanner);
        initializeControllers();
        return res;
    }

    /**
     * Inicia sesión con un usuario existente.
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     * @return true si el inicio de sesión fue exitoso, false en caso contrario.
     */
    public boolean loginUser(Scanner scanner) {
        boolean res = userController.loginUser(scanner);
        initializeControllers();
        return res;
    }

    /**
     * Muestra el menú para introducir una nueva lista de productos y delega la tarea al controlador de usuarios.
     *
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     */
    public void nuevaLista(Scanner scanner) {
        listController.introducirNuevaLista(scanner);
    }

    /**
     * Verifica si existen listas de productos para el usuario actual.
     *
     * @return Devuelve `true` si existen listas de productos, de lo contrario `false`.
     */
    public boolean verListasProductos() {
        return listController.verListas();
    }

    /**
     * Gestiona las listas de productos, permitiendo consultar, modificar o eliminar listas.
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     */
    public void gestionarListasProductos(Scanner scanner) {
        listController.gestionarListasProductos(scanner);
    }

    /**
     * Muestra los productos de una lista en pantalla.
     *
     * @param lista La lista de productos a imprimir.
     */
    private void imprimirLista(LlistaProductes lista) {
        listController.verProductosLista2(lista);
    }

    /**
     * Guarda una lista de productos en el conjunto de listas del usuario si se confirma.
     *
     * @param user El usuario actual.
     * @param lista La lista de productos a guardar.
     * @param nombre El nombre de la lista.
     * @param scanner El scanner para leer la entrada del usuario.
     */
    private void guardarLista(User user, LlistaProductes lista, String nombre, Scanner scanner) {
        if (listController.confirmarLista(scanner)) {
            CjtLlistesProductes conjunto = user.getCjtLlistesProductes();
            String nombreFinal = generarNombreUnico(conjunto, nombre);
            conjunto.agregarLista(nombreFinal, lista);
            System.out.println("Lista guardada con el nombre: " + nombreFinal);
        }
    }

    /**
     * Gestiona la creación de una nueva distribución de productos según las listas de productos existentes.
     * Permite elegir entre ajustar automáticamente las dimensiones de la estantería o especificarlas manualmente.
     * Además, permite elegir el algoritmo de distribución (Voraz o Aproximación).
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     */
    public void generarDistribucion(Scanner scanner) {
        if (listController.verListas()) {
            System.out.println("Introduzca la lista con la que quiere generar la distribucion:");
            String listaElegida = scanner.nextLine();
            String userName = userController.getCurrentUser().getName();
            Map<String, Map<String, Float>> LlistaGdS;
            if (listController.exists(listaElegida)) {
                LlistaGdS = listController.getSimilarityMatrix(listaElegida);
                distributionController.generarDistribucion(scanner, LlistaGdS, userName);
            }
            else System.out.println("No existe la lista seleccionada");
        }

    }

    /**
     * Verifica y muestra las distribuciones disponibles.
     *
     * @return True si se pueden ver distribuciones, False en caso contrario.
     */
    public boolean verDistribuciones() {
        return distributionController.verDistribuciones();
    }

    /**
     * Gestiona las distribuciones existentes, permitiendo al usuario consultar, modificar o eliminar distribuciones.
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     */
    public void gestionarDistribuciones(Scanner scanner) {
        distributionController.gestionarDistribuciones(scanner);
    }

    /**
     * Muestra el contenido de una distribución en pantalla.
     *
     * @param distribucion La distribución de productos a imprimir.
     */
    private void imprimirDistribucion(Distribucion distribucion) {
        System.out.println("Contenido de la distribucion:");
        distributionController.imprimirDistribucion(distribucion);
    }

    /**
     * Guarda una distribución en el conjunto de distribuciones del usuario si se confirma.
     *
     * @param user El usuario actual.
     * @param distribucion La distribución a guardar.
     * @param nombre El nombre de la distribución.
     * @param scanner El scanner para leer la entrada del usuario.
     */
    private void guardarDistribucion(User user, Distribucion distribucion, String nombre, Scanner scanner) {
        if (distributionController.confirmarDistribucion(scanner)) {
            CjtDistribuciones conjunto = user.getCjtDistribuciones();
            String nombreFinal = generarNombreUnico(conjunto, nombre);
            conjunto.agregarDistribucion(nombreFinal, distribucion);
            System.out.println("Distribucion guardada con el nombre: " + nombreFinal);
        }
    }


    /**
     * Imprime el menu del chat de mensajes. Muestra la bandeja de entrada y de salida
     * y permite abrir los mensajes. Tambien se puede enviar mensajes.
     * @param scanner Objeto Scanner para capturar la entrada del usuario.
     */
    public void menuMensajes(Scanner scanner) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("_________________SuperMarket CHAT_________________");
        System.out.println("--------------------------------------------------");
        System.out.println("Envia y recibe listas de productos y distribuciones\n");
        User currentUser = userController.getCurrentUser();

        Map<Integer, Mensaje> mensajesMap = new HashMap<>();
        int boundary;
        boolean keep_in = true;
        currentUser.updateMensajesNuevos();
        while (keep_in) {
            int mensajeIndex = boundary = mensajeController.verMensajesRecibidos(mensajesMap, 1, currentUser);
            mensajeController.verMensajesEnviados(mensajesMap, mensajeIndex, currentUser);
            System.out.println("------------------------------------------");
            System.out.println("1. Enviar un mensaje (una distribucion o lista de productos)");
            System.out.println("2. Seleccionar un mensaje");
            System.out.println("0. Salir");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    boolean keep_in_2 = true;
                    while (keep_in_2) {
                        if (enviarMensaje(scanner)) keep_in_2 = false;
                        else System.out.println("Vuelva a intentarlo");
                    }
                    break;
                case "2":
                    abrirMensaje(currentUser,mensajesMap,scanner, boundary);
                    break;
                case "0":
                    keep_in = false;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("\nOpcion invalida.\n");
            }
        }
    }

    /**
     * Abre un mensaje seleccionado desde el mapa y realiza acciones adicionales.
     *
     * @param currentUser El usuario actual.
     * @param mensajesMap Mapa de mensajes donde la clave es el número del mensaje.
     * @param scanner Objeto Scanner para capturar la entrada del usuario.
     * @param boundary entero usado para diferenciar entre mensajes recibidos y enviados.
     */
    private void abrirMensaje(User currentUser, Map<Integer, Mensaje> mensajesMap, Scanner scanner, int boundary) {
        try {
            int mensajeSeleccionadoNum = mensajeController.seleccionarMensaje(scanner, mensajesMap);

            if (mensajeSeleccionadoNum != 0) {
                Mensaje mensajeSeleccionado = mensajesMap.get(mensajeSeleccionadoNum);
                if (mensajeSeleccionadoNum < boundary) {
                    mensajeController.marcarMensajeLeido(mensajeSeleccionado, currentUser);
                }

                System.out.println(mensajeController.mostrarMensaje(mensajeSeleccionado));

                if (mensajeSeleccionado.isLista()) {
                    imprimirLista((LlistaProductes) mensajeSeleccionado.getObjeto());
                } else {
                    imprimirDistribucion((Distribucion) mensajeSeleccionado.getObjeto());
                }
                if (mensajeSeleccionadoNum < boundary) {
                    guardarMensaje(currentUser, mensajeSeleccionado, scanner);
                }
            }
        } catch (formatException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    /**
     * Da la opción al usuario de guardar el mensaje en su conjunto de listas o distribuciones
     * en función del tipo de mensaje (lista o distribución).
     *
     * @param user El usuario actual.
     * @param mensaje El mensaje a guardar.
     * @param scanner El scanner para leer la entrada del usuario.
     */
    private void guardarMensaje(User user, Mensaje mensaje, Scanner scanner) {
        if (mensaje.isLista()) {
            guardarLista(user, (LlistaProductes) mensaje.getObjeto(), mensaje.getNombre(), scanner);
        } else {
            guardarDistribucion(user, (Distribucion) mensaje.getObjeto(), mensaje.getNombre(), scanner);
        }
    }


    /**
     * Genera un nombre único para una lista o distribución en función del conjunto donde se agregará.
     * Si el nombre ya existe, añade un contador al final del nombre.
     *
     * @param conjunto El conjunto donde se agregará el elemento (puede ser de listas o distribuciones).
     * @param nombre El nombre original del elemento.
     * @param <T> Tipo de conjunto (CjtDistribuciones o CjtLlistesProductes).
     * @return Un nombre único para el elemento.
     */
    private <T> String generarNombreUnico(T conjunto, String nombre) {
        int contador = 1;
        String nombreFinal = nombre;

        // Comprobar si el conjunto es una instancia de CjtDistribuciones o CjtLlistesProductes
        if (conjunto instanceof CjtDistribuciones distribuciones) {
            while (distribuciones.contiene(nombreFinal)) {
                nombreFinal = nombre + "(" + contador + ")";
                contador++;
            }
        } else if (conjunto instanceof CjtLlistesProductes listas) {
            while (listas.contiene(nombreFinal)) {
                nombreFinal = nombre + "(" + contador + ")";
                contador++;
            }
        } else {
            throw new IllegalArgumentException("Tipo de conjunto no soportado: " + conjunto.getClass().getName());
        }

        return nombreFinal;
    }

    /**
     * Muestra el menú para enviar un mensaje, seleccionando el destinatario y el tipo de mensaje.
     * Permite al usuario enviar un mensaje con una lista de productos o una distribución a otro usuario.
     *
     * @param scanner Objeto Scanner para capturar la entrada del usuario.
     * @return Retorna true si el mensaje se envió correctamente o si no hay otros usuarios en el sistema,
     *         o false si hubo un error, como que el destinatario no existe o si el usuario intenta enviarse un mensaje a sí mismo.
     */
    public boolean enviarMensaje(Scanner scanner) {
        System.out.println("\n---ENVIAR UN MENSAJE---");
        User currentUser = userController.getCurrentUser();

        // Verificar si hay otros usuarios disponibles
        if (userController.getSizeCjt() == 1) {
            System.out.println("\nNo hay otros usuarios en el sistema.\n");
            return true;
        }

        // Seleccionar destinatario
        System.out.print("Introduzca el destinatario: ");
        String nombreDest = scanner.nextLine();
        if (!userController.containsUser(nombreDest)) {
            System.out.println("El usuario destinatario no existe.\n");
            mostrarDestinatariosPosibles();
            return false;
        }
        else if (Objects.equals(nombreDest, currentUser.getName())) {
            System.out.println("No te puedes autoenviar mensajes.\n");
            mostrarDestinatariosPosibles();
            return false;
        }

        // Selección del tipo de mensaje (lista o distribución)
        String tipoMensaje = mensajeController.seleccionarTipoMensaje(scanner);
        if (tipoMensaje == null) return true; // Si elige cancelar

        // Llamada al metodo adecuado en función del tipo de mensaje
        switch (tipoMensaje) {
            case "1":
                enviarListaMensaje(scanner, currentUser, nombreDest);
                break;
            case "2":
                enviarDistribucionMensaje(scanner, currentUser, nombreDest);
                break;
        }
        return true;
    }


    /**
     * Muestra los posibles destinatarios si el destinatario seleccionado no existe.
     */
    private void mostrarDestinatariosPosibles() {
        List<String> usernames = userController.getAllUsernames();
        usernames.remove(userController.getCurrentUser().getName());
        mensajeController.destinatariosPosibles(usernames);
    }


    /**
     * Envía una lista de productos seleccionada como mensaje al usuario destinatario.
     *
     * @param scanner Objeto Scanner para capturar la entrada del usuario.
     * @param currentUser El usuario actual que envía el mensaje.
     * @param nombreDest Nombre del destinatario del mensaje.
     */
    private void enviarListaMensaje(Scanner scanner, User currentUser, String nombreDest) {
        if (!listController.verListas()) return;

        String nombreLista = "";
        boolean keep_in = true;
        while (keep_in) {
            System.out.print("Introduzca el nombre de la lista que quiere enviar: ");
            nombreLista = scanner.nextLine();
            if (!listController.exists(nombreLista)) System.out.println("No existe ninguna lista llamada " + nombreLista + ".");
            else keep_in = false;
        }


        // Crear el mensaje en mensajeController con los datos proporcionados
        LlistaProductes lista = listController.obtenerLista(nombreLista);
        mensajeController.crearYEnviarMensajeLista(lista, nombreLista, currentUser, userController.getUser(nombreDest));
    }

    /**
     * Envía una distribución de productos seleccionada como mensaje al usuario destinatario.
     *
     * @param scanner Objeto Scanner para capturar la entrada del usuario.
     * @param currentUser El usuario actual que envía el mensaje.
     * @param nombreDest Nombre del destinatario del mensaje.
     */
    private void enviarDistribucionMensaje(Scanner scanner, User currentUser, String nombreDest) {
        if (!distributionController.verDistribuciones()) return;

        String nombreDist = "";
        boolean keep_in = true;
        while (keep_in) {
            System.out.print("Introduzca el nombre de la distribucion que quiere enviar: ");
            nombreDist = scanner.nextLine();
            if (!distributionController.exists(nombreDist)) System.out.println("No existe ninguna distribucion llamada " + nombreDist + ".");
            else keep_in = false;
        }

        // Crear el mensaje en mensajeController con los datos de la distribución seleccionada
        Distribucion distribucion = distributionController.getDistribucion(nombreDist);
        mensajeController.crearYEnviarMensajeDistribucion(distribucion, nombreDist, currentUser, userController.getUser(nombreDest));
    }

    /**
     * Muestra un texto indicando la cantidad de mensajes nuevos que tiene el usuario.
     *
     * @return Texto con la cantidad de mensajes nuevos que hay
     */
    public String mensajesNuevos() {
        return "(" + newMessages + " mensaje(s) nuevo(s))";
    }


}
