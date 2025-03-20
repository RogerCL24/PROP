package main.presentation.vistas;

import main.presentation.controllers.presentationController;
import main.presentation.utils.DesignUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista que muestra al usuario los mensajes que ha enviado permitiéndole consultarlos. Los botones sirven para cambiar
 * a otra funcionalidad de mensajes, para ir a otra vista (messagesView o sendMessageView).
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */
public class sentMessagesView extends JFrame {

    /**
     * Botón que lleva a messagesView, la "vista principal" en para los mensajes donde se pueden ver los mensajes
     * recibidos.
     */
    private final JButton btnVerRecibidos = new JButton("Mensajes Recibidos");
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
     * Se muestran los mensajes enviados por el usuario y se pueden clicar para consultarlos.
     */
    public sentMessagesView() {
        // Configuración de la ventana
        setTitle("Mensajes Enviados");
        setBounds(600, 400, 600, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DesignUtils.setBackgroundWithCircles(this);

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Mensajes Enviados", JLabel.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 26));
        titleLabel.setForeground(new Color(225, 90, 127)); // Rosa oscuro
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Apilado vertical
        mainPanel.setOpaque(false);
        add(mainPanel, BorderLayout.CENTER);

        JLabel sentLabel = new JLabel("Mensajes enviados", JLabel.CENTER);
        sentLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        sentLabel.setForeground(new Color(105, 105, 105)); // Gris oscuro
        sentLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centramos el texto
        sentLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(sentLabel);

        // Contenedor para la tabla de mensajes
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        mainPanel.add(tablePanel);

        // Datos y columnas para la tabla
        String[] columnNames = {"Para", "Tipo", "Título", "Fecha", "Hora"};
        Object[][] sentMessagesData = presentationController.getSentMessagesCurrentUser();

        // Modelo de tabla no editable
        DefaultTableModel tableModel = new DefaultTableModel(sentMessagesData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que ninguna celda sea editable
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
                    String recipient = (String) messageTable.getValueAt(selectedRow, 0);
                    String type = (String) messageTable.getValueAt(selectedRow, 1);
                    String title = (String) messageTable.getValueAt(selectedRow, 2);
                    String date = (String) messageTable.getValueAt(selectedRow, 3);
                    String time = (String) messageTable.getValueAt(selectedRow, 4);

                    // Mostrar detalles del mensaje seleccionado
                    JOptionPane.showMessageDialog(
                            sentMessagesView.this,
                            "Detalles del mensaje seleccionado:\n\n"
                                    + "Para: " + recipient + "\n"
                                    + "Tipo: " + type + "\n"
                                    + "Título: " + title + "\n"
                                    + "Fecha: " + date + "\n"
                                    + "Hora: " + time,
                            "Mensaje Detallado",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(messageTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Panel para botones "Mensajes Recibidos" y "Enviar Mensaje"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Botones lado a lado
        buttonPanel.setOpaque(false);

        btnVerRecibidos.setPreferredSize(new Dimension(180, 40));
        btnEnviarMensaje.setPreferredSize(new Dimension(180, 40));
        DesignUtils.configureButton(btnVerRecibidos);
        DesignUtils.configureButton(btnEnviarMensaje);

        buttonPanel.add(btnVerRecibidos);
        buttonPanel.add(btnEnviarMensaje);
        mainPanel.add(buttonPanel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        btnVolver.setPreferredSize(new Dimension(150, 40));
        DesignUtils.configureButton(btnVolver);
        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.SOUTH);

        // Ir al menú principal
        btnVolver.addActionListener(e -> {
            presentationController.disposeViews();
        });

        //Ir a messagesView
        btnVerRecibidos.addActionListener(e -> {
            presentationController.messagesView();
            dispose();
        });

        // Ir a sendMessagesView
        btnEnviarMensaje.addActionListener(e -> {
            presentationController.sendMessagesView();
            dispose();
        });

        setVisible(true);
    }
}
