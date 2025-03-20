package main.domain.controllers;

import main.domain.classes.*;

import java.time.format.DateTimeFormatter;
import main.domain.classes.exceptions.formatException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Controlador de mensajes para gestionar el envío, recepción y visualización de mensajes entre usuarios.
 * Este controlador utiliza el patrón Singleton para asegurar que solo haya una instancia.
 *
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class mensajeController {

    private static mensajeController instance; // Singleton instance

    public mensajeController() {}

    /**
     * Metodo estático para obtener la única instancia de mensajeController.
     *
     * @return La instancia de mensajeController.
     */
    public static mensajeController getInstance() {
        if (instance == null) {
            instance = new mensajeController();
        }
        return instance;
    }


    /**
     * Muestra los mensajes recibidos en la bandeja de entrada del usuario.
     *
     * @param mensajesMap Mapa para almacenar mensajes con sus índices.
     * @param startIndex  Índice inicial para numerar los mensajes.
     * @param currentUser El usuario actual.
     * @return El siguiente índice disponible después de mostrar los mensajes.
     */
    public int verMensajesRecibidos(Map<Integer, Mensaje> mensajesMap, int startIndex, User currentUser) {
        System.out.println("========== BANDEJA DE ENTRADA ==========");
        return verMensajes(true, mensajesMap, startIndex, currentUser); // Llama a verMensajes con isInbox = true
    }

    /**
     * Muestra los mensajes enviados por el usuario.
     *
     * @param mensajesMap Mapa para almacenar mensajes con sus índices.
     * @param startIndex  Índice inicial para numerar los mensajes.
     * @param currentUser El usuario actual.
     * @return El siguiente índice disponible después de mostrar los mensajes.
     */
    public int verMensajesEnviados(Map<Integer, Mensaje> mensajesMap, int startIndex, User currentUser) {
        System.out.println("========== MENSAJES ENVIADOS ==========");
        return verMensajes(false, mensajesMap, startIndex, currentUser); // Llama a verMensajes con isInbox = false
    }

    /**
     * Metodo auxiliar para mostrar mensajes recibidos o enviados.
     *
     * @param isInbox      true para bandeja de entrada, false para mensajes enviados.
     * @param mensajesMap  Mapa para almacenar mensajes con sus índices.
     * @param startIndex   Índice inicial para numerar los mensajes.
     * @param currentUser  El usuario actual.
     * @return El siguiente índice disponible después de mostrar los mensajes.
     */
    public static int verMensajes(boolean isInbox, Map<Integer, Mensaje> mensajesMap, int startIndex, User currentUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        List<Mensaje> mensajes = isInbox ? currentUser.getInbox() : currentUser.getSentMessages();
        int mensajeIndex = startIndex;  // Iniciar el contador de mensajes

        if (isInbox) {
            // Mensajes nuevos y leídos para la bandeja de entrada
            List<Mensaje> mensajesNuevos = mensajes.stream().filter(m -> !m.isLeido()).toList();
            List<Mensaje> mensajesLeidos = mensajes.stream().filter(Mensaje::isLeido).toList();

            if (!mensajesNuevos.isEmpty()) {
                System.out.println("\n-------------- Mensajes nuevos --------------\n");
                for (Mensaje m : mensajesNuevos) {
                    System.out.println(mensajeIndex + ". " +
                            "De: " + m.getSender().getUsername() + // Correcto: remitente para mensajes de entrada
                            " | Tipo: " + (m.isLista() ? "Lista" : "Distribucion") +
                            " | Nombre: " + m.getNombre() +
                            " | Fecha: " + m.getTimestamp().format(formatter));
                    mensajesMap.put(mensajeIndex++, m);
                    m.marcarComoLeido(); // Marcar como leído solo en mensajes de entrada
                }
            } else {
                System.out.println("No tienes mensajes nuevos.\n");
            }

            if (!mensajesLeidos.isEmpty()) {
                System.out.println("\n-------------- Mensajes leidos --------------\n");
                for (Mensaje m : mensajesLeidos) {
                    System.out.println(mensajeIndex + ". " +
                            "De: " + m.getSender().getUsername() + // Correcto: remitente para mensajes de entrada
                            " | Tipo: " + (m.isLista() ? "Lista" : "Distribucion") +
                            " | Nombre: " + m.getNombre() +
                            " | Fecha: " + m.getTimestamp().format(formatter));
                    mensajesMap.put(mensajeIndex++, m);
                }
            } else {
                System.out.println("No tienes mensajes leidos.\n");
            }
        } else {
            // Mensajes enviados
            if (!mensajes.isEmpty()) {
                System.out.println("\n-------- Todos los mensajes enviados --------\n");
                for (Mensaje m : mensajes) {
                    System.out.println(mensajeIndex + ". " +
                            "Para: " + m.getDestinatario().getUsername() + // Correcto: destinatario para mensajes enviados
                            " | Tipo: " + (m.isLista() ? "Lista" : "Distribucion") +
                            " | Nombre: " + m.getNombre() +
                            " | Fecha: " + m.getTimestamp().format(formatter));
                    mensajesMap.put(mensajeIndex++, m);
                }
            } else {
                System.out.println("No tienes mensajes enviados.\n");
            }
        }

        return mensajeIndex; // Devuelve el siguiente índice disponible
    }

    /**
     * Marca un mensaje como leído si no fue enviado por el usuario actual.
     *
     * @param mensaje     El mensaje a marcar como leído.
     * @param currentUser El usuario actual.
     */
    public void marcarMensajeLeido(Mensaje mensaje, User currentUser) {
        if (!mensaje.isLeido() && mensaje.getSender() != currentUser) {
            mensaje.marcarComoLeido();
        }
    }

    /**
     * Muestra el contenido de un mensaje en formato detallado.
     *
     * @param mensaje El mensaje a mostrar.
     * @return Una cadena con el contenido del mensaje formateado.
     */
    public String mostrarMensaje(Mensaje mensaje) {
        return "==================================\n" +
                "De: " + mensaje.getSender().getUsername() + "\n" +
                "Para: " + mensaje.getDestinatario().getUsername() + "\n" +
                "Tipo: " + (mensaje.isLista() ? "Lista" : "Distribucion") + "\n" +
                "Nombre: " + mensaje.getNombre() + "\n" +
                "Fecha: " + mensaje.getTimestamp() + "\n" +
                "==================================\n";
    }

    /**
     * Permite al usuario seleccionar el tipo de mensaje a enviar.
     *
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @return La opción seleccionada ("1" para lista, "2" para distribución) o null si se cancela.
     */
    public String seleccionarTipoMensaje(Scanner scanner) {
        System.out.println("\n¿Qué le quiere enviar? Seleccione.");
        System.out.println("1. Una lista de productos");
        System.out.println("2. Una distribucion de productos");
        System.out.println("0. Nada, volver.");

        String opcion = scanner.nextLine();
        if (opcion.equals("0")) {
            System.out.println("Operacion cancelada.");
            return null;
        }
        if (!opcion.equals("1") && !opcion.equals("2")) {
            System.out.println("Opcion no valida.");
            return seleccionarTipoMensaje(scanner);
        }
        return opcion;
    }

    /**
     * Valida y selecciona un mensaje del mapa de mensajes.
     *
     * @param scanner Objeto Scanner para capturar la entrada del usuario.
     * @param mensajesMap Mapa de mensajes donde la clave es el índice del mensaje.
     * @return El mensaje seleccionado, o `null` si no se seleccionó un mensaje válido.
     * @throws formatException Si la entrada no es válida.
     */
    public int seleccionarMensaje(Scanner scanner, Map<Integer, Mensaje> mensajesMap) throws formatException {
        System.out.println("Para seleccionar un mensaje escribe su indice o '0' para volver:");

        if (!scanner.hasNextInt()) {
            String invalidInput = scanner.nextLine(); // Captura la entrada inválida
            throw new formatException("La opcion ingresada '" + invalidInput + "' no es un numero valido.");
        }

        int mensajeNum = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea restante

        if (mensajeNum == 0) return mensajeNum;

        if (!mensajesMap.containsKey(mensajeNum)) {
            throw new formatException("El numero de mensaje '" + mensajeNum + "' no es valido.");
        }

        return mensajeNum;
    }


    /**
     * Muestra los usuarios que hay en el sistema
     *
     * @param usernames La lista de productos a enviar.
     *
     */
    public void destinatariosPosibles(List<String> usernames) {
        System.out.println("\nLos destinatarios posibles actuales para sus mensajes son:");
        if (usernames.isEmpty()) {
            System.out.println("No hay usuarios en el sistema.");
        } else {
            for (String username : usernames) {
                System.out.println("- " + username);
            }
        }
    }

    /**
     * Crea y envía un mensaje de lista de productos.
     *
     * @param lista        La lista de productos a enviar.
     * @param nombreLista  El nombre de la lista.
     * @param remitente    El usuario remitente.
     * @param destinatario El usuario destinatario.
     */
    public void crearYEnviarMensajeLista(LlistaProductes lista, String nombreLista, User remitente, User destinatario) {
        Mensaje mensaje = new Mensaje(lista, nombreLista, true, remitente, destinatario);
        destinatario.addInbox(mensaje);
        remitente.addSent(mensaje);
        System.out.println("\nMensaje de lista enviado!\n");
    }

    /**
     * Crea y envía un mensaje de distribución de productos.
     *
     * @param distribucion  La distribución de productos a enviar.
     * @param nombreDist    El nombre de la distribución.
     * @param remitente     El usuario remitente.
     * @param destinatario  El usuario destinatario.
     */
    public void crearYEnviarMensajeDistribucion(Distribucion distribucion, String nombreDist, User remitente, User destinatario) {
        Mensaje mensaje = new Mensaje(distribucion, nombreDist, false, remitente, destinatario);
        destinatario.addInbox(mensaje);
        remitente.addSent(mensaje);
        System.out.println("Mensaje de distribucion enviado!");
    }

}
