package drivers;

import main.presentation.controllers.presentationController;

/**
 * Driver que se usa para correr el programa.
 */
public class presentationDriver {
    /**
     * Método principal que inicia la ejecución de la aplicación.
     *
     * @param args argumentos pasados desde la línea de comandos.
     */
    public static void main(String[] args) {
        presentationController controller = new presentationController();
    }
}
