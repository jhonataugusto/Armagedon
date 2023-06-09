package br.com.anticheat.check.impl.step;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.util.MathUtil;
import br.com.anticheat.util.MovementUtils;
import br.com.anticheat.util.UtilPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class StepB extends Check {

    private double buffer;

    public StepB(final PlayerData playerData) {
        super(Category.MOVEMENT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {

            if(((FlyingEvent) event).hasMoved()) {

                if(player.getAllowFlight()
                        || playerData.getVelocityV() > 0.0
                        || event.getTimestamp() - playerData.getLastVehicle() < 2000L
                        || playerData.getTotalTicks() < 50
                        || System.currentTimeMillis() - playerData.getLastTeleport() < 2250L
                        || playerData.getLastServerPositionTick() < 65) return;

                float limit = 0.8f + ((MovementUtils.getPotionEffectLevel(player, PotionEffectType.JUMP)) * 0.2f);

                limit += System.currentTimeMillis() - playerData.getLastGlide() < 4400L ? 5.2 : 0;
                limit += System.currentTimeMillis() - playerData.getLastRiptide() < 4400L ? 5.2 : 0;
                limit += playerData.getSlimeHeight();

                if (playerData.deltaY >= limit) {
                    if (MathUtil.onGround(playerData.getLastLocation().getY()) || MathUtil.onGround(playerData.getLastLastLastLocation().getY()) && !UtilPlayer.isLandingSoonOnSlime(player.getLocation())) {
                        handleFlag(player, "Step B", "§b* §fMotionY=§b" + playerData.deltaY + "\n§b* §fpredict=§b" + limit, getBanVL("StepB"), 800000L);
                    } else {
                        if(++buffer > 1) {
                            handleFlag(player, "Step B", "§b* §fMotionY=§b" + playerData.deltaY + "\n§b* §fpredict=§b" + limit, getBanVL("StepB"), 800000L);
                        }
                    }
                } else {
                    if(playerData.deltaY > 0) {
                        buffer = Math.max(buffer - 0.5, 0);
                    }
                }
            }
        }
    }
}
