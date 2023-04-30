package br.com.core.account.enums.punishments;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PunishmentType {
    BAN("ban"),
    MUTE("mute"),
    BLACKLIST("blacklist");


    private String name;
}
