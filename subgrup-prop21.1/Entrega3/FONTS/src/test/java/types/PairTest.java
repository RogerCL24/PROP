package types;

import main.domain.classes.types.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase de pruebas para Pair.java
 */
public class PairTest {

    /**
     * Prueba del constructor de Pair
     */
    @Test
    public void testConstructor() {
        Pair<String, Double> pair = new Pair<>("Manzana", 2.5);
        assertEquals("Manzana", pair.getFirst());
        assertEquals((Double) 2.5, pair.getSecond());
    }

    /**
     * Prueba del metodo setFirst de Pair
     */
    @Test
    public void testSetFirst() {
        Pair<String, Double> pair = new Pair<>("Manzana", 2.5);
        pair.setFirst("Pera");
        assertEquals("Pera", pair.getFirst());
    }

    /**
     * Prueba del metodo setSecond de Pair
     */
    @Test
    public void testSetSecond() {
        Pair<String, Double> pair = new Pair<>("Manzana", 2.5);
        pair.setSecond(3.0);
        assertEquals((Double) 3.0, pair.getSecond());
    }

    /**
     * Prueba del metodo getFirst de Pair
     */
    @Test
    public void testGetFirst() {
        Pair<String, Double> pair = new Pair<>("Manzana", 2.5);
        assertEquals("Manzana", pair.getFirst());
    }

    /**
     * Prueba del metodo getSecond de Pair
     */
    @Test
    public void testGetSecond() {
        Pair<String, Double> pair = new Pair<>("Manzana", 2.5);
        assertEquals((Double) 2.5, pair.getSecond());
    }
}
