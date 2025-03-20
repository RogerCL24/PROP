package main.domain.classes.exceptions;

/**
 * Excepción personalizada para manejar errores relacionados con el formato.
 * 
 * @author Roger Cot Londres (roger.cot@estudiantat.upc.edu)
 */
public class formatException extends Exception {

    /**
     * Crea una instancia de formatException con un mensaje de error especificado.
     * @param message El mensaje de error.
     */
    public formatException(String message) {
        super(message);
    }

    /**
     * Crea una instancia de formatException con detalles del archivo y la línea donde ocurrió el error.
     * @param filename El nombre del archivo donde ocurrió el error.
     * @param lineNumber El número de línea donde ocurrió el error.
     * @param message El mensaje de error.
     */
    public formatException(String filename, int lineNumber, String message) {
        super("Error en el archivo '" + filename + "', linea " + lineNumber + ": " + message);
    }

    /**
     * Crea una instancia de formatException con un mensaje de error y una causa subyacente.
     * @param message El mensaje de error.
     * @param cause La causa del error.
     */
    public formatException(String message, Throwable cause) {
        super(message, cause);
    }

}
