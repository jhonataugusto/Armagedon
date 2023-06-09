package br.com.anticheat.check.impl.badpackets;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.AbilityEvent;
import org.bukkit.entity.Player;

public class BadC extends Check {

    public BadC(final PlayerData playerData) {
        super(Category.PACKET, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof AbilityEvent) {
            if(!player.getAllowFlight()) {
                handleFlag(player, "BadPackets C", "§b* §fSent ability packet without flying", getBanVL("BadPacketsC"), 6000L);
            }
        }
    }
}

