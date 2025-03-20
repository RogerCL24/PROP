package main.domain.controllers;

import main.domain.classes.*;
import main.domain.classes.algorism.AlgoritmoVoraz;
import main.domain.classes.algorism.Aproximation_Kruskal_ILS;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Controlador que gestiona las operaciones relacionadas con las distribuciones de productos.
 * Proporciona métodos para agregar, eliminar, editar, y exportar distribuciones,
 * así como para aplicar algoritmos de distribución.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 * @author David Mas Escude (david.mas@estudiantat.upc.edu)
 * @author David Sanz Martínez (david.sanz.martinez)
 */

public class distributionController {

    /**
     * Instancia del controlador de distribución.
     */
    private static distributionController instance;
    /**
     * Conjunto de distribuciones del controlador de distribución.
     */
    private CjtDistribuciones cjtD;

    /**
     * Constructor del controlador de distribuciones.
     * Es privado para implementar el patrón Singleton.
     */
    private distributionController() {}

    /**
     * Obtiene la instancia única del controlador de distribuciones.
     *
     * @return Instancia única de la clase distributionController.
     */
    public static distributionController getInstance() {
        if (instance == null) {
            instance = new distributionController();
        }
        return instance;
    }

    /**
     * Asigna un conjunto de distribuciones al controlador.
     *
     * @param cjtD Conjunto de distribuciones a asociar.
     */
    public void setCjtD(CjtDistribuciones cjtD) {
        this.cjtD = cjtD;
    }

    /**
     * Obtiene una distribución por su nombre.
     * @param nombre El nombre de la distribución.
     * @return La distribución correspondiente al nombre dado.
     */
    public Distribucion getDistribucion(String nombre) {
        return cjtD.obtenerDistribucion(nombre);
    }

    /**
     * Método que cambia el nombre de la distribución "nombreAnriguo" a "nombreNuevo" y retorna true. Si no es capaz de
     * realizar estos cambios o la distribución no existe devuelve false.
     * @param nombreAntiguo Nombre actual de la distribución que pasará a ser inválido después de llamar a este método
     *                      si todo sale bien.
     * @param nombreNuevo Nuevo nombre que pasará a tener la distribución.
     * @return true si se ha producido el cambio de nombre o false si no ha sido posible o no existe la distribución.
     */
    public boolean cambiarNombreDist(String nombreAntiguo, String nombreNuevo) {
        if (cjtD.contiene(nombreAntiguo)){
            if (cjtD.contiene(nombreNuevo)) return false;
            else { //Se produce el camio de nombre
                Distribucion distribucion = cjtD.obtenerDistribucion(nombreAntiguo);
                cjtD.eliminarDistribucionDelConjunto(nombreAntiguo);
                distribucion.setNom(nombreNuevo);
                cjtD.agregarDistribucion(nombreNuevo, distribucion);
                return true;
            }
        }
        else return false;

    }

    /**
     * Método para eliminar la distribución con nombre "nombre".
     * @param nombre Nombre que identifica a la distribución.
     * @return true si se ha eliminado la distribución o false si no se ha podido eliminar.
     */
    public boolean eliminarDistribucion(String nombre) {
        if (exists(nombre)) {
            cjtD.eliminarDistribucionDelConjunto(nombre);
            return true;
        }
        return false;
    }

    /**
     * Verifica si una distribución existe en el conjunto.
     * @param nombre El nombre de la distribución a verificar.
     * @return true si la distribución existe, false en caso contrario.
     */
    public boolean exists (String nombre) {
        return cjtD.contiene(nombre);
    }

    /**
     * Guarda una nueva distribución en el conjunto.
     * Si ya existe una distribución con el mismo nombre, no la guarda.
     * @param dist La distribución a guardar.
     * @param name El nombre de la distribución.
     * @return true si la distribución se guardó correctamente, false si ya existe.
     */
    public boolean guardarDistribucion(Distribucion dist, String name) {
        if (cjtD.contiene(name)) {
            System.out.println("Ya existe una distribucion con ese nombre.");
            return false;
        } else if (name.isEmpty()) {
            System.out.println("El nombre no puede estar vacio");
            return false;
        } else {
            dist.setNom(name);
            cjtD.agregarDistribucion(name, dist);
            System.out.println("Distribucion '" + name + "' agregada con exito.");
            return true;
        }
    }


    /**
     * Crea una nueva distribución con las dimensiones dadas.
     * @param h Altura de la distribución.
     * @param w Ancho de la distribución.
     * @return La nueva distribución creada.
     */
    public Distribucion createDistribution(int h, int w) {
        return new Distribucion(h,w);
    }


    /**
     * Permite editar la distribución cambiando la relación entre dos productos.
     * @param nombreDist El nombre de la distribución a editar.
     * @param prod1 El nombre del primer producto.
     * @param prod2 El nombre del segundo producto.
     */
    public void editarDistribucion(String nombreDist, String prod1, String prod2) {
        Distribucion d = getDistribucion(nombreDist);
        if (!d.isEmpty() && d.contieneProducto(prod1) && d.contieneProducto(prod2)) {
            d.editarDist(prod1, prod2);
            cjtD.agregarDistribucion(nombreDist, d);
        }
    }

    /**
     * Método que comprueba si la distribución con nombre "nombreDist" contiene el producto "prod".
     * @param nombreDist Nombre de la distribución.
     * @param prod Producto a comprobar si pertenece o no a la distribución.
     * @return true si el producto pertenece a la distribución o false si no pertenece.
     */
    public boolean contieneproducto(String nombreDist, String prod) {
        Distribucion d = getDistribucion(nombreDist);
        return d.contieneProducto(prod);
    }


    /**
     * Elimina una serie de productos de una distribución.
     * @param nombreDist El nombre de la distribución de la que se eliminarán los productos.
     * @param prods Array de productos que se intentaran eliminar de la distribución.
     */
    public void eliminarProducteDistribucion(String nombreDist, String[] prods) {
        Distribucion d = getDistribucion(nombreDist);
        for (String prod : prods) {
            d.eliminarProducte(prod);
            cjtD.agregarDistribucion(nombreDist, d);
        }
    }

    /**
     * Creación de una nueva distribución de productos en base a los parámetros introducidos.
     * @param algoritmo Nombre del algoritmo que se quiere usar para generar la distribución.
     * @param h Altura (número de estantes) que tendrá la estantería.
     * @param w Anchura (número de productos por estante) que tendrá la estantería.
     * @param LlistaGdS Mapa que contiene la lista de productos y sus características para la distribución.
     * @return Distribución generada con los parámetros introducidos.
     */
    public Distribucion generarDistribucion(String algoritmo, int h,int w,Map<String, Map<String, Float>> LlistaGdS) {
        Distribucion d = createDistribution(h, w);
        if (Objects.equals(algoritmo, "Voraz")) executeAlgorithm(d, "1", LlistaGdS);
        else executeAlgorithm(d, "2", LlistaGdS);
        return d;
    }

    /**
     * Método que obtiene el nombre de todas las distribuciones del conjunto de distribuciones.
     * @return Set de nombres de todas las distribuciones del conjunto de distribuciones
     */
    public Set<String> verDists() {
        if (!cjtD.isEmpty()) return cjtD.getNames();
        else return null;
    }


    /**
     * Ejecuta un algoritmo de distribución en una distribución dada.
     * El algoritmo se selecciona en función de la opción proporcionada.
     * @param distribution La distribución sobre la que se ejecutará el algoritmo.
     * @param option La opción seleccionada para el algoritmo.
     * @param LlistaProductes El mapa de productos para generar la distribución.
     */
    public void executeAlgorithm(Distribucion distribution, String option, Map<String, Map<String,Float>> LlistaProductes) {
        Estrategia estrategia;
        if (Objects.equals(option, "1")) {
            estrategia = new AlgoritmoVoraz();
        }
        else {
            estrategia = new Aproximation_Kruskal_ILS();

        }
        distribution.setAlgoritmo(estrategia);
        distribution.generateDist(LlistaProductes);
    }

}


