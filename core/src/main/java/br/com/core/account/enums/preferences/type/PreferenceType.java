package br.com.core.account.enums.preferences.type;

import br.com.core.account.enums.preferences.Preference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public enum PreferenceType {
    CUSTOM_DEATH_DEFAULT("Padrao", Preference.CUSTOM_DEATH),
    CUSTOM_DEATH_BLOOD("Sangue", Preference.CUSTOM_DEATH),
    CUSTOM_DEATH_EXPLOSION("Explosao", Preference.CUSTOM_DEATH),
    CUSTOM_DEATH_MEOW("Meow", Preference.CUSTOM_DEATH);

    private String displayName;
    private Preference preference;

    @Getter
    public static final PreferenceType[] values;

    static {
        values = values();
    }

    PreferenceType(String displayName, Preference preference) {
        this.displayName = displayName;
        this.preference = preference;
    }

    public static Set<PreferenceType> getTypesFromPreference(Preference preference) {

        Set<PreferenceType> set = new HashSet<>();

        for (PreferenceType type : getValues()) {
            if (type == null) continue;
            if (type.getPreference().equals(preference)) {
                set.add(type);
            }
        }
        return set;
    }

    public static PreferenceType getByName(String name) {
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}