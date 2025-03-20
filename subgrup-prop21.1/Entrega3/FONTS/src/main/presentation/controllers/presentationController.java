package main.presentation.controllers;

import main.domain.classes.Distribucion;
import main.domain.classes.Estrategia;
import main.domain.classes.LlistaProductes;
import main.domain.classes.algorism.AlgoritmoVoraz;
import main.domain.classes.algorism.Aproximation_Kruskal_ILS;
import main.domain.classes.exceptions.formatException;
import main.domain.classes.types.Pair;
import main.domain.controllers.domainController;
import main.presentation.vistas.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Clase principal del programa. Crea el controlador de dominio y ejerce de controlador entre las clases de la capa
 * presentación y el controlador de dominio, o sea, controla el acceso a la capa de dominio (y por tanto también a la
 * de persistencia. Además, crea todas las vistas del programa.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 * @author David Mas Escude (david.mas@estudiantat.upc.edu)
 * @author David Sanz Martínez (david.sanz.martinez)
 */
public class presentationController {

    /**
     * Controlador de dominio del programa. Mediante este se accede a la capa de dominio.
     */
    private static domainController domainController;

    /**
     * Primera vista que se muestra al usuario. Permite hacer el inicio de sesión o el registro de usuario.
     */
    private static loginView loginView;

    /**
     * Es el nombre de usuario del usuario que ha accedido al sistema y lo está usando.
     */
    private static String currentUserName;

    /**
     * Vista principal de mensajes donde el usuario ve sus mensajes recibidos y puede gestionar el apartado de mensajes.
     */
    private static messagesView messagesViewInstance;

    /**
     * Vista de mensajes donde el usuario ve sus mensajes enviados.
     */
    private static sentMessagesView sentMessagesViewInstance;

    /**
     * Constructora del controlador de presentación. Crea los atributos de controlador de dominio y la vista loginView,
     * y muestra esta última haciendola visible.
     */
    public presentationController() {
        domainController = new domainController();
        loginView = new loginView();
        showLoginView();
    }

    /**
     * Hace visible (o crea si esra null) la loginView del controlador de presentación.
     */
    public static void showLoginView() {
        if (loginView == null) {
            loginView = new loginView();
        }
        loginView.setVisible(true);
    }


    /**
     * Método que cierra las vistas de mensajes que son atributos del controlador de presentación.
     */
    public static void disposeViews() {
        // Verifica si las vistas están instanciadas y las cierra
        if (sentMessagesViewInstance != null) {
            sentMessagesViewInstance.dispose();  // Cierra sentMessagesView
            sentMessagesViewInstance = null; // Limpia la referencia
        }

        if (messagesViewInstance != null) {
            messagesViewInstance.dispose();  // Cierra sendMessagesView
            messagesViewInstance = null; // Limpia la referencia
        }
        mainMenuView();
    }

    //Creadoras de Vistas

    /**
     * Método que gestiona el inicio de sesión.
     * Muestra un pop-up (JOptionPane) donde el usuario introduce su nombre de usuario y su contraseña. Si las
     * credenciales son correctas se guarda su nombre de usuario (para luego cargar/guardar lo que deba sobre este
     * usuario) y se accede al menú principal. En caso de no ser correctas las credenciales al usuario se le muestra
     * el error con un pop-up y puede volver a intentarlo.
     */
    public static void handleLogin() {

        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel lblUsername = new JLabel("Usuario:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Inicio de Sesión",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        //Si se decide proceder con el inicio de sesión
        if (result == JOptionPane.OK_OPTION) {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            // Inicio de sesión exitoso
            if (domainController.loginUser(username, password)) {
                JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                currentUserName = username;
                loginView.dispose();
                mainMenuView();
            }
            else { // Error. Credenciales incorrectas.
                JOptionPane.showMessageDialog(null, "Credenciales incorrectas. Inténtelo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método que gestiona el registro de usuario.
     * Muestra un pop-up (JOptionPane) que permite al usuario introducir el nombre de usuario, su nombre y la contraseña
     * que quiere para su cuenta. Se han de completar todos los campos. Si no hay ningún problema se registra al usuario
     * y se accede al menú principal. En caso de error salta un mensaje de error y permite al usuario volver a
     * introducir los campos.
     */
    public static void handleRegister() {

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel lblUsername = new JLabel("Usuario:");
        JTextField txtUsername = new JTextField();
        JLabel lblName = new JLabel("Nombre:");
        JTextField txtName = new JTextField();
        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblPassword);
        panel.add(txtPassword);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Registro",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        //Si se decide proceder con el registro de usuario
        if (result == JOptionPane.OK_OPTION) {
            String username = txtUsername.getText().trim();
            String name = txtName.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            //Algún campo se ha dejado vacío
            if (username.isEmpty() || name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios. Inténtelo nuevamente.",
                        "Error",JOptionPane.ERROR_MESSAGE);
                handleRegister();
                return;
            }

            //Registro exitoso
            if (domainController.registerUser(username, name, password)) {
                JOptionPane.showMessageDialog(null, "Registro exitoso.");
                currentUserName = username;
                loginView.dispose();
                mainMenuView();
            } else { //Error en el registro
                JOptionPane.showMessageDialog(null, "Error en el registro. Inténtelo nuevamente.", "Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Crea una vista mainMenuView que hace de menú principal del programa.
     */
    public static void mainMenuView() {
        mainMenuView mainMenuView = new mainMenuView(currentUserName);
    }

    /**
     * Crea una vista insertListView que permite introducir una nueva lista.
     */
    public static void insertListView() {
        insertListView  insertListView = new insertListView();
    }

    /**
     * Crea una vista manageListsView que permite gestionar las listas de productos creadas.
     */
    public static void manageListsView() {
        manageListsView  manageListsView = new manageListsView();
    }

    /**
     * Crea una vista generateDistributionView que permite generar una distribución con una de las listas de productos
     * del usuario.
     */
    public static void generateDistributionView() {generateDistributionView  generateDistributionView = new generateDistributionView();}

    /**
     * Crea una vista manageDistView que permite gestionar las distribuciones generadas.
     */
    public static void manageDistView() {
        manageDistView manageDistView = new manageDistView();
    }

    /**
     * Crea una vista sendMessageView que permite al usuario enviar un mensaje a una serie de destinatarios.
     */
    public static void sendMessagesView() {sendMessageView sendMessagesView = new sendMessageView();}

    /**
     * Crea una vista sentMesessagesView que muestra al usuario los mensajes que ha enviado. La vista se
     * asigna como atributo de la clase.
     */
    public static void sentMessagesView() {
        sentMessagesViewInstance = new sentMessagesView();
    }

    /**
     * Crea o hace visible una vista messagesView que es la vista principal para gestionar los mensajes, además muestra
     * los mensajes recibidos. La vista se asigna como atributo de la clase.
     */
    public static void messagesView() {
        if (messagesViewInstance == null) {
            messagesViewInstance = new messagesView();
        }
        messagesViewInstance.setVisible(true);
    }

    //Gestión de la capa de dominio

    //"Creadoras" de la capa de dominio

    /**
     * Método que crea una lista de productos en la capa de dominio, coge el mapa de similitudes de la capa de dominio
     * para recrearla en la capa de presentación y la retorna.
     * @param productos es un String conformado de los productos que formaran parte de esta nueva LlistaProductes.
     * @param relaciones es un String conformado por las similitudes entre los productos (y por tanto también los
     *                   productos) que formaran parte de esta nueva LlistaProductes.
     * @return Nueva LlistaProductes recreada de la creada en la capa de dominio o null si salta la excepción, o sea,
     * ha habido un error en el formato.
     * throws formatException Ha habido un fallo en el formato al introducir los productos o sus grados de similitud, o
     *  los grados de similitud son incorrectos.
     */
    public static LlistaProductes introducirNuevaLista(String productos, String relaciones) {
        try {
            List<String> listaProductos = Arrays.asList(productos.trim().split("\\s+"));
            Set<String> setProductos = new HashSet<>(listaProductos);

            // No se ha introducido nignún producto
            if (listaProductos.isEmpty() || listaProductos.size() == 1 && listaProductos.getFirst().isEmpty()) {
                throw new formatException("Debe introducir al menos un producto.");
            }

            List<String[]> listaRelaciones = new ArrayList<>();

            //Hay grados de similitud introducidos por el usuario
            if (relaciones != null && !relaciones.trim().isEmpty()) {
                String[] relacionesArray = relaciones.trim().split(";");
                //Se separan todas las relaciones (grados de similitud)
                for (String relacion : relacionesArray) {
                    String[] partes = relacion.trim().split("\\s+");
                    if (partes.length != 3) { //Alguna relación (grado de similitud) no tiene el formato incorrecto
                        throw new formatException("Cada relación debe tener el formato 'producto1 producto2 GdS'. Relación incorrecta: " + relacion);
                    }

                    String producto1 = partes[0];
                    String producto2 = partes[1];

                    //La relación contiene algún producto que no existe en la lista de productos
                    if (!setProductos.contains(producto1) || !setProductos.contains(producto2)) {
                        List<String> productosNoIntroducidos = new ArrayList<>();
                        if (!setProductos.contains(producto1)) {
                            productosNoIntroducidos.add(producto1);
                        }
                        if (!setProductos.contains(producto2)) {
                            productosNoIntroducidos.add(producto2);
                        }

                        throw new formatException("La relación '" + relacion + "' contiene productos no introducidos: " +
                                String.join(", ", productosNoIntroducidos) + ".");
                    }

                    //Se transforma el String del grado de similitud a float
                    try {
                        float gradoSimilitud = Float.parseFloat(partes[2]);
                        if (gradoSimilitud < 0 || gradoSimilitud > 1) { //El grado de similitud no está entre 0 y 1 (incluidos).
                            throw new formatException ("El grado de similitud (GdS) de la relación '" + relacion +"' debe estar entre 0y 1.");
                        }
                    } catch (NumberFormatException e) { //Se lanza una expceción porque un grado de similitud no es válido
                        throw new formatException("El grado de similitud (GdS) de la relación '" + relacion + "' debe ser un número válido.");
                    }

                    //Se añade la relación a listaRelaciones
                    listaRelaciones.add(partes);
                }
            }

            //Se crea la lista en dominio y se devuelve su mapa de similitudes con listaProdutos y listaRelaciones
            Map<String, Map<String, Float>> SM = domainController.nuevaLista(listaProductos, listaRelaciones);

            return new LlistaProductes(SM); //Se devuelve una recreación en la capa de presentación en base al mapa de similitudes

        } catch (formatException e) { // Catch de la excepción
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Método que genera una distribución en la capa de dominio, coge la lista con los productos ordenados para
     * recrearla en la capa de presentación y retornarla.
     * @param lista nombre de la lista de productos que se va a usar para generar la distribución.
     * @param h Número de estanterías que tendrá la distribución.
     * @param w Número de productos por estantería que tendrá la distribución.
     * @param algoritmo Nombre del algoritmo que se va a usar para generar la distribución.
     * @return Distribución recreada en la capa de presentación a partir de la lista con los productos ordenados de la
     * creada en distribución.
     */
    public static Distribucion generar_Distribucion (String lista, int h, int w, String algoritmo) {
        //Se genera la distribución en la capa de dominio y devuelve la lista ordenada de los productos
        List<String> dist = domainController.generarDistribucion(lista,h,w,algoritmo);

        Pair<Integer, Integer> prestatge = new Pair<>(h,w);

        //Se crea el mapa a partir de la lista ordenada de los productos
        Map<String, Integer> mapa = new HashMap<>();
        for (int i = 0; i < dist.size(); i++) mapa.put(dist.get(i),i);

        //Se crea la estrategia
        Estrategia estrategia;
        if (Objects.equals(algoritmo, "Voraz")) estrategia = new AlgoritmoVoraz();
        else estrategia = new Aproximation_Kruskal_ILS();

        //Se recrea la disctribución y se devuelve
        return new Distribucion(prestatge, dist,"sense_nom",estrategia,mapa);
    }

    //"Destrucoras" de la capa de dominio

    /**
     * Método para eliminar una lista de productos de la capa de dominio y de la capa de persistencia.
     * @param lista Nombre de la lista a eliminar.
     * @return true si se ha eliminado la lista o false si no se ha podido eliminar.
     */
    public static boolean eliminarLista(String lista) {return domainController.eliminarLista(lista);}

    /**
     * Método para eliminar ciertos productos de una lista de productos (cambios en dominio y persistencia).
     * @param productos Lista de los productos a eliminar de la lista.
     * @param lista Nombre de la lista de la que se quieren eliminar los productos.
     * @return La lista de productos eliminados o null si no se ha eliminado ninguno.
     */
    public  static List<String> eliminarProductos(List<String> productos, String lista){return domainController.eliminarProductos(productos, lista);}

    /**
     * Método para eliminar ciertos productos de una distribución (cambios en dominio y persistencia).
     * @param nombreDist Nombre de la distribución de la que se quieren eliminar los productos.
     * @param prods Lista de los productos a eliminar de la distribución.
     * @return true si se ha podido eliminar los productos o false si no se han podido eliminar.
     */
    public static boolean eliminarProductosDeDist(String nombreDist, String[] prods) {return domainController.eliminarProductosDeDist(nombreDist,prods);}

    /**
     * Método para eliminar una distribución de la capa de dominio y de la capa de persistencia.
     * @param nameDist Es el nombre de la distribución a eliminar.
     * @return true si se ha eliminado la distribución o false si no se ha podido eliminar.
     */
    public static boolean eliminarDistribucion(String nameDist) { return domainController.eliminarDist(nameDist);}

    //Getters de la capa de dominio

    /**
     * Método que llama a la capa de dominio y retorna los nombres de todas las listas de productos del usuario.
     * @return Lista con los nombres de todas las listas de productos del usuario o null si no hay ninguna lista.
     */
    public static Set<String> verListas() {return domainController.verListasProductos();}

    /**
     * Método que llama a la capa de dominio para conseguir la matriz de grados de similitud de la lista de productos y
     * la recrear la lista en la capa de presentación para retornarla.
     * @param seleccion Nombre de la lista de productos seleccionada de la que se quiere obtener la matriz de grados de
     *                  similitud.
     * @return LlistaProductes recreada en la capa de presentación a partir de la matriz de grados de similitud de la
     * lista original de la capa de dominio y su nombre.
     */
    public static LlistaProductes obtenerProductosDeLista(String seleccion) {
        Map<String, Map<String, Float>> SM =  domainController.obtenerLista(seleccion);
        return new LlistaProductes(seleccion, SM);
    }

    /**
     * Método que llama a la capa de dominio para conseguir el tamaño de una lista de productos.
     * @param lista Nombre de la lista de productos a consultar.
     * @return Entero que es el tamaño de la lista de productos que se consulta.
     */
    public static int getSizeList(String lista) {
        return domainController.obtenerLista(lista).size();
    }

    /**
     * Método que llama a la capa de dominio para conseguir los nombres de todas las distribuciones del usuario y
     * retornarlos en un set.
     * @return Set con los nombres de todas las distribuciones del usuario o null si no tiene ninguna distribución.
     */
    public static Set<String> verDists() {return domainController.verDists();}

    /**
     * Método que llama a la capa de dominio para conseguir el inbox del usuario en forma de matriz y lo retorna.
     * @return Matriz del inbox (mensajes recibidos) del usuario.
     */
    public static Object[][] getInboxCurrentUser() {return domainController.getInboxMatrix();}

    /**
     * Método que llama a la capa de dominio para conseguir los mensajes enviados por el usuario en forma de matriz y
     * lo retorna.
     * @return Matriz de los mensajes (cada mensaje es una fila y en cada columna hay un atributo del mensaje) enviados
     * del usuario.
     */
    public static Object[][] getSentMessagesCurrentUser() {return domainController.getSentMessagesMatrix();}

    /**
     * Método que llama a la capa de dominio para conseguir la matriz de grados de similitud de la lista de productos
     * con nombre name.
     * @param name Nombre de la lista de productos a la que se quiere acceder.
     * @return Matriz de grados de similitud de la lista de productos con nombre name.
     */
    public static Map<String, Map<String, Float>> getSimilarityMatrix(String name) {return domainController.getSimilarityMatrix(name);}

    /**
     * Método que llama a la capa de dominio para conseguir el Prestatge (la estantería) de la distribución con nombre
     * name.
     * @param name Nombre de la distribución a la que se quiere acceder.
     * @return Pair de Integer con la altura y la longitud de la estantería.
     */
    public static Pair<Integer, Integer> getPrestage(String name) {
        return domainController.getPrestage(name);
    }

    /**
     * Método que llama a la capa de dominio para conseguir la lista ordenada de productos de la distribución con nombre
     *  nameDist.
     * @param nameDist Nombre de la distribución a la que se quiere acceder.
     * @return Lista ordenada de productos de la distribución con nombre nameDist.
     */
    public static List<String> getDistList(String nameDist) {
        return domainController.getDistList(nameDist);
    }

    /**
     * Método que llama a la capa de dominio para conseguir la estrategia de la distribución con nombre nameDist.
     * @param nameDist Nombre de la distribución a la que se quiere acceder.
     * @return Estrategia de la distribución con nombre nameDist.
     */
    public static Estrategia getEstrategia(String nameDist) {
        return domainController.getEstrategia(nameDist);
    }

    /**
     * Método que llama a la capa de dominio para conseguir el nombre de la estrategia, del algoritmo usado, de la
     * distribución con nombre nameDist.
     * @param nameDist Nombre de la distribución a la que se quiere acceder.
     * @return String que es el nombre del algoritmo de la distribución con nombre nameDist.
     */
    public static String getAlgoritmo(String nameDist) {
        return domainController.getAlgoritmo(nameDist);
    }

    /**
     * Método que llama a la capa de dominio para conseguir el mapa de la distribución con nombre nameDist
     * (mapa con los productos y su posición).
     * @param nameDist Nombre de la distribución de la que se quiere acceder.
     * @return Mapa de la distribución con nombre nameDist.
     */
    public static Map<String, Integer> getMapa(String nameDist) {
        return domainController.getMapa(nameDist);
    }

    /**
     * Método que llama a la capa de dominio para conseguir los posibles destinatarios de un mensaje que tiene el
     * usuario.
     * @return Lista con los posibles destinatarios que tiene el usuario.
     */
    public static List<String> mostrarDestinatariosPosibles(){
        return domainController.mostrarDestinatariosPosibles();
    }

    // Consultoras de la capa de dominio

    /**
     * Método que comprueba si existe la distribución con nombre nombreDist en la capa de dominio.
     * @param nombreDist Nombre de la distribución a comprobar si existe en la capa de dominio.
     * @return true si sí existe la distribución o false si no existe la distribución en la capa de dominio.
     */
    public static boolean DistributionExists(String nombreDist) {return domainController.DistributionExists(nombreDist);}

    /**
     * Método que comprueba si existe el producto prod en la distribución con nombre nombreDist en la capa de dominio.
     * @param nombreDist Nombre de la distribución a comprobar.
     * @param prod Producto a comprobar si está en la distribución o no.
     * @return true si el producto si está en la distribución o false si no está.
     */
    public static boolean contieneproducto(String nombreDist, String prod) {
        return domainController.contieneproducto(nombreDist,prod);
    }

    /**
     * Método que calcula el autoajuste de una lista para la estantería al generar una distribución.
     * @param lista nombre de la lista de la que se quiere hacer el autoajuste.
     * @return int[] con las filas y las columnas que tendrá la estantería según el autoajuste.
     */
    public static int[] autoAjuste(String lista) {
        int numProductos = getSizeList(lista);

        // Si hay solo un producto, no es necesario dividir
        if (numProductos == 1) {
            return new int[]{1, 1};  // 1x1
        }

        // Buscar los factores más cercanos de numProductos
        int filas = (int) Math.floor(Math.sqrt(numProductos));
        int columnas = (int) Math.ceil((double) numProductos / filas);

        // Ajustar filas y columnas si el cálculo no es exacto
        while (filas * columnas < numProductos) {
            filas++;
        }

        return new int[]{filas, columnas};
    }

    // Modificadoras de la capa de dominio

    /**
     * Método que cambia el nombre de una lista de productos con nombre oldName y pasará a tener el nombre newName (los
     * cambios se harán en dominio y persistencia)
     * @param oldName Nombre actual de la lista.
     * @param newName Nombre que pasará a tener la lista después del cambio.
     * @return true si se ha cambiado o false si no se ha podido cambiar.
     */
    public static boolean cambiarNombre(String oldName, String newName) {return domainController.cambiarNombre(oldName, newName);}

    /**
     * Método que permite introducir nuevos productos a una lista de productos ya creada.
     * @param productos Lista con los productos nuevos a añadir.
     * @param lista Nombre de la lista a la que se quieren añadir estos productos.
     * @return Devuelve los productos que que se han introducido efectivamente a la lista o null si no se ha introducido
     *  ninguno.
     */
    public static List<String> introducirProductos(List<String> productos, String lista) {return domainController.introducirProductos(productos, lista);}

    /**
     * Método que permite modificar los grados de similitud entre los productos de una lista de productos ya creada.
     * @param relaciones Lista con las relaciones a modificar (primer producto, segundo producto y nuevo grado de
     *                   similitud entre los dos).
     * @param lista Nombre de la lista a modificar.
     * @return Devuelve una lista con los cambios realizados (grado de similitud entre 2 productos modificado) o null
     * si no ha modificado ninguno.
     */
    public static List<String[]> modificarGradosDeSimilitud(List<String[]> relaciones, String lista) {
        try {
            return domainController.modificarGradosDeSimilitud(relaciones, lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar grados de similitud: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Método que permite intercambiar dos productos de sitio de una distribución.
     * @param nombreDist Nombre de la distribución que se va a modificar.
     * @param prod1 Nombre del primer producto a intercambiar.
     * @param prod2 Nombre del segundo producto a intercambiar.
     * @return true si se ha intercambiado o false si no se ha podido intercambiar.
     */
    public static boolean editarDistribucion(String nombreDist, String prod1, String prod2) {return domainController.editarDist(nombreDist,prod1,prod2);}

    /**
     * Método que permite cambiar el nombre de una distribución con nombre oldName y pasará a tener el nombre newName.
     * @param oldName Nombre actual de la distribución.
     * @param newName Nombre que pasará a tener la distribución después del cambio.
     * @return true si se ha cambiado el nombre o false si no se ha podido cambiar el nombre.
     */
    public static boolean cambiarNombreDist(String oldName, String newName) {return domainController.cambiarNombreDist(oldName, newName);}

    // Métodos que guardan en la capa de persistencia (y en dominio) por primera vez

    /**
     * Método que permite crear y guardar una nueva lista de productos en la capa de dominio y persistencia.
     * @param nombre Nombre que pasará a tener la lista de productos que se creará.
     * @param lista Matriz de grados de similitud entre los productos que tendrá la lista.
     * @return true si se ha guardado la lista o false si no se ha podido guardar.
     */
    public static boolean guardarLista(String nombre, Map<String, Map<String, Float>> lista) { return domainController.crearGuardarLista(nombre, lista);}

    /**
     * Método que permite crear y guardar una nueva distribución en la capa de dominio y persistencia.
     * @param nameDist Nombre que pasará a tener la distribución que se creará.
     * @param prestage Estantería que tendrá la distribución (Número de estantes y número de productos por estante).
     * @param dist Lista ordenada de los productos de la distribución. Está ordenada en base al orden que tendrán los
     *             productos al colocarse en la estantería comenzando por arriba a la izquierda, luego hacia la
     *             izquierda y cada vez que se baje un estante se cambiará la dirección (p.e. izquierda a derecha).
     * @param estrategia Estrategia del algoritmo usado para generar el orden de los productos en la distribución.
     * @param mapa Mapa con los productos y su número de posición en la estantería.
     * @return true si se ha guardado la distribución o false si no se ha podido guardar.
     */
    public static boolean guardarDistribucion(String nameDist, Pair<Integer, Integer> prestage, List<String> dist, Estrategia estrategia, Map<String, Integer> mapa) {
        return domainController.guardarDist(nameDist, prestage, dist, estrategia, mapa);
    }

    /**
     * Método que permite al usuario enviar un mensaje que contiene una lista de productos suya a otro usuario
     * destinatario.
     * @param destinatario Nombre de usuario del usuario destinatario del mensaje.
     * @param name Nombre de la lista de productos que se quiere enviar en el mensaje.
     * @param similarityMatrix Mapa de grados de similitud entre los productos de la lista de productos que se envía.
     */
    public static void enviarMensajeLista(String destinatario, String name, Map<String, Map<String, Float>> similarityMatrix) {
        domainController.enviarMensajeLista(destinatario, name, similarityMatrix);
    }

    /**
     * Método que permite al usuario enviar un mensaje que contiene una distribución suya a otro usuario destinatario.
     * @param destinatario Nombre de usuario del usuario destinatario del mensaje.
     * @param nameDist Nombre de la distribución que se quiere enviar en el mensaje.
     * @param prestage Estantería de la distribución que se envía.
     * @param dist Lista ordenada de productos de la distribución que se envía.
     * @param estrategia Estrategia de la distribución que se envía.
     * @param mapa Mapa con los productos y su número de posición en la estantería de la distribución que se envía.
     */
    public static void enviarMensajeDist(String destinatario, String nameDist, Pair<Integer, Integer> prestage, List<String> dist, Estrategia estrategia, Map<String, Integer> mapa) {
        domainController.enviarMensajeDist(destinatario, nameDist, prestage, dist, estrategia, mapa);
    }

    /**
     * Método que permite guardar un mensaje que ha recibido el usuario tanto en dominio como en persistencia.
     * @param sender Nombre del usuario que le ha enviado el mensaje.
     * @param title Nombre original de la lista de productos o la distribución enviada en el mensaje.
     * @param type Indica si es una lista de productos ("Lista") o una distribución ("Distribución").
     * @param newName Nombre que pasará a tener la lista de productos o la distribución recibida para el usuario. Puede
     *                ser null si se quiere mantener el nombre original.
     * @return true si se ha guardado el mensaje o false si no se ha podido guardar.
     */
    public static boolean guardarMensajeRecibido(String sender, String title, String type, String newName) {
        return domainController.guardarObjeto(sender, title, type, newName);
    }

}
