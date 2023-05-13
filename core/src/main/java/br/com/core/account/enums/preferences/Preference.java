package br.com.core.account.enums.preferences;

import br.com.core.account.enums.preferences.type.PreferenceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum Preference {
    DISABLE_CHAT("disable_chat", "Desativar chat", false),
    DISABLE_PRIVATE_MESSAGES("disable_private_messages", "Desativar mensagens privadas", false),
    CUSTOM_DEATH("custom_death", "Mudar estilo de morte", null),
    DISABLE_SPECS("disable_specs", "Desabilitar espectadores", false);

    static {
        CUSTOM_DEATH.setType(PreferenceType.CUSTOM_DEATH_DEFAULT.name());
    }

    Preference(String name, String displayName, String type) {
        this.name = name;
        this.displayName = displayName;
        this.type = type;
    }

    Preference(String name, String displayName, boolean active) {
        this.name = name;
        this.displayName = displayName;
        this.active = active;
    }

    private final String name;
    private final String displayName;
    private String type;
    private boolean active;

    public void setType(String type) {
        this.type = type;
    }
}
