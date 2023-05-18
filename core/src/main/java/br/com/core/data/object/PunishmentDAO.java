package br.com.core.data.object;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * uma classe para serialização e deserialização de dados para universalizar a comunicação de Objetos no Bukkit, Mongo e Redis.
 * resumidamente: facilitar a transferencia via JSON e deixar o código mais organizado
 */
@Getter
@Setter
public class PunishmentDAO {
    private String id = "P#" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    private String whoBanned;
    private String bannedUser;
    private String ip;
    private String date;
    private String reason;
    private String expiration;
    private boolean active;

    public PunishmentDAO(String whoBanned, String bannedUser, String ip, String date, String reason, String expiration, boolean active) {
        this.whoBanned = whoBanned;
        this.bannedUser = bannedUser;
        this.ip = ip;
        this.date = date;
        this.reason = reason;
        this.expiration = expiration;
        this.active = active;
    }
}
