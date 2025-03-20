import main.domain.classes.LlistaProductes;
import org.junit.Test;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test para probar la clase LListaProductes.java
 *
 * @author David Sanz Mdrtinez (david.sanz.martinez)
 */
public class LlistaProductesTest {

    /**
     * Probamos la clase constructora y el método GetName ya que inicializa
     * el nombre por defecto a "sin_nombre".
     */
    @Test
    public void testConstructorYGetName() {
        LlistaProductes llista = new LlistaProductes();
        assertEquals("Sin_nombre",llista.getName());
    }

    /**
     * Probamos el método SetNom.
     */
    @Test
    public void testSetNom() {
        LlistaProductes llista = new LlistaProductes();
        llista.setNom("nueva_lista");
        assertEquals("nueva_lista",llista.getName());
        assertNotEquals("sin_nombre",llista.getName());
    }

    /**
     * Probamos el método Insertar Producte y el método ProductExists.
     */
    @Test
    public void testInsertarProducteYProductExists() {
        LlistaProductes llista = new LlistaProductes();
        boolean es_nuevo = llista.insertarProducte("p_new");
        assertTrue(es_nuevo);

        boolean existeix = llista.productExists("p_new");
        assertTrue(existeix);

        existeix = llista.productExists("p_false");
        assertFalse(existeix);

        es_nuevo = llista.insertarProducte("p_new");
        assertFalse(es_nuevo);
    }

    /**
     * Probamos el método EliminarProducte.
     */
    @Test
    public void testEliminarProducte() {
        LlistaProductes llista = new LlistaProductes();
        boolean es_nuevo = llista.insertarProducte("p_new");
        boolean existeix = llista.productExists("p_new");
        assertTrue(existeix);

        llista.eliminarProducte("p_new");
        existeix = llista.productExists("p_new");
        assertFalse(existeix);

    }

    /**
     * Probamos los métodos de SetSimilarity y GetSimiliarity.
     */
    @Test
    public void testSetSimilarityYGetSImilarity() {
        LlistaProductes llista = new LlistaProductes();
        boolean es_nuevo = llista.insertarProducte("p1");
        es_nuevo = llista.insertarProducte("p2");
        assertEquals(0.0f,llista.getSimilarity("p1","p2"),0.0f);

        llista.setSimilarity("p1","p2",0.45f);
        assertEquals(0.45f,llista.getSimilarity("p1","p2"),0.0f);

        llista.setSimilarity("p1","p6",0.4f);
        assertNull(llista.getSimilarity("p1","p6"));

    }

    /**
     * Probamos el método GetList.
     */
    @Test
    public void testGetList () {
        LlistaProductes llista = new LlistaProductes();

        //Caso de lista vacía
        Set<String> fila = llista.getList();
        assertEquals(0,fila.size(),0);

        //Caso de lista con 1 elemento
        boolean es_nuevo = llista.insertarProducte("p1");
        fila = llista.getList();
        Iterator<String> it = fila.iterator();
        assertEquals(1,fila.size(),0);
        assertEquals("p1",it.next());

        //Caso de lista con más de 1 elemento
        es_nuevo = llista.insertarProducte("p2");
        fila = llista.getList();
        it = fila.iterator();
        assertEquals(2,fila.size(),0);
        assertEquals("p1",it.next());
        assertEquals("p2",it.next());
    }

    /**
     * Probamos el método GetSimilarityMatrix.
     */
    @Test
    public void testgetSimiliarityMatrix() {
        LlistaProductes llista = new LlistaProductes();

        //Caso de Matriz vacía
        Map<String,Map<String,Float>> sim = llista.getSimilarityMatrix();
        assertEquals(0,sim.size(),0);

        //Caso de Matriz de 1 elemento
        boolean es_nuevo = llista.insertarProducte("p1");
        sim = llista.getSimilarityMatrix();
        assertEquals(1,sim.size(),0);
        assertEquals(0.0f,sim.get("p1").getOrDefault("p1",0.0f),0.0f);

        //Caso de Matriz de más de 1 elemento
        es_nuevo = llista.insertarProducte("p2");
        assertEquals(0.0f,sim.get("p1").getOrDefault("p2",0.0f),0.0f);
        llista.setSimilarity("p1","p2",0.45f);
        assertEquals(0.45f,sim.get("p1").get("p2"),0.0f);
        assertEquals(0.45f,sim.get("p2").get("p1"),0.0f);
    }
}
