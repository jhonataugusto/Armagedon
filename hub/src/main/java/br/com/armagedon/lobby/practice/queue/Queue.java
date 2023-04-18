package br.com.armagedon.lobby.practice.queue;

import br.com.armagedon.Hub;
import br.com.armagedon.events.PlayerEnterQueueEvent;
import br.com.armagedon.events.PlayerLeaveQueueEvent;
import br.com.armagedon.events.QueueMatchEvent;
import br.com.armagedon.lobby.practice.queue.properties.QueueProperties;
import br.com.armagedon.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@Getter
@Setter
public class Queue {
    private final Map<User, br.com.armagedon.lobby.practice.queue.properties.QueueProperties> users;
    private final PriorityQueue<User> queue;

    public Queue() {
        users = new HashMap<>();

        Comparator<User> userComparator = (u1, u2) -> {
            QueueProperties properties1 = users.get(u1);
            QueueProperties properties2 = users.get(u2);

            return properties1.compareTo(properties2);
        };

        queue = new PriorityQueue<>(userComparator);
    }

    public void enter(User user, QueueProperties properties) {
        users.put(user, properties);
        queue.offer(user);

        if (users.containsKey(user)) {
            Hub.getInstance().getServer().getPluginManager().callEvent(new PlayerEnterQueueEvent(user, properties));
        }
    }

    public void leave(User user) {
        QueueProperties properties = users.get(user);
        users.remove(user);
        queue.remove(user);

        if (!users.containsKey(user)) {
            Hub.getInstance().getServer().getPluginManager().callEvent(new PlayerLeaveQueueEvent(user, properties));
        }
    }

    public void search() {
        if (queue.size() < 2) {
            return;
        }

        User player1 = queue.poll();
        User player2 = queue.poll();

        while (!player1.equals(player2)) {
            player1 = player2;
            player2 = queue.poll();

            if (player2 == null) {
                return;
            }
        }

        QueueProperties properties = users.get(player1);
        match(player1, player2, properties);
    }

    public void match(User player1, User player2, QueueProperties properties) {
        QueueProperties properties1 = users.get(player1);
        QueueProperties properties2 = users.get(player2);

        if (!properties1.equals(properties2)) {
            throw new IllegalStateException("Jogadores com propriedades diferentes não podem duelar");
        }

        users.remove(player1);
        users.remove(player2);
        queue.remove(player1);
        queue.remove(player2);

        if (!users.containsKey(player1) && !users.containsKey(player2)) {
            Hub.getInstance().getServer().getPluginManager().callEvent(new QueueMatchEvent(player1, player2, properties));
        }
    }
}