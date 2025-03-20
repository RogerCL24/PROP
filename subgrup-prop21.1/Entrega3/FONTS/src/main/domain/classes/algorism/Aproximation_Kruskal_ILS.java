package main.domain.classes.algorism;

import main.domain.classes.Estrategia;

import java.util.*;

/**
 * Clase que implementa el algoritmo de Aproximación Kruskal con Iterated Local Search (ILS).
 * Permite encontrar soluciones aproximadas a problemas de rutas minimizando costes, generando grafos dirigidos
 * y ciclos hamiltonianos a partir de árboles de expansión mínima.
 *
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */

public class Aproximation_Kruskal_ILS implements Estrategia {

    /**
     * Clase interna que representa una arista con dos nodos y un peso.
     */
    public static class Edge implements Comparable<Edge> {
        /**
         * Producto 1 de la relación.
         */
        String product1;
        /**
         * Producto 2 de la relación.
         */
        String product2;
        /**
         * Grado de similitud entre el product1 y el product2.
         */
        float weight;

        /**
         * Constructor de la clase Edge.
         * @param product1 Primer producto.
         * @param product2 Segundo producto.
         * @param weight Peso de la arista.
         */
        public Edge(String product1, String product2, float weight) {
            this.product1 = product1;
            this.product2 = product2;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge o) {
            return Float.compare(o.weight, this.weight);
        }
    }

    /**
     * Estructura para gestionar conjuntos disjuntos mediante unión y búsqueda.
     */
    static class MFS {

        /**
         * Es un mapa que rastrea el padre de cada elemento en el conjunto.
         */
        private Map<String, String> parent = new HashMap<>();

        /**
         * Es un mapa que almacena un número asociado a cada elemento, llamado rango. El rango refleja una aproximación de la altura del árbol que representa el conjunto.
         */
        private Map<String, Integer> rank = new HashMap<>();

        /**
         * Constructor de la clase MFS.
         * Inicializa los conjuntos con los productos proporcionados.
         * @param products Conjunto de productos.
         */
        public MFS(Set<String> products) {
            for (String product : products) {
                parent.put(product, product);
                rank.put(product, 0);
            }
        }

        /**
         * Encuentra la raíz del conjunto al que pertenece un producto.
         * @param product Producto del que se busca la raíz.
         * @return La raíz del conjunto.
         */
        public String find(String product) {
            if (!parent.get(product).equals(product)) {
                parent.put(product, find(parent.get(product)));
            }
            return parent.get(product);
        }

        /**
         * Une dos conjuntos en base a sus raíces.
         * @param product1 Producto del primer conjunto.
         * @param product2 Producto del segundo conjunto.
         */
        public void union(String product1, String product2) {
            String root1 = find(product1);
            String root2 = find(product2);

            if (!root1.equals(root2)) {
                int rank1 = rank.get(root1);
                int rank2 = rank.get(root2);

                if (rank1 > rank2) {
                    parent.put(root2, root1);
                } else if (rank1 < rank2) {
                    parent.put(root1, root2);
                } else {
                    parent.put(root2, root1);
                    rank.put(root1, rank1 + 1);
                }
            }
        }
    }

    /**
     * Aplica el algoritmo de Kruskal para obtener un árbol de expansión mínima.
     * @param G Grafo representado como un mapa de adyacencia.
     * @return Lista de aristas del MST.
     */
    public static List<Edge> kruskal(Map<String, Map<String, Float>> G) {
        List<Edge> edges = new ArrayList<>();
        Set<String> products = G.keySet();

        for (String product1 : G.keySet()) {
            for (String product2 : G.keySet()) {
                edges.add(new Edge(product1, product2, G.get(product1).getOrDefault(product2, 0.0f)));
            }
        }

        Collections.sort(edges);

        MFS mfs = new MFS(products);
        List<Edge> mst = new ArrayList<>();

        for (Edge edge : edges) {
            if (!mfs.find(edge.product1).equals(mfs.find(edge.product2))) {
                mfs.union(edge.product1, edge.product2);
                mst.add(edge);
            }
        }
        return mst;
    }

    /**
     * Calcula el coste de una solución en base al peso de las aristas.
     * @param S Lista de aristas de la solución.
     * @return Coste total.
     */
    public static float cost(List<Edge> S) {
        float totalCost = 0;
        for (Edge e : S) {
            totalCost += 1 - e.weight;
        }
        return totalCost;
    }

    /**
     * Perturba una solución intercambiando dos aristas aleatorias.
     * @param S Lista de aristas de la solución.
     * @return Nueva solución perturbada.
     */
    private static List<Edge> perturb(List<Edge> S) {
        Random r = new Random();
        List<Edge> perturbedS = new ArrayList<>(S);

        int i = r.nextInt(S.size());
        int j = r.nextInt(S.size());
        Collections.swap(perturbedS, i, j);

        return perturbedS;
    }

    /**
     * Redondea un valor decimal a dos cifras.
     * @param valor Valor a redondear.
     * @return Valor redondeado.
     */
    public static float redondearDecimal(float valor) {
        return Math.round(valor * 100.0f) / 100.0f;
    }

    /**
     * Aplica el algoritmo 2-opt para optimizar una solución.
     * @param S Lista de aristas de la solución inicial.
     * @return Solución optimizada.
     */
    public static List<Edge> twoOpt(List<Edge> S) {
        List<Edge> bestS = new ArrayList<>(S);
        boolean improved = true;

        while (improved) {
            improved = false;
            for (int i = 0; i < S.size() - 1; i++) {
                for (int j = i + 1; j < S.size(); j++) {
                    List<Edge> newS = new ArrayList<>(bestS);
                    Collections.reverse(newS.subList(i, j + 1));
                    if (redondearDecimal(cost(newS)) < redondearDecimal(cost(bestS))) {
                        bestS = new ArrayList<>(newS);
                        improved = true;
                    }
                }
            }
        }
        return bestS;
    }

    /**
     * Aplica el algoritmo de búsqueda local iterativa para mejorar una solución inicial.
     * @param mst Árbol de expansión mínima inicial.
     * @return Solución optimizada.
     */
    public static List<Edge> iteratedLocalSearch(List<Edge> mst) {
        List<Edge> currentS = new ArrayList<>(mst);
        List<Edge> bestS = new ArrayList<>(mst);
        int maxIt = 100;

        for (int i = 0; i < maxIt; i++) {
            currentS = twoOpt(currentS);
            if (cost(currentS) < cost(bestS)) {
                bestS = new ArrayList<>(currentS);
            }
            currentS = perturb(bestS);
        }
        return bestS;
    }

    /**
     * Genera un grafo dirigido a partir de un árbol de expansión mínima.
     * @param mst Lista de aristas del MST.
     * @return Grafo dirigido.
     */
    public static Map<String, List<String>> generarGrafoDirigido(List<Edge> mst) {
        Map<String, List<String>> grafoDirigido = new HashMap<>();

        for (Edge e : mst) {
            grafoDirigido.putIfAbsent(e.product1, new ArrayList<>());
            grafoDirigido.putIfAbsent(e.product2, new ArrayList<>());
            grafoDirigido.get(e.product1).add(e.product2);
            grafoDirigido.get(e.product2).add(e.product1);
        }

        return grafoDirigido;
    }

    /**
     * Realiza un recorrido DFS para generar un ciclo Euleriano a partir del nodo inicial.
     * @param nodo Nodo actual en el recorrido.
     * @param grafoDirigido Grafo dirigido representado como un mapa de adyacencia.
     * @param similitudes Mapa de similitudes entre los productos.
     * @param ciclo Lista donde se almacena el ciclo generado.
     * @param visitados Conjunto de nodos ya visitados.
     */
    public static void dfsEuler(String nodo, Map<String, List<String>> grafoDirigido,
                                Map<String, Map<String, Float>> similitudes, List<String> ciclo,
                                Set<String> visitados) {
        visitados.add(nodo);
        ciclo.add(nodo);

        List<String> conexiones = grafoDirigido.get(nodo);
        conexiones.sort((n1, n2) -> {
            float sim1 = similitudes.getOrDefault(nodo, Collections.emptyMap()).getOrDefault(n1, 0.0f);
            float sim2 = similitudes.getOrDefault(nodo, Collections.emptyMap()).getOrDefault(n2, 0.0f);
            return Float.compare(sim2, sim1);
        });

        for (String vecino : conexiones) {
            if (!visitados.contains(vecino)) {
                dfsEuler(vecino, grafoDirigido, similitudes, ciclo, visitados);
            }
        }
    }

    /**
     * Genera un ciclo Euleriano a partir de un grafo dirigido y un nodo inicial.
     * @param start Nodo inicial del ciclo.
     * @param grafoDirigido Grafo dirigido representado como un mapa de adyacencia.
     * @param similitudes Mapa de similitudes entre los productos.
     * @return Lista con los nodos del ciclo Euleriano.
     */
    public static List<String> generarCicloEuler(String start, Map<String, List<String>> grafoDirigido,
                                                 Map<String, Map<String, Float>> similitudes) {
        List<String> ciclo = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        dfsEuler(start, grafoDirigido, similitudes, ciclo, visitados);
        return ciclo;
    }

    /**
     * Transforma un ciclo Euleriano en un ciclo Hamiltoniano eliminando nodos repetidos.
     * @param cicloEuler Lista con los nodos del ciclo Euleriano.
     * @return Lista con los nodos del ciclo Hamiltoniano.
     */
    public static List<String> tranformarCicloHamiltoniano(List<String> cicloEuler) {
        List<String> cicloHamiltoniano = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (String node : cicloEuler) {
            if (!visited.contains(node)) {
                cicloHamiltoniano.add(node);
                visited.add(node);
            }
        }
        return cicloHamiltoniano;
    }

    /**
     * Busca una hoja en el grafo dirigido (un nodo con un solo vecino).
     * Si no encuentra una hoja, devuelve el primer nodo del mapa.
     * @param grafoDirigido Grafo dirigido representado como un mapa de adyacencia.
     * @return Nodo correspondiente a una hoja, o cualquier nodo del mapa si no hay hojas.
     */
    private static String searchForALeaf(Map<String, List<String>> grafoDirigido) {
        for (String product : grafoDirigido.keySet()) {
            if (grafoDirigido.get(product).size() == 1) {
                return product;
            }
        }
        return grafoDirigido.keySet().iterator().next();
    }

    /**
     * Método principal que ejecuta el algoritmo Aproximation_Kruskal_ILS.
     * Optimiza el grafo proporcionado y devuelve un ciclo Hamiltoniano.
     * @param G Grafo representado como un mapa de adyacencia con pesos flotantes.
     * @return Lista con los nodos del ciclo Hamiltoniano generado.
     */
    public List<String> executeAlg(Map<String, Map<String, Float>> G) {
        // Paso 1: Ejecutamos Kruskal y obtenemos el MST
        List<Edge> mst = kruskal(G);

        // Paso 2: Aplicamos ILS para optimizar el MST
        List<Edge> mstOpt = iteratedLocalSearch(mst);

        // Paso 3: Creamos el grafo dirigido a partir del MST optimizado
        Map<String, List<String>> grafoDirigido = generarGrafoDirigido(mstOpt);

        // Paso 4: Obtenemos el ciclo Euleriano priorizando las conexiones fuertes
        String start = searchForALeaf(grafoDirigido);
        List<String> cicloEuleriano = generarCicloEuler(start, grafoDirigido, G);

        // Paso 5: Transformamos el ciclo Euleriano en un ciclo Hamiltoniano
        return tranformarCicloHamiltoniano(cicloEuleriano);
    }
}
