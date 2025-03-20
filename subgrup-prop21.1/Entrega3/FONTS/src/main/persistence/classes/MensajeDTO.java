package main.persistence.classes;

/**
 * Data Transfer Object (DTO) clase que representa un mensaje.
 */
public class MensajeDTO {
    
    /**
     * El objeto o tipo del mensaje, tales como "Distribución" o "Lista de productos".
     */
    private String object;

    /**
     * El nombre asociado al mensaje.
     */
    private String name;

    /**
     * Inidica si el mensaje es una lista.
     */
    private boolean esLista;

    /**
     * Indica si el mensaje se ha leido.
     */
    private boolean leido;

    /**
     * La fecha y hora del mensaje, almacenados como cadena en formato JSON.
     */
    private String LocalDateTime;

    /**
     * El nombre de usuario del remitente del mensaje.
     */
    private String Sender;

    /**
     * El nombre de usuario del destinatario del mensaje.
     */
    private String Destinatario;

    /**
     * Obtiene el objeto o el tipo del mensaje.
     * 
     * @return el objeto del mensaje.
     */
    public String getObjeto() {
        return object;
    }

    /**
     * Obtiene el nombre asociado al mensaje.
     * 
     * @return el nombre del mensaje.
     */
    public String getNombre() {
        return name;
    }

    /**
     * Comprueba si el mensaje representa una lista.
     * 
     * @return true si el mensaje es una lista, false de lo contrario.
     */
    public boolean isEsLista() {
        return esLista;
    }

    /**
     * Comprueba si se ha leído el mensaje.
     * 
     * @return true si se lee el mensaje, false de lo contrario.
     */
    public boolean isLeido() {
        return leido;
    }

    /**
     * Obtiene la fecha y hora del mensaje.
     * 
     * @return la fecha y hora del mensaje como string.
     */
    public String getLocalDateTime() {
        return LocalDateTime;
    }

    /**
     * Obtiene el nombre de usuario del remitente del mensaje.
     * 
     * @return el nombre de usuario del remitente.
     */
    public String getSender() {
        return Sender;
    }

    /**
     * Obtiene el nombre de usuario del destinatario del mensaje.
     * 
     * @return el nombre de usuario del destinatario.
     */
    public String getDestinatario() {
        return Destinatario;
    }
}
