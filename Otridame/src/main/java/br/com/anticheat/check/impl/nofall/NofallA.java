package br.com.anticheat.check.impl.nofall;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.util.UtilPlayer;
import br.com.anticheat.util.task.Tasker;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NofallA extends Check {

    private double buffer;
    
    public NofallA(final PlayerData playerData) {
        super(Category.MOVEMENT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {

            if (((FlyingEvent) event).hasMoved()) {
                boolean onGround = UtilPlayer.isOnGroundBB(player);

                boolean lastOnGround = playerData.lastOnGround;
                playerData.lastOnGround = onGround;

                boolean lastLastOnGround = playerData.lastLastOnGround;
                playerData.lastLastOnGround = lastOnGround;

                if (player.getLocation().getWorld().isChunkLoaded(player.getLocation().getBlockX() >> 4, player.getLocation().getBlockZ() >> 4)) {
                    buffer = Math.max(buffer - 0.75, 0);
                    return;
                }

                if(((FlyingEvent) event).isOnGround() && playerData.getTotalTicks() - playerData.getLastPacketDrop() > 10 && !UtilPlayer.isOnBadJesusBlock(player) && !onGround && !lastOnGround && !lastLastOnGround) {
                    if(PacketEvents.getAPI().getServerUtils().getVersion().isHigherThan(ServerVersion.v_1_12_2)) {
                        Tasker.run(() -> {
                            for (Entity ent : player.getNearbyEntities(3.0, 3.0, 3.0)) {
                                if (ent instanceof Boat) {
                                    return;
                                }
                            }
                        });
                    } else {
                        for (Entity ent : player.getNearbyEntities(3.0, 3.0, 3.0)) {
                            if (ent instanceof Boat) {
                                return;
                            }
                        }
                    }
                    if(++buffer > 5) {
                        handleFlag(player, "Nofall A", "§b* §fSpoofed ground state", getBanVL("NofallA"), 60000L);
                    }
                } else {
                    buffer = Math.max(buffer - 0.75, 0);
                }
            }
        }
    }
}
