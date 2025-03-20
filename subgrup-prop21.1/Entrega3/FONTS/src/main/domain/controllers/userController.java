package main.domain.controllers;

import main.domain.classes.*;

import java.util.List;
import java.util.Map;


/**
 * Clase encargada de gestionar las distribuciones de productos asociadas a usuarios.
 * Implementa el patrón Singleton para asegurar una única instancia.
 * Proporciona métodos para registro, inicio de sesión y gestión de usuarios.
 *
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class userController {
    /**
     * Instancia de userController, es Singleton.
     */
    private static userController instance;
    /**
     * Conjunto de usuarios
     */
    private CjtUsers cjtUsers;
    /**
     * Usuario actual
     */
    private User currentUser;


    /**
     * Constructor de UserController.
     * Inicializa el conjunto de usuarios.
     * @param users Mapa con nombre de usuario y su usuario.
     */
    private userController(Map<String,User> users) {
        this.cjtUsers = new CjtUsers();
        cjtUsers.setUsersList(users);
    }


    /**
     * Obtiene la instancia única de userController (patrón Singleton).
     * @param users Mapa con nombre de usuario y su usuario.
     * @return La instancia de userController.
     */
    public static userController getInstance(Map<String,User> users) {
        if (instance == null) {
            instance = new userController(users);
        }
        return instance;
    }

    /**
     * Obtiene el usuario actual que ha iniciado sesión.
     * @return Usuario actual.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Busca y obtiene un usuario específico por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return El usuario encontrado o null si no existe.
     */
    public User getUser(String username) {
        return cjtUsers.getUser(username);
    }

    /**
     * Obtiene el número de usuarios registrados en el sistema.
     * @return Número total de usuarios.
     */
    public int getSizeCjt(){
        return cjtUsers.getSize();
    }

    /**
     * Obtiene una lista con los nombres de usuario de todos los usuarios registrados.
     * @return Lista de nombres de usuario.
     */
    public List<String> getAllUsernames() {
        return cjtUsers.getAllUsernames();
    }

    /**
     * Obtiene el número de mensajes no leídos del usuario actual.
     * @return Número de mensajes no leídos.
     */
    int getMensajesNuevos() {return currentUser.getMensajesNuevos();}

    /**
     * Obtiene el conjunto de listas de productos del usuario.
     * @return Conjunto de listas de productos.
     */
    public CjtLlistesProductes getCjtLlistesProductes() {
        return currentUser.getCjtLlistesProductes();
    }

    /**
     * Obtiene el conjunto de distribuciones del usuario.
     * @return Conjunto de distribuciones.
     */
    public CjtDistribuciones getCjtDistribuciones() {
        return currentUser.getCjtDistribuciones();
    }

    /**
     * Verifica si un usuario existe en el conjunto de usuarios.
     * @param username El nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean containsUser(String username) {
        return cjtUsers.containsUser(username);
    }


    /**
     * Registra un nuevo usuario.
     * @param username Nombre de usuario que tendrá el usuario.
     * @param name Nombre del usuario.
     * @param password Contraseña que tendrá el usuario.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registerUser(String username, String name, String password) {
        if (!username.isEmpty()) {
            if (cjtUsers.containsUser(username)) return false;
        }
        User newUser = new User(username, name, password);
        cjtUsers.addUser(newUser);
        currentUser = newUser;
        return true;
    }



    /**
     * Inicia sesión con un usuario existente.
     * @param username Nombre de usuario del usuario.
     * @param password Contraseña del usuario.
     * @return true si el inicio de sesión fue exitoso, false en caso contrario.
     */
    public boolean loginUser(String username, String password) {
        User user = cjtUsers.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        } else return false;

    }

}
