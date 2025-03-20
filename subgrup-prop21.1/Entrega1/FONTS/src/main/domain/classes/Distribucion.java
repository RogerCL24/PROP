package main.domain.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import main.domain.classes.types.Pair;
import java.util.HashMap;

/**
 * Clase que representa una Distribución, que contiene un conjunto de productos organizados según un algoritmo de estrategia.
 *
 * @author David Mas Escude (david.mas@estudiantat.upc.edu)
 */
public class Distribucion {
    // Atributos
    private Pair<Integer, Integer> Prestage;  // Par de enteros <altura, longitud>
    private List<String> Dist;                // Lista de productos (Strings)
    private String nom;
    private Estrategia estrategia;
    private Map<String, Integer> mapa;        // Mapa para gestionar las posiciones de los productos

    /**
     * Constructor que inicializa la Distribución con dimensiones, lista de productos y nombre.
     * @param prestage Par de dimensiones <altura, longitud>.
     * @param dist Lista de productos.
     * @param nom Nombre de la distribución.
     */
    public Distribucion(Pair<Integer, Integer> prestage, List<String> dist, String nom) {
        this.Prestage = prestage;
        this.Dist = dist;
        this.nom = nom;
    }

    /**
     * Constructor que inicializa la Distribución con dimensiones especificadas.
     * @param h Altura de la distribución.
     * @param w Longitud de la distribución.
     */
    public Distribucion(int h, int w) {
        this.Prestage = new Pair<>(h, w);
    }

    // Métodos Getters i Setters
    /**
     * Obtiene el nombre de la Distribución.
     * @return Nombre de la distribución.
     */
    public String getNom() {return nom;}

    /**
     * Establece el nombre de la Distribución.
     * @param nom Nuevo nombre de la distribución.
     */
    public void setNom(String nom) {this.nom = nom;}

    /**
     * Obtiene el nombre del algoritmo de estrategia utilizado en la distribución.
     * pre Ja s'ha establert l'algorisme seleccionat.
     * @return Nombre del algoritmo de estrategia.
     */
    public String getAlgoritme() {return estrategia.getClass().getSimpleName();}

    /**
     * Establece el algoritmo de estrategia de la Distribución.
     * @param estrategia Estrategia a utilizar en la distribución.
     */
    public void setAlgoritmo(Estrategia estrategia) {
        this.estrategia = estrategia;
    }

    /**
     * Obtiene la lista de productos de la Distribución.
     * @return Lista de productos.
     */
    public List<String> getDist() {
        return Dist;
    }

    /**
     * Obtiene las dimensiones de la Distribución.
     * @return Par de enteros representando <altura, longitud>.
     */
    public Pair<Integer, Integer> getPrestage() {return Prestage;}

    //Métodos de información

    /**
     * Comprueba si la distribución está vacía.
     * @return true si la distribución está vacía, false en caso contrario.
     */
    public boolean isEmpty() {
        return mapa.isEmpty();
    }

    /**
     * Comprueba si un producto está en la distribución.
     * @param name Nombre del producto a verificar.
     * @return true si el producto está en la distribución, false en caso contrario.
     */
    public boolean contieneProducto(String name) {
        return mapa.containsKey(name);
    }

    // Métodos de manipulación de la distribución
    /**
     * Genera la lista de productos en la distribución según el algoritmo de estrategia, y crea un mapa de posiciones.
     * @param LlistaProductes Mapa con los productos y sus similitudes.
     */
    public void generateDist(Map<String, Map<String, Float>> LlistaProductes) {
        if (LlistaProductes.size() == 1) {
            Dist = new ArrayList<>(LlistaProductes.keySet());
        } else {
            Dist = new ArrayList<>(estrategia.executeAlg(LlistaProductes));
        }

        // Crear el mapa de posiciones
        int numProductos = Dist.size();
        mapa = new HashMap<>();
        for (int i = 0; i < numProductos; i++) {
            mapa.put(Dist.get(i), i);
        }
    }


    /**
     * Intercambia las posiciones de dos productos en la distribución.
     * pre Los dos productos han de estar en la distribución.
     * @param nom1 Nombre del primer producto.
     * @param nom2 Nombre del segundo producto.
     */
    public void editarDist(String nom1, String nom2) {
        int pos1 = mapa.get(nom1);
        int pos2 = mapa.get(nom2);
        mapa.put(nom1, pos2);
        mapa.put(nom2, pos1);
        Dist.set(pos1, nom2);
        Dist.set(pos2, nom1);
    }

    /**
     * Elimina un producto de la distribución reemplazándolo por `null`.
     * pre La distribucion tiene que tener más de un producto.
     * @param nom Nombre del producto a eliminar.
     */
    public void eliminarProducte(String nom) {
        if (mapa.containsKey(nom)) {
            int pos = mapa.get(nom);
            Dist.set(pos, null);
            mapa.remove(nom);
        }
    }
}
