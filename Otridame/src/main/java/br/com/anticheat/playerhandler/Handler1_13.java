package br.com.anticheat.playerhandler;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import br.com.anticheat.util.MovementUtils;
import org.bukkit.entity.Player;

public class Handler1_13 {

    public static boolean isGliding(Player player) {
        if(PacketEvents.getAPI().getServerUtils().getVersion().isLowerThan(ServerVersion.v_1_9)) {
            return false;
        }
//        if(player.isGliding()) Karhu.getInstance().getPlayerDataManager().getPlayerData(player).setLastGlide(System.currentTimeMillis());
//        if(player.isGliding()) Karhu.getInstance().getPlayerDataManager().getPlayerData(player).setGliding(player.isGliding());
//        return player.isGliding();
        return false;
    }

    public static boolean isRiptiding(Player player) {
        if(PacketEvents.getAPI().getServerUtils().getVersion().isLowerThan(ServerVersion.v_1_13)) {
            return false;
        }
//        if(player.isRiptiding()) Karhu.getInstance().getPlayerDataManager().getPlayerData(player).setLastRiptide(System.currentTimeMillis());
//        if(player.isRiptiding()) Karhu.getInstance().getPlayerDataManager().getPlayerData(player).setRiptiding(player.isRiptiding());
//        return player.isRiptiding();
        return false;
    }

    public static float getDolphinLevel(Player player) {
        if(PacketEvents.getAPI().getServerUtils().getVersion().isLowerThan(ServerVersion.v_1_12_2)) {
            return 0;
        }
//        return (float) MovementUtils.getPotionEffectLevel(player, PotionEffectType.DOLPHINS_GRACE);
        return 0.0f;
    }
    public static float getSoulSpeedEnchant(Player player) {
        if(PacketEvents.getAPI().getServerUtils().getVersion().isLowerThan(ServerVersion.v_1_16)) {
            return 0;
        }
        return MovementUtils.getSoulSpeedLevel(player);
    }

    public static float getRiptideEnchant(Player player) {
        if(PacketEvents.getAPI().getServerUtils().getVersion().isLowerThan(ServerVersion.v_1_13)) {
            return 0;
        }
        return MovementUtils.getSoulSpeedLevel(player);
    }
}
