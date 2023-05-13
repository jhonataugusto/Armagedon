package br.com.core.crud.redis;

import br.com.core.Core;
import br.com.core.data.AccountData;
import br.com.core.data.ServerData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface da classe {@link ServerData} para atualizar,adicionar,remover dados do Redis.
 * */
public class ServerRedisCRUD {
    private static final JedisPool JEDIS_POOL = Core.JEDIS_POOL;
    private static final String SERVER_CACHE = Core.REDIS_CACHE.SERVER_DATABASE_CACHE;

    public static void save(ServerData data) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hset(SERVER_CACHE, data.getName(), data.toJson());
        }
    }

    public static ServerData findByName(String name) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            String json = jedis.hget(SERVER_CACHE, name);
            if (json != null) {
                return ServerData.fromJson(json);
            }
            return null;
        }
    }

    public static void delete(String name) {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            jedis.hdel(SERVER_CACHE, name);
        }
    }

    public static List<ServerData> getServers() {
        try (Jedis jedis = JEDIS_POOL.getResource()) {
            List<String> jsonList = jedis.hvals(SERVER_CACHE);
            return jsonList.stream().map(ServerData::fromJson).collect(Collectors.toList());
        }
    }
}
