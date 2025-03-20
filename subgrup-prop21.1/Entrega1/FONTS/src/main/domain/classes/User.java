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
    private String username;
    private String name;
    private String password;

    private int mensajesNuevos; //Mensajes sin ver del usuario
    private List<Mensaje> inbox;       // Mensajes recibidos
    private List<Mensaje> sentMessages; // Mensajes enviados

    public CjtDistribuciones cjtDistribuciones;
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

}
