package br.com.armagedon.arena.map;

import br.com.armagedon.Practice;
import br.com.armagedon.util.cuboid.Cuboid;
import lombok.Data;

import java.io.File;
import java.util.UUID;

@Data
public class ArenaMap {
    private String id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    private String name;
    private File directory;
    private File presetFile;
    private Cuboid area;

    public ArenaMap(String name, String presetFilePath, File arenaDirectory) {

        while (Practice.getInstance().getArenaStorage().getArenas().containsKey(getId())) {
            setId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6));
        }

        setName(name);
        setDirectory(new File(arenaDirectory, getId()));

        if (!getDirectory().exists()) {
            getDirectory().mkdir();
        }

        File presetFile = new File(presetFilePath, name);

        if (!presetFile.exists()) {
            presetFile.mkdirs();
        }

        setPresetFile(presetFile);
        setArea(Cuboid.loadProperties(getPresetFile()));
    }
}
