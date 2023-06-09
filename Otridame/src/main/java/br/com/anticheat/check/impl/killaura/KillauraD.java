package br.com.anticheat.check.impl.killaura;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.AttackEvent;
import br.com.anticheat.event.DigEvent;
import br.com.anticheat.event.FlyingEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KillauraD extends Check {

    private boolean sentDig;
    private int buffer;

    public KillauraD(final PlayerData playerData) {
        super(Category.COMBAT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof AttackEvent) {
            if (sentDig) {
                if (player.getItemInHand().getType() != Material.BOW || player.getItemInHand().getType() != Material.FISHING_ROD) {
                    if (++buffer > 6) {
                        handleFlag(player, "Killaura D", "§b* §fDigging while attacking", getBanVL("KillauraD"), 40000L);
                    }
                } else {
                    buffer = Math.max(buffer - 1, 0);
                }
            } else {
                buffer = Math.max(buffer - 1, 0);
            }
        } else if (event instanceof FlyingEvent) {
            sentDig = false;
        } else if (event instanceof DigEvent) {
            sentDig = true;
        }
    }
}
