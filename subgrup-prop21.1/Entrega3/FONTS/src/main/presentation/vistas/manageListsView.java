package main.presentation.vistas;

import main.domain.classes.LlistaProductes;
import main.presentation.controllers.presentationController;
import main.presentation.utils.DesignUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Vista de la gestión de listas de productos del usuario. Se puede consultar una lista, modificarla o eliminarla. Hay
 * 4 posibles modificaciones: cambiar el nombre de la lista, introducir nuevos productos a la lista, eliminar productos
 * de la lista y modificar los grados de similitud entre los productos de la lista.
 *
 * @author David Mas Escude (david.mas@estudiantat.upc.edu)
 */
public class manageListsView extends JFrame {
    /**
     * Panel principal de la vista.
     */
    private final JPanel buttonPanel = new JPanel();
    /**
     * Título de la vista: Gestionar listas de productos.
     */
    private final JLabel title = new JLabel("Gestionar listas de productos", JLabel.CENTER);
    /**
     * Subtítulo de la vista, pregunta al usuario que quiere hacer indicándole así que tiene diferentes opciones.
     */
    private final JLabel subtitle = new JLabel("¿Qué desea hacer?", JLabel.CENTER);
    /**
     * Botón que lleva a consultar una lista de productos.
     */
    private final JButton btnConsultar = new JButton("Consultar una lista");
    /**
     * Botón que lleva a la modificación de una lista de productos.
     */
    private final JButton btnModificar = new JButton("Modificar una lista");
    /**
     * Botón que permite al usuario poder eliminar una lista de productos.
     */
    private final JButton btnEliminar = new JButton("Eliminar una lista");
    /**
     * Botón que hace retornar al menú principal.
     */
    private final JButton btnVolver = new JButton("Volver");

    /**
     * Método principal de la vista, la constructora. Se configura la vista, se hace visible y se implementan los
     * listeners de los botones.
     */
    public manageListsView() {
        // Configuración de la ventana
        setBounds(600, 400, 600, 500); // Tamaño ajustado para que coincida con el del menú principal
        setResizable(false);
        setTitle("Gestionar listas de productos");

        // Usamos DesignUtils para aplicar fondo con círculos
        DesignUtils.setBackgroundWithCircles(this);

        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

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

        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));
        buttonPanel.setOpaque(false); // Hacer el panel transparente
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        DesignUtils.configureButton(btnConsultar);
        DesignUtils.configureButton(btnModificar);
        DesignUtils.configureButton(btnEliminar);
        DesignUtils.configureButton(btnVolver);

        buttonPanel.add(btnConsultar);
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnVolver);

        add(buttonPanel, BorderLayout.CENTER);

        // Panel para el botón de volver
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false); // Hacer el panel transparente
        btnVolver.setPreferredSize(new Dimension(150, 40));
        DesignUtils.configureButton(btnVolver);
        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.SOUTH);

        // Consultar
        btnConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<String> listasDisponibles = presentationController.verListas();

                if (listasDisponibles == null) {
                    JOptionPane.showMessageDialog(null, "No hay listas disponibles.",
                            "Consultar listas", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String[] opciones = listasDisponibles.toArray(new String[0]);
                String seleccion = (String) JOptionPane.showInputDialog(
                        null,
                        "Selecciona una lista para consultar:",
                        "Consultar listas",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );

                if (seleccion == null) {
                    return;
                }

                JOptionPane.showMessageDialog(null, "Has seleccionado la lista: " + seleccion);

                try {
                    LlistaProductes lista = presentationController.obtenerProductosDeLista(seleccion);

                    mostrarSimilitudes(lista, false, "Consultar");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al consultar la lista: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Acción para "Modificar una lista"
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<String> listasDisponibles = presentationController.verListas();

                String seleccionLista = seleccionarLista(
                        listasDisponibles,
                        "Modificar lista",
                        "Selecciona una lista para modificar:"
                );

                if (seleccionLista == null) {return;}

                // Opciones de modificación
                String[] opcionesModificacion = {"Cambiar nombre", "Introducir productos", "Eliminar productos", "Modificar grados de similitud"};
                String seleccionOpcion = (String) JOptionPane.showInputDialog(null, "Selecciona una acción:",
                        "Modificar lista - " + seleccionLista, JOptionPane.PLAIN_MESSAGE, null, opcionesModificacion, opcionesModificacion[0]);

                if (seleccionOpcion == null) {return;}

                try {
                    switch (seleccionOpcion) {
                        case "Cambiar nombre":
                            boolean keep_in = true;
                            while (keep_in) {
                                String nuevoNombre = JOptionPane.showInputDialog(null, "Introduce el nuevo nombre:");
                                if (nuevoNombre != null) {
                                    if (!nuevoNombre.trim().isEmpty()) { // Verifica que el nombre no esté vacío
                                        boolean resultado = presentationController.cambiarNombre(seleccionLista, nuevoNombre.trim());
                                        if (resultado) {
                                            JOptionPane.showMessageDialog(null, "Nombre cambiado con éxito.",
                                                    "Modificar lista", JOptionPane.INFORMATION_MESSAGE);
                                            keep_in = false;
                                        } else {
                                            JOptionPane.showMessageDialog(null, "No se pudo cambiar el nombre. El nuevo nombre ya existe.",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío. Inténtalo nuevamente.",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                                else keep_in = false;
                            }
                            break;

                        case "Introducir productos":
                            LlistaProductes lista = presentationController.obtenerProductosDeLista(seleccionLista);
                            mostrarSimilitudes(lista, false, "Introducir");
                            break;

                        case "Eliminar productos":
                            LlistaProductes listaEliminar = presentationController.obtenerProductosDeLista(seleccionLista);
                            mostrarSimilitudes(listaEliminar, false, "Eliminar");
                            break;

                        case "Modificar grados de similitud":
                            LlistaProductes listaModificar = presentationController.obtenerProductosDeLista(seleccionLista);
                            mostrarSimilitudes(listaModificar, true, "Modificar");
                            break;


                        default:
                            JOptionPane.showMessageDialog(null, "Acción no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al modificar la lista: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para "Eliminar una lista"
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<String> listasDisponibles = presentationController.verListas();

                if (listasDisponibles == null || listasDisponibles.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No tienes listas disponibles para eliminar.",
                            "Información", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Seleccionar la lista a eliminar
                String seleccion = seleccionarLista(
                        listasDisponibles,
                        "Eliminar listas",
                        "Selecciona una lista para eliminar:"
                );

                if (seleccion == null) {
                    return; // El usuario canceló la selección
                }

                // Confirmar la eliminación
                int confirmacion = JOptionPane.showConfirmDialog(
                        null,
                        "¿Estás seguro de que deseas eliminar la lista '" + seleccion + "'?\n\n" +
                                "⚠ Esto eliminará todos los productos de la lista y no se podrá deshacer.",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        // Llamar al controlador para eliminar la lista
                        boolean eliminada = presentationController.eliminarLista(seleccion);

                        if (eliminada) {
                            JOptionPane.showMessageDialog(null, "Lista eliminada con éxito.",
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al eliminar la lista.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al intentar eliminar la lista: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });




        // Acción para "Volver al menú principal"
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationController.mainMenuView();
                dispose();
            }
        });

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Muestra una tabla interactiva con los productos y sus grados de similitud.
     *
     * @param lista    La lista de productos con su matriz de similitud.
     * @param editable Indica si la tabla permite edición.
     * @param accion   Define la acción ("Eliminar" o "Modificar").
     */
    private void mostrarSimilitudes(LlistaProductes lista, boolean editable, String accion) {
        // Crear modelo de tabla con dos columnas
        String[] columnNames = {"Producto", "Relaciones"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return editable && column == 1; // Solo permite edición en la columna de relaciones
            }
        };

        JTable table = new JTable(tableModel);

        // Habilitar el ordenamiento de las filas
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        // Llenar la tabla con los productos y las relaciones
        Map<String, Map<String, Float>> SMOriginal = lista.getSimilarityMatrix();
        Map<String, Map<String, Float>> SM = new HashMap<>();

        for (Map.Entry<String, Map<String, Float>> entry : SMOriginal.entrySet()) {
            SM.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }

        for (String prod1 : SM.keySet()) {
            String relaciones;
            boolean tieneRelacionesValidas = SM.get(prod1).values().stream().anyMatch(valor -> valor > 0);
            if (tieneRelacionesValidas) {
                relaciones = "(Click para ver relaciones)";
            } else {
                relaciones = editable ? "Haz click para añadir" : "Sin relaciones";
            }

            tableModel.addRow(new Object[]{prod1, relaciones});
        }



        // Mapa temporal para almacenar los cambios realizados en las relaciones
        Map<String, Map<String, Float>> relacionesModificadas = new HashMap<>();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                if (row != -1 && column == 1) {
                    int modelRow = table.convertRowIndexToModel(row);
                    String producto = tableModel.getValueAt(modelRow, 0).toString();

                    // Obtener las relaciones actuales del producto
                    Map<String, Float> relacionesActuales = SM.getOrDefault(producto, new HashMap<>());

                    JPanel relacionesPanel = new JPanel(new BorderLayout());
                    JLabel titleLabel = new JLabel("Relaciones de " + producto, JLabel.CENTER);
                    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

                    JTextArea relacionesTextArea = new JTextArea();
                    relacionesTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

                    // Mostrar las relaciones actuales (o vacío si no hay)
                    StringBuilder relacionesText = new StringBuilder();
                    for (Map.Entry<String, Float> entry : relacionesActuales.entrySet()) {
                        if (entry.getValue() > 0) {
                            relacionesText.append(String.format("%s - %.2f%%\n", entry.getKey(), entry.getValue() * 100));
                        }
                    }
                    relacionesTextArea.setText(relacionesText.toString());
                    relacionesTextArea.setEditable(editable); // Editable solo si es permitido

                    JScrollPane textScrollPane = new JScrollPane(relacionesTextArea);

                    // Instrucciones adicionales
                    JLabel instruccionesLabel = new JLabel("<html><i>Formato: producto - porcentaje (e.g., p1 - 87.00)</i></html>", JLabel.CENTER);
                    instruccionesLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                    relacionesPanel.add(instruccionesLabel, BorderLayout.SOUTH);

                    relacionesPanel.add(titleLabel, BorderLayout.NORTH);
                    relacionesPanel.add(textScrollPane, BorderLayout.CENTER);

                    int option = JOptionPane.showConfirmDialog(null, relacionesPanel, "Relaciones de " + producto,
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (option == JOptionPane.OK_OPTION && editable) {
                        String[] nuevasRelaciones = relacionesTextArea.getText().split("\n");
                        Map<String, Float> relacionesActualizadas = new HashMap<>();
                        boolean hayCambios = false;

                        for (String relacion : nuevasRelaciones) {
                            String[] partes = relacion.split("-");
                            if (partes.length == 2) {
                                String productoRelacionado = partes[0].trim();
                                if (productoRelacionado.equals(producto)) {
                                    JOptionPane.showMessageDialog(null,
                                            "No puedes establecer una relación con el mismo producto: " + producto,
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    continue;
                                } else if (!SM.containsKey(productoRelacionado)) {
                                    JOptionPane.showMessageDialog(null,
                                            "El producto '" + productoRelacionado + "' no existe en la lista de productos.",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    continue;
                                }

                                try {
                                    float similitud = Float.parseFloat(partes[1].replace(",", ".").replace("%", "").trim()) / 100;

                                    if (similitud < 0 || similitud > 1) {
                                        JOptionPane.showMessageDialog(null,
                                                "La similitud debe estar entre 0 y 100%. Valor ingresado: " + similitud * 100 + "%",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                        continue;
                                    }

                                    if (similitud == 0) {
                                        if (SM.getOrDefault(producto, new HashMap<>()).containsKey(productoRelacionado)) {
                                            hayCambios = true;
                                            relacionesActualizadas.put(productoRelacionado, 0f);
                                        }
                                    } else {
                                        if (similitud != SM.getOrDefault(producto, new HashMap<>())
                                                .getOrDefault(productoRelacionado, -1f)) {
                                            hayCambios = true;
                                            relacionesActualizadas.put(productoRelacionado, similitud);
                                        }
                                    }




                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null,
                                            "El formato de similitud no es válido en la relación: " + relacion,
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else if (!relacion.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(null,
                                        "El formato de la relación no es válido: " + relacion + "\nUsa el formato: producto - porcentaje (e.g., p1 - 87.00)",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        if (hayCambios) {
                            // Actualizar las relaciones del producto actual en SM
                            Map<String, Float> relacionesProducto = SM.getOrDefault(producto, new HashMap<>());
                            Map<String, Float> relacionesFusionadas = new HashMap<>(relacionesProducto);
                            relacionesFusionadas.putAll(relacionesActualizadas);

                            // Eliminar relaciones con valor 0
                            relacionesFusionadas.entrySet().removeIf(entry -> entry.getValue() == 0);

                            // Actualizar el mapa `SM` y el modelo de tabla para el producto actual
                            if (!relacionesFusionadas.isEmpty()) {
                                SM.put(producto, relacionesFusionadas);
                                tableModel.setValueAt("(Click para ver relaciones)", modelRow, 1);
                            } else {
                                SM.put(producto, new HashMap<>());
                                tableModel.setValueAt("Haz click para añadir", modelRow, 1);
                            }

                            // **Actualizar relacionesModificadas para el producto actual**
                            relacionesModificadas.put(producto, new HashMap<>(relacionesFusionadas));

                            // Actualizar las relaciones de los productos relacionados
                            for (Map.Entry<String, Float> entrada : relacionesActualizadas.entrySet()) {
                                String productoRelacionado = entrada.getKey();
                                Float similitud = entrada.getValue();

                                // Obtener las relaciones del producto relacionado
                                Map<String, Float> relacionesOtroProducto = SM.getOrDefault(productoRelacionado, new HashMap<>());

                                // Actualizar o eliminar la relación inversa
                                if (similitud == 0) {
                                    relacionesOtroProducto.remove(producto);
                                } else {
                                    relacionesOtroProducto.put(producto, similitud);
                                }

                                // Actualizar SM y tableModel para el producto relacionado
                                if (relacionesOtroProducto.isEmpty()) {
                                    SM.remove(productoRelacionado);
                                    int rowIndexRelacionado = obtenerFilaDelProducto(productoRelacionado, tableModel);
                                    if (rowIndexRelacionado != -1) {
                                        tableModel.setValueAt("Haz click para añadir", rowIndexRelacionado, 1);
                                    }
                                } else {
                                    SM.put(productoRelacionado, relacionesOtroProducto);
                                    int rowIndexRelacionado = obtenerFilaDelProducto(productoRelacionado, tableModel);
                                    if (rowIndexRelacionado != -1) {
                                        tableModel.setValueAt("(Click para ver relaciones)", rowIndexRelacionado, 1);
                                    }
                                }

                                // **Actualizar relacionesModificadas para el producto relacionado**
                                if (!relacionesModificadas.containsKey(productoRelacionado)) {
                                    relacionesModificadas.put(productoRelacionado, new HashMap<>());
                                }
                                relacionesModificadas.get(productoRelacionado).put(producto, similitud);
                            }

                            // Notificar éxito
                            JOptionPane.showMessageDialog(null,
                                    "Relaciones actualizadas para el producto: " + producto,
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }


                    }

                }
            }
        });



        // Crear un JDialog para mostrar la tabla y los botones
        JDialog dialog = new JDialog();
        if (accion.equals("Consultar")) dialog.setTitle("Consulta de Productos");
        else if (accion.equals("Eliminar lista")) dialog.setTitle("Eliminación de " + lista.getName());
        else dialog.setTitle("Gestión de Productos y Similitudes");
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(null); // Centrar el JDialog
        dialog.setModal(true);
        dialog.setResizable(false);

        // Crear botones de acción según el contexto
        JPanel buttonPanel = new JPanel(new FlowLayout());
        switch (accion) {
            case "Eliminar" -> {
                JButton btnEliminar = new JButton("Eliminar seleccionados");
                btnEliminar.addActionListener(e -> {
                    int[] selectedRows = table.getSelectedRows();
                    if (selectedRows.length == 0) {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar al menos un producto para eliminar.",
                                "Eliminar Productos", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Recoger productos seleccionados
                    List<String> productosAEliminar = new ArrayList<>();
                    for (int viewRow : selectedRows) {
                        int modelRow = table.convertRowIndexToModel(viewRow);
                        productosAEliminar.add((String) tableModel.getValueAt(modelRow, 0));
                    }

                    // Verificar si todos los productos están seleccionados
                    if (productosAEliminar.size() == tableModel.getRowCount()) {
                        int confirmacion = JOptionPane.showConfirmDialog(null,
                                "Está intentando eliminar todos los productos. ¿Desea eliminar la lista completa?",
                                "Eliminar Lista",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);

                        if (confirmacion == JOptionPane.YES_OPTION) {
                            boolean listaEliminada = presentationController.eliminarLista(lista.getName());
                            if (listaEliminada) {
                                JOptionPane.showMessageDialog(null, "La lista '" + lista.getName() + "' se ha eliminado correctamente.",
                                        "Eliminar Lista", JOptionPane.INFORMATION_MESSAGE);
                                dialog.dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo eliminar la lista.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            return;
                        } else {
                            return;
                        }
                    }

                    // Llamar al controlador para eliminar los productos seleccionados
                    List<String> productosEliminados = presentationController.eliminarProductos(productosAEliminar, lista.getName());
                    if (productosEliminados != null && !productosEliminados.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Productos eliminados: " + productosEliminados,
                                "Eliminar Productos", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        mostrarSimilitudes(lista, editable, accion);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudieron eliminar los productos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                buttonPanel.add(btnEliminar);
            }

            case "Modificar" -> {
                JButton btnGuardarCambios = new JButton("Guardar cambios");
                btnGuardarCambios.addActionListener(e -> {
                    // Convertir relaciones modificadas a una lista de String[]
                    List<String[]> relacionesList = new ArrayList<>();
                    for (Map.Entry<String, Map<String, Float>> entry : relacionesModificadas.entrySet()) {
                        String producto = entry.getKey();
                        for (Map.Entry<String, Float> relacion : entry.getValue().entrySet()) {
                            String productoRelacionado = relacion.getKey();
                            float similitud = relacion.getValue();
                            relacionesList.add(new String[]{producto, productoRelacionado, String.valueOf(similitud)});
                        }
                    }

                    if (relacionesModificadas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No se han realizado cambios en los grados de similitud.",
                                "Sin cambios", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Enviar los cambios al controlador
                    List<String[]> resultado = presentationController.modificarGradosDeSimilitud(relacionesList, lista.getName());
                    if (resultado != null) {
                        JOptionPane.showMessageDialog(null, "Grados de similitud actualizados con éxito.",
                                "Modificar Grados de Similitud", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        mostrarSimilitudes(lista, editable, accion);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudieron actualizar los grados de similitud.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                buttonPanel.add(btnGuardarCambios);
            }

            case "Introducir" -> {
                JButton btnAñadir = new JButton("Añadir productos");
                btnAñadir.addActionListener(e -> {
                    String productosAIntroducir = JOptionPane.showInputDialog("Introduce los productos separados por espacios (p1 p2 p3 ...):");
                    if (productosAIntroducir != null) {
                        if (productosAIntroducir.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Debes introducir al menos un producto.",
                                    "Introducir productos", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                    else return;

                    String[] listaProductos = productosAIntroducir.trim().split("\\s+");
                    List<String> productosAñadidos = presentationController.introducirProductos(Arrays.asList(listaProductos), lista.getName());

                    Set<String> productosExistentes = new HashSet<>();
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        productosExistentes.add((String) tableModel.getValueAt(i, 0));
                    }

                    List<String> productosRechazados = Arrays.stream(listaProductos)
                            .filter(productosExistentes::contains)
                            .toList();

                    if (productosAñadidos != null && !productosAñadidos.isEmpty()) {
                        // Mostrar los productos añadidos correctamente
                        JOptionPane.showMessageDialog(null, "Productos añadidos: " + productosAñadidos,
                                "Modificar lista", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        mostrarSimilitudes(lista, editable, accion);
                    } else {
                        // Crear mensaje con productos rechazados, si los hay
                        String mensajeError = "No se añadieron productos.";
                        if (!productosRechazados.isEmpty()) {
                            mensajeError += "\nYa existen en la lista: " + productosRechazados;
                        }
                        JOptionPane.showMessageDialog(null, mensajeError,
                                "Modificar lista", JOptionPane.WARNING_MESSAGE);
                    }

                });
                buttonPanel.add(btnAñadir);
            }
        }

        JButton btnVolver2 = new JButton("Volver");
        btnVolver2.addActionListener(e -> {
            if (!relacionesModificadas.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Tienes cambios no guardados. ¿Seguro que quieres volver sin guardar?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            dialog.dispose();
        });

        buttonPanel.add(btnVolver2);

        // Panel principal para la tabla y botones
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panelPrincipal);
        dialog.setVisible(true);
    }

    /**
     * Método que encapsula la funcionalidad de seleccionar una lista (se usa un pop-up).
     * @param listasDisponibles lista con los nombres de todas las listas de productos del usuario.
     * @param titulo Título del pop-up de selección, se selecciona una lista en diferentes funcionalidades, es para
     *               diferenciarlo. Se indica la funcionalidad.
     * @param mensaje Mensaje del pop-up de selección. se selecciona una lista en diferentes funcionalidades, es para
     *                ayudar a diferenciarlo. Se pide que el usuario escoja la lista a hacerle algo.
     * @return Devuelve el pop-up de selección de lista de productos o null si no hay listas disponibles.
     */
    private String seleccionarLista(Set<String> listasDisponibles, String titulo, String mensaje) {
        //Se comprueba que haya alguna lista disponible
        if (listasDisponibles == null || listasDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay listas disponibles.", titulo, JOptionPane.WARNING_MESSAGE);
            return null;
        }

        //Array con los nombres de las listas de productos disponibles
        String[] opciones = listasDisponibles.toArray(new String[0]);

        //Se devuelve el pop-up
        return (String) JOptionPane.showInputDialog(
                null,
                mensaje,
                titulo,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
    }

    /**
     * Método que encuentra la fila de en la que se encuentra un producto en la matriz de grados de similitud.
     * @param producto Producto del que se quiere encontrar la fila a la que pertenece en la matriz de grados de
     *                 similitud.
     * @param tableModel Tabla con los productos de la lista.
     * @return Fila donde se encuentra el producto o -1 si el producto no se encuentra.
     */
    private int obtenerFilaDelProducto(String producto, DefaultTableModel tableModel) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(producto)) {
                return i;
            }
        }
        return -1; // Producto no encontrado
    }
}
