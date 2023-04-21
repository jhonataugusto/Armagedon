package br.com.armagedon.crud.redis;

import br.com.armagedon.Core;
import br.com.armagedon.data.DuelContextData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DuelContextRedisCRUD {

    private static final JedisPool JEDIS_POOL = Core.JEDIS_POOL;
    private static final String DUELS_CACHE = Core.REDIS_CACHE.DUELS_DATABASE_CACHE;

    public static void save(DuelContextData data) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hset(DUELS_CACHE, data.getUuid().toString(), data.toJson());
        }
    }

    public static DuelContextData findByUuid(UUID uuid) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            String json = jedis.hget(DUELS_CACHE, uuid.toString());
            if (json != null) {
                return DuelContextData.fromJson(json);
            }
            return null;
        }
    }

    public static void delete(UUID uuid) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hdel(DUELS_CACHE, uuid.toString());
        }
    }

    public static List<DuelContextData> getDuels() {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            List<String> jsonList = jedis.hvals(DUELS_CACHE);
            return jsonList.stream().map(DuelContextData::fromJson).collect(Collectors.toList());
        }
    }
}
