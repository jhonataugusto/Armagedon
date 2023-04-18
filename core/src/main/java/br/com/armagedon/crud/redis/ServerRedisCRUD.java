package br.com.armagedon.database.redis;

import br.com.armagedon.Core;
import br.com.armagedon.database.mongo.collections.CollectionProps;
import br.com.armagedon.server.data.ServerData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

public class ServerRedisCRUD {
    private final JedisPool JEDIS_POOL = Core.JEDIS_POOL;
    private final String SERVER_CACHE = Core.REDIS_CACHE.SERVER_DATABASE_CACHE;

    public void save(ServerData data) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hset(SERVER_CACHE, data.getName(), data.toJson());
        }
    }

    public ServerData findByName(String name) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            String json = jedis.hget(SERVER_CACHE, name);
            if (json != null) {
                return ServerData.fromJson(json);
            }
            return null;
        }
    }

    public void delete(String name) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hdel(SERVER_CACHE, name);
        }
    }
}
