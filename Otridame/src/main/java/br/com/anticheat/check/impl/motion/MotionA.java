package br.com.anticheat.check.impl.motion;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.util.MathUtil;
import br.com.anticheat.util.UtilPlayer;
import org.bukkit.entity.Player;

public class MotionA extends Check {

    private double buffer;

    public MotionA(final PlayerData playerData) {
        super(Category.MOVEMENT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {

            if (((FlyingEvent) event).hasMoved()) {

                if(UtilPlayer.isNearLiquid(player)
                        || UtilPlayer.isNearHoney(player)
                        || player.isInsideVehicle()
                        || UtilPlayer.isNearAnvil(player)
                        || UtilPlayer.isNearScaffolding(player)
                        || UtilPlayer.isNearClimbable(player)
                        || System.currentTimeMillis() - playerData.getLastMovePkt() > 2250L
                        || playerData.getVelocityV() > 0
                        || player.getAllowFlight()
                        || playerData.getDeltaY() == 0
                        || playerData.lastServerPositionTick < 60 + MathUtil.getPingInTicks(playerData.getTransPing())
                        || playerData.slimeTicks > 0) return;

                if(UtilPlayer.isNearTrapdoor(player.getLocation()) || UtilPlayer.isNearIce(player)) {
                    buffer = 0;
                    return;
                }

                if (playerData.getDeltaY() == -playerData.getLastDeltaY() && (playerData.getTeleportLocation() == null && System.currentTimeMillis() - playerData.getLastTeleport() > 3250L)) {
                    if(++buffer > 3) {
                        handleFlag(player, "Motion A", "§b* §fNot following MCP jump laws\n§b* §fMotion=§b" + playerData.getDeltaY(), getBanVL("MotionA"), 60000L);
                    }
                } else {
                    buffer = Math.max(buffer - 1, 0.0);
                }
            }
        }
    }
}
