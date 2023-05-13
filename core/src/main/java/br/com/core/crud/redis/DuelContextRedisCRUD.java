package br.com.core.crud.redis;

import br.com.core.Core;
import br.com.core.data.DuelData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Interface da classe {@link DuelData} para atualizar,adicionar,remover dados do Redis.
 */
public class DuelContextRedisCRUD {

    private static final JedisPool JEDIS_POOL = Core.JEDIS_POOL;
    private static final String DUELS_CACHE = Core.REDIS_CACHE.DUELS_DATABASE_CACHE;

    public static void save(DuelData data) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hset(DUELS_CACHE, data.getUuid().toString(), data.toJson());
        }
    }

    public static DuelData findByUuid(UUID uuid) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            String json = jedis.hget(DUELS_CACHE, uuid.toString());
            if (json != null) {
                return DuelData.fromJson(json);
            }
            return null;
        }
    }

    public static void delete(UUID uuid) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hdel(DUELS_CACHE, uuid.toString());
        }
    }

    public static List<DuelData> getDuels() {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            List<String> jsonList = jedis.hvals(DUELS_CACHE);
            return jsonList.stream().map(DuelData::fromJson).collect(Collectors.toList());
        }
    }

    public static void refreshDuels() {
        DuelContextRedisCRUD.getDuels().forEach(duel -> {
            DuelContextRedisCRUD.delete(duel.getUuid());
        });
    }
}
