package main.persistence.controllers;
import main.domain.classes.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.domain.classes.algorism.AlgoritmoVoraz;
import main.domain.classes.algorism.Aproximation_Kruskal_ILS;
import main.domain.classes.types.Pair;
import main.persistence.classes.MensajeDTO;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Classe de controlador per gestionar operacions relacionades amb la persistència,
 * inclosos usuaris, llistes de productes, distribucions i missatges.
 */
public class persistenceController {

    /** Singleton instance of the controller. */
    private static persistenceController instance;

    /** Base directory path for persistence files. */
    private final String baseDirPath = System.getProperty("user.dir") + "/src/persistenceMng";

    /** Configuration file path. */
    private final String configFilePath = baseDirPath + "/config.txt";

    /** Log of user states and their associated directory paths. */
    private Map<String, Pair<Boolean, String>> userLog;

    /**
     * Construeix una nova instància persistenceController i inicialitza la configuració.
     */
    public persistenceController() {
        userLog = new HashMap<>();
        File baseDir = new File(baseDirPath);
        if (!baseDir.exists() && baseDir.mkdirs()) {
            System.out.println("Directorio base creado en " + baseDirPath);
        }

        initializeConfigFile();
    }

     /**
     * Recupera la instància singleton del controlador.
     * 
     * @return la instància singleton de persistenceController.
     */
    public static persistenceController getInstance() {
        if (instance == null) {
            instance = new persistenceController();
        }
        return instance;
    }

    /**
     * Inicialitza el fitxer de configuració, creant-lo si no existeix.
     */
    public void initializeConfigFile() {
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            try {
                if (configFile.createNewFile()) {
                    System.out.println("ConfigFile creado en " + configFilePath);
                }
            } catch (IOException e) {
                System.out.println("Error al crear ConfigFile: " + e.getMessage());
            }
        }
    }

    /**
     * Recupera el directori associat a un usuari concret.
     * 
     * @param username el nom d'usuari de l'usuari.
     * @return el directori de l'usuari com a objecte File.
     */
    public File getUserDirectory(String username) {
        userLog.get(username).setFirst(true);
        return new File(userLog.get(username).getSecond());
    }


    // === Métodos para usuarios ===

    /**
     * Desa les dades d'un usuari i inicialitza els seus directoris.
     * 
     * @param user l'objecte User que conté les dades de l'usuari.
     */
    public void saveUser(User user) {
        String userDirPath =  baseDirPath + "/" + user.getUsername();
        File userDir = new File(userDirPath);

        try {
            if (!userDir.exists() && userDir.mkdirs()) {
                new File(userDir, "inbox").mkdirs();
                new File(userDir, "sentMessages").mkdirs();
                new File(userDir, "distribuciones").mkdirs();
                new File(userDir, "listasProductos").mkdirs();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath, true))) {
                    String userData = String.format("%s,%s,%s,%s\n",
                            user.getUsername(),
                            user.getName(),
                            user.getPassword(),
                            userDirPath);
                    writer.write(userData);
                }
            } else {
                System.out.println("Error: No se pudo crear el directorio para el usuario " + user.getUsername());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el usuario: " + e.getMessage());
        }
    }

    /**
     * Carrega tots els usuaris des del fitxer de configuració.
     * 
     * @return un mapa de noms d'usuari als objectes d'usuari.
     */
    public Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        File configFile = new File(configFilePath);

        if (configFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String username = parts[0];
                        String name = parts[1];
                        String password = parts[2];
                        String userDirPath = parts[3];

                        File userDir = new File(userDirPath);
                        if (userDir.exists() && userDir.isDirectory()) {
                            User user = new User(username, name, password);
                            userLog.put(username, new Pair<>(false, userDirPath));

                            users.put(username, user);
                        } else {
                            System.out.println("Directorio inválido para usuario: " + username);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al cargar usuarios: " + e.getMessage());
            }
        }
        return users;
    }

    /**
     * Comprova si l'estat d'impressió d'un usuari està desactivat.
     * 
     * @param username el nom d'usuari de l'usuari.
     * @return true si l'estat d'impressió de l'usuari està desactivat, false en cas contrari.
     */
    public boolean userPrintOff(String username) {
        return !userLog.get(username).getFirst();
    }

    //listas

    /**
     * Carga las listas de productos de un directorio especificado para un usuario dado.
     * 
     * <p>Este método recorre los archivos JSON en el directorio proporcionado, 
     * interpreta cada archivo como una lista de productos, y las almacena en un 
     * mapa donde las claves son los nombres de las listas (sin extensión) y los 
     * valores son instancias de {@code LlistaProductes}.</p>
     * 
     * @param listasDir el directorio que contiene los archivos JSON de listas de productos.
     * @param username el nombre de usuario asociado a las listas de productos.
     * @return un mapa que asocia el nombre de cada lista con su correspondiente
     *         objeto {@code LlistaProductes}. Si no se encuentran archivos o no se 
     *         pueden cargar, el mapa será vacío.
     * @throws NullPointerException si {@code listasDir} es {@code null}.
     * @throws SecurityException si no se tiene permiso para acceder al directorio o a sus archivos.
     * 
     * @see #loadIsolatedProductList(String, String)
     */
    public Map<String, LlistaProductes> loadListasProductos(File listasDir, String username) {
        Map<String, LlistaProductes> listasProductos = new HashMap<>();

        // Obtener todos los archivos JSON en el directorio
        File[] files = listasDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                // Extraer el nombre de la lista sin extensión
                String nameList = file.getName().replaceFirst("[.][^.]+$", "");

                // Cargar la lista usando loadIsolatedProductList
                LlistaProductes lista = loadIsolatedProductList(username, nameList);

                if (lista != null) {
                    // Añadir al mapa con el nombre como clave
                    listasProductos.put(nameList, lista);
                } else {
                    System.err.println("Error al cargar la lista: " + nameList);
                }
            }
        }

        return listasProductos;
    }

    /**
     * Guarda una lista de productos en formato JSON en el sistema de archivos para un usuario específico.
     * 
     * <p>Este método serializa la información de la lista de productos, incluyendo sus relaciones, 
     * y la guarda en un archivo dentro de un directorio específico para el usuario. 
     * Si el directorio no existe, se intenta crear automáticamente.</p>
     * 
     * @param lista la lista de productos a guardar, que incluye los productos y sus relaciones.
     * @param username el nombre de usuario asociado al archivo de la lista de productos.
     * @throws NullPointerException si {@code lista} o {@code username} son {@code null}.
     * @throws SecurityException si no se tiene permiso para crear o escribir en el directorio.
     * 
     * <p><strong>Nota:</strong> Si ocurre un error al crear el directorio o guardar el archivo, 
     * se mostrará un mensaje de error en la salida de error estándar y no se lanzará una excepción.</p>
     * 
     * @see LlistaProductes
     */
    public void saveListaProductos(LlistaProductes lista, String username) {
        // Obtener el directorio del usuario desde configFilePath
        String userDirectory = baseDirPath + File.separator + username;
        String listName = lista.getName();
        File listasDir = new File(userDirectory, "listasProductos");

        // Crear el directorio de listas de productos del usuario si no existe
        if (!listasDir.exists() && !listasDir.mkdirs()) {
            System.err.println("Error: No se pudo crear el directorio para las listas de productos.");
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Preparar los datos de la lista para serializar a JSON
        Map<String, Object> listaJson = new HashMap<>();
        listaJson.put("name", listName);

        // Crear un mapa para almacenar productos y sus relaciones
        Map<String, Object> productosJson = new HashMap<>();
        for (String producto : lista.getList()) {
            Map<String, Object> productoData = new HashMap<>();
            productoData.put("product", producto);

            // Obtener las relaciones del producto
            Map<String, Float> relaciones = lista.getSimilarityMatrix().get(producto);
            if (relaciones != null && !relaciones.isEmpty()) {
                List<Map<String, Object>> relacionesList = new ArrayList<>();
                for (Map.Entry<String, Float> entry : relaciones.entrySet()) {
                    Map<String, Object> relacion = new HashMap<>();
                    relacion.put("product", entry.getKey());
                    relacion.put("GdS", entry.getValue());
                    relacionesList.add(relacion);
                }
                productoData.put("relations", relacionesList);
            }

            productosJson.put(producto, productoData);
        }

        listaJson.put("productos", productosJson);

        // Serializar la lista a formato JSON
        String json = gson.toJson(listaJson);

        // Guardar la lista en un archivo
        File listaFile = new File(listasDir, listName + ".json");
        try (FileWriter writer = new FileWriter(listaFile)) {
            writer.write(json);
            System.out.println("Lista " + listName + " guardada exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar la lista " + listName + ": " + e.getMessage());
        }
    }

    /**
     * Elimina un archivo de lista de productos específico para un usuario dado.
     * 
     * <p>Este método busca el archivo JSON correspondiente a una lista de productos 
     * dentro del directorio del usuario especificado. Si el archivo existe, intenta 
     * eliminarlo. Si no se encuentra el archivo o el directorio, se muestra un mensaje 
     * en la salida estándar o de error según corresponda.</p>
     * 
     * @param lista el nombre de la lista de productos a eliminar (sin la extensión ".json").
     * @param username el nombre de usuario asociado a la lista de productos.
     * 
     * <p><strong>Nota:</strong> Este método no lanza excepciones, sino que maneja los 
     * errores internamente mediante mensajes en la salida estándar o de error.</p>
     * 
     * @see #saveListaProductos(LlistaProductes, String)
     */
    public void deleteListaProductos(String lista, String username) {
        File userDir = new File(userLog.get(username).getSecond());

        File listasDir = new File(userDir, "listasProductos");
        if (listasDir.exists() && listasDir.isDirectory()) {
            File listaFile = new File(listasDir, lista + ".json");

            if (listaFile.exists() && listaFile.isFile()) {
                if (listaFile.delete()) {
                    System.out.println("La lista de productos '" + lista + "' fue borrada exitosamente.");
                } else {
                    System.err.println("No se pudo borrar la lista de productos '" + lista + "'.");
                }
            } else {
                System.out.println("No se encontró la lista de productos con el nombre: " + lista);
            }
        } else {
            System.out.println("No se encontró el directorio de listas de productos para el usuario: " + username);
        }
    }

    // Los prints estan para el feedback, se puede quitar mas adelante (o no)

    /**
     * Cambia el nombre de una lista de productos específica para un usuario dado.
     * 
     * <p>Este método busca el archivo JSON correspondiente a una lista de productos con 
     * el nombre antiguo, y lo renombra al nuevo nombre dentro del directorio del usuario. 
     * Si el archivo original no existe, el nuevo nombre ya está en uso, o ocurre un error 
     * durante el proceso, se generan mensajes en la salida estándar o de error.</p>
     * 
     * @param oldName el nombre actual de la lista de productos (sin la extensión ".json").
     * @param newName el nuevo nombre deseado para la lista de productos (sin la extensión ".json").
     * @param username el nombre de usuario asociado a la lista de productos.
     * 
     * <p><strong>Nota:</strong> Este método no lanza excepciones, sino que maneja los errores 
     * internamente mediante mensajes en la salida estándar o de error. Además, no sobrescribe 
     * archivos existentes con el nuevo nombre.</p>
     * 
     * @see #deleteListaProductos(String, String)
     * @see #saveListaProductos(LlistaProductes, String)
     */
    public void changeListName(String oldName, String newName, String username) {
        File userDir = new File(userLog.get(username).getSecond());
        File listasDir = new File(userDir, "listasProductos");

        if (listasDir.exists() && listasDir.isDirectory()) {
            File oldFile = new File(listasDir, oldName + ".json");
            File newFile = new File(listasDir, newName + ".json");

            if (oldFile.exists() && oldFile.isFile()) {
                if (!newFile.exists()) {
                    if (oldFile.renameTo(newFile)) {
                        System.out.println("La lista de productos '" + oldName + "' fue renombrada a '" + newName + "' exitosamente.");
                    } else {
                        System.err.println("No se pudo renombrar la lista de productos '" + oldName + "' a '" + newName + "'.");
                    }
                } else {
                    System.out.println("Ya existe una lista con el nombre: " + newName);
                }
            } else {
                System.out.println("No se encontró la lista de productos con el nombre: " + oldName);
            }
        } else {
            System.out.println("No se encontró el directorio de listas de productos para el usuario: " + username);
        }
    }


    /**
     * Actualiza el contenido de una lista de productos específica para un usuario dado.
     * 
     * <p>Este método serializa la información actualizada de una lista de productos, 
     * incluyendo los productos y sus relaciones, y la guarda en el archivo correspondiente 
     * dentro del directorio del usuario. Si el directorio no existe, intenta crearlo 
     * automáticamente.</p>
     * 
     * @param username el nombre de usuario asociado a la lista de productos.
     * @param lista la lista de productos actualizada, que incluye los productos y sus relaciones.
     * 
     * @throws NullPointerException si {@code username} o {@code lista} son {@code null}.
     * @throws SecurityException si no se tiene permiso para crear o escribir en el directorio.
     * 
     * <p><strong>Nota:</strong> Los errores al crear directorios o al guardar el archivo se manejan 
     * internamente con mensajes en la salida estándar o de error.</p>
     * 
     * @see #saveListaProductos(LlistaProductes, String)
     * @see LlistaProductes
     */
    public void updateList(String username, LlistaProductes lista) {
        File userDir = new File(userLog.get(username).getSecond());
        File listasDir = new File(userDir, "listasProductos");

        if (!listasDir.exists() && !listasDir.mkdirs()) {
            System.err.println("Error: No es va poder crear el directori per a les llistes de productes.");
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Map<String, Object> listaJson = new HashMap<>();
        listaJson.put("name", lista.getName());

        Map<String, Object> productosJson = new HashMap<>();
        for (String producto : lista.getList()) {
            Map<String, Object> productoData = new HashMap<>();
            productoData.put("product", producto);

            // Obtenir les relacions del producte
            Map<String, Float> relaciones = lista.getSimilarityMatrix().get(producto);
            if (relaciones != null && !relaciones.isEmpty()) {
                List<Map<String, Object>> relacionesList = new ArrayList<>();
                for (Map.Entry<String, Float> entry : relaciones.entrySet()) {
                    Map<String, Object> relacion = new HashMap<>();
                    relacion.put("product", entry.getKey());
                    relacion.put("GdS", entry.getValue());
                    relacionesList.add(relacion);
                }
                productoData.put("relations", relacionesList);
            }

            productosJson.put(producto, productoData);
        }

        listaJson.put("productos", productosJson);

        String json = gson.toJson(listaJson);

        File listaFile = new File(listasDir, lista.getName() + ".json");
        try (FileWriter writer = new FileWriter(listaFile)) {
            writer.write(json);
            System.out.println("Llista " + lista.getName() + " actualitzada amb èxit.");
        } catch (IOException e) {
            System.err.println("Error en actualitzar la llista " + lista.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Carga una lista de productos específica desde un archivo JSON para un usuario dado.
     * 
     * <p>Este método busca el archivo correspondiente a la lista especificada en el directorio 
     * del usuario, lo lee y convierte su contenido en una instancia de {@code LlistaProductes}, 
     * incluyendo la matriz de similitud entre los productos.</p>
     * 
     * @param username el nombre de usuario asociado al archivo de la lista de productos.
     * @param nameList el nombre de la lista de productos (sin la extensión ".json").
     * @return una instancia de {@code LlistaProductes} que representa la lista de productos 
     *         cargada, o {@code null} si no se encuentra el archivo o ocurre un error al leerlo.
     * 
     * @throws NullPointerException si {@code username} o {@code nameList} son {@code null}.
     * @throws SecurityException si no se tiene permiso para acceder al directorio o archivo.
     * 
     * <p><strong>Nota:</strong> Este método utiliza un mapeo genérico para interpretar los 
     * datos JSON. Maneja los errores de lectura internamente mediante mensajes en la salida 
     * de error estándar.</p>
     * 
     * @see LlistaProductes
     * @see java.io.FileReader
     */
    @SuppressWarnings("unchecked")
    public LlistaProductes loadIsolatedProductList(String username, String nameList) {
        File userDir = new File(userLog.get(username).getSecond());
        File listasDir = new File(userDir, "listasProductos");
        Gson gson = new Gson();

        // Buscar el archivo que corresponde a la lista deseada
        File[] files = listasDir.listFiles((dir, name) -> name.endsWith(".json") && name.equalsIgnoreCase(nameList + ".json"));
        if (files == null || files.length == 0) {
            System.err.println("No se encontró el archivo de la lista: " + nameList);
            return null; // No se encontró la lista
        }

        File file = files[0]; // Asumimos que solo habrá un archivo con este nombre

        try (FileReader reader = new FileReader(file)) {
            // Leer el archivo JSON y convertirlo en una instancia de LlistaProductes
            Map<String, Object> jsonMap = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
            String name = (String) jsonMap.get("name");
            Map<String, Map<String, Float>> similarityMatrix = new HashMap<>();

            Map<String, Object> productos = (Map<String, Object>) jsonMap.get("productos");
            if (productos != null) {
                for (Map.Entry<String, Object> entry : productos.entrySet()) {
                    String product = entry.getKey();
                    Map<String, Object> productData = (Map<String, Object>) entry.getValue();
                    List<Map<String, Object>> relations = (List<Map<String, Object>>) productData.get("relations");

                    Map<String, Float> relacionesMap = new HashMap<>();
                    if (relations != null) {
                        for (Map<String, Object> relation : relations) {
                            String relatedProduct = (String) relation.get("product");
                            Float GdS = ((Number) relation.get("GdS")).floatValue();
                            relacionesMap.put(relatedProduct, GdS);
                        }
                    }
                    similarityMatrix.put(product, relacionesMap);
                }
            }

            // Crear la instancia de LlistaProductes
            LlistaProductes lista = new LlistaProductes();
            lista.setNom(name);
            lista.setSimilarityMatrix(similarityMatrix);

            return lista;

        } catch (IOException e) {
            System.err.println("Error al leer el archivo de lista: " + file.getName() + " - " + e.getMessage());
        }

        return null; // En caso de error, devolver null
    }

    //distribuciones

    /**
     * Carga todas las distribuciones desde archivos JSON ubicados en un directorio específico.
     * 
     * <p>Este método busca todos los archivos JSON en el directorio proporcionado, 
     * los lee y convierte su contenido en instancias de {@code Distribucion}. Cada 
     * distribución se almacena en un mapa con su nombre (sin extensión) como clave.</p>
     * 
     * @param distributionsDir el directorio que contiene los archivos JSON de las distribuciones.
     * @param username el nombre del usuario asociado a las distribuciones.
     * @return un mapa donde las claves son los nombres de las distribuciones y los valores 
     *         son las instancias correspondientes de {@code Distribucion}.
     * 
     * @throws NullPointerException si {@code distributionsDir} o {@code username} son {@code null}.
     * @throws SecurityException si no se tiene permiso para acceder al directorio o los archivos.
     * 
     * <p><strong>Nota:</strong> Si ocurre un error al cargar una distribución, se emite un mensaje 
     * en la salida de error estándar y la distribución no se incluye en el mapa.</p>
     * 
     * @see #loadIsolatedDistribution(String, String)
     * @see Distribucion
     */
    public Map<String, Distribucion> loadDistribuciones(File distributionsDir, String username) {
        Map<String, Distribucion> distributions = new HashMap<>();

        // Obtener todos los archivos JSON en el directorio
        File[] files = distributionsDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                // Extraer el nombre de la distribución sin extensión
                String distributionName = file.getName().replaceFirst("[.][^.]+$", "");

                // Cargar la distribución usando loadIsolatedDistribution
                Distribucion distribucion = loadIsolatedDistribution(username, distributionName);

                if (distribucion != null) {
                    // Añadir al mapa con el nombre como clave
                    distributions.put(distributionName, distribucion);
                } else {
                    System.err.println("Error al cargar la distribución: " + distributionName);
                }
            }
        }

        return distributions;
    }

    /**
     * Carga una distribución específica desde un archivo JSON para un usuario dado.
     * 
     * <p>Este método busca el archivo correspondiente a la distribución especificada en el 
     * directorio del usuario, lo lee y convierte su contenido en una instancia de {@code Distribucion}.
     * La distribución incluye información como el nombre, la estrategia asociada, el par de valores 
     * {@code Prestage}, la lista de productos, y el mapa de posiciones.</p>
     * 
     * @param username el nombre del usuario asociado al archivo de la distribución.
     * @param distributionName el nombre de la distribución (sin la extensión ".json").
     * @return una instancia de {@code Distribucion} que representa la distribución cargada, 
     *         o {@code null} si no se encuentra el archivo o ocurre un error al leerlo.
     * 
     * @throws NullPointerException si {@code username} o {@code distributionName} son {@code null}.
     * @throws SecurityException si no se tiene permiso para acceder al directorio o archivo.
     * 
     * <p><strong>Nota:</strong> Los errores durante la carga, ya sea al leer el archivo o al procesar 
     * su contenido, se manejan mediante mensajes en la salida de error estándar. En estos casos, 
     * el método devuelve {@code null}.</p>
     * 
     * @see Distribucion
     * @see Estrategia
     * @see AlgoritmoVoraz
     * @see Aproximation_Kruskal_ILS
     * @see java.io.FileReader
     */
    @SuppressWarnings("unchecked")
    public Distribucion loadIsolatedDistribution(String username, String distributionName) {
        File userDir = new File(userLog.get(username).getSecond());
        File distributionsDir = new File(userDir, "distribuciones");
        Gson gson = new Gson();

        // Buscar el archivo que corresponde a la distribución deseada
        File[] files = distributionsDir.listFiles((dir, name) -> name.endsWith(".json") && name.equalsIgnoreCase(distributionName + ".json"));
        if (files == null || files.length == 0) {
            System.err.println("No se encontró el archivo de la distribución: " + distributionName);
            return null; // No se encontró la distribución
        }

        File file = files[0]; // Asumimos que solo habrá un archivo con este nombre

        try (FileReader reader = new FileReader(file)) {
            // Leer el archivo JSON y convertirlo en una instancia de Distribucion
            Map<String, Object> jsonMap = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
            String name = (String) jsonMap.get("nom");

            // Cargar el par Prestage
            Map<String, Number> prestageMap = (Map<String, Number>) jsonMap.get("Prestage");
            Pair<Integer, Integer> prestage = new Pair<>(prestageMap.get("height").intValue(), prestageMap.get("length").intValue());

            // Cargar la lista de productos
            List<String> products = (List<String>) jsonMap.get("Dist");

            // Cargar la estrategia
            Estrategia estrategia;
            String Nomestrategia = ((String) jsonMap.get("estrategia"));
            if (Nomestrategia.equals("AlgoritmoVoraz")) estrategia = new AlgoritmoVoraz();
            else estrategia = new Aproximation_Kruskal_ILS();
            // Cargar el mapa de posiciones
            Map<String, Double> rawPositions = (Map<String, Double>) jsonMap.get("mapa");
            Map<String, Integer> positions = new HashMap<>();
            for (Map.Entry<String, Double> entry : rawPositions.entrySet()) {
                positions.put(entry.getKey(), entry.getValue().intValue());
            }

            // Crear la instancia de Distribucion

            return new Distribucion(prestage, products, name, estrategia, positions);

        } catch (IOException e) {
            System.err.println("Error al leer el archivo de distribución: " + file.getName() + " - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al procesar el archivo de distribución: " + file.getName() + " - " + e.getMessage());
        }

        return null; // En caso de error, devolver null
    }


    /**
     * Guarda una distribución específica como un archivo JSON en el directorio del usuario.
     * 
     * <p>Este método toma una instancia de {@code Distribucion} y la serializa a formato JSON. 
     * La información se guarda en un archivo en un directorio correspondiente al usuario.</p>
     * 
     * @param distribucion la distribución a guardar.
     * @param username el nombre del usuario asociado al archivo de distribución.
     * 
     * @throws NullPointerException si {@code distribucion} o {@code username} son {@code null}.
     * @throws SecurityException si no se tiene permiso para acceder o escribir en el directorio o archivo.
     * 
     * <p><strong>Nota:</strong> Si ocurre un error al guardar la distribución, se muestra un mensaje 
     * en la salida de error estándar y el método no completa la operación con éxito.</p>
     * 
     * @see Distribucion
     * @see Estrategia
     * @see com.google.gson.Gson
     */
    public void saveDistribution(Distribucion distribucion, String username) {
        // Obtener el directorio del usuario desde configFilePath
        String userDirectory = baseDirPath + File.separator + username;
        String distributionName = distribucion.getNom();
        File distributionsDir = new File(userDirectory, "distribuciones");

        // Crear el directorio de distribuciones del usuario si no existe
        if (!distributionsDir.exists() && !distributionsDir.mkdirs()) {
            System.err.println("Error: No se pudo crear el directorio para las distribuciones.");
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Preparar los datos de la distribución para serializar a JSON
        Map<String, Object> distributionJson = new HashMap<>();
        distributionJson.put("nom", distributionName);

        // Serializar el par Prestage
        Pair<Integer, Integer> prestage = distribucion.getPrestage();
        Map<String, Integer> prestageJson = new HashMap<>();
        prestageJson.put("height", prestage.getFirst());
        prestageJson.put("length", prestage.getSecond());
        distributionJson.put("Prestage", prestageJson);

        // Serializar la lista de productos Dist
        distributionJson.put("Dist", distribucion.getDist());

        // Agregar la estrategia (si la clase Estrategia tiene más información, puedes expandirlo)
        Estrategia estrategia = distribucion.getEstrategia();
        if (estrategia != null) {
            distributionJson.put("estrategia", estrategia.getClass().getSimpleName()); // Personaliza si Estrategia tiene métodos útiles
        }

        // Serializar el mapa de posiciones
        Map<String, Integer> mapa = distribucion.getMapa();
        if (mapa != null && !mapa.isEmpty()) {
            distributionJson.put("mapa", mapa);
        }

        // Serializar la distribución a formato JSON
        String json = gson.toJson(distributionJson);

        // Guardar la distribución en un archivo
        File distributionFile = new File(distributionsDir, distributionName + ".json");
        try (FileWriter writer = new FileWriter(distributionFile)) {
            writer.write(json);
            System.out.println("Distribución " + distributionName + " guardada exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar la distribución " + distributionName + ": " + e.getMessage());
        }
    }


    /**
     * Elimina una distribución específica basada en su nombre desde el directorio del usuario.
     * 
     * <p>Este método busca el archivo JSON que corresponde a la distribución especificada y lo elimina 
     * del directorio de distribuciones asociado al usuario.</p>
     * 
     * @param distributionName el nombre de la distribución a eliminar (sin la extensión ".json").
     * @param username el nombre del usuario asociado al archivo de distribución.
     * 
     * @throws NullPointerException si {@code distributionName} o {@code username} son {@code null}.
     * @throws SecurityException si no se tiene permiso para acceder o eliminar el archivo o directorio.
     * 
     * <p><strong>Nota:</strong> Si ocurre un error al eliminar la distribución, se mostrará un mensaje 
     * en la salida de error estándar y el método no completará la operación con éxito.</p>
     * 
     * @see java.io.File
     * @see java.io.File#delete()
     */
    public void deleteDistribution(String distributionName, String username) {
        // Obtener el directorio del usuario
        File userDir = new File(userLog.get(username).getSecond());

        // Directorio donde se guardan las distribuciones
        File distributionsDir = new File(userDir, "distribuciones");
        if (distributionsDir.exists() && distributionsDir.isDirectory()) {
            // Archivo de la distribución a eliminar
            File distributionFile = new File(distributionsDir, distributionName + ".json");

            if (distributionFile.exists() && distributionFile.isFile()) {
                // Intentar borrar el archivo
                if (distributionFile.delete()) {
                    System.out.println("La distribución '" + distributionName + "' fue borrada exitosamente.");
                } else {
                    System.err.println("No se pudo borrar la distribución '" + distributionName + "'.");
                }
            } else {
                System.out.println("No se encontró la distribución con el nombre: " + distributionName);
            }
        } else {
            System.out.println("No se encontró el directorio de distribuciones para el usuario: " + username);
        }
    }


    /**
     * Cambia el nombre de una distribución específica en el sistema.
     * 
     * <p>Este método renombra el archivo JSON asociado a una distribución del usuario de un nombre antiguo 
     * a un nuevo nombre, siempre y cuando el nuevo nombre no esté ya en uso.</p>
     * 
     * @param oldName Nombre antiguo de la distribución, sin la extensión ".json".
     * @param newName Nuevo nombre que se desea asignar a la distribución, sin la extensión ".json".
     * @param username Nombre del usuario asociado a la distribución.
     * 
     * @throws NullPointerException Si {@code oldName}, {@code newName} o {@code username} son {@code null}.
     * @throws SecurityException Si no se tiene permiso para acceder o modificar el archivo o directorio.
     * 
     * <p><strong>Nota:</strong> Si ocurre algún error durante el renombramiento, se mostrará un mensaje 
     * en la consola y la operación no será completada satisfactoriamente.</p>
     * 
     * @see java.io.File
     * @see java.io.File#renameTo(File)
     */
    public void changeDistName(String oldName, String newName, String username) {
        // Obtener el directorio del usuario
        File userDir = new File(userLog.get(username).getSecond());
        // Directorio donde se guardan las distribuciones
        File distributionsDir = new File(userDir, "distribuciones");

        if (distributionsDir.exists() && distributionsDir.isDirectory()) {
            // Archivo de la distribución con el nombre antiguo
            File oldFile = new File(distributionsDir, oldName + ".json");
            // Archivo de la distribución con el nuevo nombre
            File newFile = new File(distributionsDir, newName + ".json");

            if (oldFile.exists() && oldFile.isFile()) {
                // Verificar si ya existe una distribución con el nuevo nombre
                if (!newFile.exists()) {
                    // Intentar renombrar el archivo
                    if (oldFile.renameTo(newFile)) {
                        System.out.println("La distribución '" + oldName + "' fue renombrada a '" + newName + "' exitosamente.");
                    } else {
                        System.err.println("No se pudo renombrar la distribución '" + oldName + "' a '" + newName + "'.");
                    }
                } else {
                    System.out.println("Ya existe una distribución con el nombre: " + newName);
                }
            } else {
                System.out.println("No se encontró la distribución con el nombre: " + oldName);
            }
        } else {
            System.out.println("No se encontró el directorio de distribuciones para el usuario: " + username);
        }
    }

    //mensaje

    /**
     * Carga los mensajes almacenados en archivos JSON desde un directorio especificado.
     * 
     * <p>Este método recorre todos los archivos JSON en el directorio proporcionado, deserializándolos
     * en instancias de {@link MensajeDTO}. Los mensajes se almacenan en una lista que se devuelve al final.</p>
     * 
     * @param MessagesDir Directorio que contiene los archivos JSON de mensajes.
     * @return Una lista de {@link MensajeDTO} que contiene todos los mensajes cargados.
     * 
     * throws IOException Si ocurre un error al leer los archivos JSON.
     * @throws DateTimeParseException Si ocurre un error al procesar datos de fecha/hora en algún mensaje.
     * 
     * <p><strong>Nota:</strong> Si ocurre algún error al procesar un mensaje individual, se registrará un mensaje 
     * de error pero se continuará procesando los siguientes mensajes en el directorio.</p>
     * 
     * @see java.io.File
     * @see java.io.FileReader
     * @see com.google.gson.Gson
     * @see MensajeDTO
     */
    public List<MensajeDTO> loadMessages(File MessagesDir) {
        List<MensajeDTO> inbox = new ArrayList<>();
        File[] files = MessagesDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            Gson gson = new Gson();
            for (File file : files) {
                try (FileReader reader = new FileReader(file)) {
                    MensajeDTO dto = gson.fromJson(reader, MensajeDTO.class);
                    inbox.add(dto);
                } catch (IOException | DateTimeParseException e) {
                    System.err.println("Error al cargar el mensaje del inbox: " + file.getName() + " - " + e.getMessage());
                }
            }
        }
        return inbox;
    }

    /**
     * Guarda un mensaje en los directorios correspondientes según el remitente y destinatario.
     * 
     * <p>Esta función serializa un objeto {@link Mensaje} en formato JSON utilizando la clase {@link Gson}. Luego,
     * guarda este JSON en dos ubicaciones: en el directorio "inbox" del destinatario y en el directorio "sentMessages"
     * del remitente. Además, incluye un timestamp para distinguir archivos únicos.</p>
     * 
     * @param mensaje Instancia de {@link Mensaje} que se desea guardar.
     * 
     * throws IOException Si ocurre un error al escribir en los archivos JSON.
     * 
     * <p><strong>Nota:</strong> Si ocurre algún problema al intentar guardar el mensaje en cualquiera de las ubicaciones, 
     * se registrará un mensaje de error pero no se detendrá el proceso de escritura.</p>
     * 
     * @see com.google.gson.Gson
     * @see com.google.gson.GsonBuilder
     * @see java.io.File
     * @see java.io.FileWriter
     * @see java.time.format.DateTimeFormatter
     * @see Mensaje
     */
    public void guardarMensaje(Mensaje mensaje) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Map<String, Object> mensajeJson = new HashMap<>();
        mensajeJson.put("name", mensaje.getNombre());
        mensajeJson.put("object", mensaje.getObjeto() instanceof LlistaProductes ? "Lista" : "Distribucion");
        mensajeJson.put("esLista", mensaje.isLista());
        mensajeJson.put("leido", mensaje.isLeido());
        mensajeJson.put("LocalDateTime", mensaje.getTimestamp().toString());
        mensajeJson.put("Sender", mensaje.getSender().getUsername());
        mensajeJson.put("Destinatario", mensaje.getDestinatario().getUsername());

        String mensajeJsonStr = gson.toJson(mensajeJson);

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = mensaje.getTimestamp().format(formatter);

            String destinatarioUsername = mensaje.getDestinatario().getUsername();

            // Guardar en el inbox del destinatario
            String destinatarioDirPath = baseDirPath + File.separator + destinatarioUsername + File.separator + "inbox";
            File destinatarioDir = new File(destinatarioDirPath);
            if (!destinatarioDir.exists() && !destinatarioDir.mkdirs()) {
                System.err.println("Error: No se pudo crear el directorio del inbox del destinatario.");
                return;
            }

            String mensajeDestinatarioFileName = mensaje.getNombre() + "_" + destinatarioUsername + "_" + timestamp + ".json";
            File mensajeDestinatarioFile = new File(destinatarioDir, mensajeDestinatarioFileName);

            try (FileWriter writer = new FileWriter(mensajeDestinatarioFile)) {
                writer.write(mensajeJsonStr);
            }

            // Guardar en sentMessages del remitente
            String remitenteDirPath = baseDirPath + File.separator + mensaje.getSender().getUsername() + File.separator + "sentMessages";
            File remitenteDir = new File(remitenteDirPath);
            if (!remitenteDir.exists() && !remitenteDir.mkdirs()) {
                System.err.println("Error: No se pudo crear el directorio de sentMessages del remitente.");
                return;
            }

            String mensajeRemitenteFileName = mensaje.getNombre() + "_" + destinatarioUsername + "_" + timestamp + ".json";
            File mensajeRemitenteFile = new File(remitenteDir, mensajeRemitenteFileName);

            try (FileWriter writer = new FileWriter(mensajeRemitenteFile)) {
                writer.write(mensajeJsonStr);
            }

            System.out.println("Mensaje guardado exitosamente en inbox y sentMessages.");
        } catch (IOException e) {
            System.err.println("Error al guardar el mensaje: " + e.getMessage());
        }
    }


}
