package drivers;

import main.domain.controllers.domainController;

import java.io.File;
import java.util.Scanner;

/**
 * Clase que actúa como controlador principal para ejecutar el programa.
 * Maneja la interacción con el usuario, la autenticación y el acceso al menú principal.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */

public class domainDriver {

    /**
     * Metodo principal que inicia la ejecución del programa.
     * Maneja la autenticación y el acceso al menú principal de la aplicación.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        String rutaBase = System.getProperty("user.dir");

        if (rutaBase.endsWith("/src")) {
            rutaBase = rutaBase.replace("/src", "");
        }
        
        String outputDir = rutaBase + File.separator + "src" + File.separator + "test" + File.separator + "outputs";
        clearDirectory(outputDir);

        Scanner scanner = new Scanner(System.in);
        domainController domC = new domainController();

        while (true) {
            boolean authenticated = false;
            while (!authenticated) {
                System.out.println("¡Bienvenido a Supermarket Distribution Generator!");
                System.out.println("Seleccione una opcion:");
                System.out.println("1. Registrarse");
                System.out.println("2. Iniciar sesion");
                String opcion = scanner.nextLine();

                switch (opcion) {
                    case "1":
                        authenticated = domC.registerUser(scanner);
                        break;
                    case "2":
                        authenticated = domC.loginUser(scanner);
                        break;
                    default:
                        System.out.println("Opcion invalida. Intente nuevamente.");
                        break;
                }
            }

            boolean continuar = true;
            while (continuar) {
                domC.menuPrincipal();
                String opcion = scanner.nextLine();
                continuar = domC.ejecutarOpcionMenuPrincipal(opcion, scanner);
            }
        }
    }

    /**
     * Metodo para limpiar el contenido de un directorio dado.
     * @param directoryPath La ruta del directorio a limpiar.
     */
    private static void clearDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        } else {
            System.out.println("El directorio especificado no existe o no es un directorio.");
        }
    }
}
