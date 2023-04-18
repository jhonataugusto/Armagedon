package br.com.armagedon.data;

import br.com.armagedon.Core;
import br.com.armagedon.crud.mongo.AccountMongoCRUD;
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

    public AccountData setDefaultData(UUID uuid) {
        return new AccountData(uuid);
    }

    public AccountData(UUID uuid) {
        this.uuid = uuid;
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

    public AccountData createDataOrGet(UUID uuid) {
        AccountData data = AccountRedisCRUD.findByUuid(uuid);

        if (data == null) {
            data = AccountMongoCRUD.get(uuid);

            if (data != null) {
                AccountRedisCRUD.save(data);
            }
        }

        if (data == null) {
            data = new AccountData().setDefaultData(getUuid());
            AccountRedisCRUD.save(data);
            AccountMongoCRUD.create(data);
        }

        return data;
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
