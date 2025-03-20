
package main.domain.classes;

import java.util.List;
import java.util.Map;

/**
 * Interfaz que define una estrategia para ejecutar algoritmos de distribución.
 * Las implementaciones de esta interfaz representan diferentes estrategias
 * que se pueden aplicar a una estructura de datos que modela un grafo.
 *
 * Esta interfaz se utiliza, por ejemplo, en el contexto de un controlador de distribución
 * para seleccionar y ejecutar algoritmos específicos como Algoritmo Voraz o Kruskal con ILS.
 *
 * @author David Mas Escude (david.mas@estudiantat.upc.edu)
 * @author David Sanz Martinez (david.sanz.martinez@estudiantat.upc.edu)
 */
public interface Estrategia {

    /**
     * Ejecuta un algoritmo sobre un grafo representado como un mapa anidado.
     * Las implementaciones de este método definen el comportamiento específico del algoritmo.
     *
     * @param G Grafo representado como un mapa anidado. La clave principal es un nodo del grafo,
     *          y su valor es otro mapa que asocia nodos vecinos con el peso de las aristas correspondientes.
     * @return Una lista de cadenas que representa el resultado de la ejecución del algoritmo.
     */
    List<String> executeAlg(Map<String, Map<String, Float>> G);
}

