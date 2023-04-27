package br.com.practice.util.cps;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;

@Getter
public class CPSManager {
    public static final HashMap<Player, Long> lastClickTime = new HashMap<>();

}
