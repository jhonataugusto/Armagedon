package br.com.hub.util.serializer;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SerializerUtils {

    public static String serializeInventory(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(new Base64OutputStream(outputStream));

            dataOutput.writeInt(inventory.getSize());

            for (ItemStack item : inventory.getContents()) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return outputStream.toString("UTF-8");

        } catch (Exception exception) {
            throw new RuntimeException("Não foi possível serializar o inventário", exception);
        }
    }

    public static Inventory deserializeInventory(String base64, InventoryHolder owner) {
        try {

            if(base64 == null) {
                return null;
            }

            Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(base64.getBytes("UTF-8")));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            int size = dataInput.readInt();

            Inventory inventory = Bukkit.getServer().createInventory(owner, size);

            for (int i = 0; i < size; i++) {
                ItemStack item = (ItemStack) dataInput.readObject();
                inventory.setItem(i, item);
            }

            dataInput.close();
            return inventory;

        } catch (Exception exception) {
            throw new RuntimeException("Não foi possivel deserializar o inventário", exception);
        }
    }

}
