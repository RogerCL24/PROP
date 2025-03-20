package main.presentation.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que encapsula métodos que sirven para crear el fondo y el estilo de los botones de todas las vistas del
 * programa.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */
public class DesignUtils {

    /**
     * Método para dibujar círculos en el fondo de la vista.
     * @param g Graphics context.
     * @param width Anchura del fondo.
     * @param height Altura del fondo.
     */
    public static void drawCircles(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;

        // Color de los círculos (rosa ligeramente más oscuro que el fondo)
        g2d.setColor(new Color(255, 182, 193)); // Rosa ligeramente más oscuro

        // Dibujar círculos aleatorios
        for (int i = 0; i < 30; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            int size = (int) (Math.random() * 15 + 15); // Tamaño del círculo

            g2d.fillOval(x, y, size, size); // Dibujar círculo
        }
    }

    /**
     * Método para configurar el fondo con círculos (usando un JPanel).
     * @param frame Frame del que se quiere establecer el fondo.
     */
    public static void setBackgroundWithCircles(JFrame frame) {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCircles(g, getWidth(), getHeight());
            }
        };

        backgroundPanel.setBackground(new Color(255, 238, 235)); // Fondo rosa pastel
        frame.setContentPane(backgroundPanel); // Establece el panel de fondo como el contenido principal
        frame.revalidate(); // Asegúrate de que el contenido se vuelva a validar y dibujar correctamente
    }

    /**
     * Método para configurar los botones.
     * @param button Botón que se quiere configurar.
     */
    public static void configureButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente grande y negrita
        button.setBackground(new Color(255, 215, 240)); // Color rosa claro
        button.setForeground(new Color(225, 90, 127)); // Texto blanco
        button.setFocusPainted(false); // Quitar el efecto de clic
        button.setBorder(BorderFactory.createLineBorder(new Color(225, 90, 127), 2)); // Borde rosa oscuro
    }
}
