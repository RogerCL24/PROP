
package main.domain.classes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Clase que representa una lista de productos con una matriz de similitud entre ellos.
 *
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class LlistaProductes {

    /**
     * Nombre de la lista de productos. Hace de clave primaria de la clase, identifica a las listas de productos.
     */
    private String name;
    /**
     * Mapa de grados de similitud entre los productos. También sirve para guardar los productos de la lista.
     */
    private Map<String, Map<String, Float>> similarityMatrix;

    //Constructores

    /**
     * Constructor por defecto. Inicializa la lista sin nombre.
     */
    public LlistaProductes() {
        name = "Sin_nombre";
        similarityMatrix = new HashMap<>();
    }

    /**
     * Constructor de LlistaProductes que crea una lista con la matriz de similitud introducida, se le asigna el
     * nombre "Sin_nombre".
     * @param similarityMatrix Matriz de grados de similitud entre productos que tendrá la lista.
     */
    public LlistaProductes( Map<String, Map<String, Float>> similarityMatrix) {
        name = "Sin_nombre";
        this.similarityMatrix = similarityMatrix;
    }

    /**
     * Constructor de LlistaProductes que crea una lista con la matriz de similitud introducida y el nombre itnroducido.
     * @param name Nombre que tendrá la lista de productos.
     * @param similarityMatrix Matriz de grados de similitud entre productos que tendrá la lista.
     */
    public LlistaProductes(String name, Map<String, Map<String, Float>> similarityMatrix) {
        this.name = name;
        this.similarityMatrix = similarityMatrix;
    }


    //Getters i setters

    /**
     * Obtiene el conjunto de productos en la lista.
     * @return Conjunto de nombres de productos.
     */
    public Set<String> getList() {
        return similarityMatrix.keySet();
    }

    /**
     * Obtiene la matriz de similitud entre productos.
     * @return Mapa de productos con sus grados de similitud.
     */
    public Map<String, Map<String, Float>> getSimilarityMatrix() {
        return similarityMatrix;
    }

    /**
     * Obtiene el nombre de la lista de productos.
     * @return Nombre de la lista.
     */
    public String getName() {
        return name;
    }

    /**
     * Asigna un nombre a la lista.
     * @param n Nombre de la lista.
     */
    public void setNom(String n) {
        name = n;
    }


    //Métodos de información

    /**
     * Verifica si un producto existe en la lista.
     * @param product Nombre del producto.
     * @return true si el producto existe, false en caso contrario.
     */
    public boolean productExists(String product) {
        return similarityMatrix.containsKey(product);
    }

    /**
     * Obtiene la similitud entre dos productos.
     * @param prod1 Nombre del primer producto.
     * @param prod2 Nombre del segundo producto.
     * @return Grado de similitud entre los productos, o null si alguno no existe.
     */
    public Float getSimilarity(String prod1, String prod2) {
        // Si no existen devolvemos null
        if (similarityMatrix.get(prod1) != null && similarityMatrix.get(prod2) != null) {
            // Si encuentra el valor entre prod1 y prod2 devuelve el float, de lo contrario devuelve el valor
            // por default new HashMap(), que esta vacio pero asi no obtenemos NullPointerException, si no existe
            // prod2 dentro de prod1 significa que no se ha definido un GdS, devolvemos 0
            return similarityMatrix.getOrDefault(prod1, new HashMap<>()).getOrDefault(prod2,0.0f);
        }
        return null;
    }

    //Métodos de manipulación de la lista

    /**
     * Inserta un producto en la lista si no está registrado.
     * @param product Nombre del producto a insertar.
     * @return true si el producto fue insertado, false si ya existía.
     */
    public boolean insertarProducte(String product) {
        // Si esta registrado no hace nada, de lo contrario:
        if (!similarityMatrix.containsKey(product)) {
            // Como es nuevo no tiene ningun producto asociado a el, creamos new HashMap()
            // para añadir sus proximos GdS con otros productos (sus indices)
            similarityMatrix.put(product, new HashMap<>());
            return true;
        }
        return false;
    }

    /**
     * Elimina un producto de la lista y sus asociaciones de similitud.
     * @param product Nombre del producto a eliminar.
     */
    public void eliminarProducte(String product) {
        if (similarityMatrix.remove(product) != null) {
            for (Map<String, Float> row : similarityMatrix.values()) {
                row.remove(product);
            }
        }
    }

    /**
     * Establece la similitud entre dos productos.
     * @param prod1 Nombre del primer producto.
     * @param prod2 Nombre del segundo producto.
     * @param GdS Grado de similitud entre los productos.
     */
    public void setSimilarity(String prod1, String prod2, float GdS) {
        if (similarityMatrix.get(prod1) != null && similarityMatrix.get(prod2) != null) {
            // Añadimos al producto1 (index1) un new HashMap() SOLO SI no estaba registrado en la similarityMatrix()
            // anteriormente y añadimos el producto2 (index2) con su GdS, de lo contrario no se crea nada y
            // solo añadimos el producto2 (index2) con su GdS y lo mismo de index2 a index1
            similarityMatrix.computeIfAbsent(prod1, k -> new HashMap<>()).put(prod2, GdS);
            similarityMatrix.computeIfAbsent(prod2, k -> new HashMap<>()).put(prod1, GdS);
        }
    }

    /**
     * Establece la matriz de grados de similitud entre los productos de la lista, por tanto también establece los
     * productos de la lista.
     * @param similarityMatrix Matriz de grados de similitud entre productos que tendrá la lista.
     */
    public void setSimilarityMatrix(Map<String, Map<String, Float>> similarityMatrix) {
        this.similarityMatrix = similarityMatrix;
    }
}
