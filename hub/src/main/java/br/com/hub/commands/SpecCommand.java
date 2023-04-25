package br.com.hub.commands;

import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SpecCommand {

    @Command(name = "spec", aliases = {"spectate","spectar","espectar","assistir"}, target = CommandTarget.PLAYER)
    public void handleCommand(Context<Player> context, Player target, String[] message) {
        context.getSender().sendMessage(target.getDisplayName());
    }

    @Command(name = "command.child")
    public void handleCommandChild(Context<ConsoleCommandSender> context) {
        // ...
    }

}
