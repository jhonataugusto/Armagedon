package br.com.core.data;

import br.com.core.Core;
import br.com.core.crud.mongo.AccountMongoCRUD;
import br.com.core.crud.redis.AccountRedisCRUD;
import br.com.core.utils.json.JsonUtils;
import com.google.gson.*;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Data
public class AccountData {
    private Object _id = null;
    private String name = "...";
    private UUID uuid;
    private Map<String, String> inventories = new HashMap<>();

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

    public AccountData createOrGetAccountData() {
        AccountData data = AccountRedisCRUD.findByUuid(getUuid());

        if (data == null) {
            data = AccountMongoCRUD.get(getUuid());

            if (data != null) {
                AccountRedisCRUD.save(data);
            }
        }

        if (data == null) {
            data = new AccountData(getUuid());
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
