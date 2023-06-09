package br.com.anticheat.check.impl.killaura;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.AttackEvent;
import br.com.anticheat.event.FlyingEvent;
import org.bukkit.entity.Player;

public class KillauraA extends Check {

    public Long lastUseEntity;
    private int buffer;

    public KillauraA(final PlayerData playerData) {
        super(Category.COMBAT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {
            if (lastUseEntity != null) {
                double delay = System.currentTimeMillis() - lastUseEntity;
                if (delay < 100 && delay > 40 && !playerData.hasFast() && !playerData.droppedPackets && !playerData.isLagging(System.currentTimeMillis(), 250L)) {
                    if (++buffer > 3) {
                        handleFlag(player, "Killaura A", "§b* §fPost killaura\n§b* §fdelay=§b" + delay, getBanVL("KillauraA"), 60000L);
                        lastUseEntity = null;
                    }
                    //handleVerbose(player, "Killaura A", "§b* §fPost killaura\n§b* §fdelay=§b" + delay);
                } else {
                    buffer = Math.max(buffer - 1, 0);
                }
            }
        } else if (event instanceof AttackEvent) {

            if (System.currentTimeMillis() - playerData.getLastFlying() < 10L) {
                lastUseEntity = playerData.getLastFlying();
            } else {
                buffer = Math.max(buffer - 1, 0);
            }
        }
    }
}