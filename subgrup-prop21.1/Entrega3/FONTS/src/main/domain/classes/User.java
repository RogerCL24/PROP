package main.domain.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un usuario con información personal y sus conjuntos de distribuciones y listas de productos.
 * Incluye funcionalidades para gestionar la bandeja de entrada y los mensajes enviados,
 * así como los conjuntos de listas de productos y distribuciones del usuario.
 *
 *  @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class User {
    /**
     * Nombre de usuario del usuario
     */
    private String username;
    /**
     * Nombre del usuario
     */
    private String name;
    /**
     * Contraseña del usuario
     */
    private String password;

    /**
     * Número de nuevos mensajes que le han llegado al usuario y el usuario no ha visto.
     */
    private int mensajesNuevos; //Mensajes sin ver del usuario
    /**
     * Lista de mensajes recibidos.
     */
    private List<Mensaje> inbox;
    /**
     * Lista de mensajes que ha enviado el usuario.
     */
    private List<Mensaje> sentMessages;
    /**
     * Conjunto de distribuciones que tiene el usuario.
     */
    public CjtDistribuciones cjtDistribuciones;
    /**
     * Conjunto de listas de productos que tiene el usuario.
     */
    public CjtLlistesProductes cjtLlistesProductes;

    /**
     * Constructor de la clase User.
     * @param username Nombre de usuario.
     * @param name Nombre real del usuario.
     * @param password Contraseña del usuario.
     */
    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.cjtDistribuciones = new CjtDistribuciones();
        this.cjtLlistesProductes = new CjtLlistesProductes();

        this.mensajesNuevos = 0;
        this.inbox = new ArrayList<>();
        this.sentMessages = new ArrayList<>();

    }

    /**
     * Obtiene el número de mensajes nuevos y luego restablece el contador a cero.
     * @return Número de mensajes nuevos sin leer.
     */
    public int getMensajesNuevos() {
        return mensajesNuevos;
    }

    /**
     * Establece los nuevos mensajes recibidos del usuario (que no ha visto).
     * @param mensajesNuevos Nuevos mensajes recibidos del usuario.
     */
    public void setMensajesNuevos(int mensajesNuevos) {
        this.mensajesNuevos = mensajesNuevos;
    }

    /**
     * Establece los mensajes recibidos del usuario.
     * @param inbox Lista de mensajes recibidos del usuario.
     */
    public void setInbox(List<Mensaje> inbox) {
        this.inbox = inbox;
    }

    /**
     * Obtiene la lista de mensajes recibidos (inbox).
     * @return Lista de mensajes en la bandeja de entrada.
     */
    public List<Mensaje> getInbox() {
        return inbox;
    }

    /**
     * Obtiene la lista de mensajes enviados.
     * @return Lista de mensajes enviados.
     */
    public List<Mensaje> getSentMessages() {
        return sentMessages;
    }

    /**
     * Restablece el contador de mensajes nuevos a cero.
     * Este método se usa para indicar que todos los mensajes nuevos han sido leídos.
     */
    public void updateMensajesNuevos() {mensajesNuevos = 0;}

    /**
     * Añade un mensaje a la bandeja de entrada del usuario y aumenta el contador de mensajes nuevos.
     * @param mensaje Mensaje a añadir en la bandeja de entrada.
     */
    public void addInbox(Mensaje mensaje) {
        this.inbox.add(mensaje);
        ++this.mensajesNuevos;
    }

    /**
     * Añade un mensaje a la lista de mensajes enviados del usuario.
     * @param mensaje Mensaje a añadir en los mensajes enviados.
     */
    public void addSent(Mensaje mensaje) {
        this.sentMessages.add(mensaje);
    }


    /**
     * Obtiene el nombre de usuario.
     * @return Nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtiene el nombre real del usuario.
     * @return Nombre real del usuario.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return Contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }


    /**
     * Obtiene el conjunto de listas de productos del usuario.
     * @return Conjunto de listas de productos.
     */
    public CjtLlistesProductes getCjtLlistesProductes() {
        return cjtLlistesProductes;
    }

    /**
     * Obtiene el conjunto de distribuciones del usuario.
     * @return Conjunto de distribuciones.
     */
    public CjtDistribuciones getCjtDistribuciones() {
        return cjtDistribuciones;
    }

    /**
     * Representación en cadena de la información del usuario.
     * @return Cadena con la información del usuario.
     */
    @Override
    public String toString() {
        return "User{" + "username=" + username + ", name=" + name + ", password=" + password + '}';
    }

    /**
     * Establece el conjunto de listas de productos del usuario.
     * @param cjtLlistes Conjunto de listas de productos del usuario.
     */
    public void setCjtLlistesProductes(CjtLlistesProductes cjtLlistes) {
        this.cjtLlistesProductes = cjtLlistes;
    }

    /**
     * Establece los mensajes enviados por el usuario.
     * @param sentMessages Lista de mensajes enviados por el usuario.
     */
    public void setSentMessages(List<Mensaje> sentMessages) {
        this.sentMessages = sentMessages;
    }

    /**
     * Establece el conjunto de distribuciones del usuario.
     * @param cjtDistribuciones Conjunto de distribuciones del usuario.
     */
    public void setCjtDistribuciones(CjtDistribuciones cjtDistribuciones) {
        this.cjtDistribuciones = cjtDistribuciones;
    }
}
