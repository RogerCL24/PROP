package main.domain.controllers;

/**
 * Clase Singleton que asegura que solo haya una instancia de la clase en todo el sistema.
 *
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class Singleton {

    /**
     * Constructor privado para evitar la instanciación externa.
     */
    private Singleton() {}

    /**
     * Clase interna estática que contiene la instancia única de Singleton.
     * Es inicializada de manera perezosa (lazy initialization).
     */
    private static class SingletonHelper {
        /**
         *  Aquí se define una instancia única de la clase Singleton.
         */
        private static final Singleton INSTANCE = new Singleton();
    }

    /**
     * Método público para obtener la instancia única de Singleton.
     * @return La instancia única de Singleton.
     */
    public static Singleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
