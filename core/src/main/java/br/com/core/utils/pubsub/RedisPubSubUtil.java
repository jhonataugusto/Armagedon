package br.com.core.utils.pubsub;

import br.com.core.Core;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisPubSubUtil {
    public static void subscribe(RedisPubSubExecutor redisPubSubExecutor, String... channels) {

        try (Jedis jedis = Core.JEDIS_POOL.getResource()) {
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    super.onMessage(channel, message);
                    if (redisPubSubExecutor != null) redisPubSubExecutor.execute(channel, message);
                }
            }, channels);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void publish(String channel, String message) {

        try (Jedis jedis = Core.JEDIS_POOL.getResource()) {

            jedis.publish(channel, message);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public interface RedisPubSubExecutor {
        void execute(String channel, String message);
    }
}
