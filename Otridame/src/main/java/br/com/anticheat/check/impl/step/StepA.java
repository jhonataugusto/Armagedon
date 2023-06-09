package br.com.anticheat.check.impl.step;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.util.MathUtil;
import br.com.anticheat.util.MovementUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class StepA extends Check {

    private double buffer;

    public StepA(final PlayerData playerData) {
        super(Category.MOVEMENT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {

            if(((FlyingEvent) event).hasMoved()) {

                //Bukkit.broadcastMessage("dY: " + playerData.getDeltaY() + " gLL: " + MathUtil.onGround(playerData.getLastLocation().getY()) + " gLLL: " + MathUtil.onGround(playerData.getLastLastLastLocation().getY()));

                if(player.getAllowFlight()
                        || playerData.getVelocityV() > 0
                        || event.getTimestamp() - playerData.getLastVehicle() < 2000L
                        || playerData.getSlimeTicks() > 0
                        || System.currentTimeMillis() - playerData.lastOnSlime < 4250L
                        || playerData.getLastServerPositionTick() < 65
                        || System.currentTimeMillis() - playerData.getLastTeleport() < 2250L
                        || playerData.getTotalTicks() < 50
                        || playerData.liquidTicks > 0) return;

                float limit = 0.5625f + ((MovementUtils.getPotionEffectLevel(player, PotionEffectType.JUMP) * 0.2f) + playerData.getVelocityV());

                limit += System.currentTimeMillis() - playerData.getLastGlide() < 4400L ? 4.2 : 0;
                limit += System.currentTimeMillis() - playerData.getLastRiptide() < 4400L ? 4.2 : 0;
                limit += playerData.getSlimeHeight();

                if (playerData.deltaY > limit && (MathUtil.onGround(playerData.getLastLocation().getY()) || MathUtil.onGround(playerData.getLastLastLastLocation().getY()))) {
                    if (++buffer > 2) {
                        handleFlag(player, "Step A", "§b* §fMotionY=§b" + playerData.deltaY + "\n§b* §fpredict=§b" + limit, getBanVL("StepA"), 800000L);
                    }
                } else {
                    if(playerData.deltaY > 0) {
                        buffer = Math.max(buffer - 1, 0);
                    }
                }
            }
        }
    }
}
