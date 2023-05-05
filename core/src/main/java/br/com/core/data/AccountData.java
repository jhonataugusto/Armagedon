package br.com.core.data;

import br.com.core.Core;
import br.com.core.account.enums.preferences.Preferences;
import br.com.core.account.enums.rank.Rank;
import br.com.core.crud.mongo.AccountMongoCRUD;
import br.com.core.crud.redis.account.AccountRedisCRUD;
import br.com.core.data.object.*;
import br.com.core.enums.game.GameMode;
import br.com.core.utils.json.JsonUtils;
import br.com.core.utils.serialization.GenericSerialization;
import com.google.gson.*;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;


@Data
public class AccountData implements Serializable {
    private Object _id = null;
    private String name;
    private UUID uuid;
    private String currentDuelContextUuid;
    private List<RankDAO> ranks = new ArrayList<>();
    private List<InventoryDAO> inventories = new ArrayList<>();
    private List<EloDAO> elos = new ArrayList<>();
    private List<PreferencesDAO> preferences = new ArrayList<>();
    private List<StatisticsDAO> statistics = new ArrayList<>(); //TODO
    private List<PunishmentDAO> punishments = new ArrayList<>(); //TODO
    private List<AltDAO> alts = new ArrayList<>(); //TODO: fazer ligação de contas alts vinculadas a essa conta

    //DEFAULT CONSTRUCTOR WHEN THE PROGRAM CREATES A NEW ACCOUNT
    public AccountData(UUID uuid) {
        this.uuid = uuid;

        for (GameMode mode : GameMode.values()) {
            elos.add(new EloDAO(mode.getName(), 1000));
            statistics.add(new StatisticsDAO(mode.getName(), 0, 0, 0, 0, 0, 0));
        }

        if (ranks.isEmpty()) {
            ranks.add(new RankDAO(Rank.MEMBER.getName(), -1));
        }

        for (Preferences preferencesInfo : Preferences.values()) {
            preferences.add(new PreferencesDAO(preferencesInfo.getName(), false));
        }
    }

    //DEFAULT CONSTRUCTOR WHEN THE PROGRAM LOAD A ACCOUNT
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

    public int getElos(String gameModeName) {
        return getEloByGameModeName(gameModeName).getElo();
    }

    public EloDAO getEloByGameModeName(String gameModeName) {
        return getElos().stream().filter(elo -> elo.getName().equalsIgnoreCase(gameModeName)).findFirst().orElse(null);
    }

    public InventoryDAO getInventoryByGameModeName(String gameModeName) {
        return getInventories().stream().filter(inventory -> inventory.getGamemodeName().equalsIgnoreCase(gameModeName)).findFirst().orElse(null);
    }
}
