import main.domain.classes.CjtUsers;
import main.domain.classes.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


/**
 * Test para probar la clase CjtUsers.java
 *
 * @author David Sanz Mdrtinez (david.sanz.martinez)
 */
@RunWith(value= MockitoJUnitRunner.class)
public class CjtUsersTest {

    //Mockeamos 2 instancias de la clase user para poder user 2
    @Mock
    User userMock;

    @Mock
    User userMock2;

    /**
     * Probamos la constructora y que haya 0 users al inicializar.
     */
    @Test
    public void testConstructoraYIsEmpty() {
        CjtUsers cjt = new CjtUsers();
        boolean vacio = cjt.isEmpty();
        assertTrue(vacio);
    }

    /**
     * Probamos los métodos de AddUser y GetSize.
     */
    @Test
    public void testAdduserYGetSize() {
        when (userMock.getUsername()).thenReturn("Alarico I");
        when (userMock2.getUsername()).thenReturn("Ataulfo");

        //Creamos un conjunto y nos aseguramos que sea de tamaño 0
        CjtUsers cjt = new CjtUsers();
        assertEquals(0,cjt.getSize(),0);

        //Probamos añadiendo un usuario
        cjt.addUser(userMock);
        assertEquals(1,cjt.getSize(),0);

        //Probamos a volver a añadir el mismo
        cjt.addUser(userMock);
        assertEquals(1,cjt.getSize(),0);

        //Probamos a añadir otro diferente
        cjt.addUser(userMock2);
        assertEquals(2,cjt.getSize(),0);
    }

    /**
     * Probamos el método IsEmpty, suponemos que addUser funciona correctamente.
     */
    @Test
    public void testIsEmpty() {
        when (userMock.getUsername()).thenReturn("Sigerico");
        CjtUsers cjt = new CjtUsers();

        cjt.addUser(userMock);
        boolean vacio = cjt.isEmpty();
        assertFalse(vacio);
    }

    /**
     * Probamos el método containsUser, suponemos que addUser funciona correctamente.
     */
    @Test
    public void testContainsUser() {
        when (userMock.getUsername()).thenReturn("Walia");
        CjtUsers cjt = new CjtUsers();

        cjt.addUser(userMock);
        boolean contiene = cjt.containsUser("Walia");
        assertTrue(contiene);

        contiene = cjt.containsUser("Teodorico I");
        assertFalse(contiene);
    }

    /**
     * Probamos el método GetUser, suponemos que addUser funciona correctamente.
     */
    @Test
    public void testGetUser() {
        when (userMock2.getUsername()).thenReturn("Turismundo");
        CjtUsers cjt = new CjtUsers();
        userMock = cjt.getUser("Turismundo");
        assertNull(userMock);

        cjt.addUser(userMock2);
        userMock = cjt.getUser("Turismundo");
        assertNotNull(userMock);

        userMock = cjt.getUser("Teodorico II");
        assertNull(userMock);
    }

    /**
     * Probamos el método GetAllUsernames, suponemos que addUser funciona correctamente.
     */
    @Test
    public void testGetAllUsernames() {
        when (userMock.getUsername()).thenReturn("Eurico");
        when (userMock2.getUsername()).thenReturn("Alarico II");
        CjtUsers cjt = new CjtUsers();

        //Probamos con el conjunto estando vacío
        List<String> users = cjt.getAllUsernames();
        assertEquals(0,users.size(),0);

        //Probamos habiendo 1 usuario
        cjt.addUser(userMock);
        users = cjt.getAllUsernames();
        assertEquals(1,users.size(),0);
        assertEquals(userMock.getUsername(),users.get(0),"Eurico");

        //Probamos habiendo más de 1 usuario
        cjt.addUser(userMock2);
        users = cjt.getAllUsernames();
        assertEquals(2,users.size(),0);
        assertEquals(userMock.getUsername(),users.get(0),"Eurico");
        assertEquals(userMock2.getUsername(),users.get(1),"Alarico II");
    }
}
