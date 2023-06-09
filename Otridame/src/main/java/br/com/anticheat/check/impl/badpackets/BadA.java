package br.com.anticheat.check.impl.badpackets;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import org.bukkit.entity.Player;

public class BadA extends Check {

    public BadA(final PlayerData playerData) {
        super(Category.PACKET, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {
            if (((FlyingEvent) event).hasLooked() || ((FlyingEvent) event).hasMoved()) {
                if(Math.abs(((FlyingEvent) event).getPitch()) > 90 && playerData.getTeleportLocation() == null) {
                    handleFlag(player, "BadPackets A", "§b* §fInvalid pitch", getBanVL("BadPacketsA"), 200000L);
                }
            }
        }
    }

}
