package main.presentation.vistas;

import main.domain.classes.Estrategia;
import main.domain.classes.types.Pair;
import main.presentation.controllers.presentationController;
import main.presentation.utils.DesignUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Vista que permite al usuario enviar nuevo mensaje con una lista o una distribución a una serie de usuarios
 * destinatarios.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */
public class sendMessageView extends JFrame {
    /**
     * JComboBox con los nombres de usuario de todos los destinatarios posibles. Permite seleccionar usuarios
     * destinatarios.
     */
    private final JComboBox<String> cbDestinatarios = new JComboBox<>();
    /**
     * JComboBOx que permite escoger si en el mensaje se enviará una lista de productos o una distribución.
     */
    private final JComboBox<String> cbTipoMensaje = new JComboBox<>(new String[]{"Selecciona una opción","Lista", "Distribución"});
    /**
     * JComboBox que permite escoger el elemento, el objeto (lista de productos o distribución), que se enviará en el
     * mensaje.
     */
    private final JComboBox<String> cbElementos = new JComboBox<>();
    /**
     * Botón que permite eliminar a un destinatario seleccionado del mensaje.
     */
    private final JButton btnEliminarDestinatario = new JButton("Eliminar");
    /**
     * Botón que hace retornar a messagesView, la "vista principal" en para los mensajes.
     */
    private final JButton btnVolver = new JButton("Volver");
    /**
     * Botón que provoca el envío del mensaje a los destinatarios indicados y con la información indicada.
     */
    private final JButton btnEnviar = new JButton("Enviar");
    /**
     * Lista con los destinatarios añadidos hasta el momento en el mensaje, se pueden ver y seleccionar para eliminarlos
     *  pulsando btnEliminarDestinatario si el usuario lo desea.
     */
    private final JList<String> listDestinatarios = new JList<>(new DefaultListModel<>());
    /**
     * Booleano que sirve para evitar que el primer destinatario se añada por defecto
     */
    private boolean initialized = false; //

    /**
     * Método principal de la vista, la constructora. Se configura la vista, se hace visible y se implementan los
     * listeners de los botones.
     */
    public sendMessageView() {
        // Configuración de la ventana
        setTitle("Enviar Mensaje");
        setBounds(600, 400, 600, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DesignUtils.setBackgroundWithCircles(this);

        // Panel principal con GroupLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);

        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Componentes
        JLabel lblTitulo = new JLabel("Enviar Mensaje", JLabel.CENTER);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 26));
        lblTitulo.setForeground(new Color(225, 90, 127)); // Rosa oscuro

        JLabel lblDestinatarios = new JLabel("Seleccionar Destinatarios:");
        JLabel lblTipoMensaje = new JLabel("Tipo de Mensaje:");
        JLabel lblElementos = new JLabel("Lista a enviar / Distribución a enviar:");

        JScrollPane scrollDestinatarios = new JScrollPane(listDestinatarios);
        scrollDestinatarios.setPreferredSize(new Dimension(200, 100));

        DesignUtils.configureButton(btnEliminarDestinatario);
        DesignUtils.configureButton(btnVolver);
        DesignUtils.configureButton(btnEnviar);

        // Layout horizontal y vertical
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(lblTitulo)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblDestinatarios)
                                        .addComponent(lblTipoMensaje)
                                        .addComponent(lblElementos))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(cbDestinatarios, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scrollDestinatarios)
                                        .addComponent(btnEliminarDestinatario)
                                        .addComponent(cbTipoMensaje, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbElementos, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(btnVolver, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnEnviar, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)) // Botones intercambiados
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDestinatarios)
                                .addComponent(cbDestinatarios, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(scrollDestinatarios, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnEliminarDestinatario)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblTipoMensaje)
                                .addComponent(cbTipoMensaje, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblElementos)
                                .addComponent(cbElementos, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnVolver, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE) // Botón Volver ahora está primero
                                .addComponent(btnEnviar, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)) // Botón Enviar ahora está segundo
        );

        add(mainPanel);

        // Acciones de los componentes
        cbDestinatarios.addActionListener(e -> {
            if (initialized) { // Solo agregar si la ventana ya fue inicializada
                agregarDestinatario();
            }
        });

        cbDestinatarios.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    agregarDestinatario();
                }
            }
        });

        btnEliminarDestinatario.addActionListener(e -> eliminarDestinatario());
        cbTipoMensaje.addActionListener(e -> {
            if (!"Selecciona una opción".equals(cbTipoMensaje.getSelectedItem())) {
                actualizarElementos();
            }
        });

        btnEnviar.addActionListener(e -> enviarMensaje());
        btnVolver.addActionListener(e -> volverAMensajes());
        cbTipoMensaje.setSelectedIndex(0);

        // Cargar datos iniciales
        cargarDestinatarios();
        actualizarElementos();

        initialized = true; // Marcar la ventana como inicializada para evitar eventos automáticos
        setVisible(true);
    }

    /**
     * Carga los posibles usuarios destinatarios del mensaje en cbDestinatarios.
     */
    private void cargarDestinatarios() {
        List<String> destinatarios = presentationController.mostrarDestinatariosPosibles();
        cbDestinatarios.addItem("-Todos-"); // Opción especial para seleccionar a todos

        if (destinatarios != null && !destinatarios.isEmpty()) {
            for (String destinatario : destinatarios) {
                cbDestinatarios.addItem(destinatario);
            }
        }
    }

    /**
     * Carga los nombres de las distribuciones o los nombres de las listas de productos disponibles en cbElementos.
     * Cambia dependiendo de lo que se escoja en cbTipoMensaje. Este método se usa para ir actualizando cbElementos de
     * forma acorde a lo que el usuario seleccione.
     */
    private void actualizarElementos() {
        String tipoSeleccionado = (String) cbTipoMensaje.getSelectedItem();
        cbElementos.removeAllItems();

        if ("Lista".equals(tipoSeleccionado)) { // tipoSelecciona es lista de productos
            Set<String> aux = presentationController.verListas();
            if (aux != null && !aux.isEmpty()) {
                List<String> listas = new ArrayList<>(aux);
                for (String lista : listas) {
                    cbElementos.addItem(lista);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No tienes listas disponibles para enviar.",
                        "Información", JOptionPane.WARNING_MESSAGE);
            }
        } else if ("Distribución".equals(tipoSeleccionado)) { // tipoSeleccionado es distribución
            Set<String> distribuciones = presentationController.verDists();
            if (distribuciones != null && !distribuciones.isEmpty()) {
                for (String distribucion : distribuciones) {
                    cbElementos.addItem(distribucion);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No tienes distribuciones disponibles para enviar.",
                        "Información", JOptionPane.WARNING_MESSAGE);
            }
        }
    }


    /**
     * Agrega un usuario seleccionado en cbDestinatarios a listDestinatarios, esto le añade a los usuarios
     * destinatarios.
     */
    private void agregarDestinatario() {
        String destinatario = (String) cbDestinatarios.getSelectedItem();
        if (destinatario != null) {
            DefaultListModel<String> model = (DefaultListModel<String>) listDestinatarios.getModel();
            if (!model.contains(destinatario)) {
                model.addElement(destinatario);
            }
        }
    }

    /**
     * Elimina un destinatario de listDestinatarios. Este método se llama cuando se clica btnEliminarDestinatario. En
     * caso de no haber seleccionado ningún nombre de destinatario no se hace nada.
     */
    private void eliminarDestinatario() {
        String seleccionado = listDestinatarios.getSelectedValue();
        if (seleccionado != null) {
            DefaultListModel<String> model = (DefaultListModel<String>) listDestinatarios.getModel();
            model.removeElement(seleccionado);
        }
    }

    /**
     * Método que implementa la creación, el guardado y el envío del mensaje con el objeto indicado y con/a los destinatarios
     * indicados. Este método se llama desde el listener de btnEnviar.
     */
    private void enviarMensaje() {
        DefaultListModel<String> destinatariosModel = (DefaultListModel<String>) listDestinatarios.getModel();

        String tipoMensaje = (String) cbTipoMensaje.getSelectedItem();
        String elementoSeleccionado = (String) cbElementos.getSelectedItem();

        if ("Selecciona una opción".equals(tipoMensaje)) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de mensaje válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tipoMensaje == null || elementoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de mensaje y un elemento válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si la lista de destinatarios está vacía, verificar si se seleccionó "-Todos-"
        if (destinatariosModel.isEmpty() || destinatariosModel.getElementAt(0).equals("-Todos-")) {
            destinatariosModel.removeAllElements();
            String seleccionado = (String) cbDestinatarios.getSelectedItem();
            if ("-Todos-".equals(seleccionado)) {
                List<String> destinatarios = presentationController.mostrarDestinatariosPosibles();

                if (destinatarios == null || destinatarios.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay destinatarios disponibles para enviar el mensaje.", "Información", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                for (String destinatario : destinatarios) {
                    destinatariosModel.addElement(destinatario);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe agregar al menos un destinatario.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Enviar mensajes para cada destinatario
        for (int i = 0; i < destinatariosModel.getSize(); i++) {
            String destinatario = destinatariosModel.getElementAt(i);

            // Llamada al controlador para enviar el mensaje
            try {
                if (tipoMensaje.equals("Lista")){
                    String nameList = elementoSeleccionado;
                    Map<String, Map<String, Float>> similarityMatrix = presentationController.getSimilarityMatrix(nameList);
                    presentationController.enviarMensajeLista(destinatario, nameList, similarityMatrix);
                }
                else {
                    String nameDist = elementoSeleccionado;

                    Pair<Integer, Integer> Prestage = presentationController.getPrestage(nameDist);  // Par de enteros <altura, longitud>
                    List<String> Dist = presentationController.getDistList(nameDist);
                    Estrategia estrategia = presentationController.getEstrategia(nameDist);
                    Map<String, Integer> mapa = presentationController.getMapa(nameDist);
                    presentationController.enviarMensajeDist(destinatario, nameDist, Prestage, Dist, estrategia, mapa);

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al enviar mensaje a: " + destinatario, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Mostrar confirmación de éxito
        JOptionPane.showMessageDialog(this, "Mensaje(s) enviado(s) con éxito.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);

        // Limpiar la lista de destinatarios y reiniciar los campos
        destinatariosModel.clear();
        cbTipoMensaje.setSelectedIndex(0);
        actualizarElementos();
    }


    /**
     * Método que retorna a la vista messagesView, vista "principal" de los mensajes.
     */
    private void volverAMensajes() {
        presentationController.messagesView();
        dispose();
    }
}
