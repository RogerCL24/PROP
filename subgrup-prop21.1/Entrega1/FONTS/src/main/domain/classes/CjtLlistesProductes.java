package main.domain.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Clase que representa un conjunto de listas de productos.
 * Permite gestionar la colección de listas de productos y realizar operaciones como añadir, eliminar y consultar listas.
 * Esta clase tiene una única instancia.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */
public class CjtLlistesProductes {
    private Map<String, LlistaProductes> conjuntoListas;  // Mapa que asocia nombres de listas con instancias de LlistaProductes

    /**
     * Constructor que inicializa un conjunto vacío de listas de productos.
     */
    public CjtLlistesProductes() {
        conjuntoListas = new HashMap<>();
    }

    /**
     * Obtiene el tamaño del conjunto de listas de productos.
     *
     * @return Número de listas en el conjunto.
     */
    public int getSize() {
        return conjuntoListas.size();
    }

    /**
     * Verifica si existe una lista de productos con el nombre dado en el conjunto.
     *
     * @param name Nombre de la lista a verificar.
     * @return true si la lista existe, false en caso contrario.
     */
    public boolean contiene(String name) {return conjuntoListas.containsKey(name);}

    /**
     * Verifica si el conjunto de listas de productos está vacío.
     *
     * @return true si el conjunto está vacío, false en caso contrario.
     */
    public boolean isEmpty() {
        return conjuntoListas.isEmpty();
    }

    /**
     * Agrega una nueva lista de productos al conjunto.
     *
     * @param name Nombre de la lista.
     * @param lista Objeto LlistaProductes que representa la lista de productos.
     */
    public void agregarLista(String name, LlistaProductes lista) {
        conjuntoListas.put(name, lista);
    }

    /**
     * Obtiene el conjunto de nombres de las listas de productos.
     *
     * @return Conjunto de nombres de las listas.
     */
    public Set<String> getListas() {
        return conjuntoListas.keySet();
    }

    /**
     * Obtiene la lista de productos con el nombre dado.
     *
     * @param nombre Nombre de la lista de productos.
     * @return Objeto LlistaProductes correspondiente al nombre, o null si no existe.
     */
    public LlistaProductes obtenerLista(String nombre) {return conjuntoListas.get(nombre);}

    /**
     * Elimina una lista de productos del conjunto.
     *
     * @param nombre Nombre de la lista a eliminar.
     */
    public void eliminarListaDelConjunto(String nombre) {
        conjuntoListas.remove(nombre);
    }
}
