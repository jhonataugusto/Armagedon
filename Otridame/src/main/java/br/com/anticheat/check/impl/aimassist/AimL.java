package br.com.anticheat.check.impl.aimassist;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.util.MathUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AimL extends Check {

    private double buffer;

    public AimL(final PlayerData playerData) {
        super(Category.COMBAT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {
            if (((FlyingEvent) event).hasLooked()) {
                Location to = ((FlyingEvent) event).toLocation();
                Location from = playerData.getLastLocation();

                if (playerData.lastAttackTick > 4) {
                    return;
                }

                final float angleYaw = MathUtil.getDistanceBetweenAngles(to.getYaw(), from.getYaw());
                final float anglePitch = MathUtil.getDistanceBetweenAngles(to.getPitch(), from.getPitch());

                final double shit = anglePitch % angleYaw;

                //Bukkit.broadcastMessage("A: " + angleYaw + " NaN: " + Double.isNaN(shit));

                if(angleYaw > 0.1 && Double.isNaN(shit)) {
                    if(++buffer > 10) {
                        handleFlag(player, "AimAssist L", "§b* §fInvalid rotation" + "\n§b* §fdeltaYaw=§b" + angleYaw + "\n§b* §fP % Y=§b" + shit, getBanVL("AimL"), 500000L);
                    }
                } else {
                    buffer = Math.max(buffer - 1, 0);
                }
            }
        }
    }
}
