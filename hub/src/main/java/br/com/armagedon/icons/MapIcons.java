package br.com.armagedon.icons;

import br.com.armagedon.enums.map.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MapIcons {
    GRASS(Maps.GRASS.getName(), Maps.GRASS.getDisplayName(), Material.GRASS),
    STONE(Maps.CASTLE.getName(), Maps.CASTLE.getDisplayName(), Material.STONE);
    private String name, displayName;
    private Material material;

    private static final MapIcons[] values;

    static {
        values = values();
    }

    public MapIcons findByName(String name) {
        return Arrays.stream(values).filter(map -> map.getName().equals(name)).findFirst().orElse(null);
    }
}
