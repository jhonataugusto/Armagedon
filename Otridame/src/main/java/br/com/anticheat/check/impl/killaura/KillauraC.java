package br.com.anticheat.check.impl.killaura;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.AttackEvent;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.util.MovementUtils;
import br.com.anticheat.util.UtilPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class KillauraC extends Check {

    private double buffer;
    private int ticksSinceHit;
    private double lastDist;
    
    public KillauraC(final PlayerData playerData) {
        super(Category.COMBAT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {

            if(((FlyingEvent) event).hasMoved()) {

                if(player == null) return;

                Location to = ((FlyingEvent) event).toLocation();
                Location from = playerData.getLastLocation();

                double distX = to.getX() - from.getX();
                double distZ = to.getZ() - from.getZ();
                double distXZ = Math.sqrt((distX * distX) + (distZ * distZ));

                double lastDist = this.lastDist;
                this.lastDist = distXZ;

                if(playerData.getLastTarget() == null) return;

                if (++ticksSinceHit <= 2 && !PacketEvents.getAPI().getPlayerUtils().getClientVersion(player).isHigherThan(ClientVersion.v_1_15_2)) {

                    if(playerData.getVelocityH() > 0 || playerData.getVelocityV() > 0) return;

                    if (playerData.isSprinting() && playerData.getTotalTicks() - playerData.getLastPacketDrop() > 10 && !playerData.isLagging(System.currentTimeMillis(), 100L) && !UtilPlayer.isOnSoulSand(player) && player.getVehicle() == null) {
                        double acceleration = Math.abs(distXZ - lastDist);
                        float speedLevel = MovementUtils.getPotionEffectLevel(player, PotionEffectType.SPEED);
                        acceleration += playerData.getGroundTicks() < 5 ? speedLevel * 0.07f : speedLevel * 0.0573f;
                        if (acceleration <= 0.05 && distXZ > 0.23 && !UtilPlayer.blockNearHead(player)) {
                            if (++buffer > 8) {
                                handleFlag(player, "Killaura C", "§b* §fKeepsprint\n§b* §faccel=§b" + acceleration, getBanVL("KillauraC"), 50000L);
                            }
                        } else {
                            buffer = 0;
                        }
                    } else {
                        buffer = 0;
                    }
                }
            }
        } else if(event instanceof AttackEvent) {
            ticksSinceHit = 0;
        }
    }
}