package main.domain.classes.types;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador para serializar y des-serializar {@link LocalDateTime} objetos con Gson.
 * Esta clase implementa {@link JsonDeserializer} y {@link JsonSerializer} para convertir instancias de
 * {@link LocalDateTime} a y desde JSON strings usando el formato {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
 *
 * @author Nadia Khier (nadia.khier@estudiantat.upc.edu)
 */
public class LocalDateTimeAdapter implements JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {

    /**
     * Formateador usado para convertir {@link LocalDateTime} a y desde su representación en string de ISO-8601.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Serializa un objeto {@link LocalDateTime} en su representación de string JSON.
     *
     * @param src la instancia {@link LocalDateTime} a serializar.
     * @param typeOfSrc el tipo real (ignorado en esta implementación).
     * @param context el contexto de serialización (proporcionado por Gson).
     * @return un {@link JsonPrimitive} que contiene el string date-time formateado.
     */
    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(formatter));
    }
/**
 * Deserializes a JSON string into a {@link LocalDateTime} object.
 *
 * @param json the JSON element containing the date-time string.
 * @param typeOfT the actual type (ignored in this implementation).
 * @param context the deserialization context (provided by Gson).
 * @return a {@link LocalDateTime} parsed from the input JSON string.
 * @throws JsonParseException if the input JSON string cannot be parsed into a {@link LocalDateTime}.
 */

    /**
     * Des-serializa un string JSON en un objeto {@link LocalDateTime}.
     * @param json el elemento JSON conteniendo el date-time string.
     * @param typeOfT el tipo real (ignorado en esta implementación).
     * @param context el contexto de des-serialización (proporcionado por Gson).
     * @return un {@link LocalDateTime} parseado del input JSON string.
     * @throws JsonParseException si el input JSON string no puede ser parseado a {@link LocalDateTime}.
     */
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), formatter);
    }
}
