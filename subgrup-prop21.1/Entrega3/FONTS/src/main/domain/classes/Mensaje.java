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
    /**
     * Objeto del mensaje. Puede ser una LlistaProductes o una Distribucion.
     */
    private Object objeto;
    /**
     * Nombre del Objeto
     */
    private String nombre;
    /**
     * Booleano que indica si es el objeto es LListaProductes o Distribucion. En el primer caso será true y en el
     * segundo caso será false.
     */
    private boolean esLista;
    /**
     * Booleano que indica si el mensaje ha sido leído, en cuyo caso será true, o si no ha sido leído, que por lo tanto
     * será false.
     */
    private boolean leido;       // True si el mensaje ha sido leído
    /**
     * Fecha y hora de creación del mensaje
     */
    private LocalDateTime timestamp;
    /**
     * Usuario qeu ha enviado el mensaje.
     */
    private User sender;
    /**
     * Usuario que ha recibido el mensaje.
     */
    private User destinatario;

    /**
     * Constructora por defecto del mensaje.
     */
    public Mensaje() {

    }

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

    /**
     * Establece el objeto del mensaje.
     * @param objeto LlistaProductes o Distribucion que se asigna como objeto del mensaje.
     */
    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    /**
     * Establece el nombre del objeto del mensaje.
     * @param nombre Nombre del objeto del mensaje.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el atributo esLista.
     * @param esLista booleano que indica si es LlistaProductes o Distribucion.
     */
    public void setEsLista(boolean esLista) {
        this.esLista = esLista;
    }

    /**
     * Establece el atributo leido.
     * @param leido booleano que indica si el mensaje ha sido leído o no.
     */
    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    /**
     * Establece la fecha y hora de creación del mensaje.
     * @param parse Fecha y hora de creación del mensaje.
     */
    public void setTimestamp(LocalDateTime parse) {
        this.timestamp = parse;
    }

    /**
     * Establece el usuario que ha enviado el mensaje.
     * @param user Usuario que ha enviado el mensaje.
     */
    public void setSender(User user) {
        this.sender = user;
    }

    /**
     * Establece el usuario que ha recibido el mensaje.
     * @param user Usuario que ha recibido el mensaje.
     */
    public void setDestinatario(User user) {
        this.destinatario = user;
    }
}
