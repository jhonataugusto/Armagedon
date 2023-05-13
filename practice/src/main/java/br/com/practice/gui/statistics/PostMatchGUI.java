package br.com.practice.gui.statistics;

import br.com.core.Core;
import br.com.core.enums.game.GameMode;
import br.com.practice.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

public class PostMatchGUI implements InventoryHolder {

    private final Inventory inventory;

    public PostMatchGUI(User user) {
        this.inventory = Bukkit.createInventory(user.getPlayer(), 45, user.getPlayer().getName());

        ItemStack potion = new ItemStack(Material.POTION, 1, (short) 16421);

        int potionAmount = 0;

        for (int i = 0; i < user.getPlayer().getInventory().getContents().length; i++) {
            ItemStack item = user.getPlayer().getInventory().getContents()[i];
            inventory.setItem(i, item);

            if (user.getArena().getGame().getMode() == GameMode.NODEBUFF && item != null && item.getType() == potion.getType() && item.getDurability() == potion.getDurability()) {
                potionAmount += item.getAmount();
            }
        }

        potion.setAmount(potionAmount);

        Iterator<ItemStack> iterator = Arrays.stream(user.getPlayer().getInventory().getArmorContents()).iterator();

        int i = 36;

        while (iterator.hasNext()) {
            inventory.setItem(i, iterator.next());
            i++;
        }


        if (user.getArena().getGame().getMode() == GameMode.NODEBUFF) {
            ItemMeta potionMeta = potion.getItemMeta();

            potionMeta.setDisplayName("Estatísticas com as poções");

            List<String> potionLore = new ArrayList<>();
            user.setAverageAccuracyPotions(user.getSumAccuracyPotions() / user.getSuccessfulPotions());

            potionLore.add(ChatColor.WHITE + ("Poções totais: " + potionAmount));
            potionLore.add(ChatColor.WHITE + ("Poções jogadas: " + user.getThrowedPotions()));
            potionLore.add(ChatColor.WHITE + ("Poções erradas ao jogar: " + user.getMissedPotions()));
            potionLore.add(ChatColor.WHITE + ("Poções roubadas: " + user.getStealedPotions()));

            DecimalFormat df = new DecimalFormat("0.0");
            String potionAccuracyAverage = df.format(user.getAverageAccuracyPotions() * 100).replace(".", ",") + "%";

            potionLore.add(ChatColor.WHITE + ("Precisão média em poções: " + potionAccuracyAverage));

            potionMeta.setLore(potionLore);
            potion.setItemMeta(potionMeta);

            inventory.setItem(42, potion);
        }

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();

        meta.setDisplayName("Estatísticas gerais");

        List<String> lorePaper = new ArrayList<>();

        user.setAverageRange(user.getSumOfRanges() / user.getHits());

        lorePaper.add(ChatColor.WHITE + ("Hits: " + user.getHits()));
        lorePaper.add(ChatColor.WHITE + ("Hits críticos: " + user.getCriticalHits()));
        lorePaper.add(ChatColor.WHITE + ("Hits bloqueados: " + user.getBlockedHits()));
        lorePaper.add(ChatColor.WHITE + ("Combo máximo: " + user.getMaxCombo()));
        lorePaper.add(ChatColor.WHITE + "Vida: " + String.format("%,d", Math.round(user.getPlayer().getHealth())));

        lorePaper.add(ChatColor.WHITE + ("CPS máximo: " + user.getMaxClicksPerSecond()));

        String rangeAverage = (user.getAverageRange() > 0 ? Core.DECIMAL_FORMAT.format(user.getAverageRange()) : "0,0");
        lorePaper.add(ChatColor.WHITE + "Range médio: " + rangeAverage);

        lorePaper.add("");
        lorePaper.add(ChatColor.WHITE + "Data e hora: " + Core.DATE_FORMAT.format(new Date(System.currentTimeMillis())));

        meta.setLore(lorePaper);
        paper.setItemMeta(meta);
        inventory.setItem(43, paper);
    }

    public Inventory getInventory() {
        return inventory;
    }
}