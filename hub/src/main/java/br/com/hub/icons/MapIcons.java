package br.com.hub.icons;

import br.com.core.enums.map.Maps;
import de.tr7zw.changeme.nbtapi.NBT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@Getter
public enum MapIcons {
    GRASS(Maps.GRASS.getName(), Maps.GRASS.getDisplayName(), Material.GRASS),
    STONE(Maps.STONE.getName(), Maps.STONE.getDisplayName(), Material.STONE),
    MUSHROOM(Maps.MUSHROOM.getName(), Maps.MUSHROOM.getDisplayName(), Material.MUSHROOM_SOUP),
    NETHERBRICK(Maps.NETHERBRICK.getName(), Maps.NETHERBRICK.getDisplayName(), Material.NETHER_BRICK);

    private final String name, displayName;
    private int column, row;
    private final Material material;
    private static final MapIcons[] values;

    static {
        values = values();
    }

    MapIcons(String name, String displayName, Material material) {
        this.name = name;
        this.displayName = displayName;
        this.material = material;
    }

    public MapIcons findByName(String name) {
        return Arrays.stream(values).filter(map -> map.getName().equals(name)).findFirst().orElse(null);
    }


    public void setColumn(int column) {
        this.column = column;
    }


    public void setRow(int row) {
        this.row = row;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(getMaterial());
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(getDisplayName());

        itemStack.setItemMeta(meta);
        itemStack.setAmount(1);
        itemStack.setDurability((short) 0);
        return itemStack;
    }
}
