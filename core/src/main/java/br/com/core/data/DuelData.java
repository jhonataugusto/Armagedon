package br.com.core.data;

import br.com.core.Core;
import br.com.core.account.Account;
import br.com.core.crud.mongo.DuelMongoCRUD;
import br.com.core.crud.redis.DuelRedisCRUD;
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
        DuelData data = Core.GSON.fromJson(json, DuelData.class);

        this._id = data.get_id();
        this.team1 = data.getTeam1();
        this.team2 = data.getTeam2();
        this.custom = data.isCustom();
        this.gameModeName = data.getGameModeName();
        this.mapName = data.getMapName();
        this.arenaId = data.getArenaId();
        this.spectators = data.getSpectators();
        this.registeredSpectators = data.getRegisteredSpectators();
        this.inventories = data.getInventories();
        this.ranked = data.isRanked();
    }

    public void saveData() {
        DuelRedisCRUD.save(this);

        DuelData data = DuelMongoCRUD.get(this.getUuid());

        if (data == null) {
            DuelMongoCRUD.create(this);
        } else {
            DuelMongoCRUD.save(this);
        }
    }

    public DuelData updateData() {
        return DuelRedisCRUD.findByUuid(uuid);
    }

    public void deleteData() {
        DuelRedisCRUD.delete(getUuid());
    }

    public String toJson() {
        return Core.GSON.toJson(this);
    }

    public static DuelData fromJson(String json) {
        return Core.GSON.fromJson(json, DuelData.class);
    }

    public static DuelData getContext(Account account) {
        return DuelRedisCRUD.getDuels().stream().filter(duel -> duel.getTeam1().contains(account.getUuid()) || duel.getTeam2().contains(account.getUuid())).findFirst().orElse(null);
    }

    public static DuelData getSpectatorContext(Account account) {
        return DuelRedisCRUD.getDuels().stream().filter(duel -> duel.getSpectators().contains(account.getUuid())).findFirst().orElse(null);
    }

    public static void removeDuelsFromAccount(Account account) {
        List<DuelData> duels = DuelRedisCRUD.getDuels().stream().filter(duel -> duel.getTeam1().contains(account.getUuid()) || duel.getTeam2().contains(account.getUuid())).collect(Collectors.toList());
        duels.forEach(duel -> {
            duel.saveData();
            duel.deleteData();
        });

        account.getData().setCurrentDuelUuid(null);
        account.getData().saveData();
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
