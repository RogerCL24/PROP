package drivers;

import main.domain.classes.algorism.Aproximation_Kruskal_ILS;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AproximationKruskalILSTest {

    @Test
    public void testExecuteAlg() {
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
        Aproximation_Kruskal_ILS alg = new Aproximation_Kruskal_ILS();

        // Ejecutar el algoritmo
        List<String> result = alg.executeAlg(graph);

        // Comprobamos si el resultado es el esperado
        assertNotNull(result);
        assertEquals(4, result.size());

        // El ciclo esperado sería p4 -> p1 -> p2 -> p3
        assertEquals("p3", result.get(0));
        assertEquals("p2", result.get(1));
        assertEquals("p1", result.get(2));
        assertEquals("p4", result.get(3));
    }
}