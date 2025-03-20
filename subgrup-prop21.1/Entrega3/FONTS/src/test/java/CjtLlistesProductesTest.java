import main.domain.classes.CjtLlistesProductes;
import main.domain.classes.LlistaProductes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.*;


/**
 * Test para probar la clase CjtLListesProductes.java
 *
 * @author David Sanz Mdrtinez (david.sanz.martinez)
 */
@RunWith(value= MockitoJUnitRunner.class)
public class CjtLlistesProductesTest {

    //Moqueamos 2 veces LlistaProductes porque usaremos 2 listas diferentes.
    @Mock
    private LlistaProductes llistaproductesmock;

    @Mock
    private LlistaProductes llistaproductesmock2;

    /**
     * Probamos la constructora y que haya 0 listas al inicializar.
     */
    @Test
    public void testConstructorYgetSize() {
        CjtLlistesProductes cjt = new CjtLlistesProductes();
        assertEquals(0,cjt.getSize(),0);
    }


    /**
     * Probamos el método AgregarLista y miramos que se han añadido correctamente con getSize,
     * así que estamos probando los 2.
     */
    @Test
    public void testAgregarListaYgetSize() {
        CjtLlistesProductes cjt = new CjtLlistesProductes();

        cjt.agregarLista("lista_1",llistaproductesmock);
        assertEquals(1,cjt.getSize(),0);

        cjt.agregarLista("lista_1",llistaproductesmock2);
        assertEquals(1,cjt.getSize(),0);

        cjt.agregarLista("lista_2",llistaproductesmock);
        assertEquals(2,cjt.getSize(),0);
    }

    /**
     * Probamos el método contiene, suponiendo que agregarLista funciona correctamente.
     */
    @Test
    public void testContiene() {
        CjtLlistesProductes cjt = new CjtLlistesProductes();
        cjt.agregarLista("lista_1",llistaproductesmock);
        boolean contiene = cjt.contiene("lista_1");
        assertTrue(contiene);

        contiene = cjt.contiene("lista_2");
        assertFalse(contiene);
    }


    /**
     * Probamos el método isEmpty, suponiendo que agregarLista y eliminarListaDelConjunto
     * funcionan correctamente.
     */
    @Test
    public void testIsEmpty() {
        CjtLlistesProductes cjt = new CjtLlistesProductes();
        //Ninguna lista ha sido añadiada
        boolean buit = cjt.isEmpty();
        assertTrue(buit);

        //Se ha añadido 1 lista
        cjt.agregarLista("lista_1",llistaproductesmock);
        buit = cjt.isEmpty();
        assertFalse(buit);

        //Se han borrado todas las listas del Conjunto
        cjt.eliminarListaDelConjunto("lista_1");
        buit = cjt.isEmpty();
        assertTrue(buit);
    }

    /**
     * Probamos el método GetListas, suponiendo que agregarLista y eliminarListaDelConjunto
     * funcionan correctamente.
     */
    @Test
    public void testGetListas() {
        CjtLlistesProductes cjt = new CjtLlistesProductes();
        //No hay ninguna lista
        Set<String> noms = cjt.getListas();
        assertEquals(0,noms.size(),0);

        //Hay 1 lista
        cjt.agregarLista("lista_1",llistaproductesmock);
        noms = cjt.getListas();
        assertEquals(1,noms.size(),0);

        //Hay 2 listas
        cjt.agregarLista("lista_2",llistaproductesmock2);
        noms = cjt.getListas();
        assertEquals(2,noms.size(),0);

        //Vuelve a haber 1 lista
        cjt.eliminarListaDelConjunto("lista_1");
        noms = cjt.getListas();
        assertEquals(1,noms.size(),0);
    }

    /**
     * Probamos el método ObtenerLista, suponiendo que agregarLista funciona correctamente.
     */
    @Test
    public void testObtenerLista() {
        CjtLlistesProductes cjt = new CjtLlistesProductes();
        //No hay ninguna lista
        LlistaProductes llista = cjt.obtenerLista("lista_1");
        assertNull(llista);

        //Hay listas y está la que buscamos
        cjt.agregarLista("lista_1",llistaproductesmock);
        llista = cjt.obtenerLista("lista_1");
        assertNotNull(llista);

        //Hay listas y no está la que buscamos
        llista = cjt.obtenerLista("lista_2");
        assertNull(llista);
    }

    /**
     * Probamos el método EliminarListaDelConjunto, suponiendo que agregarLista funciona correctamente.
     */
    @Test
    public void testEliminarListaDelCOnjunto() {
        CjtLlistesProductes cjt = new CjtLlistesProductes();

        //Probamos a eliminar una lista existente
        cjt.eliminarListaDelConjunto("lista");
        cjt.agregarLista("lista_1",llistaproductesmock);
        assertEquals(1,cjt.getSize(),0);

        //Probamos a eliminar una lista inexistente
        cjt.eliminarListaDelConjunto("lista_1");
        assertEquals(0,cjt.getSize(),0);
    }
}
