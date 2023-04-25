package br.com.hub.util.bungee;

import br.com.core.Core;
import br.com.hub.Hub;
import br.com.core.enums.server.Server;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeUtils implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals(Core.BUNGEECORD_MESSAGING_CHANNEL)) return;

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String subchannel = input.readUTF();
    }

    public static void connect(Player player, Server server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Connect");
        output.writeUTF(server.getName());

        player.sendPluginMessage(Hub.getInstance(),Core.BUNGEECORD_MESSAGING_CHANNEL, output.toByteArray());
    }
}
