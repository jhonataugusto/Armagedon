package br.com.core.data.object;

import lombok.Data;

import java.util.UUID;

/**
 * uma classe para serialização e deserialização de dados para universalizar a comunicação de Objetos no Bukkit, Mongo e Redis.
 * resumidamente: facilitar a transferencia via JSON e deixar o código mais organizado
 * */
@Data
public class PunishmentDAO {
    private String id = "P#" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    private String whoBanned;
    private String bannedUser;
    private String ip;
    private String date;
    private String type;
    private String reason;
    private String expiration;
    private boolean active;
}
