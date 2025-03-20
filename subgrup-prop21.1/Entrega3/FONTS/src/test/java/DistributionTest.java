
import main.domain.classes.Distribucion;
import main.domain.classes.types.Pair;
import java.util.Map;
import java.util.List;
import main.domain.classes.Estrategia;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Clase de pruebas para Distribucion.java
 *
 * @author David Sanz Martínez (david.sanz.martinez)
 */
@RunWith(value= MockitoJUnitRunner.class)
public class DistributionTest {

    //Mockeamos 3 estrategias
    @Mock
    Estrategia estrategiaMock;
    @Mock
    Estrategia estrategiaMock2;
    @Mock
    Estrategia estrategiaMock3;

    /**
     * Prueba de las 2 constructoras y los gets de GetNom, GetPrestatge y GetDist.
     */
    @Test
    public void testConstructorasYGetters() {
        Pair<Integer, Integer> prestage = new Pair<>(3, 3);

        //Constructora que inicializa prestage, Dist y nom
        List<String> dist = Arrays.asList("p1", "p2", "p3");
        Distribucion distribucion = new Distribucion(prestage, dist, "Estante1");
        assertEquals(prestage, distribucion.getPrestage());
        assertEquals(dist, distribucion.getDist());
        assertEquals("Estante1", distribucion.getNom());

        //Constructora que inicializa solo prestage
        Distribucion dist2 = new Distribucion(3,3);
        assertEquals(prestage.getFirst(), dist2.getPrestage().getFirst());
        assertEquals(prestage.getSecond(), dist2.getPrestage().getSecond());
    }

    /**
     * Probamos get y set Algorisme. No miramos que el string que devuelva sea el mismo que el de
     * estrategiaMock porque no se puede mockear la funcion ya que es un getClass().getSimpleName()
     * que es de Object.
     */
    @Test
    public void testGetYSetAlgorisme() {
        Distribucion dist = new Distribucion(6,6);
        dist.setAlgoritmo(estrategiaMock);
        assertNotNull(dist.getAlgoritme());
    }

    /**
     * Probamos el método SetNom.
     */
    @Test
    public void testSetNom() {
        //Sin inicalizar el nombre con la constructora
        Distribucion dist = new Distribucion(3,8);
        assertNull(dist.getNom());
        dist.setNom("Distribucion_Azul");
        assertEquals("Distribucion_Azul", dist.getNom());
        dist.setNom("Distribucion_Roja");
        assertEquals("Distribucion_Roja", dist.getNom());

        //Inicializando el nombre con la constructora
        Pair<Integer, Integer> prestatge = new Pair<>(4, 2);
        List<String> list = Arrays.asList("p1", "p2", "p3");
        Distribucion dist2 = new Distribucion(prestatge,list,"Distribucion_Verde");
        assertEquals("Distribucion_Verde", dist2.getNom());
        dist2.setNom("Distribucion_Amarilla");
        assertEquals("Distribucion_Amarilla", dist2.getNom());
    }

    /**
     * Prueba del metodo generateDist y contieneProducto, suponemos que setAlgoritme funciona bien.
     */
    @Test
    public void testGenerateDistYContieneProducto() {
        Map<String, Map<String, Float>> productos = new HashMap<>();
        when (estrategiaMock.executeAlg(productos)).thenReturn(Arrays.asList("p1", "p2", "p3", "p4"));
        List<String> empty_list = new ArrayList<>();
        when (estrategiaMock2.executeAlg(productos)).thenReturn(empty_list);
        when (estrategiaMock3.executeAlg(productos)).thenReturn(Arrays.asList("p1"));
        Distribucion distribucion = new Distribucion(2, 2);
        //Caso de varios productos
        distribucion.setAlgoritmo(estrategiaMock);
        distribucion.generateDist(productos);

        // Verificar que se haya generado la distribución y los productos están en el mapa
        assertEquals(Arrays.asList("p1", "p2", "p3", "p4"), distribucion.getDist());
        assertTrue(distribucion.contieneProducto("p1"));
        assertTrue(distribucion.contieneProducto("p2"));
        assertTrue(distribucion.contieneProducto("p4"));
        assertFalse(distribucion.contieneProducto("p5"));

        //Caso de ningun producto
        distribucion.setAlgoritmo(estrategiaMock2);
        distribucion.generateDist(productos);
        assertFalse(distribucion.contieneProducto("p1"));
        assertEquals(empty_list, distribucion.getDist());

        //Caso de 1 producto
        distribucion.setAlgoritmo(estrategiaMock3);
        distribucion.generateDist(productos);
        assertTrue(distribucion.contieneProducto("p1"));
        assertEquals(Arrays.asList("p1"), distribucion.getDist());
        assertFalse(distribucion.contieneProducto("p2"));

    }

    /**
     * Probamos el método IsEmpty, es una clase redundante porque no puede haber una distribucion vacía.
     */
    @Test
    public void testIsEmpty() {
        Distribucion distribucion = new Distribucion(4,4);
        boolean empty;
        Map<String, Map<String, Float>> productos = new HashMap<>();
        when (estrategiaMock.executeAlg(productos)).thenReturn(Arrays.asList("p1", "p2", "p3", "p4"));
        distribucion.setAlgoritmo(estrategiaMock);
        distribucion.generateDist(productos);
        empty = distribucion.isEmpty();
        assertFalse(empty);
    }

    /**
     * Probamos el método editarDist, suponemos que setAlgoritme funciona bien.
     */
    @Test
    public void testEditarDist() {
        //Caso de distribucion de 1 producto
        Pair<Integer,Integer> prestatge0 = new Pair<>(1,1);
        List<String> list0 = Arrays.asList("p1");
        Distribucion distribucion_0 = new Distribucion(prestatge0,list0,"Estante_unico");
        assertEquals("p1", distribucion_0.getDist().get(0));
        Map<String, Map<String, Float>> productos_0 = new HashMap<>();
        productos_0.put("p1", new HashMap<>());
        distribucion_0.setAlgoritmo(estrategiaMock3);
        distribucion_0.generateDist(productos_0);
        distribucion_0.editarDist("p1","p1");
        assertEquals("p1", distribucion_0.getDist().get(0));
        assertEquals(1, distribucion_0.getDist().size(),0);

        //Caso de distribucion de 3 productos
        Pair<Integer, Integer> prestage = new Pair<>(3, 3);
        List<String> dist = Arrays.asList("p1", "p2", "p3");
        Distribucion distribucion = new Distribucion(prestage, dist, "Estante1");
        Map<String, Map<String, Float>> productos = new HashMap<>();
        productos.put("p1", new HashMap<>());
        productos.put("p2", new HashMap<>());
        productos.put("p3", new HashMap<>());

        when (estrategiaMock.executeAlg(productos)).thenReturn(Arrays.asList("p1", "p2", "p3"));
        distribucion.setAlgoritmo(estrategiaMock);
        distribucion.generateDist(productos);

        assertEquals("p3", distribucion.getDist().get(2));
        assertEquals("p1", distribucion.getDist().get(0));

        //Se cambian de sitio p1 y p3
        distribucion.editarDist("p1", "p3");
        assertEquals("p3", distribucion.getDist().get(0));
        assertEquals("p1", distribucion.getDist().get(2));

        //Caso de 2 productos
        Map<String, Map<String, Float>> productos_2 = new HashMap<>();
        productos_2.put("p1", new HashMap<>());
        productos_2.put("p2", new HashMap<>());
        productos_2.put("p3", new HashMap<>());

        when (estrategiaMock2.executeAlg(productos_2)).thenReturn(Arrays.asList("p1", "p2"));
        Pair<Integer,Integer> prestatge_2 = new Pair<>(2,2);
        List<String> lista_2 = Arrays.asList("p1", "p2");
        Distribucion distribucion_2 = new Distribucion(prestatge_2,lista_2,"Estante2");
        distribucion_2.setAlgoritmo(estrategiaMock2);
        distribucion_2.generateDist(productos_2);

        assertEquals("p1", distribucion_2.getDist().get(0));
        assertEquals("p2", distribucion_2.getDist().get(1));

        //Se cambian de sitio p1 y p2
        distribucion_2.editarDist("p1", "p2");
        assertEquals("p1", distribucion_2.getDist().get(1));
        assertEquals("p2", distribucion_2.getDist().get(0));

    }


    /**
     * Probamos el metodo eliminarProducte
     */
    @Test
    public void testEliminarProducte() {
        Pair<Integer, Integer> prestage = new Pair<>(3, 3);
        List<String> dist = new ArrayList<>(Arrays.asList("p1", "p2", "p3"));
        Distribucion distribucion = new Distribucion(prestage, dist, "Estante1");

        // Inicializar Dist y mapa llamando a generateDist
        Map<String, Map<String, Float>> productos = new HashMap<>();
        productos.put("p1", new HashMap<>());
        productos.put("p2", new HashMap<>());
        productos.put("p3", new HashMap<>());
        when (estrategiaMock.executeAlg(productos)).thenReturn(Arrays.asList("p1", "p2", "p3"));
        distribucion.setAlgoritmo(estrategiaMock);
        distribucion.generateDist(productos);

        // Ahora sí llamamos a eliminarProducte, eliminamos un producto que existe
        distribucion.eliminarProducte("p2");

        // Verificar el resultado
        assertFalse(distribucion.contieneProducto("p2"));
        
    }
}

