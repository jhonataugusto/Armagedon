package br.com.armagedon.data;

import br.com.armagedon.Core;
import br.com.armagedon.crud.redis.AccountRedisCRUD;
import br.com.armagedon.utils.JsonUtils;
import com.google.gson.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;


@Data
@NoArgsConstructor
public class AccountData {
    private Object _id;
    private String name;
    private UUID uuid;
    private int blocks;

    public AccountData setDefaultData(String name, UUID uuid) {
        return new AccountData(name, uuid, 0);
    }

    private AccountData(String name, UUID uuid, int blocks) {
        this.name = name;
        this.uuid = uuid;
        this.blocks = blocks;
    }

    public AccountData(String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String fieldName = entry.getKey();
            JsonElement value = entry.getValue();

            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                JsonUtils.setFieldFromJson(field, value, this);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveData() {
        AccountRedisCRUD.save(this);
    }

    public void deleteData() {
        AccountRedisCRUD.delete(this.getUuid());
    }

    public String toJson() {
        return Core.GSON.toJson(this);
    }

    public static AccountData fromJson(String json) {
        return Core.GSON.fromJson(json, AccountData.class);
    }
}
