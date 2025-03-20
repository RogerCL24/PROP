package main.presentation.vistas;

import main.presentation.controllers.presentationController;
import main.presentation.utils.DesignUtils;

import main.domain.classes.LlistaProductes;
import main.domain.classes.exceptions.formatException;
import main.presentation.controllers.presentationController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

/**
 * Vista del apartado de introducir una nueva lista de productos. En esta vista se puede introducir una lista
 * introduciendo los parámetros de la lista de dos formas distintas: por terminal (o sea escribiendolo en el momento)
 * o por fichero.
 *
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class insertListView extends JFrame {

    /**
     * Panel principal de la vista.
     */
    private final JPanel buttonPanel = new JPanel();
    /**
     * Título de la vista: Introducir Nueva Lista de Productos.
     */
    private final JLabel title = new JLabel("Introducir Nueva Lista de Productos", JLabel.CENTER);
    /**
     * Botón que permite introducir los productos y grados de similitud entre ellos que tendrá la lista por terminal.
     */
    private final JButton btnDesdeTerminal = new JButton("Desde la pantalla");
    /**
     * Botón que permite introducir los productos y grados de similitud entre ellos que tendrá la lista por fichero.
     */
    private final JButton btnDesdeFichero = new JButton("Desde Fichero");
    /**
     * Botón que hace retornar al menú principal.
     */
    private final JButton btnVolver = new JButton("Volver");

    /**
     * Método principal de la vista, la constructora. Se configura la vista, se hace visible y se implementan los
     * listeners de los botones.
     * throws formatException Ha habido algún fallo al introducir la lista de productos mediante fichero.
     */
    public insertListView() {
        // Configuración de la ventana
        setBounds(600, 400, 600, 500);
        setResizable(false);
        setTitle("Introducir Nueva Lista de Productos");
        setLocationRelativeTo(null);

        // Usamos DesignUtils para aplicar fondo con círculos
        DesignUtils.setBackgroundWithCircles(this);

        setLayout(new BorderLayout());

        // Título de la ventana
        title.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 26));
        title.setForeground(new Color(225, 90, 127)); // Rosa oscuro
        add(title, BorderLayout.NORTH);

        // Panel de botones con GridLayout (si es igual siempre saldra igual siempre)
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));



        // Añadir botones y aplicarles diseño con DesignUtils
        DesignUtils.configureButton(btnDesdeTerminal);  // Botón de tamaño estándar
        DesignUtils.configureButton(btnDesdeFichero);   // Botón de tamaño estándar


        // Agregar botones al panel
        buttonPanel.add(btnDesdeTerminal);
        buttonPanel.add(btnDesdeFichero);

        add(buttonPanel, BorderLayout.CENTER);

        // Panel para el botón "Volver"
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Alineado a la izquierda
        backButtonPanel.setOpaque(false); // Hacer transparente para ver el fondo

        btnVolver.setPreferredSize(new Dimension(150, 40));
        btnVolver.setFont(new Font("Arial", Font.BOLD, 16));  // Fuente más grande para "Volver"
        DesignUtils.configureButton(btnVolver);  // Configurar el botón "Volver"

        backButtonPanel.add(btnVolver);
        add(backButtonPanel, BorderLayout.SOUTH);

        // Hacer visible la ventana y configurar cierre
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Acciones de los botones
        btnDesdeTerminal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] datos = solicitarProductosYRelaciones();

                if (datos == null) return;

                String productos = datos[0];
                String relaciones = datos[1];

                LlistaProductes lista = presentationController.introducirNuevaLista(productos, relaciones);

                // Comprovar si la llista és null
                if (lista == null) {
                    return;
                }

               guardarLista(lista);

            }
        });


        btnDesdeFichero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecciona un fichero para cargar la lista");
                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    try {
                        List<String> lines = Files.readAllLines(file.toPath());
                        if (lines.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "El fichero está vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        StringBuilder productos = new StringBuilder();
                        StringBuilder relaciones = new StringBuilder();
                        boolean leyendoRelaciones = false;
                        boolean etiquetaProductosEncontrada = false;
                        boolean etiquetaRelacionesEncontrada = false;

                        for (String line : lines) {
                            line = line.trim();

                            if (line.isEmpty()) continue;

                            // Normalizar etiquetas
                            String normalizedLine = line.toLowerCase().replaceAll("\\s", "");

                            if (normalizedLine.equals("#productos")) {
                                if (etiquetaProductosEncontrada) {
                                    throw new formatException("La etiqueta '#Productos' aparece más de una vez.");
                                }
                                etiquetaProductosEncontrada = true;
                            } else if (normalizedLine.equals("#relaciones")) {
                                if (!etiquetaProductosEncontrada) {
                                    throw new formatException("La etiqueta '#Relaciones' aparece antes de '#Productos'.");
                                }
                                if (etiquetaRelacionesEncontrada) {
                                    throw new formatException("La etiqueta '#Relaciones' aparece más de una vez.");
                                }
                                etiquetaRelacionesEncontrada = true;
                                leyendoRelaciones = true; // Cambiamos el contexto a relaciones
                            } else if (!leyendoRelaciones && etiquetaProductosEncontrada) {
                                productos.append(line).append(" ");
                            } else if (etiquetaRelacionesEncontrada) {
                                String[] partes = line.split("\\s+");
                                if (partes.length != 3) {
                                    throw new formatException("La relación '" + line + "' no tiene el formato correcto 'producto1 producto2 GdS'.");
                                }

                                // Validar grado de similitud (GdS)
                                try {
                                    float gds = Float.parseFloat(partes[2]);
                                    if (gds < 0 || gds > 1) {
                                        throw new formatException("El grado de similitud (GdS) debe estar entre 0 y 1 en la relación: '" + line + "'.");
                                    }
                                } catch (NumberFormatException e2) {
                                    throw new formatException("El grado de similitud (GdS) de la relación '" + line + "' debe ser un número válido.");
                                }

                                relaciones.append(line).append(";");
                            }
                        }


                        if (!etiquetaProductosEncontrada) {
                            throw new formatException("Falta la etiqueta '#Productos'.");
                        }

                        if (productos.toString().trim().isEmpty()) {
                            throw new formatException("No se encontraron productos después de la etiqueta '#Productos'.");
                        }

                        if (!etiquetaRelacionesEncontrada) {
                            throw new formatException("Falta la etiqueta '#Relaciones'.");
                        }

                        LlistaProductes lista = presentationController.introducirNuevaLista(productos.toString().trim(), relaciones.toString().trim());
                        if (lista != null) {
                           guardarLista(lista);
                        }

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error al leer el fichero: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error en el formato del fichero: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                presentationController.mainMenuView();
            }
        });

    }

    /**
     * Muestra una tabla con los productos y sus grados de similitud en un JOptionPane.
     *
     * @param lista La lista de productos con su matriz de similitud.
     */
    private void mostrarSimilitudes(LlistaProductes lista) {
        String[] columnNames = {"Producto", "Similitudes"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // Habilitar el ordenamiento de las filas
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        Map<String, Map<String, Float>> SM = lista.getSimilarityMatrix();
        for (String prod1 : SM.keySet()) {
            if (SM.containsKey(prod1) && !SM.get(prod1).isEmpty()) {
                tableModel.addRow(new Object[]{prod1, "(Click para ver relaciones)"});
            } else {
                tableModel.addRow(new Object[]{prod1, "Sin relaciones"});
            }
        }
        // Añadir listener para gestionar clics en las filas
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int modelRow = table.convertRowIndexToModel(row); // Usar el índice del modelo original
                    String producto = tableModel.getValueAt(modelRow, 0).toString();
                    if (SM.containsKey(producto) && !SM.get(producto).isEmpty()) {
                        JPanel relacionesPanel = new JPanel(new BorderLayout());

                        JLabel titleLabel = new JLabel("Relaciones de " + producto, JLabel.CENTER);
                        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

                        JList<String> relacionesList = new JList<>();
                        DefaultListModel<String> listModel = new DefaultListModel<>();

                        for (Map.Entry<String, Float> entry : SM.get(producto).entrySet()) {
                            String relacion = String.format("%s - %.2f%%", entry.getKey(), entry.getValue() * 100);
                            listModel.addElement(relacion);
                        }

                        relacionesList.setModel(listModel);
                        relacionesList.setFont(new Font("Arial", Font.PLAIN, 14));
                        JScrollPane listScrollPane = new JScrollPane(relacionesList);

                        relacionesPanel.add(titleLabel, BorderLayout.NORTH);
                        relacionesPanel.add(listScrollPane, BorderLayout.CENTER);

                        JOptionPane.showMessageDialog(null, relacionesPanel, "Relaciones de " + producto, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        JLabel infoLabel = new JLabel("Lista introducida correctamente:", JLabel.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(null, panel, "Productos y Similitudes", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Muestra un formulario gráfico para introducir productos y relaciones con listas y tablas.
     *
     * @return Un array con dos elementos: el primero es la lista de productos y el segundo son las relaciones, o null si se cancela.
     */
    private String[] solicitarProductosYRelaciones() {
        // Panel principal
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Lista de productos
        DefaultListModel<String> productosModel = new DefaultListModel<>();
        JList<String> productosList = new JList<>(productosModel);
        productosList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton agregarProductoBtn = new JButton("Agregar Producto");
        JButton eliminarProductoBtn = new JButton("Eliminar Producto");

        agregarProductoBtn.addActionListener(e -> {
            String nuevoProducto = JOptionPane.showInputDialog("Introduce el nombre del nuevo producto:");

            if (nuevoProducto != null) {
                nuevoProducto = nuevoProducto.trim();

                if (nuevoProducto.isEmpty()) { //
                    JOptionPane.showMessageDialog(null, "El nombre del producto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (nuevoProducto.contains(" ")) {
                    JOptionPane.showMessageDialog(null,
                            "El nombre del producto no puede contener espacios. Por favor, usa guiones (-, _) u otros símbolos para separar palabras.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (productosModel.contains(nuevoProducto)) {
                    JOptionPane.showMessageDialog(null, "Este producto ya está en la lista.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                productosModel.addElement(nuevoProducto);
            }
        });

        // Tabla de relaciones
        String[] columnas = {"Producto 1", "Producto 2", "Peso"};
        DefaultTableModel relacionesModel = new DefaultTableModel(columnas, 0);
        JTable relacionesTable = new JTable(relacionesModel);
        relacionesTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);


        eliminarProductoBtn.addActionListener(e -> {
            int[] selectedIndices = productosList.getSelectedIndices(); // Obtener productos seleccionados

            if (selectedIndices.length > 0) {
                boolean tieneRelaciones = false;

                // Verificar si alguno de los productos seleccionados tiene relaciones
                for (int selectedIndex : selectedIndices) {
                    String productoSeleccionado = productosModel.getElementAt(selectedIndex);

                    for (int i = 0; i < relacionesModel.getRowCount(); i++) {
                        String producto1 = relacionesModel.getValueAt(i, 0).toString();
                        String producto2 = relacionesModel.getValueAt(i, 1).toString();

                        if (productoSeleccionado.equals(producto1) || productoSeleccionado.equals(producto2)) {
                            tieneRelaciones = true;
                            break;
                        }
                    }

                    if (tieneRelaciones) break; // No seguir si ya sabemos que hay relaciones
                }

                if (tieneRelaciones) {
                    // Avisar al usuario si el producto tiene relaciones
                    JOptionPane.showMessageDialog(
                            null,
                            "No puedes eliminar productos que tienen relaciones asociadas. Elimina las relaciones primero.",
                            "Error al eliminar",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    // Confirmar eliminación
                    int confirmacion = JOptionPane.showConfirmDialog(
                            null,
                            "¿Estás seguro de que deseas eliminar los productos seleccionados?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        // Eliminar productos de atrás hacia adelante
                        for (int i = selectedIndices.length - 1; i >= 0; i--) {
                            productosModel.remove(selectedIndices[i]);
                        }
                    }
                }
            } else {
                // Avisar si no hay productos seleccionados
                JOptionPane.showMessageDialog(
                        null,
                        "Selecciona al menos un producto para eliminar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        JPanel productosPanel = new JPanel(new BorderLayout(5, 5));
        productosPanel.add(new JLabel("Productos:"), BorderLayout.NORTH);
        productosPanel.add(new JScrollPane(productosList), BorderLayout.CENTER);

        JPanel botonesProductosPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        botonesProductosPanel.add(agregarProductoBtn);
        botonesProductosPanel.add(eliminarProductoBtn);
        productosPanel.add(botonesProductosPanel, BorderLayout.SOUTH);

        JButton agregarRelacionBtn = new JButton("Agregar Relación");
        JButton eliminarRelacionBtn = new JButton("Eliminar Relación");

        agregarRelacionBtn.addActionListener(e -> {
            if (productosModel.isEmpty() || productosModel.size() < 2) {
                JOptionPane.showMessageDialog(null, "Primero debes agregar al menos dos productos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear los combos para seleccionar productos
            JComboBox<String> producto1Combo = new JComboBox<>();
            JComboBox<String> producto2Combo = new JComboBox<>();
            productosModel.elements().asIterator().forEachRemaining(producto -> {
                producto1Combo.addItem(producto);
                producto2Combo.addItem(producto);
            });

            // Campo para el peso
            JTextField pesoField = new JTextField();
            JPanel relacionPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            relacionPanel.add(new JLabel("Producto 1:"));
            relacionPanel.add(producto1Combo);
            relacionPanel.add(new JLabel("Producto 2:"));
            relacionPanel.add(producto2Combo);
            relacionPanel.add(new JLabel("Peso [0-1]:"));
            relacionPanel.add(pesoField);

            int result = JOptionPane.showConfirmDialog(null, relacionPanel, "Agregar Relación", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String producto1 = (String) producto1Combo.getSelectedItem();
                String producto2 = (String) producto2Combo.getSelectedItem();
                String peso = pesoField.getText().trim().replace(",",".");

                // Validar inputs
                assert producto1 != null;
                if (producto1.equals(producto2)) {
                    JOptionPane.showMessageDialog(null, "No puedes establecer una relación entre el mismo producto.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    float pesoFloat = Float.parseFloat(peso);
                    if (pesoFloat < 0 || pesoFloat > 1) {
                        JOptionPane.showMessageDialog(null, "El peso debe estar entre 0 y 1 (ambos incluidos).", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Verificar que no exista una relación duplicada (opcional)
                    for (int i = 0; i < relacionesModel.getRowCount(); i++) {
                        String existente1 = (String) relacionesModel.getValueAt(i, 0);
                        String existente2 = (String) relacionesModel.getValueAt(i, 1);
                        if ((existente1.equals(producto1) && existente2.equals(producto2)) ||
                                (existente1.equals(producto2) && existente2.equals(producto1))) {
                            JOptionPane.showMessageDialog(null, "Esta relación ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Agregar la relación si todo es válido
                    relacionesModel.addRow(new Object[]{producto1, producto2, peso});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El peso debe ser un número válido entre 0 y 1.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        eliminarRelacionBtn.addActionListener(e -> {
            // Obtener las filas seleccionadas
            int[] selectedRows = relacionesTable.getSelectedRows();

            if (selectedRows.length > 0) {
                // Confirmar eliminación
                int confirmacion = JOptionPane.showConfirmDialog(
                        null,
                        "¿Estás seguro de que deseas eliminar las relaciones seleccionadas?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        relacionesModel.removeRow(selectedRows[i]);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona al menos una relación.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        JPanel relacionesPanel = new JPanel(new BorderLayout(5, 5));
        relacionesPanel.add(new JLabel("Relaciones:"), BorderLayout.NORTH);
        relacionesPanel.add(new JScrollPane(relacionesTable), BorderLayout.CENTER);

        JPanel botonesRelacionesPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        botonesRelacionesPanel.add(agregarRelacionBtn);
        botonesRelacionesPanel.add(eliminarRelacionBtn);
        relacionesPanel.add(botonesRelacionesPanel, BorderLayout.SOUTH);

        // Añadir paneles al panel principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, productosPanel, relacionesPanel);
        panel.add(splitPane, BorderLayout.CENTER);

        // Mostrar el formulario en un JOptionPane
        int result = JOptionPane.showConfirmDialog(null, panel, "Introducir productos y relaciones", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Obtener productos
            StringBuilder productos = new StringBuilder();
            for (int i = 0; i < productosModel.size(); i++) {
                if (i > 0) productos.append(" ");
                productos.append(productosModel.get(i));
            }

            // Obtener relaciones
            StringBuilder relaciones = new StringBuilder();
            for (int i = 0; i < relacionesModel.getRowCount(); i++) {
                if (i > 0) relaciones.append(";");
                relaciones.append(relacionesModel.getValueAt(i, 0)).append(" ")
                        .append(relacionesModel.getValueAt(i, 1)).append(" ")
                        .append(relacionesModel.getValueAt(i, 2));
            }

            return new String[]{productos.toString(), relaciones.toString()};
        }

        return null;
    }

    /**
     * Función para guardar la lista de productos en las capas de dominio y persistencia.
     * @param lista LlistaProductes creada en la capa de presentación con la que se creará la lista que se guardará.
     *              Es básicamente la lista que se va a guardar.
     */
    private void guardarLista(LlistaProductes lista) {
        // Mostrar similitudes (común en ambos casos)
        mostrarSimilitudes(lista);

        // Preguntar si desea guardar o descartar
        int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas guardar la lista?", "Guardar Lista", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            boolean keepIn = true;

            while (keepIn) {
                String nombre = JOptionPane.showInputDialog("Introduce un nombre para la lista:");

                if (nombre == null) {
                    // El usuario presionó "Cancelar" o cerró el cuadro
                    int confirmarSalida = JOptionPane.showConfirmDialog(
                            null,
                            "¿Deseas cancelar el guardado de la lista?",
                            "Cancelar operación",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirmarSalida == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "Operación cancelada. La lista no se guardó.", "Error", JOptionPane.ERROR_MESSAGE);
                        keepIn = false;
                    }
                } else if (nombre.trim().isEmpty()) { // Se ha dejado el nombre vacío
                    JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío. Inténtalo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (presentationController.guardarLista(nombre, lista.getSimilarityMatrix())) {
                    JOptionPane.showMessageDialog(null, "Lista guardada correctamente con el nombre: " + nombre);
                    keepIn = false;
                } else {
                    JOptionPane.showMessageDialog(null, "El nombre '" + nombre + "' ya existe. Por favor, elige otro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else { // Se descarta la lista
            JOptionPane.showMessageDialog(null, "La lista ha sido descartada.");
        }
    }

}
