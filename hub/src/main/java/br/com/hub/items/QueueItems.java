package br.com.hub.items;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@NoArgsConstructor
public enum QueueItems {
    QUIT_QUEUE("Sair da queue", Material.REDSTONE, 0, 1, "QUIT_QUEUE");

    private String displayName;
    private Material material;
    private int position;
    private int amount;
    private String value;
    public final String key = "id";

    QueueItems(String name, Material material, int position, int amount, String value) {
        this.displayName = name;
        this.material = material;
        this.position = position;
        this.amount = amount;
        this.value = value;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(getMaterial());
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(getDisplayName());

        itemStack.setItemMeta(meta);
        itemStack.setAmount(getAmount());
        itemStack.setDurability((short) 0);

        NBT.modify(itemStack, nbt -> {
            nbt.setString(key, getValue());
        });
        return itemStack;
    }
}
