package br.com.armagedon.icons;

import br.com.armagedon.enums.server.Server;
import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@Getter
@NoArgsConstructor
public enum ServerIcons {
    LOBBY(Server.LOBBY.getName(), null, false),
    LOBBY_PRACTICE(Server.LOBBY_PRACTICE.getName(), Material.DIAMOND_SWORD, true);

    private String name;
    private Material onlineMaterial;
    private static final Material defaultMaterial = Material.WOOL;
    private final Material offlineMaterial = Material.REDSTONE_BLOCK;
    private int column, row;
    private final String key = "id";
    private final String value = getName();

    private boolean shown;

    ServerIcons(String name, Material onlineMaterial, boolean shown) {
        this.name = name;
        this.onlineMaterial = onlineMaterial;
        this.shown = shown;
    }

    @Getter
    private static final ServerIcons[] values;

    static {
        values = values();
    }

    public static ServerIcons getServerIcon(String name) {
        return Arrays.stream(values).filter(serverIcons -> serverIcons.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public ItemStack toItemStack(Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(getName());

        itemStack.setItemMeta(meta);
        itemStack.setAmount(1);
        itemStack.setDurability((short) 0);

        NBT.modify(itemStack, nbt -> {
            nbt.setString(getKey(), getValue());
        });
        return itemStack;
    }
}
