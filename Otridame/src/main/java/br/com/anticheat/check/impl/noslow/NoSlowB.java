package br.com.anticheat.check.impl.noslow;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.DigEvent;
import org.bukkit.entity.Player;

public class NoSlowB extends Check {

    private int buffer;

    public NoSlowB(final PlayerData playerData) {
        super(Category.MOVEMENT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof DigEvent) {
            if(((DigEvent) event).getDigType().equals(WrappedPacketInBlockDig.PlayerDigType.RELEASE_USE_ITEM) && playerData.isPlacing() && playerData.getDeltaXZ() > 0.125) {
                if(++buffer > 3) {
                    handleFlag(player, "NoSlow B", "§b* §fdeltaXZ=§b" + playerData.deltaXZ + "\n§b* §fSending bad block packets", getBanVL("NoSlowB"), 40000L);
                }
            } else {
                buffer = 0;
            }
        }
    }
}
