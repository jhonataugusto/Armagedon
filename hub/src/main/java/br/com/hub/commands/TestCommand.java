package br.com.hub.commands;

import br.com.core.holder.command.ACommand;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class TestCommand implements ACommand {

    @Command(name = "command", aliases = {"nice"}, target = CommandTarget.CONSOLE)
    public void handleCommand(Context<ConsoleCommandSender> context, Player target, @Optional(def = {"Hello,", "World!"}) String[] message) {

        target.sendMessage(String.format("Console sent you: %s", String.join(" ", message)));
    }

    @Command(name = "command.child")
    public void handleCommandChild(Context<ConsoleCommandSender> context) {
        // ...
    }
}
