package br.com.practice.arena.rollback.block.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

@Data
@AllArgsConstructor
public class BlockAction {
    private Block block;
    private Location location;
    private BlockState state;
    private ActionType type;


    public enum ActionType {
        PLACE, BREAK;
    }

}
