package br.com.hub.items;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@NoArgsConstructor
public enum PracticeItems {
    RANKED_UNRANKED("Escolha sua partida", Material.COMPASS, 0, 1, "RANKED_UNRANKED"),
    LEADERBOARD("Leaderboard", Material.EMERALD, 4, 1, "LEADERBOARD"),
    KIT_EDITOR("Edite seus Kits", Material.ANVIL, 7,1,"KIT_EDITOR"),
    CONFIGURATIONS("Configurações", Material.COMMAND, 8, 1, "CONFIGURATIONS");

    private String displayName;
    private Material material;
    private int position;
    private int amount;
    private String value;
    public final String KEY = "id";

    PracticeItems(String name, Material material, int position, int amount, String value) {
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
            nbt.setString(KEY, getValue());
        });
        return itemStack;
    }
}
