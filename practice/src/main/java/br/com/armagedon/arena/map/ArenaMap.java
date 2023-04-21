package br.com.armagedon.arena.map;

import br.com.armagedon.util.cuboid.Cuboid;
import lombok.Data;

import java.io.File;
import java.util.UUID;

@Data
public class ArenaMap {
    private final String id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    private String name;
    private File directory;
    private File presetFile;
    private Cuboid area;

    //TODO: resolver as dependencias do arenaMap (está dando um NullException Cabuloso!)
    public ArenaMap(String name, String presetFilePath, File arenaDirectory) {

        setName(name);
        setDirectory(new File(arenaDirectory, getId()));

        if (!getDirectory().exists()) {
            getDirectory().mkdir();
        }

        File presetFile = new File(presetFilePath, name);

        if(!presetFile.exists()) {
            presetFile.mkdirs();
        }

        setPresetFile(presetFile);
        setArea(Cuboid.loadProperties(getPresetFile()));
    }
}
