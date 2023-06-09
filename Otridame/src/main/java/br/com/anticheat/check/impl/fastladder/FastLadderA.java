package br.com.anticheat.check.impl.fastladder;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.util.UtilPlayer;
import org.bukkit.entity.Player;

public class FastLadderA extends Check {

    public FastLadderA(final PlayerData playerData) {
        super(Category.MOVEMENT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {

            if (((FlyingEvent) event).hasMoved()) {
                if(playerData.getClimbableTicks() <= 15
                        || playerData.getTotalTicks() - playerData.getLastFlyTick() < 40
                        || UtilPlayer.isNearGround(player.getLocation())
                        || playerData.getVelocityV() > 0
                        || playerData.getVelocityH() > 0) return;
                if(playerData.deltaY >= 0.15) {
                    handleFlag(player, "FastLadder A", "§b* §fdeltaY=§b" + playerData.deltaY + "\n§b* §fmaxSpeed=§b0.15", getBanVL("FastLadderA"), 60000L);
                }
            }
        }
    }
}
