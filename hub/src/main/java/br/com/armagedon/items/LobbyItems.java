package br.com.armagedon.items;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@NoArgsConstructor
public enum LobbyItem {
    COMPASS("Clique para selecionar o Servidor", Material.COMPASS, 0, 1, "MAIN_COMPASS");

    private String displayName;
    private Material material;
    private int position;
    private int amount;
    private String id;
    public final String KEY = "id";

    LobbyItem(String name, Material material, int position, int amount, String id) {
        this.displayName = name;
        this.material = material;
        this.position = position;
        this.amount = amount;
        this.id = id;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(getMaterial());
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(getDisplayName());

        itemStack.setItemMeta(meta);
        itemStack.setAmount(getAmount());
        itemStack.setDurability((short) 0);

        NBT.modify(itemStack, nbt -> {
            nbt.setString(KEY, getId());
        });
        return itemStack;
    }
}
