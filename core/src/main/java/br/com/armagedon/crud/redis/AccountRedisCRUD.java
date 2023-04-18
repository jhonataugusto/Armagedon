package br.com.armagedon.crud.redis;

import br.com.armagedon.Core;
import br.com.armagedon.database.mongo.collections.CollectionProps;
import br.com.armagedon.data.AccountData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

public class AccountRedisCRUD {

    private static final JedisPool JEDIS_POOL = Core.JEDIS_POOL;
    private static final String ACCOUNT_CACHE = Core.REDIS_CACHE.ACCOUNT_DATABASE_CACHE;

    public static void save(AccountData data) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hset(ACCOUNT_CACHE, data.getUuid().toString(), data.toJson());
        }
    }

    public static AccountData findByUuid(UUID uuid) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            String json = jedis.hget(ACCOUNT_CACHE, uuid.toString());
            if (json != null) {
                return AccountData.fromJson(json);
            }
            return null;
        }
    }

    public static void delete(UUID uuid) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hdel(ACCOUNT_CACHE, uuid.toString());
        }
    }
}
