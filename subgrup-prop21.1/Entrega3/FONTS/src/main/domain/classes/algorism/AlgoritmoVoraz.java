package main.domain.classes.algorism;

import main.domain.classes.Estrategia;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase que implementa el algoritmo Voraz (backtracking).
 * Permite encontrar la combinacion correcta ya que prueba todas las combinaciones aunque no sin un gran coste,
 * el coste seria de O(n!) (en realidad O(n-1!)), siendo bastante lento para matrices de más de 10 productos.
 * En este caso nos consigue dar el ciclo hamiltoniano correcto aunque siempre empezando por el primer producto.
 *
 * @author David Sanz Martínez (david.sanz.martinez)
 */
public class AlgoritmoVoraz implements Estrategia {

    /**
     * Mejor similitud encontrada.
     */
    private static float best_sim;
    /**
     * Lista que contendrá todos los productos ordenados una vez acabada la ejecución del backtracking.
     */
    private static List<String> lordenada;
    /**
     * Matriz con los productos y sus grados de similitud entre ellos.
     */
    private static Map<String, Map<String,Float>> Matrix;
    /**
     * Lista que guarda los productos que han sido visitados con true y los que no a false según su posición en Matrix.
     */
    private static List<Boolean> visitados;


    /**
     * Método recursivo que prueba todas las combinaciones con el primer producto siempre al principio y se que con
     * la que tiene una suma mayor.
     * @param lista Lista de Strings con la combinación actual de productos que en el caso base se mirara si es mejor que la mejor suma
     *              encontrada.
     * @param products Lista de Strings con los productos guardados en el orden que estaban en la matriz
     * @param n Entero que indica cuantos productos llevamos puestos en la lista, seria el nivel de profundidad en el arbol que generamos.
     */
    public static void backtracking( List<String> lista, List<String> products, int n)  {
        //caso base de la recursion calcula cual seria la suma de los grados de similtud del ciclo conseguido
        // y comprueba si es mejor que la mejor suma actual. En este caso la sustituiria y guardariamos el ciclo actual
        // por si fuese el mejor.
        if (n == products.size()-1) {
            for (int i = 0; i < products.size(); i++) { if (!visitados.get(i)) { lista.set(n, products.get(i)); }}
            float x = calcular_sim(lista,n);
            if (best_sim < x) {
                best_sim = x;
                lordenada = List.copyOf(lista);
            }
        }
        //Caso recursivo en el que queda más de un producto sin visitar, se exploran todos los caminos
        //posibles para los productos no visitados que quedan. O sea, si quedan x productos se hace
        //una llamada a esta funcion para cada uno de estos colocandolo como el siguiente producto en la lista.
        else {
            for (int i = 1; i < products.size(); i++) {
                if (!visitados.get(i)) {
                    lista.set(n, products.get(i));
                    visitados.set(i, true);
                    backtracking(lista, products, n + 1);
                    visitados.set(i, false);
                }
            }
        }
    }

    /**
     * Método que calcula la suma de todos los grados de similitud en una cierta lista de productos, calcula la de los productos
     * adyacentes en la lista y la del primero con el ultimo.
     * @param lista Lista de Strings donde aparecen los productos en un orden concreto.
     * @param n Entero, tamaño de la lista.
     * @return Suma de las similitudes entre productos.
     */
    public static float calcular_sim(List<String> lista, int n) {
        float sum = 0.0f;
        for (int j = 0; j < n; j++) {
            sum += Matrix.get(lista.get(j)).getOrDefault(lista.get(j+1),0.0f);
        }
        sum+= Matrix.get(lista.getFirst()).getOrDefault(lista.getLast(),0.0f);
        return sum;
    }

    /**
     * Método principal que ejecuta el algoritmo Voraz.
     * Hace las inicializaciones que correspondan, la llamada a la funcion recursiva para el cálculo del ciclo hamiltoniano,
     * y devuelve este mismo en forma de lista.
     * pre Las Matrices son como mínimo de tamaño 2.
     * @param G Grafo representado como un mapa de adyacencia con pesos flotantes.
     * @return Lista con los nodos del ciclo Hamiltoniano generado.
     */
    public List<String> executeAlg(Map<String, Map<String, Float>> G) {
        //Inicializamos todos los atributos y variables necesarias
        Matrix = G;
        List<String> products = G.keySet().stream().toList();
        lordenada = products;
        best_sim = 0.0f;
        List<String> nueva_lista = new ArrayList<>();
        visitados =  new ArrayList<>();
        for (int i = 0; i < lordenada.size(); i++) {
            nueva_lista.add(".");
            visitados.add(false);
        }

        //Colocamos el primer producto al principio, lo marcamos como visitado y llamamos a la funcion backtracking
        nueva_lista.set(0, products.get(0));
        visitados.set(0, true);
        backtracking(nueva_lista,products,1);
        visitados.set(0, false);


        return lordenada;
    }
}
