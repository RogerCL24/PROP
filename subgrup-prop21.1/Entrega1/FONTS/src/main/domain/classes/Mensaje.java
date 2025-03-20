package main.domain.classes;

import java.time.LocalDateTime;

/**
 * Clase que representa un mensaje que puede contener una lista de productos o una distribución.
 * Cada mensaje incluye información sobre el objeto enviado, su nombre, si es una lista o una distribución,
 * el usuario remitente, el usuario destinatario, la fecha y hora de envío, y el estado de lectura del mensaje.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */

public class Mensaje {
    private Object objeto;       // La lista o distribución
    private String nombre;
    private boolean esLista;     // True si es lista, false si es distribución
    private boolean leido;       // True si el mensaje ha sido leído
    private LocalDateTime timestamp;
    private User sender;
    private User destinatario;

    /**
     * Constructor de la clase Mensaje. Crea un mensaje con el objeto, nombre, tipo, remitente y destinatario especificados.
     * @param objeto La lista o distribución que se envía en el mensaje.
     * @param nombre Nombre del objeto (lista o distribución).
     * @param esLista True si el mensaje contiene una lista; false si contiene una distribución.
     * @param sender Usuario remitente del mensaje.
     * @param destinatario Usuario destinatario del mensaje.
     */

    public Mensaje(Object objeto, String nombre, boolean esLista, User sender, User destinatario) {
        this.objeto = objeto;
        this.nombre = nombre;
        this.esLista = esLista;
        this.sender = sender;
        this.destinatario = destinatario;
        this.timestamp = LocalDateTime.now(); // Asigna el tiempo actual
        this.leido = false; // Inicialmente el mensaje es no leído
    }

    /**
     * Obtiene la fecha y hora en que se creó el mensaje.
     * @return La marca de tiempo (timestamp) del mensaje.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Obtiene el usuario remitente del mensaje.
     * @return El objeto User correspondiente al remitente del mensaje.
     */
    public User getSender() {
        return sender;
    }

    /**
     * Obtiene el objeto (lista o distribución) contenido en el mensaje.
     * @return El objeto enviado en el mensaje.
     */
    public Object getObjeto() {
        return objeto;
    }

    /**
     * Verifica si el mensaje contiene una lista.
     * @return True si el mensaje contiene una lista, false si contiene una distribución.
     */
    public boolean isLista() {
        return esLista;
    }

    /**
     * Verifica si el mensaje ha sido leído.
     * @return True si el mensaje ha sido leído, false en caso contrario.
     */
    public boolean isLeido() {
        return leido;
    }

    /**
     * Marca el mensaje como leído.
     */
    public void marcarComoLeido() {
        this.leido = true;

    }

    /**
     * Obtiene el usuario destinatario del mensaje.
     * @return El objeto User correspondiente al destinatario del mensaje.
     */
    public User getDestinatario() {
        return destinatario;
    }

    /**
     * Obtiene el nombre del objeto (lista o distribución) contenido en el mensaje.
     * @return El nombre del objeto en el mensaje.
     */
    public String getNombre() {
        return nombre;
    }

}
