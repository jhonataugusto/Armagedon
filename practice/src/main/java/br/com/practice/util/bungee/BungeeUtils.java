package br.com.practice.util.bungee;

import br.com.core.Core;
import br.com.practice.Practice;
import br.com.core.enums.server.Server;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.IOException;

public class BungeeUtils implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(Core.BUNGEECORD_MESSAGING_CHANNEL)) return;

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String messageType = input.readUTF();
    }

    public static void connect(Player player, Server server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Connect");
        output.writeUTF(server.getName());

        player.sendPluginMessage(Practice.getInstance(), Core.BUNGEECORD_MESSAGING_CHANNEL, output.toByteArray());
    }
}
