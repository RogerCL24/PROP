import main.domain.classes.Distribucion;
import main.domain.classes.LlistaProductes;
import main.domain.classes.Mensaje;
import main.domain.classes.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

/**
 * Clase de pruebas para MensajeTest.java
 *
 * @author David Sanz Martínez (david.sanz.martinez)
 */
@RunWith(value= MockitoJUnitRunner.class)
public class MensajeTest {

    //Mockeamos 2 users, 1 distribucion y 1 LListaProductes
    @Mock
    User userMock1;

    @Mock
    User userMock2;

    @Mock
    Distribucion distMock1;

    @Mock
    LlistaProductes LlistaProductesMock1;

    /**
     * Probamos la constructora y los gets de Objeto, timestamp, Sender, Destinatario,
     * Nombre y que se inicialize a no leido.
     */
    @Test
    public void testConstructoraYGetters() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Mensaje mensaje = new Mensaje(distMock1,"Carta a los Reyes Magos", false,userMock1,userMock2);
        LocalDateTime timestamp = LocalDateTime.now();

        assertEquals(distMock1,mensaje.getObjeto());
        assertEquals("Carta a los Reyes Magos",mensaje.getNombre());
        assertEquals(userMock1,mensaje.getSender());
        assertEquals(userMock2,mensaje.getDestinatario());
        assertFalse(mensaje.isLeido());
        assertEquals(timestamp.format(formatter),mensaje.getTimestamp().format(formatter));


        Mensaje nuevo_mensaje = new Mensaje(LlistaProductesMock1,"Carta a los Reyes Magos",false,userMock1,userMock2);
        assertEquals(LlistaProductesMock1,nuevo_mensaje.getObjeto());
    }

    /**
     * Probamos el método IsLista, para mirar si es una lista (true) o una distribucion (false).
     */
    @Test
    public void testIsLista() {
        Mensaje mensaje1 = new Mensaje(distMock1,"Carta a Papa Noel",false,userMock1,userMock2);
        assertFalse(mensaje1.isLista());

        Mensaje mensaje2 = new Mensaje(LlistaProductesMock1,"Carta a Papa Noel",true,userMock1,userMock2);
        assertTrue(mensaje2.isLista());
    }

    /**
     * Probamos el método IsLeido y MarcarComoLeido.
     */
    @Test
    public void testIsLeidoYMarcarComoLeido() {
        Mensaje mensaje = new Mensaje(distMock1,"Carta a Papa Noel",false,userMock1,userMock2);
        assertFalse(mensaje.isLeido());
        mensaje.marcarComoLeido();
        assertTrue(mensaje.isLeido());
    }
}
