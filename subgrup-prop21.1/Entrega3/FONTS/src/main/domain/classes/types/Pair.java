package main.domain.classes.types;

/**
 * Clase genérica que representa un par de elementos (una tupla de dos elementos).
 * La clase permite almacenar dos objetos de tipos diferentes y acceder a ellos de manera sencilla.
 *
 * @param <U> El tipo del primer elemento del par.
 * @param <V> El tipo del segundo elemento del par.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */
public class Pair<U, V> {
    /**
     * El primer elemento del par.
     */
    private U first;
    /**
     * El segundo elemento del par.
     */
    private V second;

    /**
     * Constructor para crear un nuevo par con los elementos proporcionados.
     * @param first El primer elemento del par.
     * @param second El segundo elemento del par.
     */
    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Obtiene el primer elemento del par.
     * @return El primer elemento del par.
     */
    public U getFirst() {
        return first;
    }

    /**
     * Obtiene el segundo elemento del par.
     * @return El segundo elemento del par.
     */
    public V getSecond() {
        return second;
    }

    /**
     * Establece el primer elemento del par.
     * @param first El nuevo valor para el primer elemento del par.
     */
    public void setFirst(U first) {
        this.first = first;
    }

    /**
     * Establece el segundo elemento del par.
     * @param second El nuevo valor para el segundo elemento del par.
     */
    public void setSecond(V second) {
        this.second = second;
    }
}
