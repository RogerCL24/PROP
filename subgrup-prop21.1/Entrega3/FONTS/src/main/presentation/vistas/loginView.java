package main.presentation.vistas;

import main.presentation.controllers.presentationController;
import main.presentation.utils.DesignUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Primera vista verá y con la que interactuará el usuario. Permite al usuario iniciar sesión, registrarse en el sistema
 *  o cerrar el programa. Después de iniciar sesión o registrarse podrá acceder al resto de funcionalidades, será
 * llevado al menú principal (mainMenuView).
 *
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class loginView extends JFrame {

    /**
     * Panel principal de la vista.
     */
    private final JPanel buttonPanel = new JPanel();
    /**
     * Botón que lleva al inicio de sesión.
     */
    private final JButton btnLogin = new JButton("Iniciar Sesión");
    /**
     * Botón que lleva al registro de un nuevo usuario en el sistema.
     */
    private final JButton btnRegister = new JButton("Registrarse");
    /**
     * Botón que provoca el cierre del programa.
     */
    private final JButton btnExit = new JButton("Salir");

    /**
     * Único método de la vista. Se configura la vista, se hace visible y se implementan los listeners de los botones.
     */
    public loginView() {
        // Configuración básica de la ventana
        setBounds(600, 400, 400, 300);
        setResizable(true);
        setTitle("Supermarket Distribution Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Aplicar fondo decorativo con círculos
        DesignUtils.setBackgroundWithCircles(this);

        // Título estilizado
        JLabel labelTitulo = new JLabel("¡Bienvenido/a!", JLabel.CENTER);
        labelTitulo.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 26));
        labelTitulo.setForeground(new Color(225, 90, 127)); // Rosa oscuro
        add(labelTitulo, BorderLayout.NORTH);

        // Panel para los botones
        buttonPanel.setLayout(new GridLayout(3, 1, 40, 10)); // Espaciado más amplio entre botones
        buttonPanel.setOpaque(false); // Hacer el panel transparente
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        Dimension buttonSize = new Dimension(200, 50); // Ancho: 200 px, Alto: 50 px
        btnLogin.setPreferredSize(buttonSize);
        btnRegister.setPreferredSize(buttonSize);
        btnExit.setPreferredSize(buttonSize);

        // Configurar los botones con estilo usando DesignUtils
        DesignUtils.configureButton(btnLogin);
        DesignUtils.configureButton(btnRegister);
        DesignUtils.configureButton(btnExit);

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.CENTER);

        // Mostrar la ventana
        setVisible(true);

        // Listeners para los botones
        btnLogin.addActionListener(e -> {
            presentationController.handleLogin(); //Gestiona el inicio de sesión
        });

        btnRegister.addActionListener(e -> {
            presentationController.handleRegister(); //Gestiona el registro de un nuevo usuario
        });

        btnExit.addActionListener(e -> System.exit(0)); //Cierra el programa
    }
}
