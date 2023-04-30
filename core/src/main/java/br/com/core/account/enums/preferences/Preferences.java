package br.com.core.account.enums.preferences;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Preferences {

    CLOSE_SCOREBOARD("close_scoreboard", "Desativar Scoreboard");

    private final String name;
    private final String displayName;
}
