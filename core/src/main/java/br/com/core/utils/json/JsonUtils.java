package br.com.core.utils.json;

import br.com.core.Core;
import br.com.core.data.object.EloDAO;
import br.com.core.data.object.InventoryDAO;
import br.com.core.data.object.PreferenceDAO;
import br.com.core.data.object.RankDAO;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
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

                } else if (keyType.equals(String.class) && (valueType.equals(Integer.class) || valueType.equals(Long.class))) {
                    Map<String, Integer> mapValue = Core.GSON.fromJson(jsonElement, new TypeToken<Map<String, Integer>>() {
                    }.getType());
                    field.set(object, mapValue);
                }

            } else if (field.getType() == Set.class) {
                Type genericType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

                if (genericType.equals(String.class)) {
                    Set<String> listValue = Core.GSON.fromJson(jsonElement, new TypeToken<Set<String>>() {
                    }.getType());
                    field.set(object, listValue);
                } else if (genericType.equals(UUID.class)) {
                    Set<UUID> listValue = Core.GSON.fromJson(jsonElement, new TypeToken<Set<UUID>>() {
                    }.getType());
                    field.set(object, listValue);
                } else if (genericType.equals(RankDAO.class)) {
                    Set<RankDAO> listValue = Core.GSON.fromJson(jsonElement, new TypeToken<Set<RankDAO>>() {
                    }.getType());
                    field.set(object, listValue);
                } else if (genericType.equals(InventoryDAO.class)) {

                    Set<InventoryDAO> listValue = Core.GSON.fromJson(jsonElement, new TypeToken<Set<InventoryDAO>>() {
                    }.getType());
                    field.set(object, listValue);
                } else if (genericType.equals(EloDAO.class)) {

                    Set<EloDAO> listValue = Core.GSON.fromJson(jsonElement, new TypeToken<Set<EloDAO>>() {
                    }.getType());
                    field.set(object, listValue);
                } else if (genericType.equals(PreferenceDAO.class)) {
                    Set<PreferenceDAO> listValue = Core.GSON.fromJson(jsonElement, new TypeToken<Set<PreferenceDAO>>() {
                    }.getType());
                    field.set(object, listValue);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
