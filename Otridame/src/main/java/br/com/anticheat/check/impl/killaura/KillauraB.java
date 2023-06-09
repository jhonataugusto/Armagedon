package br.com.anticheat.check.impl.killaura;

import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.AttackEvent;
import br.com.anticheat.event.BlockPlaceEvent;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.event.InteractEvent;
import org.bukkit.entity.Player;

public class KillauraB extends Check {

    private boolean sentInteract;
    private boolean sentAttack;

    public KillauraB(final PlayerData playerData) {
        super(Category.COMBAT, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent) {
            sentInteract = false;
            sentAttack = false;
        } else if(event instanceof AttackEvent) {
            sentAttack = true;
        } else if(event instanceof InteractEvent) {
            sentInteract = true;
        } else if(event instanceof BlockPlaceEvent) {
            if (sentAttack &! sentInteract) {
                handleFlag(player, "Killaura B", "§b* §fAutoblock", getBanVL("KillauraB"), 60000L);
            }
        }
    }
}
