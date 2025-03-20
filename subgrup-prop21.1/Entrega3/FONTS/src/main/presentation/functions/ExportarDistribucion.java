package main.presentation.functions;

import main.domain.classes.Distribucion;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Clase que encapsula funciones que permiten al usuario seleccionar la carpeta de exportación.
 *
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class ExportarDistribucion {

    /**
     * Genera una cadena con el formato adecuado para exportar la distribución a un archivo de texto.
     *
     * @param distribucion La instancia de Distribucion a exportar.
     * @return Una cadena con el formato de la distribución.
     */
    public String formatoDistribucion(Distribucion distribucion) {
        StringBuilder sb = new StringBuilder();
        List<String> Dist = distribucion.getDist();
        int numFilas = distribucion.getPrestage().getFirst();
        int numColumnas = distribucion.getPrestage().getSecond();
        int numProductos = Dist.size();

        boolean turn = false;
        int productoIndex = 0;

        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                if (!turn && productoIndex < numProductos && Dist.get(productoIndex) != null) {
                    sb.append(Dist.get(productoIndex)).append(" ");
                    productoIndex++;
                } else if (turn && productoIndex < numProductos) {
                    int reverseIndex = i * numColumnas + (numColumnas - j - 1);
                    if (reverseIndex < numProductos && Dist.get(reverseIndex) != null) {
                        sb.append(Dist.get(reverseIndex)).append(" ");
                        productoIndex++;
                    } else {
                        sb.append("/*/ ");
                    }
                } else {
                    sb.append("/*/ ");
                    if (Dist.get(productoIndex) == null) productoIndex++;
                }
            }
            sb.append("\n");
            turn = !turn;
        }

        return sb.toString();
    }

    /**
     * Exporta una distribución a un archivo de texto en una carpeta seleccionada por el usuario.
     *
     * @param nombre El nombre de la distribución a exportar.
     * @param distribucion La instancia de Distribucion a exportar.
     * @return La ruta completa del archivo exportado, o null si ocurrió un error.
     */
    public String exportarDistribucion(String nombre, Distribucion distribucion) {
        // Crear un selector de archivos para elegir la carpeta de destino
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar carpeta de destino");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File carpetaSeleccionada = fileChooser.getSelectedFile();
        String rutaCarpeta = carpetaSeleccionada.getAbsolutePath();
        String rutaFichero = rutaCarpeta + File.separator + nombre + ".txt";

        // Verificar si ya existe un archivo con el mismo nombre
        rutaFichero = generarNombreUnico(rutaFichero);

        try (FileWriter writer = new FileWriter(rutaFichero)) {
            writer.write("Distribucion generada con el algoritmo " + distribucion.getAlgoritme() + "\n");
            writer.write(formatoDistribucion(distribucion));
            return rutaFichero;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al exportar la distribución: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Genera un nombre único para un archivo en función de su ruta base y extensión. Si un archivo con
     * el nombre especificado ya existe, añade un sufijo incremental al nombre base (e.g., "_1", "_2")
     * hasta que se encuentra un nombre disponible.
     *
     * @param rutaFichero La ruta completa del archivo objetivo (incluyendo extensión).
     * @return Una ruta única para el archivo que no colisiona con archivos existentes.
     */
    private String generarNombreUnico(String rutaFichero) {
        File archivo = new File(rutaFichero);
        String rutaBase = rutaFichero.substring(0, rutaFichero.lastIndexOf('.'));
        String extension = rutaFichero.substring(rutaFichero.lastIndexOf('.'));
        int contador = 1;

        // Incrementar el sufijo hasta encontrar un nombre único
        while (archivo.exists()) {
            archivo = new File(rutaBase + "_" + contador + extension);
            contador++;
        }
        return archivo.getAbsolutePath();
    }
}	
