package br.com.hub.gui.challenge;

import br.com.core.crud.redis.DuelContextRedisCRUD;
import br.com.core.data.DuelData;
import br.com.core.enums.game.GameMode;
import br.com.hub.Hub;
import br.com.hub.events.QueueMatchEvent;
import br.com.hub.icons.MapIcons;
import br.com.hub.lobby.practice.queue.properties.DuelProperties;
import br.com.hub.user.User;
import br.com.hub.user.request.DuelRequest;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.npc.ai.speech.Chat;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static br.com.hub.util.scheduler.SchedulerUtils.delay;

@Getter
@Setter
public class ChallengeMapSelectorGUI implements InventoryProvider {

    private User target;
    private SmartInventory INVENTORY;
    private GameMode mode;

    public ChallengeMapSelectorGUI(GameMode mode, User target) {
        this.target = target;
        this.mode = mode;
        this.INVENTORY = SmartInventory.builder()
                .id(ID)
                .provider(this)
                .manager(Hub.getInstance().getInventoryManager())
                .size(MAX_ROWS, MAX_COLUMNS)
                .title("Escolha o mapa que deseja duelar.")
                .build();

    }

    private static final String ID = "CHALLENGE_MAP_SELECTOR_GUI";
    private static final int MAX_ROWS = 6;
    private static final int MAX_COLUMNS = 9;

    SlotPos lastPos = new SlotPos(MAX_ROWS - 1, MAX_COLUMNS - 1);

    @Override
    public void init(Player player, InventoryContents contents) {

        int row = 1;
        int column = 1;

        for (MapIcons mapIcon : MapIcons.values()) {


            SlotPos pos = new SlotPos(row, column);

            contents.set(pos, ClickableItem.of(mapIcon.toItemStack(), event -> {

                player.closeInventory();

                if (target == null) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "O jogador não está no servidor.");
                    return;
                }

                if (target.getAccount().getData().getCurrentDuelContextUuid() != null) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "O jogador já está em um duelo.");
                    return;
                }

                GameMode mode = GameMode.getByName(getMode().getName());
                User challenger = User.fetch(player.getUniqueId());
                DuelRequest duelRequest = new DuelRequest(mode, challenger, mapIcon.getName());

                target.getDuelRequests().add(duelRequest);

                delay(() -> target.getDuelRequests().remove(duelRequest), 60 * 2);

                TextComponent challengeMessageComponent = new TextComponent(ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " te desafiou para um duelo " + ChatColor.YELLOW + mode.getName() + ChatColor.GREEN + " no mapa " + ChatColor.YELLOW + mapIcon.getName() + ChatColor.GREEN + ". Aceita o desafio?");
                TextComponent challengeClickComponent = new TextComponent(ChatColor.GREEN + "Clique aqui para aceitar o duelo.");
                String messageExpiresWarning = "§c(você tem 1 minuto para aceitar ou seu convite irá expirar)";

                challengeClickComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptduel " + player.getUniqueId() + " " + duelRequest.getId()));

                target.getPlayer().sendMessage("");
                target.getPlayer().spigot().sendMessage(challengeMessageComponent);
                target.getPlayer().spigot().sendMessage(challengeClickComponent);
                target.getPlayer().sendMessage(messageExpiresWarning);
                target.getPlayer().sendMessage("");

                player.sendMessage(ChatColor.GREEN + "✉ §aVocê enviou uma solicitação de duelo para §e" + target.getName() + "§a no modo §e" + mode.getName() + "§a com o mapa " + mapIcon.getName());

                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 3.5f, 3.5f);
                target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.NOTE_STICKS, 3.5f, 3.5f);
            }));

            mapIcon.setRow(row);
            mapIcon.setColumn(column);

            column++;

            if (column >= MAX_COLUMNS) {
                column = 1;
                row++;
            }

            if (row >= MAX_ROWS) {
                break;
            }
        }

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
    }
}
