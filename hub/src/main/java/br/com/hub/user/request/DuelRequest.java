package br.com.hub.user.request;

import br.com.core.enums.game.GameMode;
import br.com.hub.user.User;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class DuelRequest {
    private final String id = "request#" + UUID.randomUUID().toString().substring(0, 5).replaceAll("-", "").toLowerCase();
    private final long expiration = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
    private final GameMode mode;
    private final String mapName;
    private final User challenger;

    public DuelRequest(GameMode mode, User challenger, String mapName) {
        this.mode = mode;
        this.challenger = challenger;
        this.mapName = mapName;
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() >= expiration;
    }
}
