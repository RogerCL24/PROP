package algorism;

import main.domain.classes.algorism.AlgoritmoVoraz;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Test para probar el Algoritmo Voraz
 *
 * @author David Sanz Mdrtinez (david.sanz.martinez)
 */
public class VorazTest {

    @Test
    public void testAlgoritmoVoraz() {

        //Caso 1
        // Crear un ejemplo de input para probar
        Map<String, Map<String, Float>> graph = new HashMap<>();

        // Nodo p1
        Map<String, Float> p1 = new HashMap<>();
        p1.put("p2", 1.0f);
        p1.put("p4", 0.8f);
        graph.put("p1", p1);

        // Nodo p2
        Map<String, Float> p2 = new HashMap<>();
        p2.put("p1", 1.0f);
        p2.put("p3", 1.0f);
        graph.put("p2", p2);

        // Nodo p3
        Map<String, Float> p3 = new HashMap<>();
        p3.put("p2", 1.0f);
        graph.put("p3", p3);

        // Nodo p4
        Map<String, Float> p4 = new HashMap<>();
        p4.put("p1", 0.8f);
        graph.put("p4", p4);

        // Crear la instancia del algoritmo
        AlgoritmoVoraz alg = new AlgoritmoVoraz();

        // Ejecutar el algoritmo
        List<String> result = alg.executeAlg(graph);

        // Comprobamos si el resultado es el esperado
        assertNotNull(result);
        assertEquals(4, result.size());

        // El ciclo esperado sería p4 -> p1 -> p2 -> p3
        assertEquals("p1", result.get(0));
        assertEquals("p2", result.get(1));
        assertEquals("p3", result.get(2));
        assertEquals("p4", result.get(3));

        //Caso 2
        // Crear un ejemplo de input para probar
        Map<String, Map<String, Float>> graph2 = new HashMap<>();

        // Nodo p5
        Map<String, Float> p5 = new HashMap<>();
        p5.put("p7", 1.0f);
        p5.put("p6", 0.1f);
        graph2.put("p5", p5);

        // Nodo p6
        Map<String, Float> p6 = new HashMap<>();
        p6.put("p5", 0.1f);
        p6.put("p8", 0.2f);
        graph2.put("p6", p6);

        // Nodo p7
        Map<String, Float> p7 = new HashMap<>();
        p7.put("p5", 1.0f);
        p7.put("p8",1.0f);
        graph2.put("p7", p7);

        // Nodo p8
        Map<String, Float> p8 = new HashMap<>();
        p8.put("p6", 0.2f);
        p8.put("p7",1.0f);
        graph2.put("p8", p8);

        // Crear la instancia del algoritmo
        alg = new AlgoritmoVoraz();

        // Ejecutar el algoritmo
        result = alg.executeAlg(graph2);

        // Comprobamos si el resultado es el esperado
        assertNotNull(result);
        assertEquals(4, result.size());

        // El ciclo esperado sería p4 -> p1 -> p2 -> p3
        assertEquals("p5", result.get(0));
        assertEquals("p6", result.get(1));
        assertEquals("p8", result.get(2));
        assertEquals("p7", result.get(3));
    }


}
