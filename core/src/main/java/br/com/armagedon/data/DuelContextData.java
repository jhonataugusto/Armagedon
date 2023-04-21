package br.com.armagedon.data;

import br.com.armagedon.Core;
import br.com.armagedon.account.Account;
import br.com.armagedon.crud.redis.DuelContextRedisCRUD;
import br.com.armagedon.utils.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class DuelContextData {
    private final UUID uuid = UUID.randomUUID();
    private List<UUID> team1 = new ArrayList<>(), team2 = new ArrayList<>();
    private boolean custom = false;
    private String gameMode = null;
    private String mapName = null;
    private boolean ranked = false;

    public DuelContextData() {
    }

    public DuelContextData(String json) {

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

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

    public void save() {
        DuelContextRedisCRUD.save(this);
    }

    public DuelContextData update() {
        return DuelContextRedisCRUD.findByUuid(uuid);
    }

    public void delete() {
        DuelContextRedisCRUD.delete(getUuid());
    }

    public String toJson() {
        return Core.GSON.toJson(this);
    }

    public static DuelContextData fromJson(String json) {
        return Core.GSON.fromJson(json, DuelContextData.class);
    }

    public static DuelContextData getContext(Account account) {
        return DuelContextRedisCRUD.getDuels().stream().filter(duel -> duel.getTeam1().contains(account.getUuid()) || duel.getTeam2().contains(account.getUuid())).findFirst().orElse(null);
    }

}
