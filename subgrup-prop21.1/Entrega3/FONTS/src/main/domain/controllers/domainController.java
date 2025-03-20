package main.domain.controllers;


import main.domain.classes.*;

import main.domain.classes.types.Pair;
import main.persistence.classes.MensajeDTO;
import main.persistence.controllers.persistenceController;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Set;

/**
 * Clase encargada de gestionar la capa de dominio entre las diferentes opciones
 * relacionadas con usuarios, listas de productos y distribuciones.
 * Además, gestiona la comunicación de esta capa con la de persistencia (y con la de presentación).
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 * @author David Mas Escude (david.mas@estudiantat.upc.edu)
 * @author David Sanz Martínez (david.sanz.martinez)
 */
public class domainController {

    /**
     * Controlador de usuarios
     */
    private userController userController;
    /**
     * Controlador de listas
     */
    private listController listController;
    /**
     * Controlador de distribuciones
     */
    private distributionController distributionController;
    /**
     * Controlador de mensajes
     */
    private mensajeController mensajeController;
    /**
     * Controlador de persistencia
     */
    private persistenceController persistenceController;

    /**
     * Constructor de la clase domain controller.
     * Inicializa el userController
     */
    public domainController() {
        this.persistenceController = persistenceController.getInstance();
        this.persistenceController.initializeConfigFile();
        this.userController = userController.getInstance(persistenceController.loadUsers());
        this.listController = listController.getInstance();
        this.distributionController = distributionController.getInstance();
        this.mensajeController = mensajeController.getInstance();
    }


    /**
     * Inicializa los controladores para listas de productos y distribuciones.
     */
    private void initializeControllers() {
        listController.setCjt(userController.getCjtLlistesProductes());
        distributionController.setCjtD(userController.getCjtDistribuciones());
    }

    //Users


    /**
     * Registra un nuevo usuario.
     *
     * @param username Nombre de usuario que tendrá el usuario.
     * @param name Nombre del usuario
     * @param password Contraseña que tendrá el usuario.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registerUser(String username, String name, String password) {
        boolean res = userController.registerUser(username, name, password);
        if (res) {
            User newUser = userController.getCurrentUser();
            persistenceController.saveUser(newUser);
            initializeControllers();
        }
        return res;
    }


    /**
     * Inicia sesión con un usuario existente.
     * @param username Nombre de usuario del usuario.
     * @param password Contraseña del usuario.
     * @return true si el inicio de sesión fue exitoso, false en caso contrario.
     */
    public boolean loginUser(String username, String password) {
        boolean res = userController.loginUser(username, password);
        if (res)  {
            User user = userController.getCurrentUser();
            if (persistenceController.userPrintOff(username)) loadUserStructures(user);
            initializeControllers();
        }
        return res;
    }

    /**
     * Carga los datos en la capa de dominio del usuario user.
     * @param user Usuario del que se cargan los datos.
     */
    public void loadUserStructures(User user) {
        String username = user.getUsername();
        File userDir = persistenceController.getUserDirectory(username);

        // Listas
        File listasDir = new File(userDir, "listasProductos");
        if (listasDir.exists() && listasDir.isDirectory()) {
            Map<String, LlistaProductes> listasProductos = persistenceController.loadListasProductos(listasDir, username);
            CjtLlistesProductes cjtLlistes = new CjtLlistesProductes(listasProductos);
            user.setCjtLlistesProductes(cjtLlistes);
        } else {
            System.out.println("No se encontró el directorio de listas de productos para el usuario: " + user.getUsername());
        }

        // Distribuciones
        File distDir = new File(userDir, "distribuciones");
        if (distDir.exists() && distDir.isDirectory()) {
            Map<String, Distribucion> distribuciones = persistenceController.loadDistribuciones(distDir, username);
            CjtDistribuciones cjtDistribuciones = new CjtDistribuciones(distribuciones);
            user.setCjtDistribuciones(cjtDistribuciones);
        } else {
            System.out.println("No se encontró el directorio de distribuciones para el usuario: " + user.getUsername());
        }

        // Inbox
        File inboxDir = new File(userDir, "inbox");
        if (inboxDir.exists() && inboxDir.isDirectory()) {
            List<MensajeDTO> inboxDTO = persistenceController.loadMessages(inboxDir);
            List<Mensaje> inbox = convertirDTOaMensaje(inboxDTO);
            if (!inbox.isEmpty()) {
                user.setInbox(inbox);
                System.out.println("Inbox cargado con " + inbox.size() + " mensajes.");
            } else {
                System.out.println("El inbox está vacío o no se pudo cargar.");
            }
        }
        else {
            System.out.println("No se encontró el directorio de inbox para el usuario: " + user.getUsername());
        }


        // Enviados
        File sentMessagesDir = new File(userDir, "sentMessages");
        if (sentMessagesDir.exists() && sentMessagesDir.isDirectory()) {
            List<MensajeDTO> sentMessagesDTO = persistenceController.loadMessages(sentMessagesDir);
            List<Mensaje> sentMessages = convertirDTOaMensaje(sentMessagesDTO);
            if (!sentMessages.isEmpty()) {
                user.setSentMessages(sentMessages);
                System.out.println("Mensajes enviados cargados con " + sentMessages.size() + " mensajes.");
            } else {
                System.out.println("Los mensajes enviados están vacíos o no se pudieron cargar.");
            }
        }
        else {
            System.out.println("No se encontró el directorio de sentMessages para el usuario: " + user.getUsername());
        }
    }


    /**
     * Guarda un Objeto enviado en un mensaje.
     * @param sender Usuario que ha enviado el mensaje.
     * @param title Nombre original del objeto.
     * @param type Tipo que indica si es lista de productos o distribución.
     * @param newName Nuevo nombre del objeto (puede ser null).
     * @return Devuelve true si se ha podido guardar el objeto o false si no se ha podido guardar.
     */
    public boolean guardarObjeto(String sender, String title, String type, String newName) {
        if (newName == null) newName = title;

        if (Objects.equals(type, "Lista"))  {
            LlistaProductes lista = persistenceController.loadIsolatedProductList(sender, title);
            lista.setNom(newName);
            return guardarLista(lista);
        }
        else {
            Distribucion dist = persistenceController.loadIsolatedDistribution(sender, title);
            dist.setNom(newName);
            return guardarDist(dist.getNom(), dist.getPrestage(), dist.getDist(), dist.getEstrategia(), dist.getMapa());
        }
    }

    //listas

    /**
     * Crea una nueva lista de productos solo en dominio y devuelve su matriz de grados de similitud.
     * @param productos Lista de los productos que va a tener la nueva LListaProductes.
     * @param relaciones Grados de similitud entre los productos de la lista.
     * @return Devuelve la matriz de grados de similitud de ls lista creada.
     */
    public Map<String, Map<String, Float>> nuevaLista(List<String> productos, List<String[]> relaciones) {
        LlistaProductes lista = listController.introducirNuevaLista(productos, relaciones);
        return lista.getSimilarityMatrix();
    }

    /**
     * Crea una lista y la guarda en la capa de persistencia.
     * @param name Nombre que tendrá la lista.
     * @param SM Matriz de grados de similitud entre los productos que tendrá la lista.
     * @return true si se ha podido guardar la lista o false si no se ha podido guardar.
     */
    public boolean crearGuardarLista(String name, Map<String, Map<String, Float>> SM) {
        LlistaProductes lista = new LlistaProductes(name,SM);
        return guardarLista(lista);
    }

    /**
     * Guarda una lista de productos en el listController y en la capa de persistencia.
     * @param lista LlistaProductes que se quiere guardar.
     * @return true si se ha podido guardar o false si no se ha podido guardar.
     */
    public boolean guardarLista(LlistaProductes lista) {
        boolean res = listController.guardarLista(lista.getName(), lista);
        if (res) {
            String currentUser = userController.getCurrentUser().getUsername();
            persistenceController.saveListaProductos(lista, currentUser);
        }
        return res;
    }

    /**
     * Método para obtener la matriz de grados de similitud entre sus productos (y por tanto sus
     * productos también) de la lista de productos con nombre "seleccion".
     * @param seleccion Nombre de la lista de productos a la que se quiere acceder.
     * @return Matriz de grados de similitud entre los productos de la lista.
     */
    public Map<String, Map<String, Float>> obtenerLista(String seleccion) {
        LlistaProductes lista = listController.obtenerLista(seleccion);
        return lista.getSimilarityMatrix();
    }

    /**
     * Verifica si existen listas de productos para el usuario actual.
     *
     * @return Devuelve `true` si existen listas de productos, de lo contrario `false`.
     */
    public Set<String> verListasProductos() {
        return listController.verListas();
    }

    /**
     * Elimina la lista con nombre "lista" del listController y de la capa de persistencia.
     * @param lista Nombre de la lista a eliminar.
     * @return true si se ha podido eliminar o false si no se ha podido.
     */
    public boolean eliminarLista(String lista) {
        if (listController.eliminarLista(lista)) {
            persistenceController.deleteListaProductos(lista, userController.getCurrentUser().getUsername());
            return true;
        }
        return false;
    }

    /**
     * Método que cambia el nombre de la lista de productos.
     * @param oldName Nombre actual de la lista que pasará a ser inválido después de ejecutarse este método si todo
     *                sale bien.
     * @param newName Nombre que pasará a tener la lista de productos y por el cual se identificará.
     * @return true si se ha cambiado el nombre o false si no se ha podido cambiar.
     */
    public boolean cambiarNombre(String oldName, String newName) {
        boolean res = listController.cambiarNombreLista(oldName, newName);
        if (res) {
            persistenceController.changeListName(oldName, newName, userController.getCurrentUser().getUsername());
            return true;
        }
        return false;
    }

    /**
     * Método que introduce nuevos productos a una lista de productos ya creada.
     * @param productos Productos que se van a añadir a la lista.
     * @param listName Nombre de la lista a modificar.
     * @return una lista con los productos que se han introducido en la lista o null si no se ha introducido ninguno.
     */
    public List<String> introducirProductos(List<String> productos, String listName) {
        LlistaProductes lista = listController.obtenerLista(listName);
        List<String> insertados = listController.introducirProductos(productos, lista);
        if (!insertados.isEmpty()) {
            persistenceController.updateList(userController.getCurrentUser().getUsername(), lista);
            return insertados;
        }
        return null;
    }

    /**
     * Método que elimina ciertos productos de una lista de productos.
     * @param productos Productos a eliminar de la lista de productos.
     * @param listName Nombre de la lista de productos a modificar.
     * @return Lista con los productos eliminados de la lista o null si no ha eliminado ninguno.
     */
    public List<String> eliminarProductos(List<String> productos, String listName) {
        LlistaProductes lista = listController.obtenerLista(listName);
        List<String> eliminados = listController.eliminarProductosDeLista(productos, lista);
        if (!eliminados.isEmpty()) {
            persistenceController.updateList(userController.getCurrentUser().getUsername(), lista);
            return eliminados;
        }
        return null;
    }

    /**
     * Método que modifica los grados de similitud entre productos de una lista de productos.
     * @param relaciones Relaciones entre productos que se modificaran. Contiene los productos y sus nuevos grados de
     *                   similitud.
     * @param listName Nombre de la lista de productos a modificar.
     * @return Las relaciones de similitud modificadas o null si no se ha modificado ninguna.
     */
    public List<String[]> modificarGradosDeSimilitud(List<String[]> relaciones, String listName) {
        LlistaProductes lista = listController.obtenerLista(listName);
        List<String[]> modificados = listController.modificarMultiplesSimilitudes(lista, relaciones);
        if (!modificados.isEmpty()) {
            persistenceController.updateList(userController.getCurrentUser().getUsername(), lista);
            return modificados;
        }
        return null;
    }

    /**
     * Método que obtiene la matriz de grados de similitud de la lista de productos con nombre "name".
     * @param name Nombre de la lista de productos a acceder.
     * @return Matriz de grados de similitud de la lista de productos.
     */
    public Map<String, Map<String, Float>> getSimilarityMatrix(String name) {
        return listController.getSimilarityMatrix(name);
    }

    //Distribuciones

    /**
     * Método que crea una distribución con los parámetros introducidos y la guarda en la capa de persistencia.
     * @param nameDist Nombre que tendrá la distribución.
     * @param prestage Estantería que tendrá la distribución.
     * @param dist Lista ordenada de productos que tendrá la distribución.
     * @param estrategia Estrategia usada para generar la distribución.
     * @param mapa Mapa de productos y sus posiciones que tendrá la distribución.
     * @return true si se crea y guarda la distribución o false si no se ha podido guardar.
     */
    public boolean guardarDist(String nameDist, Pair<Integer, Integer> prestage, List<String> dist, Estrategia estrategia, Map<String, Integer> mapa) {
        Distribucion distribucion = new Distribucion(prestage, dist, nameDist, estrategia, mapa);
        boolean res = distributionController.guardarDistribucion(distribucion, nameDist);
        if (res) {
            String currentUser = userController.getCurrentUser().getUsername();
            persistenceController.saveDistribution(distribucion, currentUser);
        }
        return res;
    }

    /**
     * Método que elimina una distribución con nombre "dist".
     * @param dist Nombre de la distribución a eliminar.
     * @return true si se ha eliminado la distribución o false si no se ha podido eliminar.
     */
    public boolean eliminarDist(String dist) {
        if (distributionController.eliminarDistribucion(dist)) {
            persistenceController.deleteDistribution(dist, userController.getCurrentUser().getUsername());
            return true;
        }
        return false;
    }

    /**
     * Método que cambia el nombre de una distribución.
     * @param oldName Nombre actual de la distribución, que si todo sale bien pasará a ser inválido.
     * @param newName Nombre que pasará a tener la distribución, si todo sale bien.
     * @return true si se cambia el nombre de la distribución o false si no se ha podido cambiar.
     */
    public boolean cambiarNombreDist(String oldName, String newName) {
        boolean res = distributionController.cambiarNombreDist(oldName, newName);
        if (res) {
            persistenceController.changeDistName(oldName, newName, userController.getCurrentUser().getUsername());
            return true;
        }
        return false;
    }


    /**
     * Método que retorna los nombres de todas la distribuciones que tiene el usuario.
     * @return Set con todos los nombres de todas las distribuciones que tiene un usuario.
     */
    public Set<String> verDists() {
        return distributionController.verDists();
    }

    /**
     * Método para generar una distribución y devolver su lista ordenada de productos,
     * @param nom_llista Nombre de la lista que usará la distribución.
     * @param h Altura que tendrá la estantería de la distribución.
     * @param w Anchura que tendrá la estantería de la distribución.
     * @param algoritmo Nombre del algoritmo que se usará para generar la distribución, la estrategia.
     * @return Lista ordenada de los productos en la distribución.
     */
    public List<String> generarDistribucion(String nom_llista, int h, int w, String algoritmo) {

        LlistaProductes lista = listController.obtenerLista(nom_llista);
        if (lista != null) {
            Map<String, Map<String, Float>> LlistaGdS = lista.getSimilarityMatrix();
            Distribucion dist = distributionController.generarDistribucion(algoritmo,h,w,LlistaGdS);
            return dist.getDist();
        }
        return null;
    }

    /**
     * Método que intercambia dos productos de sitio en una distribución con nombre "nombreDist".
     * @param nombreDist Nombre de la distribución a modificar.
     * @param prod1 Primer producto a intercambiar de posición.
     * @param prod2 Segundo producto a intercambiar de posición.
     * @return true si los productos se han intercambiado de sitio o false si no se han podido intercambiar.
     */
    public boolean editarDist(String nombreDist, String prod1, String prod2) {
        distributionController.editarDistribucion(nombreDist,prod1,prod2);
        Estrategia estrategia = getEstrategia(nombreDist);
        List<String> dist = getDistList(nombreDist);
        Pair<Integer, Integer> prestatge = getPrestage(nombreDist);
        Map<String, Integer> mapa = getMapa(nombreDist);
        boolean salebien = eliminarDist(nombreDist);
        if (salebien) {
            salebien = guardarDist(nombreDist, prestatge, dist, estrategia, mapa);
        }
        return salebien;
    }

    /**
     * Método para eliminar ciertos productos de una distribución con nombre "NombreDist".
     * @param nombreDist Nombre de la distribución que se modifica.
     * @param prods Productos a eliminar de la distribución.
     * @return true si se han eliminado los productos de la distribución o false si no se han podido eliminar.
     */
    public boolean eliminarProductosDeDist(String nombreDist, String[] prods) {
        distributionController.eliminarProducteDistribucion(nombreDist,prods);
        Estrategia estrategia = getEstrategia(nombreDist);
        List<String> dist = getDistList(nombreDist);
        Pair<Integer, Integer> prestatge = getPrestage(nombreDist);
        Map<String, Integer> mapa = getMapa(nombreDist);
        boolean salebien = eliminarDist(nombreDist);
        if (salebien) {
            salebien = guardarDist(nombreDist, prestatge, dist, estrategia, mapa);
        }
        return salebien;
    }

    /**
     * Método que comprueba si existe la distribución con nombre "nombreDist".
     * @param nombreDist Nombre de la distribución a comprobar su existencia.
     * @return true si la distribución con nombre "nombreDist" existe o false si no existe.
     */
    public boolean DistributionExists(String nombreDist) {return distributionController.exists(nombreDist);}

    /**
     * Método que obtiene la estantería de la distribución con nombre "name".
     * @param name Nombre de la distribución a acceder.
     * @return Estantería de la distribución con nombre "name".
     */
    public Pair<Integer, Integer> getPrestage(String name) {
        return distributionController.getDistribucion(name).getPrestage();
    }

    /**
     * Método que obtiene la lista ordenada de los productos de la distribución con nombre "nameDist".
     * @param nameDist Nombre de la distribución a acceder.
     * @return lista ordenada de los productos en la distribución.
     */
    public List<String> getDistList(String nameDist) {
        return distributionController.getDistribucion(nameDist).getDist();
    }

    /**
     * Método que obtiene la estrategia de la distribución con nombre "nameDist".
     * @param nameDist Nombre de la distribución a acceder.
     * @return Estrategia de la distribución con nombre "nameDist".
     */
    public Estrategia getEstrategia(String nameDist) {
        return distributionController.getDistribucion(nameDist).getEstrategia();
    }

    //Mensajes

    /**
     * Método que retorna los posibles destinatarios, quita de los posibles usuarios al usuario que está usando el
     * programa.
     * @return Lista con los posibles destinatarios de un mensaje del usuario actual.
     */
    public List<String> mostrarDestinatariosPosibles() {
        List<String> usernames = userController.getAllUsernames();
        usernames.remove(userController.getCurrentUser().getUsername());
        return usernames;
    }

    /**
     * Método que retorna el inbox del usuario que está usando el programa.
     * @return Object[][], o sea, el inbox del usuario convertido en matriz con send = true (o sea, tendrá quien es el
     * sender).
     */
    public Object[][] getInboxMatrix() {
        User usuario = userController.getCurrentUser();
        List<Mensaje> inbox = usuario.getInbox();
        return convertMessagesToMatrix(inbox, true);
    }

    /**
     * Método que retorna los mensajes enviados por el usuario que está usando el programa.
     * @return Object[][], o sea, los mensajes enviados por el usuario convertidos en matriz con send = false (o sea,
     * tendrán el usuario destinatario del mensaje.
     */
    public Object[][] getSentMessagesMatrix() {
        User usuario = userController.getCurrentUser();
        List<Mensaje> sentMessages = usuario.getSentMessages();
        return convertMessagesToMatrix(sentMessages, false);
    }

    /**
     * Transforma una lista de mensajes en una matriz Object[][] con esos mismos mensajes con una fila por mensaje y 5
     * columnas para sus atributos: el destinatario o el sender, si es una lista de productos o una distribución, el
     * nombre de la lista o la distribución, la fecha de envío/creación del mensaje y la hora de envío/creación.
     * @param mensajes Lista de mensajes a convertir.
     * @param send Si en la primera columna se quieren los senders entonces send = true, si send = false se pondran los
     *              destinatarios.
     * @return Matriz transformada de la lista de mensajes.
     */
    private Object[][] convertMessagesToMatrix(List<Mensaje> mensajes, boolean send) {
        Object[][] data = new Object[mensajes.size()][5]; // 5 columnas: sender/Destinatario, tipo, título, fecha, hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje mensaje = mensajes.get(i);
            if (send) data[i][0] = mensaje.getSender().getName();
            else data[i][0] = mensaje.getDestinatario().getName(); // Remitente
            data[i][1] = mensaje.isLista() ? "Lista" : "Distribución"; // Tipo (lista o distribución)
            data[i][2] = mensaje.getNombre(); // Título
            data[i][3] = mensaje.getTimestamp().toLocalDate().format(dateFormatter); // Fecha
            data[i][4] = mensaje.getTimestamp().toLocalTime().format(timeFormatter); // Hora
        }
        return data;
    }

    /**
     * Método que retorna el nombre del algoritmo usado para generar la distribución con nombre "nameDist".
     * @param nameDist Nombre de la distribución a acceder.
     * @return String del nombre del algoritmo usado en la dsitribución.
     */
    public String getAlgoritmo(String nameDist) {
        return distributionController.getDistribucion(nameDist).getAlgoritme();
    }

    /**
     * Método que comprueba si el producgto "prod" se encuentra en la distribución "nombreDist".
     * @param nombreDist Nombre de la distribución a comprobar.
     * @param prod Producto a comprobar si es parte de la distribución.
     * @return true si contiene al producto o false si no lo contiene.
     */
    public boolean contieneproducto(String nombreDist, String prod) {return distributionController.contieneproducto(nombreDist,prod);}

    /**
     * Método que coge el mapa de la distribución con nombre "nameDist".
     * @param nameDist Nombre de la distribución a acceder.
     * @return Mapa de la distribución.
     */
    public Map<String, Integer> getMapa(String nameDist) {
        return distributionController.getDistribucion(nameDist).getMapa();
    }

    /**
     * Método para que el usuario envíe un mensaje con una lista de productos al destinatario indicado (el mensaje se
     * guarda en el mensajeController y en la capa de persistencia).
     * @param destinatario Nombre del usuario destinatario.
     * @param name Nombre de la lista de productos que se va a enviar.
     * @param similarityMatrix Matriz de grados de similitud (que además contiene los productos) de la lista a enviar.
     */
    public void enviarMensajeLista(String destinatario, String name, Map<String, Map<String, Float>> similarityMatrix) {
        LlistaProductes lista = new LlistaProductes(name,similarityMatrix);
        Mensaje mensaje = new Mensaje(lista, name, true, userController.getCurrentUser(), userController.getUser(destinatario));
        mensajeController.EnviarMensaje(mensaje);
        persistenceController.guardarMensaje(mensaje);
    }

    /**
     * Método para que el usuario envíe un mensaje con una distribución al destinatario indicado (el mensaje se guarda
     * en el mensajeController y en la capa de persistencia).
     * @param destinatario Nombre del usuario destinatario.
     * @param nameDist Nombre de la distribución que se va a enviar.
     * @param prestage Estantería de la distribución a enviar.
     * @param dist Lista ordenada de los productos de la distribución a enviar.
     * @param estrategia Estrategia de la distribución a enviar.
     * @param mapa Mapa con los productos y sus posiciones de la distribución a enviar.
     */
    public void enviarMensajeDist(String destinatario, String nameDist, Pair<Integer, Integer> prestage, List<String> dist, Estrategia estrategia, Map<String, Integer> mapa) {
        Distribucion distribucion = new Distribucion(prestage, dist, nameDist, estrategia, mapa);
        Mensaje mensaje = new Mensaje(distribucion, nameDist, false, userController.getCurrentUser(), userController.getUser(destinatario) );
        mensajeController.EnviarMensaje(mensaje);
        persistenceController.guardarMensaje(mensaje);
    }

    /**
     * Convierte una lista de mensajesDTO en una lista de Mensajes.
     *
     * @param ListDTO lista de mensajesDTO a convertir.
     * @return la lista de mensajes creada a partir de la lista de mensajesDTO.
     */
    private List<Mensaje> convertirDTOaMensaje(List<MensajeDTO> ListDTO) {
        List<Mensaje> mensajes = new ArrayList<>();
        for (MensajeDTO mensajeDTO : ListDTO) {
            Mensaje mensaje = new Mensaje();

            mensaje.setObjeto(mensajeDTO.getObjeto());
            mensaje.setNombre(mensajeDTO.getNombre());
            mensaje.setEsLista(mensajeDTO.isEsLista());
            mensaje.setLeido(mensajeDTO.isLeido());
            mensaje.setTimestamp(LocalDateTime.parse(mensajeDTO.getLocalDateTime()));

            mensaje.setSender(userController.getUser(mensajeDTO.getSender()));
            mensaje.setDestinatario(userController.getUser(mensajeDTO.getDestinatario()));

            mensajes.add(mensaje);
        }
        return mensajes;
    }
}
