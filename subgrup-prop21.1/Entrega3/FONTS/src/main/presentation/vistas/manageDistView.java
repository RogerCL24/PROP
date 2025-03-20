package main.presentation.vistas;

import main.presentation.controllers.presentationController;
import main.presentation.utils.DesignUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Vista de la gestión de distribuciones del usuario. Se puede consultar una distribución, modificarla o eliminarla. Si
 * se decide modificarla se puede cambiar su nombre, intercambiar dos de sus productos de sitio o eliminar productos de
 * la distribución.
 *
 * @author David Sanz Martínez (david.sanz.martinez)
 */
public class manageDistView extends JFrame {

    /**
     * Panel principal de la vista.
     */
    private final JPanel buttonPanel = new JPanel();
    /**
     * Título de la vista: Gestionar distribuciones.
     */
    private final JLabel title = new JLabel("Gestionar distribuciones", JLabel.CENTER);
    /**
     * Subtítulo de la vista, pregunta al usuario que quiere hacer indicándole así que tiene diferentes opciones.
     */
    private final JLabel subtitle = new JLabel("¿Qué desea hacer?", JLabel.CENTER);
    /**
     * Botón que lleva a consultar una distribución.
     */
    private final JButton btnConsultar = new JButton("Consultar una distribucion");
    /**
     * Botón que lleva a modificar una distribución.
     */
    private final JButton btnModificar = new JButton("Modificar una distribucion");
    /**
     * Botón que lleva a eliminar una distribución.
     */
    private final JButton btnEliminar = new JButton("Eliminar una distribucion");
    /**
     * Botón que hace retornar al menú principal.
     */
    private final JButton btnVolver = new JButton("Volver");

    /**
     * Método principal de la vista, la constructora. Se configura la vista, se hace visible y se implementan los
     * listeners de los botones.
     */
    public manageDistView() {
        // Configuración de la ventana
        setBounds(600, 400, 600, 500); // Tamaño ajustado para que coincida con el del menú principal
        setResizable(false);
        setTitle("Gestionar distribuciones");

        // Usamos DesignUtils para aplicar fondo con círculos
        DesignUtils.setBackgroundWithCircles(this);

        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Título de la ventana
        title.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 26));
        title.setForeground(new Color(225, 90, 127)); // Rosa oscuro
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // Subtítulo de la ventana
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(new Color(105, 105, 105)); // Gris oscuro
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(subtitle, BorderLayout.CENTER);

        // Panel de botones con GridLayout
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));
        buttonPanel.setOpaque(false); // Hacer el panel transparente
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100)); // Márgenes

        // Añadir botones y aplicarles diseño con DesignUtils
        DesignUtils.configureButton(btnConsultar);
        DesignUtils.configureButton(btnModificar);
        DesignUtils.configureButton(btnEliminar);
        DesignUtils.configureButton(btnVolver);


        // Agregar botones al panel
        buttonPanel.add(btnConsultar);
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnVolver);

        add(buttonPanel, BorderLayout.CENTER);

        // Panel para el botón de volver
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Alineamos el botón a la izquierda
        bottomPanel.setOpaque(false); // Hacer el panel transparente
        btnVolver.setPreferredSize(new Dimension(150, 40)); // Tamaño del botón "Volver"
        DesignUtils.configureButton(btnVolver); // Configurar el botón de volver
        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnConsultar.addActionListener(e -> consultarDistribucion());
        btnModificar.addActionListener(e -> modificarDistribucion());
        btnEliminar.addActionListener(e -> eliminarDistribucion());
        btnVolver.addActionListener(e -> {
            dispose();
            presentationController.mainMenuView();
        });
    }

    /**
     * Consulta una distribución seleccionada por el usuario.
     */
    private void consultarDistribucion() {
        String seleccion = seleccionarDistribucion("Consultar");

        if (seleccion == null) {return;}

        try {
            List<String> dist = presentationController.getDistList(seleccion);
            String algoritmo = presentationController.getAlgoritmo(seleccion);
            int h = presentationController.getPrestage(seleccion).getFirst();
            int w = presentationController.getPrestage(seleccion).getSecond();

            mostrarDistribucion(this, seleccion, dist, h, w, algoritmo, false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar la distribución: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Modifica una distribución seleccionada por el usuario.
     */
    private void modificarDistribucion() {
        String seleccion = seleccionarDistribucion("Modificar");

        if (seleccion == null) {return;}

        try {
            List<String> dist = presentationController.getDistList(seleccion);
            String algoritmo = presentationController.getAlgoritmo(seleccion);
            int h = presentationController.getPrestage(seleccion).getFirst();
            int w = presentationController.getPrestage(seleccion).getSecond();

            mostrarDistribucion(this, seleccion, dist, h, w, algoritmo, true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar la distribución: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Elimina una distribución seleccionada por el usuario.
     */
    private void eliminarDistribucion() {
        String seleccion = seleccionarDistribucion("Eliminar");

        if (seleccion == null) {return;}

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro que deseas eliminar la distribución '" + seleccion + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                if (presentationController.eliminarDistribucion(seleccion)) {
                    JOptionPane.showMessageDialog(this, "Distribución eliminada con éxito.",
                            "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la distribución.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar la distribución: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Muestra la distribución seleccionada en una ventana gráfica.
     *
     * @param parentFrame La ventana principal que se volverá a activar después de cerrar esta ventana.
     * @param nombre Nombre de la distribución.
     * @param dist Lista de productos en la distribución.
     * @param h Altura de la estantería (número de filas).
     * @param w Anchura de la estantería (número de columnas).
     * @param algoritmo Nombre del algoritmo usado para generar la distribución.
     * @param modificar Indica si la vista incluye opciones de modificación.
     */
    private void mostrarDistribucion(JFrame parentFrame, String nombre, List<String> dist, int h, int w, String algoritmo, boolean modificar) {
        JFrame frame = new JFrame("Distribución: " + nombre);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel infoPanel = new JPanel(new BorderLayout());
        JLabel lblInfo = new JLabel("Distribución: " + nombre + " | Algoritmo: " + convertirNombreAlgoritmo(algoritmo), JLabel.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(lblInfo, BorderLayout.NORTH);
        frame.add(infoPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(h, w, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int numProductos = dist.size();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int index = (i * w + j);
                JLabel cell = new JLabel("", JLabel.CENTER);
                cell.setOpaque(true);

                if (index < numProductos && dist.get(index) != null) {
                    cell.setText(dist.get(index));
                    cell.setBackground(new Color(173, 216, 230));
                    cell.setToolTipText("Producto: " + dist.get(index));
                } else {
                    cell.setBackground(new Color(211, 211, 211));
                    cell.setToolTipText("Espacio vacío");
                }

                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridPanel.add(cell);
            }
        }
        frame.add(gridPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        if (modificar) {
            JButton btnMover = new JButton("Mover Productos");
            btnMover.addActionListener(e -> moverProductos(nombre, frame, dist, h, w, algoritmo));
            buttonPanel.add(btnMover);

            JButton btnEliminar = new JButton("Eliminar Productos");
            btnEliminar.addActionListener(e -> eliminarProductos(nombre, frame, h, w, algoritmo));
            buttonPanel.add(btnEliminar);

            JButton btnCambiarNombre = new JButton("Cambiar Nombre");
            btnCambiarNombre.addActionListener(e -> cambiarNombre(nombre, frame, dist, h, w, algoritmo));
            buttonPanel.add(btnCambiarNombre);
        }

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setEnabled(true);
            }
            frame.dispose();
        });
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (parentFrame != null) {
                    parentFrame.setEnabled(true);
                }
            }
        });
        
        buttonPanel.add(btnCerrar);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    /**
     * Lógica para mover productos dentro de la distribución.
     * @param nombre Nombre de la distribución.
     * @param parentFrame Frame que muestra la distribución.
     * @param dist Lista de productos en la distribución.
     * @param h Altura de la estantería (número de filas).
     * @param w Anchura de la estantería (número de columnas).
     * @param algoritmo Nombre del algoritmo usado para generar la distribución.
     */
    private void moverProductos(String nombre, JFrame parentFrame, List<String> dist, int h, int w, String algoritmo) {
        JTextField producto1Field = new JTextField(10);
        JTextField producto2Field = new JTextField(10);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Producto 1:"));
        inputPanel.add(producto1Field);
        inputPanel.add(Box.createHorizontalStrut(15));
        inputPanel.add(new JLabel("Producto 2:"));
        inputPanel.add(producto2Field);

        int result = JOptionPane.showConfirmDialog(parentFrame, inputPanel,
                "Mover productos", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String producto1 = producto1Field.getText().trim();
            String producto2 = producto2Field.getText().trim();

            if (producto1.isEmpty() || producto2.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Debe introducir ambos productos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!presentationController.contieneproducto(nombre, producto1) ||
                    !presentationController.contieneproducto(nombre, producto2)) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Uno o ambos productos no están en la distribución.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (producto1.equals(producto2)) {
                JOptionPane.showMessageDialog(parentFrame,
                        "No puede seleccionar el mismo producto dos veces.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean editado = presentationController.editarDistribucion(nombre, producto1, producto2);
            if (editado) {
                JOptionPane.showMessageDialog(parentFrame, "Productos intercambiados con éxito.");
                parentFrame.dispose();
                mostrarDistribucion(this, nombre, dist, h, w, algoritmo, true);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "No se pudo mover los productos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Lógica para eliminar productos de una distribución.
     * @param nombre Nombre de la distribución.
     * @param parentFrame Frame que muestra la distribución.
     * @param h Altura de la estantería (número de filas).
     * @param w Anchura de la estantería (número de columnas).
     * @param algoritmo Nombre del algoritmo usado para generar la distribución.
     */
    private void eliminarProductos(String nombre, JFrame parentFrame, int h, int w, String algoritmo) {
        List<String> dist = presentationController.getDistList(nombre);
        List<String> distFiltrada = dist.stream().filter(Objects::nonNull).toList();
        JList<String> productList = new JList<>(distFiltrada.toArray(new String[0]));
        productList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        int result = JOptionPane.showConfirmDialog(parentFrame, scrollPane,
                "Seleccione productos para eliminar", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            List<String> selectedProducts = productList.getSelectedValuesList();
            if (selectedProducts.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Debe seleccionar al menos un producto.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si todos los productos están seleccionados
            if (selectedProducts.size() == productList.getModel().getSize()) {
                int confirmacion = JOptionPane.showConfirmDialog(parentFrame,
                        "Has seleccionado todos los productos. ¿Deseas eliminar la distribución completa?",
                        "Confirmar eliminación de distribución",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        if (presentationController.eliminarDistribucion(nombre)) {
                            JOptionPane.showMessageDialog(parentFrame, "Distribución eliminada con éxito.",
                                    "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                            parentFrame.dispose(); // Cerrar la ventana actual
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, "Error al eliminar la distribución.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(parentFrame,
                                "Error al eliminar la distribución: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return; // Salir de la función, ya no es necesario eliminar productos
            }

            // Eliminar solo los productos seleccionados
            boolean eliminado = presentationController.eliminarProductosDeDist(nombre,
                    selectedProducts.toArray(new String[0]));
            if (eliminado) {
                JOptionPane.showMessageDialog(parentFrame, "Productos eliminados con éxito.");
                parentFrame.dispose();
                mostrarDistribucion(this, nombre, dist, h, w, algoritmo, true);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "No se pudo eliminar los productos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Lógica para cambiar el nombre de una distribución.
     * @param nombre Nombre de la distribución.
     * @param parentFrame Frame que muestra la distribución.
     * @param dist Lista de productos en la distribución.
     * @param h Altura de la estantería (número de filas).
     * @param w Anchura de la estantería (número de columnas).
     * @param algoritmo Nombre del algoritmo usado para generar la distribución.
     */
    private void cambiarNombre(String nombre, JFrame parentFrame, List<String> dist, int h, int w, String algoritmo) {
        String nuevoNombre = JOptionPane.showInputDialog(parentFrame, "Introduce el nuevo nombre para la distribución:");

        if (nuevoNombre == null) {
            return;
        }

        if (nuevoNombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Debe introducir un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (presentationController.DistributionExists(nuevoNombre)) {
            JOptionPane.showMessageDialog(parentFrame, "Ya existe una distribución con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean renombrado = presentationController.cambiarNombreDist(nombre, nuevoNombre);
        if (renombrado) {
            JOptionPane.showMessageDialog(parentFrame, "El nombre de la distribución se ha cambiado a " + nuevoNombre + ".");
            parentFrame.dispose();
            mostrarDistribucion(this, nuevoNombre, dist, h, w, algoritmo, true);
        } else {
            JOptionPane.showMessageDialog(parentFrame, "No se pudo cambiar el nombre de la distribución.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método que encapsula la funcionalidad de seleccionar una distribución (se usa un pop-up). Para identificar las
     * distribuciones se muestran los nombres de estas y es lo que el usuario podrá seleccionar.
     * @param operacion Es un String con la operación que se quiere realizar.
     * @return Devuelve el pop-up de selección de distribuciones o null si no hay distribuciones disponibles.
     */
    private String seleccionarDistribucion(String operacion) {
        Set<String> DistsDisponibles = presentationController.verDists();

        if (DistsDisponibles == null || DistsDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay distribuciones disponibles.",
                    operacion, JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String[] opciones = DistsDisponibles.toArray(new String[0]);
        return (String) JOptionPane.showInputDialog(this,
                "Selecciona una distribución para " + operacion.toLowerCase() + ":",
                operacion,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);
    }

    /**
     * Método para obtener el nombre del algoritmo usado para generar la distribución como queremos mostrarlo.
     * @param algoritmo Nombre del algoritmo usado para generar la distribución.
     * @return Devuelve el nombre que queremos mostrar del algoritmo o "Desconocido" si no sabemos qué algoritmo se ha
     * usado para generar la distribución.
     */
    private String convertirNombreAlgoritmo(String algoritmo) {
        if (algoritmo.contains("Voraz")) {
            return "Voraz";
        } else if (algoritmo.contains("Aproximation") || algoritmo.contains("Kruskal")) {
            return "Aproximación";
        }
        return "Desconocido";
    }

}
