import main.domain.classes.CjtDistribuciones;
import main.domain.classes.CjtLlistesProductes;
import main.domain.classes.Mensaje;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.List;
import main.domain.classes.User;
import org.mockito.junit.MockitoJUnitRunner;


/**
 * Test para probar la clase User.java
 *
 * @author David Sanz Mdrtinez (david.sanz.martinez)
 */
@RunWith(value= MockitoJUnitRunner.class)
public class UserTest {

    //Mockeamos todas las clases que son parte de los atributos 1 vez
    @Mock
    private Mensaje menasjeMock;

    @Mock
    private CjtDistribuciones cjtDistribucionesMock;

    @Mock
    private CjtDistribuciones cjtDistribucionesMock2;

    @Mock
    private CjtLlistesProductes cjtLlistesProductesMock;

    @Mock
    private CjtLlistesProductes cjtLlistesProductesMock2;

    /**
     * Porbamos la Constructora y los gets de username, name y password.
     */
    @Test
    public void testConstructoraYGetters() {
        User user = new User("Yo","Rodrigo","Ultimo rey visigodo");
        assertEquals("Yo",user.getUsername());
        assertEquals("Rodrigo",user.getName());
        assertEquals("Ultimo rey visigodo",user.getPassword());
    }

    /**
     * Probamos los métodos de AddInbox y GetInbox.
     */
    @Test
    public void testAddYGetInbox() {
        User user = new User("Yo","Harald III","Rey de Noruega Haardrade");
        //No ha recibido ningun mensaje
        assertEquals(0,user.getInbox().size(),0);

        //Ha recibido 1 mensaje
        user.addInbox(menasjeMock);
        assertEquals(1,user.getInbox().size(),0);

        //Ha recibido 2 mensajes
        user.addInbox(menasjeMock);
        assertEquals(2,user.getInbox().size(),0);
    }

    /**
     * Porbamos los métodos de AddSent y GetSentMessages.
     */
    @Test
    public void testAddYGetSentMessages() {
        User user = new User("Yo","Pedro I","Zar de Rusia el Grande");

        //No ha enviado ningún mensaje
        assertEquals(0,user.getSentMessages().size(),0);

        //Ha enviado 1 mensaje
        user.addSent(menasjeMock);
        assertEquals(1,user.getSentMessages().size(),0);

        //Ha enviado 2 mensajes
        user.addSent(menasjeMock);
        assertEquals(2,user.getSentMessages().size(),0);
    }

    /**
     * Probamos los métodos de GetMensajesNuevos y GetMensajesNuevos, suponemos que addInbox funciona correctamente.
     */
    @Test
    public void testGetYUpdateMensajesNuevos() {
        User user = new User("Yo","Augusto","Primer emperador Romano");

        //No tiene mensajes nuevos, acaba de ser creado
        int mensajes_nuevos = user.getMensajesNuevos();
        assertEquals(0,mensajes_nuevos,0);

        //Tiene 1 mensajes nuevos
        user.addInbox(menasjeMock);
        mensajes_nuevos = user.getMensajesNuevos();
        assertEquals(1,mensajes_nuevos,0);

        //Tiene 2 mensajes nuevos
        user.addInbox(menasjeMock);
        mensajes_nuevos = user.getMensajesNuevos();
        assertEquals(2,mensajes_nuevos,0);

        //Ha leido los mensajes nuevos
        user.updateMensajesNuevos();
        mensajes_nuevos = user.getMensajesNuevos();
        assertEquals(0,mensajes_nuevos,0);
    }

    /**
     * Probamos el método GetCjtDistribuciones.
     */
    @Test
    public void testGetCjtDistribuciones() {
        when(cjtDistribucionesMock2.getSize()).thenReturn(1);

        User user = new User("Yo","El programador","Estudiante de PROP");

        cjtDistribucionesMock = user.getCjtDistribuciones();
        boolean isEmpty = cjtDistribucionesMock.isEmpty();
        assertTrue(isEmpty);

        User user2 = new User("Tu","El profesor","Que está mirando el código");
        assertEquals(1,cjtDistribucionesMock2.getSize(),0);
    }

    /**
     * Probamos el método GetCjtLlistesProductes.
     */
    @Test
    public void testGetCjtLlistesProductes() {
        when(cjtLlistesProductesMock2.getSize()).thenReturn(2);

        User user = new User("Yo","El programador","Estudiante de PROP");
        cjtLlistesProductesMock = user.getCjtLlistesProductes();
        boolean isEmpty = cjtLlistesProductesMock.isEmpty();
        assertTrue(isEmpty);

        int size = cjtLlistesProductesMock2.getSize();
        assertEquals(2,size,0);
    }

    /**
     * Probamos el Overrude de toString.
     */
    @Test
    public void testToString() {
        User user = new User("Nos","Felipe II","Rey de Espanna");
        String salida = "User{username=Nos, name=Felipe II, password=Rey de Espanna}";
        assertEquals(salida,user.toString());
    }
}
