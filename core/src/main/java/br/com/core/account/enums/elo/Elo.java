package br.com.core.account.enums.elo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Elo {
    NODEBUFF("elo_nodebuff"),
    SKYWARS("elo_skywars");

    private String key;
}
