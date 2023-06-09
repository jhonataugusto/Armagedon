package br.com.core.crud.redis;

import br.com.core.Core;
import br.com.core.data.AccountData;
import br.com.core.database.redis.RedisChannels;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * Interface da classe {@link AccountData} para atualizar,adicionar,remover dados do Redis.
 * */
public class AccountRedisCRUD {

    private static final JedisPool JEDIS_POOL = Core.JEDIS_POOL;
    private static final String ACCOUNT_CACHE = RedisChannels.ACCOUNT_DATABASE_CHANNEL.getChannel();

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
