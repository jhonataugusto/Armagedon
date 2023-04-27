package br.com.core.data;

import br.com.core.Core;
import br.com.core.account.Account;
import br.com.core.crud.mongo.DuelContextMongoCRUD;
import br.com.core.crud.redis.DuelContextRedisCRUD;
import br.com.core.utils.json.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class DuelContextData implements Serializable {
    private Object _id;
    private final UUID uuid = UUID.randomUUID();
    private List<UUID> team1 = new ArrayList<>(), team2 = new ArrayList<>();
    private boolean custom = false;
    private String gameMode = null;
    private String mapName = null;
    private String arenaId = null;
    private List<UUID> spectators = new ArrayList<>();
    private Map<String, String> inventories = new HashMap<>();
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

    public void saveData() {
        DuelContextRedisCRUD.save(this);

        DuelContextData data = DuelContextMongoCRUD.get(this.getUuid());

        if (data == null) {
            DuelContextMongoCRUD.create(this);
        } else {
            DuelContextMongoCRUD.save(this);
        }

    }

    public DuelContextData updateData() {
        return DuelContextRedisCRUD.findByUuid(uuid);
    }

    public void deleteData() {
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

    public static DuelContextData getSpectatorContext(Account account) {
        return DuelContextRedisCRUD.getDuels().stream().filter(duel -> duel.getSpectators().contains(account.getUuid())).findFirst().orElse(null);
    }

    public static void removeAllDuelContextsFromAccount(Account account) {
        List<DuelContextData> duels = DuelContextRedisCRUD.getDuels().stream().filter(duel -> duel.getTeam1().contains(account.getUuid()) || duel.getTeam2().contains(account.getUuid())).collect(Collectors.toList());
        duels.forEach(duel -> {
            duel.saveData();
            duel.deleteData();
        });
    }

    public String getNameAndUuidKey(String uuid) {

        for (Map.Entry<String, String> entry : getInventories().entrySet()) {
            String[] split = entry.getKey().split("_");
            String name = split[0];
            String uniqueId = split[1];

            if(uniqueId.contains(uuid)) {
                return name + "_" + uniqueId;
            }

        }
        return null;
    }
}
