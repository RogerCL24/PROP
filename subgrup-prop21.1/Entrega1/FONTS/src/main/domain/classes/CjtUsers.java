package main.domain.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que representa un conjunto de usuarios.
 * Permite gestionar la colección de usuarios y realizar operaciones de añadir, eliminar y buscar usuarios.
 * Esta clase tiene una única instancia.
 *
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class CjtUsers {
    // Mapa de usuarios, con el nombre de usuario como clave
    private Map<String, User> usersList;

    /**
     * Constructor que inicializa un conjunto vacío de usuarios.
     */
    public CjtUsers() {
        this.usersList = new HashMap<>();
    }

    /**
     * Verifica si un usuario con el nombre de usuario dado existe en el conjunto.
     *
     * @param username Nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean containsUser(String username) {
        return usersList.containsKey(username);
    }

    /**
     * Añade un usuario al conjunto.
     *
     * @param user Usuario a añadir.
     *             Debe tener un nombre de usuario único.
     */
    public void addUser(User user) {
        this.usersList.put(user.getUsername(), user);
    }

    /**
     * Obtiene un usuario dado su nombre de usuario.
     *
     * @param username Nombre de usuario.
     * @return El objeto User correspondiente al nombre de usuario, o null si no existe.
     */
    public User getUser(String username) {
        return usersList.get(username);
    }

    /**
     * Obtiene el número total de usuarios en el conjunto.
     *
     * @return Número de usuarios en el conjunto.
     */
    public int getSize() {
        return usersList.size();
    }

    /**
     * Obtiene una lista con todos los nombres de usuario en el conjunto.
     *
     * @return Lista de nombres de usuario.
     */
    public List<String> getAllUsernames() {
        return new ArrayList<>(usersList.keySet());
    }

    /**
     * Verifica si el conjunto de usuarios está vacío.
     *
     * @return true si el conjunto está vacío, false en caso contrario.
     */
    public boolean isEmpty() {
        return usersList.isEmpty();
    }
}
