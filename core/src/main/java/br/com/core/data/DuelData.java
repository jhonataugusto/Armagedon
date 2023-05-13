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
public class DuelData implements Serializable {
    private Object _id;
    private final UUID uuid = UUID.randomUUID();
    private Set<UUID> team1 = new HashSet<>(), team2 = new HashSet<>();
    private boolean custom = false;
    private String gameModeName = null;
    private String mapName = null;
    private String arenaId = null;
    private Set<UUID> spectators = new HashSet<>();
    private Set<UUID> registeredSpectators = new HashSet<>();
    private Map<String, String> inventories = new HashMap<>();
    private boolean ranked = false;

    public static final String REGEX_NAME_UUID_SEPARATOR = "#";

    public DuelData() {
    }

    public DuelData(String json) {

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

        DuelData data = DuelContextMongoCRUD.get(this.getUuid());

        if (data == null) {
            DuelContextMongoCRUD.create(this);
        } else {
            DuelContextMongoCRUD.save(this);
        }
    }

    public DuelData updateData() {
        return DuelContextRedisCRUD.findByUuid(uuid);
    }

    public void deleteData() {
        DuelContextRedisCRUD.delete(getUuid());
    }

    public String toJson() {
        return Core.GSON.toJson(this);
    }

    public static DuelData fromJson(String json) {
        return Core.GSON.fromJson(json, DuelData.class);
    }

    public static DuelData getContext(Account account) {
        return DuelContextRedisCRUD.getDuels().stream().filter(duel -> duel.getTeam1().contains(account.getUuid()) || duel.getTeam2().contains(account.getUuid())).findFirst().orElse(null);
    }

    public static DuelData getSpectatorContext(Account account) {
        return DuelContextRedisCRUD.getDuels().stream().filter(duel -> duel.getRegisteredSpectators().contains(account.getUuid())).findFirst().orElse(null);
    }

    public static void removeAllDuelContextsFromAccount(Account account) {
        List<DuelData> duels = DuelContextRedisCRUD.getDuels().stream().filter(duel -> duel.getTeam1().contains(account.getUuid()) || duel.getTeam2().contains(account.getUuid())).collect(Collectors.toList());
        duels.forEach(duel -> {
            duel.saveData();
            duel.deleteData();
        });
    }

    public String getNameAndUuidKey(String uuid) {

        for (Map.Entry<String, String> entry : getInventories().entrySet()) {
            String[] split = entry.getKey().split(REGEX_NAME_UUID_SEPARATOR);
            String name = split[0];
            String uniqueId = split[1];

            if (uniqueId.contains(uuid)) {
                return name + DuelData.REGEX_NAME_UUID_SEPARATOR + uniqueId;
            }

        }
        return null;
    }

    public boolean is1v1() {
        return this.getTeam1().size() == 1 && this.getTeam2().size() == 1;
    }
}
