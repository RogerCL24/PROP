package main.domain.controllers;

import main.domain.classes.LlistaProductes;
import main.domain.classes.CjtLlistesProductes;
import main.domain.classes.exceptions.formatException;

import java.util.*;

/**
 * Clase que gestiona un conjunto de listas de productos con sus respectivas
 * matrices de similitud.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */

public class listController {

    /**
     * Instancia del controlador de listas de productos.
     */
    private static listController instance;
    /**
     * Conjunto de listas de productos del controlador de listas de productos.
     */
    private CjtLlistesProductes cjt;


    /**
     * Constructor del controlador de listas.
     */
    private listController() {}

    /**
     * Devuelve la instancia única de listController, creándola si aún no existe.
     * Este método implementa el patrón Singleton, asegurando que solo haya una instancia de listController.
     *
     * @return La instancia única de listController.
     */
    public static listController getInstance() {
        if (instance == null) {
            instance = new listController();
        }
        return instance;
    }

    /**
     * Establece el conjunto de listas de productos (CjtLlistesProductes) que será gestionado por el controlador.
     *
     * @param cjt El conjunto de listas de productos a asignar.
     */
    public void setCjt(CjtLlistesProductes cjt) {
        this.cjt = cjt;
    }


    /**
     * Obtiene la lista de productos por su nombre.
     *
     * @param nombre El nombre de la lista.
     * @return La lista de productos correspondiente al nombre.
     */
    public LlistaProductes obtenerLista(String nombre) {
        return cjt.obtenerLista(nombre);
    }

    /**
     * Obtiene la matriz de similitudes de productos de una lista dada.
     *
     * @param nombre El nombre de la lista.
     * @return La matriz de similitudes de productos.
     */
    public Map<String, Map<String, Float>> getSimilarityMatrix(String nombre){return obtenerLista(nombre).getSimilarityMatrix();}

    /**
     * Verifica si una lista con el nombre proporcionado existe en el conjunto de listas.
     *
     * @param name El nombre de la lista a buscar.
     * @return true si la lista existe, false en caso contrario.
     */
    public boolean exists (String name) {
        return cjt.contiene(name);
    }

    /**
     * Permite introducir una nueva lista de productos.
     * @param productos Lista con los productos que tendrá la nueva lista.
     * @param relaciones Grados de similitud entre los productos que tendrá la lista.
     * @return LlistaProductes creada a partir de los parámetros introducidos.
     */
    public LlistaProductes introducirNuevaLista(List<String> productos, List<String[]> relaciones) {
        LlistaProductes LP = new LlistaProductes();

        introducirProductos(productos, LP);
        try {
            if (relaciones != null) introducirSimilitudes(LP, relaciones);
        } catch (formatException e) {
            System.err.println(e.getMessage());
        }
        return LP;
    }


    /**
     * Introduce productos a una lista desde la entrada estándar.
     *      * El usuario ingresa productos que son añadidos a la lista.
     *      * Dependiendo del modo (`mod`), los productos se insertan con una validación adicional.
     * @param productos Productos a introducir en la lista.
     * @param LP LlistaProductes a modificar.
     * @return Retorna `true` si los productos fueron introducidos correctamente.
     *        En el modo 1, retorna `false` si solo se introduce un producto (lo cual no es válido en ese modo).
     */
    public List<String> introducirProductos(List<String> productos, LlistaProductes LP) {
        List<String> insertados = new ArrayList<>();
        for (String producto : productos) {
            if (LP.insertarProducte(producto)) insertados.add(producto);
        }
        return insertados;
    }


    /**
     * Introduce las similitudes entre productos en la lista.
     * @param lista lista La lista de productos.
     * @param relaciones Grados de similitud entre los productos de la lista.
     * @throws formatException Excepción lanzada si algo sale mal.
     */
    public void introducirSimilitudes(LlistaProductes lista, List<String[]> relaciones) throws formatException {
        for (String[] relacion : relaciones) {
                String prod1 = relacion[0];
                String prod2 = relacion[1];
                float GdS = Float.parseFloat(relacion[2]);
                lista.setSimilarity(prod1, prod2, GdS);
        }
    }


    /**
     * Modifica els graus de similitud entre parelles de productes en una llista.
     *
     * @param lista La llista de productes a modificar.
     * @param relaciones Una llista de String[] on cada array conté {producte1, producte2, nouGdS}.
     * @return Una llista de String[] amb les modificacions realitzades: {producte1, producte2, GdS antic, GdS nou}.
     */
    public List<String[]> modificarMultiplesSimilitudes(LlistaProductes lista, List<String[]> relaciones) {
        List<String[]> modificadas = new ArrayList<>();

        for (String[] relacion : relaciones) {
            String prod1 = relacion[0];
            String prod2 = relacion[1];
            float nuevoGdS;
            nuevoGdS = Float.parseFloat(relacion[2]);

            Float gdsAntiguo = lista.getSimilarity(prod1, prod2);
            lista.setSimilarity(prod1, prod2, nuevoGdS);
            modificadas.add(new String[]{prod1, prod2, gdsAntiguo == null ? "null" : String.valueOf(gdsAntiguo), String.valueOf(nuevoGdS)});
        }

        return modificadas;
    }


    /**
     * Muestra todas las listas de productos guardadas.
     *
     * @return true si hay listas, false si no hay listas guardadas.
     */
    public Set<String> verListas(){
        if (!cjt.isEmpty()) return cjt.getListas();
        return null;
    }


    /**
     * Guarda o modifica una lista de productos.
     *
     * @param name El nombre de la lista.
     * @param lista La lista de productos.
     * @return true si la operación se realiza correctamente, false en caso contrario.
     */
    public boolean guardarLista(String name, LlistaProductes lista) {
        if (cjt.contiene(name)) return false;
        else {
            lista.setNom(name);
            cjt.agregarLista(name, lista);
            System.out.println("Lista '" + name + "' agregada con exito.");
            return true;
        }
    }


    /**
     * Elimina productos de una lista de productos.
     * @param productos Productos a eliminar de la lista.
     * @param lista Nombre de la lista de productos de la que se quieren eliminar los productos.
     * @return Productos que se han conseguido eliminar, si no se ha conseguido eliminar ninguno se devuelve vacío.
     */
    public List<String> eliminarProductosDeLista(List<String> productos, LlistaProductes lista) {
        List<String> eliminados = new ArrayList<>();

        for (String producto : productos) {
            if (lista.getSimilarityMatrix().containsKey(producto)) {
                lista.eliminarProducte(producto);
                eliminados.add(producto);
            }
        }
        return eliminados;
    }



    /**
     *  Cambia el nombre de una lista existente.
     *
     * @param nombreAntiguo El nombre actual de la lista.
     * @param nombreNuevo El nuevo nombre para la lista.
     * @return Devuelve true si se ha podido cambiar el nombre o false si no se ha podido cambiar.
     */
    public boolean cambiarNombreLista(String nombreAntiguo, String nombreNuevo) {
        if (cjt.contiene(nombreAntiguo)){
            if (cjt.contiene(nombreNuevo)) return false;
            else {
                LlistaProductes lista = cjt.obtenerLista(nombreAntiguo);
                cjt.eliminarListaDelConjunto(nombreAntiguo);
                lista.setNom(nombreNuevo);
                cjt.agregarLista(nombreNuevo, lista);
                return true;
            }
        }
        else return false;

    }

    /**
     * Elimina una lista del conjunto, pidiendo confirmación al usuario.
     *
     * @param nombre El nombre de la lista a eliminar.
     * @return true si la lista eliminada era la última, false si no lo era.
     */
    public boolean eliminarLista(String nombre) {
        if (exists(nombre)) {
            cjt.eliminarListaDelConjunto(nombre);
            return true;
        }
        return false;
    }
}
