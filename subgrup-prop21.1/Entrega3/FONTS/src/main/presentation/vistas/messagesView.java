package main.presentation.vistas;

import main.presentation.controllers.presentationController;
import main.presentation.utils.DesignUtils;
import main.domain.classes.Mensaje;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Vista del apartado de gestión de mensajes del usuario. En esta vista se pueden ver los mensajes recibidos por el
 * usuario y mediante los botones se puede acceder a la funcionalidad de enviar un mensaje y la funcionalidad de ver
 * los mensajes recibidos por el usuario.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */
public class messagesView extends JFrame {

    /**
     * Botón que accede a la vista de ver mensajes enviados del usuario donde verá sus mensajes enviados.
     */
    private final JButton btnVerEnviados = new JButton("Mensajes Enviados");
    /**
     * Botón que accede a la vista de enviar mensaje donde el usuario podrá enviar un mensaje.
     */
    private final JButton btnEnviarMensaje = new JButton("Enviar Mensaje");
    /**
     * Botón que hace retornar al menú principal.
     */
    private final JButton btnVolver = new JButton("Volver");

    /**
     * Único método de la vista. Se configura la vista, se hace visible y se implementan los listeners de los botones.
     * Se muestran los mensajes recibidos por el usuario y se pueden clicar para consultarlos.
     */
    public messagesView() {
        // Configuración de la ventana
        setTitle("Mensajes Recibidos");
        setBounds(600, 400, 600, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DesignUtils.setBackgroundWithCircles(this);

        setLayout(new BorderLayout());

        // Título principal
        JLabel titleLabel = new JLabel("Mensajes Recibidos", JLabel.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 26));
        titleLabel.setForeground(new Color(225, 90, 127)); // Rosa oscuro
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Apilado vertical
        mainPanel.setOpaque(false);
        add(mainPanel, BorderLayout.CENTER);

        JLabel receivedLabel = new JLabel("Mensajes recibidos", JLabel.CENTER);
        receivedLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        receivedLabel.setForeground(new Color(105, 105, 105)); // Gris oscuro
        receivedLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centramos el texto
        receivedLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(receivedLabel);

        // Contenedor para la tabla de mensajes
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        mainPanel.add(tablePanel);

        // Obtener los mensajes reales del usuario
        String[] columnNames = {"De", "Tipo", "Título", "Fecha", "Hora"};
        Object[][] receivedMessagesData = loadReceivedMessages(); // Cargar los mensajes

        // Modelo de tabla no editable
        DefaultTableModel tableModel = new DefaultTableModel(receivedMessagesData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Configuración de la tabla
        JTable messageTable = new JTable(tableModel);
        messageTable.setFont(new Font("Arial", Font.PLAIN, 14));
        messageTable.setRowHeight(30);
        messageTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        messageTable.getTableHeader().setBackground(new Color(225, 90, 127)); // Fondo rosa oscuro para el header
        messageTable.getTableHeader().setForeground(Color.WHITE); // Texto blanco para el header

        // Acción al hacer clic en una fila
        messageTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = messageTable.getSelectedRow(); // Fila seleccionada
                if (selectedRow != -1) {
                    String sender = (String) messageTable.getValueAt(selectedRow, 0);
                    String type = (String) messageTable.getValueAt(selectedRow, 1);
                    String title = (String) messageTable.getValueAt(selectedRow, 2);
                    String date = (String) messageTable.getValueAt(selectedRow, 3);
                    String time = (String) messageTable.getValueAt(selectedRow, 4);

                    // Mostrar detalles del mensaje seleccionado
                    int option = JOptionPane.showOptionDialog(
                            messagesView.this,
                            "Detalles del mensaje seleccionado:\n\n"
                                    + "De: " + sender + "\n"
                                    + "Tipo: " + type + "\n"
                                    + "Título: " + title + "\n"
                                    + "Fecha: " + date + "\n"
                                    + "Hora: " + time + "\n\n"
                                    + "¿Quieres guardar este mensaje?",
                            "Mensaje Detallado",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new String[]{"Guardar", "Cancelar"},
                            "Guardar"
                    );

                    if (option == JOptionPane.YES_OPTION) {
                        String newName = null;
                        boolean keep_in = true;

                        while (keep_in) {
                            // Intentar guardar el mensaje
                            boolean correct = presentationController.guardarMensajeRecibido(sender, title, type, newName);

                            if (newName == null) newName = title;
                            if (correct) {

                                keep_in = false;

                                // Mensaje guardado exitosamente
                                JOptionPane.showMessageDialog(
                                        messagesView.this,
                                        "El objeto se ha guardado correctamente con el nombre: " + newName,
                                        "Éxito",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                            } else {
                                boolean keep_in_2 = true;
                                while (keep_in_2) {
                                    String mensaje;
                                    if (Objects.equals(newName,"")) {
                                        mensaje = "El nombre del mensaje no puede estar vacío. Introduce un nombre para guardar el mensaje:";
                                    }
                                    else {
                                        mensaje = "El título \"" + newName + "\" ya existe. Introduce un nuevo nombre para guardar el mensaje:";
                                    }
                                    newName = JOptionPane.showInputDialog(
                                            messagesView.this,
                                            mensaje,
                                            "Nombre duplicado",
                                            JOptionPane.WARNING_MESSAGE
                                    );

                                        if (newName == null || newName.trim().isEmpty()) {
                                        int cancelOption = JOptionPane.showConfirmDialog(
                                                messagesView.this,
                                                "No se ha introducido un nombre válido. ¿Quieres cancelar el guardado?",
                                                "Cancelar guardado",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE
                                        );

                                        if (cancelOption == JOptionPane.YES_OPTION) keep_in = keep_in_2 = false;

                                    }
                                    else keep_in_2 = false;
                                }

                            }
                        }
                    }

                }
            }
        });


        JScrollPane scrollPane = new JScrollPane(messageTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Panel para botones "Mensajes Enviados" y "Enviar Mensaje"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        btnVerEnviados.setPreferredSize(new Dimension(180, 40));
        btnEnviarMensaje.setPreferredSize(new Dimension(180, 40));
        DesignUtils.configureButton(btnVerEnviados);
        DesignUtils.configureButton(btnEnviarMensaje);

        buttonPanel.add(btnVerEnviados);
        buttonPanel.add(btnEnviarMensaje);
        mainPanel.add(buttonPanel);

        // Botón "Volver"
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        btnVolver.setPreferredSize(new Dimension(150, 40));
        DesignUtils.configureButton(btnVolver);
        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.SOUTH);

        // Acción para el botón "Volver"
        btnVolver.addActionListener(e -> {
            presentationController.disposeViews();
        });

        // Acción para el botón "Mensajes Enviados"
        btnVerEnviados.addActionListener(e -> {
            presentationController.sentMessagesView();
            setVisible(false);
        });

        // Acción para el botón "Enviar Mensaje"
        btnEnviarMensaje.addActionListener(e -> {
            presentationController.sendMessagesView();
            setVisible(false);
        });

        setVisible(true);
    }

    /**
     * Carga los mensajes recibidos del usuario actual.
     *
     * @return Una matriz con los datos de los mensajes.
     */
    private Object[][] loadReceivedMessages() {
        return presentationController.getInboxCurrentUser();
    }
}
