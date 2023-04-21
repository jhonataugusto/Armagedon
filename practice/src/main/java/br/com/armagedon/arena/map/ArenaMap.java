package br.com.armagedon.arena.map;

import br.com.armagedon.util.cuboid.Cuboid;
import br.com.armagedon.util.file.CompressionUtil;
import lombok.Data;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Data
public class ArenaMap {
    private final String id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    private String name;
    private String directory;
    private File file;
    private Cuboid area;

    public ArenaMap(String name, String directory) {
        if (directory == null) {
            throw new IllegalArgumentException("Diretório " + directory + " não encontrado.");
        }

        this.file = new File(directory + name);
        this.area = Cuboid.loadProperties(file);
    }

    public boolean copyTo(File destination) {
        try {
            CompressionUtil.copy(getFile(), destination, null);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
