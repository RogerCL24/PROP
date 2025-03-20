package main.presentation.vistas;

import main.presentation.controllers.presentationController;
import main.presentation.utils.DesignUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista que hace de menú principal del programa, precedida siempre por loginView.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */
public class mainMenuView extends JFrame {

    /**
     * Panel principal de la vista.
     */
    private final JPanel buttonPanel = new JPanel();
    /**
     * Título de la vista: Supermarket Generator.
     */
    private final JLabel title = new JLabel("SuperMarket Generator", JLabel.CENTER);
    /**
     * Label con el nombre de usuario del usuario que está usando el programa.
     */
    private final JLabel lblUsername;
    /**
     * Botón que lleva al apartado de introducir una nueva lista de productos en el sistema.
     */
    private final JButton btnNuevaLista = new JButton("Introducir una nueva lista de productos");
    /**
     * Botón que lleva al apartado de gestión de listas de productos que tiene el usuario.
     */
    private final JButton btnGestionarListas = new JButton("Gestionar listas de productos");
    /**
     * Botón que lleva al apartado de generación de una nueva distribución en el sistema.
     */
    private final JButton btnGenerarDistribucion = new JButton("Generar una distribución");
    /**
     * Botón que lleva al apartado de gestión de distribuciones que tiene el usuario.
     */
    private final JButton btnGestionarDistribuciones = new JButton("Gestionar las distribuciones generadas");
    /**
     * Botón que lleva al apartado de gestión de mensajes del usuario.
     */
    private final JButton btnMensajes = new JButton("Mensajes");
    /**
     * Botón que permite cerrar la sesión del usuario.
     */
    private final JButton btnCerrarSesion = new JButton("Cerrar sesión");
    /**
     * Botón que permite cerrar el programa.
     */
    private final JButton btnSalir = new JButton("Salir");

    /**
     * Único método de la vista. Se configura la vista, se hace visible y se implementan los listeners de los botones.
     * @param username Nombre de usuario del usuario que está usando el programa.
     */
    public mainMenuView(String username) {
        // Configuración de la ventana
        setBounds(600, 400, 600, 500);
        setResizable(false);
        setTitle("SuperMarket Generator");
        setLocationRelativeTo(null);
        // Usamos DesignUtils para aplicar fondo con círculos
        DesignUtils.setBackgroundWithCircles(this);

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false); // Mantenir el fons transparent

        title.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 26));
        title.setForeground(new Color(225, 90, 127)); // Rosa fosc
        title.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(title);

        lblUsername = new JLabel("Bienvenido/a, " + username + "!", JLabel.CENTER);
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 18));
        lblUsername.setForeground(new Color(105, 105, 105)); // Gris fosc
        topPanel.add(lblUsername);

        add(topPanel, BorderLayout.NORTH);

        // Panel de botones
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        // Añadir botones y aplicarles diseño con DesignUtils
        DesignUtils.configureButton(btnNuevaLista);
        DesignUtils.configureButton(btnGestionarListas);
        DesignUtils.configureButton(btnGenerarDistribucion);
        DesignUtils.configureButton(btnGestionarDistribuciones);
        DesignUtils.configureButton(btnMensajes);
        DesignUtils.configureButton(btnCerrarSesion);

        // Agregar botones al panel
        buttonPanel.add(btnNuevaLista);
        buttonPanel.add(btnGestionarListas);
        buttonPanel.add(btnGenerarDistribucion);
        buttonPanel.add(btnGestionarDistribuciones);
        buttonPanel.add(btnMensajes);
        buttonPanel.add(btnCerrarSesion);

        add(buttonPanel, BorderLayout.CENTER);

        // Panel para el botón de salir
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        btnSalir.setPreferredSize(new Dimension(80, 30));
        DesignUtils.configureButton(btnSalir); // Configurar el botón de salir
        bottomPanel.add(btnSalir);
        add(bottomPanel, BorderLayout.SOUTH);

        // Acción para "Introducir una nueva lista de productos"
        btnNuevaLista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationController.insertListView();
                dispose();
            }
        });

        // Acción para "Gestionar listas de productos"
        btnGestionarListas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationController.manageListsView();
                dispose();
            }
        });

        // Acción para "Generar una distribución"
        btnGenerarDistribucion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationController.generateDistributionView();
                dispose();
            }
        });

        // Acción para "Gestionar las distribuciones generadas"
        btnGestionarDistribuciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationController.manageDistView();
                dispose();
            }
        });

        // Acción para "Ver mensajes"
        btnMensajes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationController.messagesView();
                dispose();
            }
        });

        // Acción para "Cerrar sesión"
        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                        null,
                        "¿Estás seguro de que deseas cerrar sesión?",
                        "Confirmar cierre de sesión",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    setVisible(false);
                    dispose();
                    presentationController.showLoginView();
                }
            }
        });

        //Acción para "Salir"
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
