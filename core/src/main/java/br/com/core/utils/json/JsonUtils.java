package br.com.core.utils.json;

import br.com.core.Core;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class JsonUtils {

    public static void setFieldFromJson(Field field, JsonElement jsonElement, Object object) {
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
            } else if (Map.class.isAssignableFrom(field.getType())) {
                Type[] typeArgs = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                Type keyType = typeArgs[0];
                Type valueType = typeArgs[1];
                if (keyType.equals(String.class) && valueType.equals(String.class)) {
                    Map<String, String> mapValue = Core.GSON.fromJson(jsonElement, new TypeToken<Map<String, String>>(){}.getType());
                    field.set(object, mapValue);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
