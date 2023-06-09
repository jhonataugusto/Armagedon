package br.com.anticheat.check.impl.aimassist;


import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.util.MathUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AimC extends Check {

    private int buffer;

    public AimC(final PlayerData playerData) {
        super(Category.COMBAT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {
            if (((FlyingEvent) event).hasLooked()) {
                Location to = ((FlyingEvent) event).toLocation();
                Location from = playerData.getLastLocation();
                if (event.getTimestamp() - playerData.getLastAttackPacket() > 500L) {
                    return;
                }

                float yawChange = MathUtil.getDistanceBetweenAngles(to.getYaw(), from.getYaw());
                if (yawChange > 0 && Math.abs(Math.floor(yawChange) - yawChange) < 1.0E-10) {
                    if (++buffer > 2) {
                        handleFlag(player, "AimAssist C", "§b* §fRound YAW" + "\n§b* §fyaw=§b" + yawChange, getBanVL("AimC"), 300000L);
                    } else {
                        buffer = 0;
                    }
                }
            }
        }
    }
}

