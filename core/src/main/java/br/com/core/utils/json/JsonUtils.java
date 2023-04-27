package br.com.core.utils.json;

import br.com.core.Core;
import br.com.core.data.AccountData;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
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
                    Map<String, String> mapValue = Core.GSON.fromJson(jsonElement, new TypeToken<Map<String, String>>() {
                    }.getType());
                    field.set(object, mapValue);
                } else if (keyType.equals(String.class) && valueType.equals(Integer.class)) {
                    Map<String, Integer> mapValue = Core.GSON.fromJson(jsonElement, new TypeToken<Map<String, Integer>>() {
                    }.getType());
                    field.set(object, mapValue);
                } else if (keyType.equals(AccountData.class) && valueType.equals(String.class)) {
                    Map<AccountData, String> mapValue = Core.GSON.fromJson(jsonElement, new TypeToken<Map<AccountData, String>>() {
                    }.getType());
                    field.set(object, mapValue);
                }
            } else if (field.getType() == List.class) {
                Type genericType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                if (genericType.equals(String.class)) {
                    List<String> listValue = Core.GSON.fromJson(jsonElement, new TypeToken<List<String>>() {
                    }.getType());
                    field.set(object, listValue);
                }
                if (genericType.equals(UUID.class)) {
                    List<UUID> listValue = Core.GSON.fromJson(jsonElement, new TypeToken<List<UUID>>() {
                    }.getType());
                    field.set(object, listValue);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
