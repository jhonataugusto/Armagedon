package br.com.anticheat.check.impl.pingspoof;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import org.bukkit.entity.Player;

public class PingSpoofA extends Check {

    private int ticks;

    public PingSpoofA(final PlayerData playerData) {
        super(Category.PACKET, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {
            if(playerData.getPing() > playerData.getTransPing() + 100
                    && Math.abs(playerData.getPing() - playerData.getTransPing()) > 300L
                    && playerData.getTotalTicks() - playerData.getLastPacketDrop() > 10
                    && !playerData.hasFast()
                    && playerData.lastServerPositionTick > 150
                    && !playerData.isLagging(System.currentTimeMillis(), 400L)
                    && playerData.getTotalTicks() > 400) {
                if(++ticks > 1000) {
                    handleFlag(player, "PingSpoof A §4^", "§b* §fKeepAlive=§b" + playerData.getPing() + "\n§b* §fTransaction=§b" + playerData.getTransPing(), getBanVL("PingSpoofA"), 40000L);
                }
            } else ticks = 0;
        }
    }
}
