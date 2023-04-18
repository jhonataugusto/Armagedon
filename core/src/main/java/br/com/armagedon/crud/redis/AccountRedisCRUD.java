package br.com.armagedon.crud.redis;

import br.com.armagedon.Core;
import br.com.armagedon.database.mongo.collections.CollectionProps;
import br.com.armagedon.data.UserData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

public class UserRedisCRUD {

    private static final JedisPool JEDIS_POOL = Core.JEDIS_POOL;

    public static void save(UserData data) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hset(CollectionProps.USERS.getName(), data.getUuid().toString(), data.toJson());
        }
    }

    public static UserData findByUuid(UUID uuid) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            String json = jedis.hget(CollectionProps.USERS.getName(), uuid.toString());
            if (json != null) {
                return UserData.fromJson(json);
            }
            return null;
        }
    }

    public static void delete(UUID uuid) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hdel(CollectionProps.USERS.getName(), uuid.toString());
        }
    }
}
