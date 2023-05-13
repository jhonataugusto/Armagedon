package br.com.bungee.commands;

import br.com.bungee.Bungee;
import br.com.core.crud.redis.ServerRedisCRUD;
import br.com.core.data.ServerData;
import br.com.core.enums.server.Server;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Single;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.io.IOException;


@CommandAlias("close|fechar")
@Description("desliga qualquer servidor")
public class CloseCommand extends BaseCommand {

    @Default
    public void onClose(ProxiedPlayer player, @Single String serverName) {

        if (Server.getByName(serverName) == null && !serverName.equalsIgnoreCase("bungee")) {
            player.sendMessage(ChatColor.RED + "Esse servidor não existe.");
            return;
        }

        if (!serverName.equalsIgnoreCase("bungee")) {
            ServerData server = ServerRedisCRUD.findByName(serverName);

            if (!server.isOnline()) {
                player.sendMessage("§cO server já está desligado!");
                return;
            }

            File serverPath = new File(System.getProperty("user.home") + File.separator + "armagedon", serverName);

            try {
                Process newProcess = new ProcessBuilder().command("screen", "-X", "-S", serverName, "quit").directory(serverPath).start();
                newProcess.waitFor();

                player.sendMessage("§cVocê desligou o servidor " + serverName + " com sucesso.");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return;
        }

        Bungee.getInstance().getProxy().stop();
    }

}
