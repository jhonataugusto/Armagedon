package br.com.core.database.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum RedisChannels {

    ACCOUNT_DATABASE_CHANNEL("a"),
    SERVER_DATABASE_CHANNEL("b"),
    DUELS_DATABASE_CHANNEL("c"),
    MINECRAFT_RECEIVE_MESSAGES_CHANNEL("c_mrm"),
    MINECRAFT_ANTICHEAT_MESSAGES_CHANNEL("c_ma"),
    DISCORD_RECEIVE_MESSAGES_CHANNEL("c_rdcm");

    private String channel;

    RedisChannels(String channel) {
        this.channel = channel;
    }

    @Getter
    public static final RedisChannels[] values;

    static {
        values = values();
    }
}
