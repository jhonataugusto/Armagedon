package br.com.anticheat.manager;

import br.com.anticheat.AntiCheat;
import lombok.Getter;
import br.com.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerDataManager {

    @Getter
    private final Map<UUID, PlayerData> playerDataMap = Collections.synchronizedMap(new IdentityHashMap<>());

    private final AntiCheat antiCheat;

    public PlayerDataManager(AntiCheat antiCheat) {
        this.antiCheat = antiCheat;
    }

    public PlayerData getPlayerData(final Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public PlayerData remove(final Player player) {
        return playerDataMap.remove(player.getUniqueId());
    }

    public void add(final Player player) {
        this.playerDataMap.put(player.getUniqueId(), new PlayerData(player, antiCheat));
    }

}