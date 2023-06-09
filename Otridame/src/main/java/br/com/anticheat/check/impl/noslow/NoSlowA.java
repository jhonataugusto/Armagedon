package br.com.anticheat.check.impl.noslow;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.BlockPlaceEvent;
import br.com.anticheat.event.DigEvent;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.util.UtilPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import static br.com.anticheat.util.MovementUtils.getPotionEffectLevel;

public class NoSlowA extends Check {

    private double buffer;
    private boolean blocking;

    public NoSlowA(final PlayerData playerData) {
        super(Category.MOVEMENT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {

            if (((FlyingEvent) event).hasMoved()) {

                float maxSpeed = ((FlyingEvent) event).isOnGround() ? 0.21f : 0.32400000005960464f;

                float speedLevel = getPotionEffectLevel(player, PotionEffectType.SPEED);

                maxSpeed += (player.getWalkSpeed() - 0.2) * 0.02;
                maxSpeed += (player.getFlySpeed() - 0.1) * 0.01;
                maxSpeed += playerData.getVelocityHorizontal();
                maxSpeed *= playerData.iceTicks > 0 ? 6.8f : 1.0;
                maxSpeed += playerData.getGroundTicks() < 5 ? speedLevel * 0.07f : speedLevel * 0.0573f;

                if(playerData.iceTicks == 0) {
                    if(UtilPlayer.isNearIce(player)) {
                        maxSpeed *= 6.8f;
                    }
                }

                if (player.isBlocking() && blocking) {
                    if (playerData.deltaXZ > maxSpeed) {
                        if (++buffer > 15) {
                            buffer -= 5;
                            handleFlag(player, "NoSlow A", "§b* §fdeltaXZ=§b" + playerData.deltaXZ + "\n§b* §fmaxSpeed=§b" + maxSpeed, getBanVL("NoSlowA"), 40000L);
                        }
                    } else {
                        buffer = 0;
                    }
                } else {
                    buffer = 0;
                }
            }
        } else if (event instanceof DigEvent) {
            if (((DigEvent) event).getDigType().equals(WrappedPacketInBlockDig.PlayerDigType.RELEASE_USE_ITEM)) {
                blocking = false;
            }
        } else if (event instanceof BlockPlaceEvent) {
            if (((BlockPlaceEvent) event).getItemStack().toString().toUpperCase().contains("SWORD")) {
                blocking = true;
            }
        }
    }
}
