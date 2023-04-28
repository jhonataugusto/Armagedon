package br.com.core.utils.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

    private static final Map<UUID, Map<CooldownType, Long>> cooldownMap = new HashMap<>();

    public static void addCooldown(UUID uuid, CooldownType cooldownType) {
        long cooldownEndTime = System.currentTimeMillis() + cooldownType.getCooldown();
        Map<CooldownType, Long> cooldownTypeMap = cooldownMap.getOrDefault(uuid, new HashMap<>());
        cooldownTypeMap.put(cooldownType, cooldownEndTime);
        cooldownMap.put(uuid, cooldownTypeMap);
    }

    public static boolean removeCooldown(UUID uuid, CooldownType cooldownType) {
        Map<CooldownType, Long> cooldownTypeLong = cooldownMap.get(uuid);

        if (cooldownTypeLong == null) {
            return false;
        }

        Long cooldownTime = cooldownTypeLong.get(cooldownType);

        if (cooldownTime == null || cooldownTime > System.currentTimeMillis()) {
            return false;
        }

        cooldownTypeLong.remove(cooldownType);

        if (cooldownTypeLong.isEmpty()) {
            cooldownMap.remove(uuid);
        }

        return true;
    }

    public static Long getCooldown(UUID uuid, CooldownType cooldownType) {
        if (removeCooldown(uuid, cooldownType)) {
            return null;
        }
        Map<CooldownType, Long> cooldownTypeMap = cooldownMap.get(uuid);
        if (cooldownTypeMap == null) {
            return null;
        }
        Long cooldownEndTime = cooldownTypeMap.get(cooldownType);
        if (cooldownEndTime == null) {
            return null;
        }
        return Math.max(cooldownEndTime - System.currentTimeMillis(), 0);
    }

    public enum CooldownType {
        ENDERPEARL(TimeUnit.SECONDS.toMillis(16)),
        COMMAND(TimeUnit.SECONDS.toMillis(3));

        private long cooldown;

        CooldownType(long cooldown) {
            this.cooldown = cooldown;
        }

        public long getCooldown() {
            return cooldown;
        }
    }
}