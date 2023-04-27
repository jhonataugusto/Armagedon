package br.com.practice.gui;

import br.com.core.enums.game.GameMode;
import br.com.practice.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class PostMatchGUI implements InventoryHolder {

    private final Inventory inventory;

    public PostMatchGUI(User user) {
        this.inventory = Bukkit.createInventory(user.getPlayer(), 45, user.getPlayer().getName());

        for (int i = 0; i < user.getPlayer().getInventory().getContents().length; i++) {
            inventory.setItem(i, user.getPlayer().getInventory().getContents()[i]);
        }

        ItemStack head = new ItemStack(Material.SKULL_ITEM);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwner(user.getPlayer().getName());
        head.setItemMeta(headMeta);
        inventory.setItem(36, head);

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + ("Hits: " + user.getHits()));
        lore.add(ChatColor.WHITE + ("Hits críticos: " + user.getCriticalHits()));
        lore.add(ChatColor.WHITE + ("Hits bloqueados: " + user.getBlockedHits()));
        lore.add(ChatColor.WHITE + ("Combo máximo: " + user.getMaxCombo()));
        lore.add(ChatColor.WHITE + ("Vida: " + user.getPlayer().getHealth()));
        if (user.getArena().getGame().getMode() == GameMode.NODEBUFF) {
            lore.add(ChatColor.WHITE + ("Poções jogadas: " + user.getThrowedPotions()));
            lore.add(ChatColor.WHITE + ("Precisão ao lançar poções: " + user.getAccuracyPotions()));
        }
        lore.add(ChatColor.WHITE + ("CPS máximo: " + user.getMaxClicksPerSecond()));
        lore.add(ChatColor.WHITE + ("Range máximo: " + user.getMaxRange()));
        meta.setLore(lore);
        paper.setItemMeta(meta);
        inventory.setItem(40, paper);
    }

    public Inventory getInventory() {
        return inventory;
    }
}