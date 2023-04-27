package br.com.practice.util.visibility;

import br.com.practice.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class Visibility {

    public static void visible(Player shown, List<User> observers) {
        observers.forEach(observer -> {
            show(observer.getPlayer(), shown);
            show(shown, observer.getPlayer());
        });
    }

    public static void invisible(Player hidden, List<User> observers) {
        observers.forEach(observer -> {
            hide(observer.getPlayer(), hidden);
            show(hidden, observer.getPlayer());
        });
    }

    private static void show(Player observer, Player shown) {
        if (!observer.equals(shown)) {
            observer.showPlayer(shown);
        }
    }

    private static void hide(Player observer, Player hidden) {
        if (!observer.equals(hidden)) {
            observer.hidePlayer(hidden);
        }
    }
}
