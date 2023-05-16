package br.com.practice.user.death;

import br.com.core.account.enums.preferences.type.PreferenceType;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Random;

@Getter
public enum DeathStyle {

    MEOW(PreferenceType.CUSTOM_DEATH_MEOW, (player) -> {

        if(player == null) {
            return;
        }

        int numParticles = 10;
        double speed = 0.2;
        double maxOffset = 2.0;
        Location playerLocation = player.getLocation();
        Random random = new Random();

        for (int i = 0; i < numParticles; i++) {
            double offsetX = (random.nextDouble() - 0.5) * maxOffset * i * speed;
            double offsetY = (random.nextDouble() - 0.5) * maxOffset * i * speed;
            double offsetZ = (random.nextDouble() - 0.5) * maxOffset * i * speed;

            Location particleLocation = playerLocation.clone().add(offsetX, offsetY, offsetZ);

            player.getWorld().playEffect(particleLocation, Effect.HEART, 0);
        }

        player.getWorld().playSound(playerLocation, Sound.CAT_MEOW, 5f, 5f);
    }),
    BLOOD(PreferenceType.CUSTOM_DEATH_BLOOD, (player) -> {

        if(player == null) {
            return;
        }

        int numParticles = 10;
        double speed = 0.2;
        double maxOffset = 4.0;
        Location playerLocation = player.getLocation();
        Random random = new Random();

        for (int i = 0; i < numParticles; i++) {
            double offsetX = (random.nextDouble() - 0.5) * maxOffset * i * speed;
            double offsetY = (random.nextDouble() - 0.5) * maxOffset * i * speed;
            double offsetZ = (random.nextDouble() - 0.5) * maxOffset * i * speed;

            Location particleLocation = playerLocation.clone().add(offsetX, offsetY, offsetZ);

            player.getWorld().playEffect(particleLocation, Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId());
        }

        player.getWorld().playSound(playerLocation, Sound.SLIME_ATTACK, 5f, 5f);
    }),
    EXPLOSION(PreferenceType.CUSTOM_DEATH_EXPLOSION, (player -> {

        if(player == null) {
            return;
        }

        Location playerLocation = player.getLocation();
        player.getWorld().playEffect(playerLocation, Effect.EXPLOSION_HUGE, 0);
        player.getWorld().playSound(playerLocation, Sound.EXPLODE, 5f, 5f);
    })),
    DEFAULT(PreferenceType.CUSTOM_DEATH_DEFAULT, (player -> {

        if(player == null) {
            return;
        }

        player.getWorld().strikeLightningEffect(player.getLocation());
    }));

    private final PreferenceType deathType;
    private final Executor executor;

    DeathStyle(PreferenceType deathType, Executor executor) {
        this.deathType = deathType;
        this.executor = executor;
    }

    public interface Executor {
        void execute(Player player);
    }

    public static DeathStyle getByPreference(PreferenceType type) {
        return Arrays.stream(values()).filter(deathStyle -> deathStyle.getDeathType().equals(type)).findFirst().orElse(null);
    }
}
