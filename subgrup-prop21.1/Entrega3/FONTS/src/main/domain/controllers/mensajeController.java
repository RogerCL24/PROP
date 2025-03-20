package main.domain.controllers;

import main.domain.classes.*;


/**
 * Controlador de mensajes para gestionar el envío, recepción y visualización de mensajes entre usuarios.
 * Este controlador utiliza el patrón Singleton para asegurar que solo haya una instancia.
 *
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */
public class mensajeController {

    /**
     * Instancia del controlador de mensajes.
     */
    private static mensajeController instance; // Singleton instance

    /**
     * Constructora del controlador de mensajes.
     */
    public mensajeController() {}

    /**
     * Método estático para obtener la única instancia de mensajeController.
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
     * Envía un mensaje al destinatario del mensaje, por lo tantos al destinatario le añade un mensaje recibido y al
     * sender le añade un mensaje enviado.
     * @param mensaje Mensaje que se envía.
     */
    public void EnviarMensaje(Mensaje mensaje) {
        mensaje.getSender().addSent(mensaje);
        mensaje.getDestinatario().addInbox(mensaje);
        System.out.println("\nMensaje enviado!\n");
    }

}
