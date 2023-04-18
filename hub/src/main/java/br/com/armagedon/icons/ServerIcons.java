package br.com.armagedon.items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@NoArgsConstructor
public enum ServerItem {
    LOBBY("lobby", Material.DIAMOND_SWORD),
    TEST("test", Material.ACACIA_FENCE);

    private String name;
    private Material onlineMaterial;
    private final Material offlineMaterial = Material.REDSTONE_BLOCK;

    private int column, row;

    ServerItem(String name, Material onlineMaterial) {
        this.name = name;
        this.onlineMaterial = onlineMaterial;
    }

    @Getter
    private static final ServerItem[] values;

    static {
        values = values();
    }

    public static ServerItem getServerItem(String name) {
        return Arrays.stream(values).filter(serverItem -> serverItem.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
