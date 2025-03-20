package main.domain.controllers;

import main.domain.classes.*;

import java.util.List;
import java.util.Scanner;


/**
 * Clase encargada de gestionar las distribuciones de productos asociadas a usuarios.
 * Implementa el patrón Singleton para asegurar una única instancia.
 * Proporciona métodos para registro, inicio de sesión y gestión de usuarios.
 *
 * @autor Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class userController {
    private static userController instance; // Singleton instance
    private CjtUsers cjtUsers;  // Conjunto de usuarios
    private User currentUser;   // Usuario actual

    /**
     * Constructor de UserController.
     * Inicializa el conjunto de usuarios.
     */
    private userController() {
        this.cjtUsers = new CjtUsers();
    }

    /**
     * Obtiene la instancia única de userController (patrón Singleton).
     * @return La instancia de userController.
     */
    public static userController getInstance() {
        if (instance == null) {
            instance = new userController();
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
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registerUser(Scanner scanner) {
        String username, name, password;
        username = name = password = "";
        boolean keep_in = true;
        while (keep_in) {
            System.out.print("Ingrese un nombre de usuario: ");
            username = scanner.nextLine().trim();
            if (!username.isEmpty()) {
                if (cjtUsers.containsUser(username)) System.out.println("El nombre de usuario ya existe, intente con otro.");
                else keep_in = false;
            } else System.out.println("El nombre de usuario no puede estar vacio.");

        }
        keep_in = true;
        while (keep_in) {
            System.out.print("Ingrese su nombre: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) keep_in = false;
            else System.out.println("El nombre no puede estar vacio.");
        }
        keep_in = true;
        while (keep_in) {
            System.out.print("Ingrese una contraseña: ");
            password = scanner.nextLine().trim();
            if (!password.isEmpty()) keep_in = false;
            else System.out.println("La contraseña no puede estar vacia.");
        }

        User newUser = new User(username, name, password);
        cjtUsers.addUser(newUser);
        currentUser = newUser;
        System.out.println("Usuario registrado exitosamente.");
        return true;
    }


    /**
     * Inicia sesión con un usuario existente.
     *
     * @param scanner Objeto Scanner para leer entradas del usuario.
     * @return true si el inicio de sesión fue exitoso, false en caso contrario.
     */
    public boolean loginUser(Scanner scanner) {
        System.out.print("Ingrese su nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();

        User user = cjtUsers.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Inicio de sesion exitoso. ¡Bienvenido, " + user.getName() + "!");
            return true;
        } else {
            System.out.println("Nombre de usuario o contraseña incorrectos.");
            return false;
        }
    }

}
