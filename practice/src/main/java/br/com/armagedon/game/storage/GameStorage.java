package br.com.armagedon.game.storage;

import br.com.armagedon.Practice;
import br.com.armagedon.game.Game;
import br.com.armagedon.enums.game.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class GameStorage {

    Reflections reflections = new Reflections("br.com.armagedon.game.list");
    Set<Class<? extends Game>> gamesList = reflections.getSubTypesOf(Game.class);
    Set<Game> games = new HashSet<>();


    public void load() {

        FileConfiguration fileConfiguration = Practice.getInstance().getConfig();
        ConfigurationSection section = fileConfiguration.getConfigurationSection("modes");

        for (String name : section.getKeys(false)) {
            try {

                Class<? extends Game> clazz = getGameClass(name);
                String clazzName = clazz.getSimpleName().toLowerCase();

                int min_rooms = 0;
                String directory = null;

                if (!name.equalsIgnoreCase(clazzName)) {
                    continue;
                }

                min_rooms = section.getInt(clazzName + ".min_rooms");
                directory = section.getString(clazzName + ".directory");


                if (min_rooms == 0 && directory == null) {
                    continue;
                }

                Game game = clazz.getConstructor(Practice.class, Integer.class, String.class).newInstance(Practice.getInstance(), min_rooms, directory);

                game.load();
                games.add(game);

            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public void unload() {
        games.forEach(Game::unload);
        games.clear();
    }

    public Class<? extends Game> getGameClass(String name) {
        return gamesList.stream().filter(gameClass -> gameClass.getSimpleName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Game getGame(GameMode mode) {
        return games.stream().filter(game -> game.getMode().equals(mode)).findFirst().orElse(null);
    }
}
