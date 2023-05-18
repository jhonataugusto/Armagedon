package br.com.core.data;

import br.com.core.Core;
import br.com.core.account.enums.preferences.Preference;
import br.com.core.account.enums.rank.Rank;
import br.com.core.crud.mongo.AccountMongoCRUD;
import br.com.core.crud.redis.AccountRedisCRUD;
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
    private String currentDuelUuid;
    private Set<RankDAO> ranks = new HashSet<>();
    private Set<InventoryDAO> inventories = new HashSet<>();
    private Set<EloDAO> elos = new HashSet<>();
    private Set<PreferenceDAO> preferences = new HashSet<>();
    private Set<StatisticsDAO> statistics = new HashSet<>(); //TODO
    private Set<PunishmentDAO> punishments = new HashSet<>(); //TODO
    private Set<AltDAO> alts = new HashSet<>(); //TODO: fazer ligação de contas alts vinculadas a essa conta

    /**
     * cria uma nova data para o jogador.
     *
     * @param uuid id único do jogador.
     */
    public AccountData(UUID uuid) {

        this.uuid = uuid;

        for (GameMode mode : GameMode.values()) {
            elos.add(new EloDAO(mode.getName(), 1000));
            statistics.add(new StatisticsDAO(mode.getName(), 0, 0, 0, 0, 0, 0));
        }

        if (ranks.isEmpty()) {
            ranks.add(new RankDAO(Rank.MEMBER.getName(), -1));
        }

        for (Preference preferenceInfo : Preference.values()) {
            if (preferenceInfo.getType() != null) {
                preferences.add(new PreferenceDAO(preferenceInfo.getName(), preferenceInfo.getType().toLowerCase(), false));
            } else {
                preferences.add(new PreferenceDAO(preferenceInfo.getName(), null, false));
            }
        }
    }

    /**
     * carrega uma nova data para o jogador.
     *
     * @param json json da classe {@link AccountData}.
     */
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

    /**
     * se não existir nenhuma conta no Redis ou no Mongo, ele cria e salva nos dois bancos de dados.
     */
    public static AccountData fetch(UUID uuid) {

        AccountData data = AccountRedisCRUD.findByUuid(uuid);

        if (data == null) {

            data = AccountMongoCRUD.get(uuid);

            if (data != null) {
                AccountRedisCRUD.save(data);
            } else {
                data = new AccountData(uuid);
                AccountRedisCRUD.save(data);
                AccountMongoCRUD.create(data);
            }
        }

        return data;
    }

    /**
     * salva os dados no MongoDB e Redis.
     */
    public void saveData() {
        AccountData oldData = AccountRedisCRUD.findByUuid(this.getUuid());

        if (oldData == null) {
            oldData = AccountMongoCRUD.get(this.getUuid());
        }

        if (oldData != null && oldData.hashCode() != this.hashCode()) {
            AccountRedisCRUD.save(this);
            AccountMongoCRUD.save(this);
        }
    }

    /**
     * Deleta os dados no Redis
     */
    public void deleteData() {
        AccountRedisCRUD.delete(this.getUuid());
    }

    public String toJson() {
        return Core.GSON.toJson(this);
    }

    public static AccountData fromJson(String json) {
        return Core.GSON.fromJson(json, AccountData.class);
    }

    /**
     * Serializa o {@link AccountData}.
     *
     * @return uma string do tipo base64.
     */

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

    public PreferenceDAO getPreferenceByName(Preference preference) {
        return getPreferences().stream().filter(preferences -> preferences.getName().equalsIgnoreCase(preference.getName())).findFirst().orElse(null);
    }


}
