package br.com.practice.listener;

import br.com.core.account.enums.preferences.Preference;
import br.com.core.account.enums.rank.Rank;
import br.com.core.data.object.PreferenceDAO;
import br.com.practice.user.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();

        BaseComponent[] baseComponents = buildChatMessage(player, event.getMessage());

        for (Player recipient : Bukkit.getOnlinePlayers()) {
            User user = User.fetch(recipient.getUniqueId());

            if (user == null) {
                continue;
            }

            PreferenceDAO preference = user.getAccount().getData().getPreferenceByName(Preference.DISABLE_CHAT);

            if (preference.isActive()) {
                return;
            }

            recipient.spigot().sendMessage(baseComponents);
        }
    }

    private BaseComponent[] buildChatMessage(Player player, String message) {

        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            return null;
        }

        Rank rank = Rank.getById(user.getAccount().getRank().getId());

        TextComponent nameComponent;

        if (rank.equals(Rank.MEMBER)) {
            nameComponent = new TextComponent(rank.getColor() + rank.getChatDisplay() + user.getName());
        } else {
            nameComponent = new TextComponent(rank.getColor() + rank.getChatDisplay() + " " + user.getName());
        }

        TextComponent colon = new TextComponent(ChatColor.WHITE + ": ");
        TextComponent messageComponent = new TextComponent(ChatColor.WHITE + message);

        return new BaseComponent[]{nameComponent, colon, messageComponent};
    }
}
