package br.com.armagedon.util.bungee;

import br.com.armagedon.Core;
import br.com.armagedon.Hub;
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

    public static void connect(Player player, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Connect");
        output.writeUTF(server);

        player.sendPluginMessage(Hub.getInstance(),Core.BUNGEECORD_MESSAGING_CHANNEL, output.toByteArray());
    }
}
