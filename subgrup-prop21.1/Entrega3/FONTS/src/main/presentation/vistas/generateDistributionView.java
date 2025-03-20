package main.presentation.vistas;


import main.domain.classes.Distribucion;
import main.domain.classes.Estrategia;
import main.domain.classes.algorism.AlgoritmoVoraz;
import main.domain.classes.algorism.Aproximation_Kruskal_ILS;
import main.presentation.controllers.presentationController;
import main.presentation.functions.ExportarDistribucion;
import main.presentation.utils.DesignUtils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Vista del apartado de generar una nueva distribución. Se pueden introducir los parámetros para la generación de la
 * distribución: De que lista se genera la distribución, si la estantería se ajusta manualmente o automáticamente, o si
 *  se usa el algoritmo Voraz o el de Aproximación.
 *
 *  @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class generateDistributionView extends JFrame {

    /**
     * Panel principal de la vista.
     */
    private final JPanel mainPanel = new JPanel();

    /**
     * Título de la vista: Generar Distribución.
     */
    private final JLabel title = new JLabel("Generar Distribución", JLabel.CENTER);

    /**
     * ComboBox que permite escoger la lista de productos que usará la distribución.
     */
    private final JComboBox<String> listaComboBox = new JComboBox<>(listoflists());

    /**
     * Radio button de la opción de Autoajuste de la estantería.
     */
    private final JRadioButton rbtnAutoajuste = new JRadioButton("Autoajuste", true);
    /**
     * Radio button de la opción de establecer manualmente la estantería.
     */
    private final JRadioButton rbtnManual = new JRadioButton("Ajuste Manual");
    /**
     * ButtonGroup del ajuste de la estantería, para relacionarlos.
     */
    private final ButtonGroup ajusteGroup = new ButtonGroup();
    /**
     * JTextField para introducir manualmente la altura que queremos que tenga la estantería (número de estantes).
     */
    private final JTextField txtAltura = new JTextField(10);
    /**
     * JTextField para introducir manualmente la anchura que queremos que tenga la estantería (número de productos por
     * estante).
     */
    private final JTextField txtAnchura = new JTextField(10);
    /**
     * Radio button para escoger el algoritmo de aproximación.
     */
    private final JRadioButton rbtnAproximacion = new JRadioButton("Aproximación", true);
    /**
     * Radio button para escoger el algoritmo Voraz.
     */
    private final JRadioButton rbtnVoraz = new JRadioButton("Voraz");
    /**
     * ButtonGroup de la selección de algoritmo, para relacionar los Radio Button.
     */
    private final ButtonGroup algoritmoGroup = new ButtonGroup();
    /**
     * Botón que lleva a generar la distribución con la lista de productos escogida y las especificaciones escogidas.
     */
    private final JButton btnGenerar = new JButton("Generar");
    /**
     * Botón que hace retornar al menú principal.
     */
    private final JButton btnVolver = new JButton("Volver");

    /**
     * Método principal de la vista, la constructora. Se configura la vista, se hace visible, el usuario puede
     * seleccionar la lista de productos que quiere usar, el ajuste que quiera y el algoritmo que quiere usar,
     * y se implementan los listeners de los botones.
     */
    public generateDistributionView() {
        setBounds(600, 400, 700, 550);
        setResizable(false);
        setTitle("Generar Distribución");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana

        // Estilo general
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255)); // Azul claro

        title.setFont(new Font("Verdana", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(70, 130, 180)); // Azul acero

        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaciado

        // Sección de selección de lista
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BorderLayout());
        listaPanel.setBorder(BorderFactory.createTitledBorder("Seleccionar lista"));
        listaPanel.add(new JLabel("Lista disponible:"), BorderLayout.WEST);
        listaPanel.add(listaComboBox, BorderLayout.CENTER);
        listaPanel.setOpaque(false);
        mainPanel.add(listaPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Espaciado

        // Sección de ajuste
        JPanel ajustePanel = new JPanel();
        ajustePanel.setLayout(new BoxLayout(ajustePanel, BoxLayout.Y_AXIS));
        ajustePanel.setBorder(BorderFactory.createTitledBorder("Seleccionar ajuste de las dimensiones de la estanteria"));
        ajustePanel.setOpaque(false);

        JPanel ajusteOpcionesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ajusteOpcionesPanel.setOpaque(false);
        ajusteOpcionesPanel.add(rbtnAutoajuste);
        ajusteOpcionesPanel.add(rbtnManual);
        ajustePanel.add(ajusteOpcionesPanel);

        JPanel dimensionesPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        dimensionesPanel.setOpaque(false);
        dimensionesPanel.add(new JLabel("Altura:"));
        dimensionesPanel.add(txtAltura);
        dimensionesPanel.add(new JLabel("Anchura:"));
        dimensionesPanel.add(txtAnchura);
        ajustePanel.add(dimensionesPanel);
        toggleManualInputFields(false); // Iniciar desactivado

        mainPanel.add(ajustePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Espaciado

        // Sección de selección de algoritmo
        JPanel algoritmoPanel = new JPanel();
        algoritmoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        algoritmoPanel.setBorder(BorderFactory.createTitledBorder("Seleccionar algoritmo"));
        algoritmoPanel.setOpaque(false);
        algoritmoPanel.add(rbtnVoraz);
        algoritmoPanel.add(rbtnAproximacion);
        mainPanel.add(algoritmoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaciado

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        btnGenerar.setPreferredSize(new Dimension(150, 40));
        btnVolver.setPreferredSize(new Dimension(150, 40));
        DesignUtils.configureButton(btnGenerar);
        DesignUtils.configureButton(btnVolver);
        buttonPanel.add(btnVolver);
        buttonPanel.add(btnGenerar);
        mainPanel.add(buttonPanel);

        add(mainPanel);

        // Configuración de los grupos de botones
        ajusteGroup.add(rbtnAutoajuste);
        ajusteGroup.add(rbtnManual);
        algoritmoGroup.add(rbtnVoraz);
        algoritmoGroup.add(rbtnAproximacion);

        // Acción para alternar entre ajustes
        rbtnAutoajuste.addActionListener(e -> toggleManualInputFields(false));
        rbtnManual.addActionListener(e -> toggleManualInputFields(true));

        // Acción para el botón "Volver"
        btnVolver.addActionListener(e -> {
            dispose();
            presentationController.mainMenuView();
        });

        // Acción para el botón "Generar"
        btnGenerar.addActionListener(e -> generarDistribucion());

        setVisible(true);
    }

    /**
     * Método que hace alternar entre mostrar los JTextFields para introducir manualment al altura y anchura de la
     * estantería y no mostrarlos.
     * @param enable Es un boolean, si es true se pasará a mostrar los JTextFields y si es false se pasará a no
     *               mostrarlos.
     */
    private void toggleManualInputFields(boolean enable) {
        txtAltura.setEnabled(enable);
        txtAnchura.setEnabled(enable);
    }

    /**
     * Método que consigue los nombres de las listas de productos creadas antes de mostrar la vista.
     * @return Devuelve los nombres de las listas de productos creadas en un array o si no hay ninguna creada devuelve
     * "No has creado ninguna lista.".
     */
    private String[] listoflists(){
        String[] list2;
        Set<String> lista = presentationController.verListas();

        if (lista == null) {
            //No hay ninguna lista creada
            list2 = new String[1];
            list2[0] = "No has creado ninguna lista.";
        }
        else {
            //Se rellena el array con los nombres de las listas de productos ya creadas
            String[] lista1 =  lista.toArray(new String[0]);
            list2 = new String[lista1.length];
            for (int i = 0; i < lista1.length; i++){
                list2[i] = lista1[i];
            }
        }
        return list2;
    }

    /**
     * Método que se llama cuando se clica el botón "Generar" y genera la distribución. Gestiona toda la generación
     * de la distribución.
     */
    private void generarDistribucion() {
        if (Objects.equals(listaComboBox.getSelectedItem(), "No has creado ninguna lista.")) {
            JOptionPane.showMessageDialog(null, "Aún no has introducido ninguna lista. Vuelve a intentarlo una vez introducida alguna.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String listaSeleccionada = (String) listaComboBox.getSelectedItem();
        String ajuste = rbtnManual.isSelected() ? "Manual" : "Autoajuste";
        String algoritmo = rbtnVoraz.isSelected() ? "Voraz" : "Aproximación";

        int altura = 0, anchura = 0;
        if ("Manual".equals(ajuste)) { //Ajuste Manual
            try {
                // Validar dimensiones ingresadas manualmente
                altura = validarEnteroPositivo(txtAltura.getText(), "Altura");
                anchura = validarEnteroPositivo(txtAnchura.getText(), "Anchura");

                // Verificar si el tamaño de la estantería es suficiente
                int capacidadTotal = altura * anchura;
                int cantidadProductos = presentationController.getSizeList(listaSeleccionada);
                if (capacidadTotal < cantidadProductos) {
                    JOptionPane.showMessageDialog(null,
                            "Las dimensiones especificadas (" + altura + " x " + anchura + ") no son suficientes para almacenar los " +
                                    cantidadProductos + " productos.",
                            "Dimensiones insuficientes",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Entrada inválida", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        else { //Autoajuste
            int[] dimensiones = presentationController.autoAjuste(listaSeleccionada);
            altura = dimensiones[0];
            anchura = dimensiones[1];
        }

        try {
            Distribucion dist = presentationController.generar_Distribucion(listaSeleccionada, altura, anchura, algoritmo);
            // Mostrar distribución
            mostrarDistribucion(this, dist, altura, anchura, algoritmo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al generar la distribución: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método que transforma en entero un String que es la altura o la anchura de la estantería.
     * @param input String con la altura o la anchura.
     * @param campo "Altura" si es la altura o "Anchura" si es la anchura.
     * @return Devuelve el entero positivo que representa la altura o la anchura de la estantería.
     * @throws NumberFormatException Se lanza una al principio, pero las que son excepciones son cuando el input está
     * vacío o lo que se ha introducido no es correcto.
     */
    private int validarEnteroPositivo(String input, String campo) throws NumberFormatException {
        if (input == null || input.trim().isEmpty()) {
            throw new NumberFormatException(campo + " no puede estar vacío.");
        }
        try {
            int valor = Integer.parseInt(input.trim());
            if (valor <= 0) {
                throw new NumberFormatException(campo + " debe ser un entero positivo.");
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new NumberFormatException(campo + " debe ser un número entero válido.");
        }
    }


    /**
     * Muestra la distribución generada en una interfaz gráfica.
     *
     * @param parentFrame La ventana principal que se desactiva mientras esta ventana está abierta.
     * @param distribucion La distribución a mostrar.
     * @param h Altura (filas) de la estantería.
     * @param w Anchura (columnas) de la estantería.
     * @param algoritmo Nombre del algoritmo usado.
     */
    private void mostrarDistribucion(JFrame parentFrame, Distribucion distribucion, int h, int w, String algoritmo) {
         if (parentFrame != null) {
            parentFrame.setEnabled(false);
        }
        // Crear la ventana principal
        JFrame frame = new JFrame("Distribución Generada");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);


        // Panel superior para mostrar información sobre el algoritmo usado
        JPanel infoPanel = new JPanel(new BorderLayout());
        JLabel lblInfo = new JLabel("Algoritmo usado: " + algoritmo, JLabel.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(lblInfo, BorderLayout.NORTH);
        frame.add(infoPanel, BorderLayout.NORTH);

        // Panel central para la representación gráfica de la distribución
        JPanel gridPanel = new JPanel(new GridLayout(h, w, 5, 5)); // Espaciado entre celdas
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(gridPanel, BorderLayout.CENTER);

        // Lista de productos de la distribución
        List<String> dist = distribucion.getDist();
        int numProductos = dist.size();

        // Crear celdas para la estantería
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int index = (i * w + j); // Índice de la lista de productos
                JLabel cell = new JLabel("", JLabel.CENTER);
                cell.setOpaque(true); // Necesario para aplicar colores de fondo

                if (index < numProductos && dist.get(index) != null) {
                    // Producto encontrado: mostrar el nombre y colorear la celda
                    cell.setText(dist.get(index));
                    cell.setBackground(new Color(173, 216, 230)); // Azul claro para productos
                    cell.setToolTipText("Producto: " + dist.get(index)); // Detalles del producto
                } else {
                    // Espacio vacío: mostrar como una celda gris vacía
                    cell.setBackground(new Color(211, 211, 211)); // Gris claro
                    cell.setToolTipText("Espacio vacío");
                }

                // Añadir bordes para mejorar la separación visual
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridPanel.add(cell); // Añadir la celda al panel de la estantería
            }
        }

        // Panel inferior para los botones de guardar/exportar
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton btnGuardar = new JButton("Guardar distribución");
        JButton btnGuardarYExportar = new JButton("Guardar y Exportar");
        JButton btnCancelar = new JButton("Cancelar");
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnGuardarYExportar);
        buttonPanel.add(btnCancelar);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarDistribucion(parentFrame, false, distribucion, frame));
        btnGuardarYExportar.addActionListener(e -> guardarDistribucion(parentFrame, true, distribucion, frame));

        btnCancelar.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "La distribución ha sido descartada.");
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

        // Mostrar la ventana
        frame.setVisible(true);
    }

    /**
     * Función común para guardar una distribución en las capas de dominio y persistencia.
     *
     * @param parentFrame La ventana principal que se volverá a activar después de cerrar esta ventana.
     * @param exportar booleano indicando si se quiere exportar o no la distribución además de guardarla.
     * @param distribucion Distribucion creada en la capa de presentación con la que se creará la distribución que se
     *                     guardará. Es básicamente la distribución que se va a guardar.
     * @param frame Frame que muestra la distribución creada y permitiendo guardar, está aquí para cerrarlo una vez
     *              acabados.
     */
    private void guardarDistribucion(JFrame parentFrame, boolean exportar, Distribucion distribucion, JFrame frame) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Introduce un nombre para la distribución:");
        JTextField textField = new JTextField(10);
        panel.add(label);
        panel.add(textField);

        int option = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Nueva Distribución",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
            return; // El usuario canceló o cerró el cuadro
        }

        String nombre = textField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (presentationController.DistributionExists(nombre)) {
            JOptionPane.showMessageDialog(null, "El nombre " + nombre + " ya es nombre de una distribución.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean guardada = presentationController.guardarDistribucion(
                nombre,
                distribucion.getPrestage(),
                distribucion.getDist(),
                distribucion.getEstrategia(),
                distribucion.getMapa()
        );

        if (guardada) {
            JOptionPane.showMessageDialog(null, "Distribución guardada correctamente.");

            // Si el botón fue "Guardar y Exportar", hacemos la exportación
            if (exportar) {
                ExportarDistribucion exportador = new ExportarDistribucion();
                String rutaFichero = exportador.exportarDistribucion(nombre, distribucion);

                if (rutaFichero != null) {
                    JOptionPane.showMessageDialog(null, "Distribución exportada con éxito a " + rutaFichero + ".");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al exportar la distribución.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
           if (parentFrame != null) {
                parentFrame.setEnabled(true);
            }
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo guardar la distribución.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
