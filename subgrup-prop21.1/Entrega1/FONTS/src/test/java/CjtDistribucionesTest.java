import main.domain.classes.CjtDistribuciones;
import main.domain.classes.Distribucion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;
import java.util.Iterator;

import static org.junit.Assert.*;


/**
 * Test para probar la clase CjtDistribuciones.java
 *
 * @author David Sanz Mdrtinez (david.sanz.martinez)
 */
@RunWith(value= MockitoJUnitRunner.class)
public class CjtDistribucionesTest {

    //Mockamos 2 veces dsitribucion para tener 2 distintas
    @Mock
    private Distribucion distribucionMock;

    @Mock
    private Distribucion distribucionMock2;

    /**
     * Probamos la constructora y que haya 0 distribuciones al inicializar.
     */
    @Test
    public void testConstructoraYGetSize() {
        CjtDistribuciones cjtdist = new CjtDistribuciones();
        assertEquals(0,cjtdist.getSize(),0);
    }

    /**
     * Probamos los métodos de AgregarDistribucion y GetSize.
     */
    @Test
    public void testAgregarDistribucionYGetSize() {
        CjtDistribuciones cjtdist = new CjtDistribuciones();

        cjtdist.agregarDistribucion("dist1",distribucionMock);
        assertEquals(1,cjtdist.getSize(),0);

        cjtdist.agregarDistribucion("dist1",distribucionMock2);
        assertEquals(1,cjtdist.getSize(),0);

        cjtdist.agregarDistribucion("dist2",distribucionMock);
        assertEquals(2,cjtdist.getSize(),0);
    }

    /**
     * Probamos el método contiene, suponemos que AgregarDistribucion funciona correctamente.
     */
    @Test
    public void testContiene() {
        CjtDistribuciones cjtdist = new CjtDistribuciones();

        boolean contiene = cjtdist.contiene("dist1");
        assertFalse(contiene);

        cjtdist.agregarDistribucion("dist1",distribucionMock);
        contiene = cjtdist.contiene("dist1");
        assertTrue(contiene);

        contiene = cjtdist.contiene("dist2");
        assertFalse(contiene);
    }

    /**
     * Probamos el método IsEmpty, suponemos que AgregarDistribucion funciona correctamente.
     */
    @Test
    public void testIsEmpty() {
        CjtDistribuciones cjtdist = new CjtDistribuciones();

        boolean buit = cjtdist.isEmpty();
        assertTrue(buit);

        cjtdist.agregarDistribucion("dist1",distribucionMock);
        buit = cjtdist.isEmpty();
        assertFalse(buit);
    }

    /**
     * Probamos el método ObtenerDistibucion, suponemos que AgregarDistribucion funciona correctamente.
     */
    @Test
    public void testObtenerDistribucion() {
        CjtDistribuciones cjtdist = new CjtDistribuciones();

        distribucionMock = cjtdist.obtenerDistribucion("dist1");
        assertNull(distribucionMock);

        cjtdist.agregarDistribucion("dist1",distribucionMock2);
        distribucionMock2 = cjtdist.obtenerDistribucion("dist1");
        assertNotNull(distribucionMock2);

        distribucionMock = cjtdist.obtenerDistribucion("dist2");
        assertNull(distribucionMock);
    }

    /**
     * Probamos el método EliminarDistribucionDelConjunto, suponemos que AgregarDistribucion funciona correctamente.
     */
    @Test
    public void testEliminarDistribucionDelConjunto() {
        CjtDistribuciones cjtdist = new CjtDistribuciones();

        cjtdist.eliminarDistribucionDelConjunto("dist1");

        cjtdist.agregarDistribucion("dist1",distribucionMock);
        cjtdist.eliminarDistribucionDelConjunto("dist2");
        assertEquals(1,cjtdist.getSize(),0);

        cjtdist.eliminarDistribucionDelConjunto("dist1");
        assertEquals(0,cjtdist.getSize(),0);
    }

    /**
     * Probamos el método GetNames, suponemos que AgregarDistribucion y EliminarfDistribucionDelConjunto
     * funcionan correctamente.
     */
    @Test
    public void testgetNames() {
        CjtDistribuciones cjtdist = new CjtDistribuciones();

        //Caso de conjunto vacío
        Set<String> nombres = cjtdist.getNames();
        assertEquals(0,nombres.size(),0);

        //Caso de 1 distribucion en el conjunto
        cjtdist.agregarDistribucion("dist1",distribucionMock);
        nombres = cjtdist.getNames();
        assertEquals(1,nombres.size(),0);
        assertEquals("dist1",nombres.iterator().next());

        //Caso de haber borrado la unica distribucion del conjunto
        cjtdist.eliminarDistribucionDelConjunto("dist1");
        nombres = cjtdist.getNames();
        assertEquals(0,nombres.size(),0);

        //Caso de tener más de 1 distribucion en el conjunto
        cjtdist.agregarDistribucion("dist1",distribucionMock);
        cjtdist.agregarDistribucion("dist2",distribucionMock);
        nombres = cjtdist.getNames();
        assertEquals(2,nombres.size(),0);
        assertEquals("dist1",nombres.iterator().next());
        Iterator<String> it = nombres.iterator();
        String useless = it.next();
        assertEquals("dist2",it.next());
    }
}
