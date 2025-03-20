package main.domain.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Clase que representa un conjunto de distribuciones.
 * Permite gestionar la colección de distribuciones, realizar operaciones como añadir, eliminar, consultar, e imprimir.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */
public class CjtDistribuciones {
    private Map<String, Distribucion> conjuntoDistribuciones;  // Mapa que asocia nombres de distribuciones con instancias de Distribucion

    /**
     * Constructor que inicializa un conjunto vacío de distribuciones.
     */
    public CjtDistribuciones() {
        conjuntoDistribuciones = new HashMap<>();
    }

    /**
     * Obtiene la cantidad total de distribuciones en el conjunto.
     *
     * @return Número total de distribuciones almacenadas en el conjunto.
     */
    public int getSize() {return conjuntoDistribuciones.size();}

    /**
     * Obtiene los nombres de todas las distribuciones almacenadas en el conjunto.
     *
     * @return Conjunto de cadenas que representan los nombres de las distribuciones.
     */
    public Set<String> getNames() {
        return conjuntoDistribuciones.keySet();
    }

    /**
     * Verifica si existe una distribución con el nombre dado en el conjunto.
     *
     * @param name Nombre de la distribución a verificar.
     * @return true si la distribución existe, false en caso contrario.
     */
    public boolean contiene(String name) {
        return conjuntoDistribuciones.containsKey(name);
    }


    /**
     * Verifica si el conjunto de distribuciones está vacío.
     *
     * @return true si el conjunto está vacío, false en caso contrario.
     */
    public boolean isEmpty() {
        return conjuntoDistribuciones.isEmpty();
    }

    /**
     * Agrega una nueva distribución al conjunto.
     *
     * @param name Nombre de la distribución.
     * @param distribucion Objeto Distribucion que representa la distribución a añadir.
     */
    public void agregarDistribucion(String name, Distribucion distribucion) {conjuntoDistribuciones.put(name, distribucion);}

    /**
     * Obtiene la distribución con el nombre dado.
     *
     * @param nombre Nombre de la distribución.
     * @return Objeto Distribucion correspondiente al nombre, o null si no existe.
     */
    public Distribucion obtenerDistribucion(String nombre) {
        return conjuntoDistribuciones.get(nombre);
    }

    /**
     * Elimina una distribución del conjunto.
     *
     * @param nombre Nombre de la distribución a eliminar.
     */
    public void eliminarDistribucionDelConjunto(String nombre) {
        conjuntoDistribuciones.remove(nombre);
    }

}
