package br.com.hub.icons;

import br.com.core.enums.game.GameMode;
import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@Getter
@NoArgsConstructor
public enum PracticeIcons {
    NODEBUFF(GameMode.NODEBUFF.getName(), GameMode.NODEBUFF.getDisplayName(), Material.DIAMOND_SWORD, true),
    SKYWARS(GameMode.SKYWARS.getName(), GameMode.SKYWARS.getDisplayName() , Material.GRASS, true),
    RANKED_MODE("ranked_mode", "Modo Ranqueado Ativado", Material.SLIME_BLOCK, true),
    UNRANKED_MODE("unranked_mode", "Modo Ranqueado Desativado", Material.REDSTONE, true);

    private String name;
    private String displayName;
    private Material material;
    private int column, row;
    private boolean shown;
    private final String key = "id";
    private final String value = getName();

    PracticeIcons(String name, String displayName, Material material, boolean shown) {
        this.name = name;
        this.displayName = displayName;
        this.material = material;
        this.shown = shown;
    }

    @Getter
    private static final PracticeIcons[] values;

    static {
        values = values();
    }

    public static PracticeIcons getIcon(String name) {
        return Arrays.stream(values).filter(icons -> icons.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
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

        NBT.modify(itemStack, nbt -> {
            nbt.setString(getKey(), getValue());
        });
        return itemStack;
    }
}
