package br.com.core.data.object;

import lombok.Data;

import java.util.UUID;

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
