package br.com.hub.lobby.practice.queue;

import br.com.hub.Hub;
import br.com.hub.events.PlayerEnterQueueEvent;
import br.com.hub.events.PlayerLeaveQueueEvent;
import br.com.hub.events.QueueMatchEvent;
import br.com.hub.lobby.practice.queue.properties.DuelProperties;
import br.com.hub.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.*;

@Getter
@Setter
public class Queue {
    private final Map<User, DuelProperties> users;
    private final PriorityQueue<User> queue;

    private static final int ELO_NON_MATCH_CHANGE = 25;

    public Queue() {
        users = new HashMap<>();

        Comparator<User> userComparator = (u1, u2) -> {
            DuelProperties properties1 = users.get(u1);
            DuelProperties properties2 = users.get(u2);

            return properties1.compareTo(properties2);
        };

        queue = new PriorityQueue<>(userComparator);
    }

    public void enter(User user, DuelProperties properties) {
        users.put(user, properties);
        queue.offer(user);

        if (users.containsKey(user)) {
            Hub.getInstance().getServer().getPluginManager().callEvent(new PlayerEnterQueueEvent(user, properties));
        }
    }

    public void leave(User user) {
        DuelProperties properties = users.get(user);
        users.remove(user);
        queue.remove(user);

        if (!users.containsKey(user)) {
            Hub.getInstance().getServer().getPluginManager().callEvent(new PlayerLeaveQueueEvent(user, properties));
        }
    }

    public void search() {

        for (User player : queue) {
            DuelProperties properties = users.get(player);

            if (properties.isRanked()) {
                properties.setMinElo(properties.getMinElo() - ELO_NON_MATCH_CHANGE);
                properties.setMaxElo(properties.getMaxElo() + ELO_NON_MATCH_CHANGE);
                player.getPlayer().sendMessage(" ");
                player.getPlayer().sendMessage(ChatColor.YELLOW + "O alcance atual de ELO é de :" + properties.getMinElo() + " a " + properties.getMaxElo() + ".");
                player.getPlayer().sendMessage(" ");
            }
        }

        if (queue.size() < 2) {
            return;
        }

        User player1 = queue.poll();
        User player2 = queue.poll();

        if (player2 == null) {
            queue.add(player1);
            return;
        }

        if (player1.equals(player2)) {
            return;
        }

        DuelProperties properties1 = users.get(player1);
        DuelProperties properties2 = users.get(player2);

        if (properties1.compareTo(properties2) == 0) {
            match(player1, player2, properties1);

        } else {
            queue.add(player1);
            queue.add(player2);
        }
    }

    public void match(User player1, User player2, DuelProperties properties) {
        leave(player1);
        leave(player2);

        if (!users.containsKey(player1) && !users.containsKey(player2)) {
            Hub.getInstance().getServer().getPluginManager().callEvent(new QueueMatchEvent(player1, player2, properties));
        }
    }

    public boolean inQueue(User user) {
        return getUsers().containsKey(user);
    }
}