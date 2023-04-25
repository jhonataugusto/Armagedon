package br.com.practice.commands;

import br.com.core.holder.command.ACommand;
import br.com.practice.util.serializer.SerializerUtils;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import static br.com.core.utils.hastebin.HasteBinUtils.post;
import static br.com.practice.util.scheduler.SchedulerUtils.async;

public class ExportInventoryCommand implements ACommand {

    @Command(name = "exportinventory", target = CommandTarget.PLAYER)
    public void handleCommand(Context<Player> context) {
        async(() -> {
            try {
                String url = post(SerializerUtils.serializeInventory(context.getSender().getInventory()), true);

                TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText("§aInventário exportado com sucesso."));

                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

                context.getSender().sendMessage(textComponent);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
