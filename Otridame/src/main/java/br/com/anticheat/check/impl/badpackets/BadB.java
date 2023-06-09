package br.com.anticheat.check.impl.badpackets;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.HeldItemSlotEvent;
import org.bukkit.entity.Player;

public class BadB extends Check {

    public BadB(final PlayerData playerData) {
        super(Category.PACKET, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof HeldItemSlotEvent) {
            if(playerData.isPlacing()) {
                handleFlag(player, "BadPackets B", "§b* §fPlacing while changing slot", getBanVL("BadPacketsB"), 200000L);
            }
        }
    }
}
