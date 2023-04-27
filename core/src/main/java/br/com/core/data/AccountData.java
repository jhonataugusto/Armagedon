package br.com.core.data;

import br.com.core.Core;
import br.com.core.crud.mongo.AccountMongoCRUD;
import br.com.core.crud.redis.AccountRedisCRUD;
import br.com.core.enums.game.GameMode;
import br.com.core.utils.json.JsonUtils;
import br.com.core.utils.serialization.GenericSerialization;
import com.google.gson.*;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Data
public class AccountData implements Serializable {
    private Object _id = null;
    private String name;
    private UUID uuid;
    private String currentDuelUuid;
    private Map<String, String> inventories = new HashMap<>();

    private Map<String, Integer> elo = new HashMap<>();
    private Map<String, Integer> wins = new HashMap<>();
    private Map<String, Integer> loses = new HashMap<>();

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

        for (GameMode mode : GameMode.values()) {
            elo.put(mode.getName(), 1000);
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
        AccountData oldData = AccountRedisCRUD.findByUuid(this.getUuid());

        AccountRedisCRUD.save(this);

        if (oldData == null) {
            return;
        }

        if (oldData.getName() == null) {
            AccountMongoCRUD.save(this);
        }
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

    public String tobase64() {
        return GenericSerialization.serializeToBase64(this);
    }
}
