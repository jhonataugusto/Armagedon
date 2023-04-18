package br.com.armagedon.util;

import com.google.gson.JsonElement;

import java.lang.reflect.Field;
import java.util.UUID;

public class JsonUtils {

    public static void setFieldFromJson(Field field, Object object, JsonElement jsonElement) {
        try {
            field.setAccessible(true);

            if (field.getType() == int.class || field.getType() == Integer.class) {
                field.set(object, jsonElement.getAsInt());
            } else if (field.getType() == String.class) {
                field.set(object, jsonElement.getAsString());
            } else if (field.getType() == UUID.class) {
                field.set(object, UUID.fromString(jsonElement.getAsString()));
            } else if (field.getType() == boolean.class) {
                field.set(object, jsonElement.getAsBoolean());
            } else if (field.getType() == double.class) {
                field.set(object, jsonElement.getAsDouble());
            } else if (field.getType() == JsonElement.class) {
                field.set(object, jsonElement);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
