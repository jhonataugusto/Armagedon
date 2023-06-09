package br.com.anticheat.check.impl.inventory;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.WindowEvent;
import org.bukkit.entity.Player;

public class InventoryA extends Check {

    private int buffer;

    public InventoryA(final PlayerData playerData) {
        super(Category.PACKET, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof WindowEvent) {
            if (
                    playerData.getDeltaXZ() > 0.12
                    && playerData.liquidTicks == 0
                    && playerData.getVelocityH() <= 0
                    && playerData.getIceTicks() <= 0
                    && playerData.getGroundTicks() > 2
            ) {
                if (++buffer > 6) {
                    handleFlag(player, "Inventory A", "§b* §fMoving while clicking" + "\n§b* §fdeltaXZ=§b" + playerData.getDeltaXZ(), getBanVL("InventoryA"), 60000L);
                }
            } else {
                buffer = Math.max(buffer - 2, 0);
            }
        }
    }
}
