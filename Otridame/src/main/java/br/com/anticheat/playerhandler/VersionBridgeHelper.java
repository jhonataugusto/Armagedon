package br.com.anticheat.playerhandler;

import br.com.anticheat.AntiCheat;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import br.com.anticheat.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class VersionBridgeHelper {

    public static ItemStack getStackInHand(PlayerData data){
        return getStackInHand(data.getDataPlayer());
    }

    public static ItemStack getStackInHand(Player data){
        return AntiCheat.SERVER_VERSION.isHigherThan(ServerVersion.v_1_8_8) ? data.getInventory().getItemInHand() : data.getItemInHand();
    }

}
