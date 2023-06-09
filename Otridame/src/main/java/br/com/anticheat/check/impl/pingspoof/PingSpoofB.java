package br.com.anticheat.check.impl.pingspoof;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import org.bukkit.entity.Player;

public class PingSpoofB extends Check {

    private int ticks;

    public PingSpoofB(final PlayerData playerData) {
        super(Category.PACKET, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {
            if(playerData.getTransPing() > playerData.getPing() + 100
                    && Math.abs(playerData.getTransPing() - playerData.getPing()) > 250L
                    && playerData.getTotalTicks() - playerData.getLastPacketDrop() > 10
                    && !playerData.hasFast()
                    && playerData.lastServerPositionTick > 150
                    && !playerData.isLagging(System.currentTimeMillis(), 400L)
                    && playerData.getTotalTicks() > 400) {
                if(++ticks > 1000) {
                    handleFlag(player, "PingSpoof B §4^", "§b* §fTransaction=§b" + playerData.getTransPing() + "\n§b* §fKeepAlive=§b" + playerData.getPing(), getBanVL("PingSpoofB"), 40000L);
                }
            } else ticks = 0;
        }
    }
}
