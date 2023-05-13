package br.com.practice.game.list;

import br.com.practice.Practice;
import br.com.practice.game.Game;
import br.com.practice.user.User;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SkyWars extends Game {
    public SkyWars(Practice plugin, Integer min_rooms, String mapDirectory) {
        super(plugin, min_rooms, mapDirectory);
        setAllowedBuild(true);
    }


    @Override
    public void handleInventory(User user) {
        super.handleInventory(user);

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

        user.getPlayer().getInventory().setHelmet(helmet);
        user.getPlayer().getInventory().setChestplate(chestplate);
        user.getPlayer().getInventory().setLeggings(leggings);
        user.getPlayer().getInventory().setBoots(boots);
    }
}
