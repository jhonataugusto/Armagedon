package br.com.practice.arena.map;

import br.com.core.utils.id.IDUtils;
import br.com.practice.Practice;
import br.com.practice.util.cuboid.Cuboid;
import br.com.practice.util.file.CompressionUtil;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Data
public class ArenaMap {
    private String id = IDUtils.createCustomUuid(6);
    private String name;
    private File directory;
    private File presetDirectory;
    private Cuboid area;

    public ArenaMap(String name, String presetDirectoryPath, File arenaDirectory) {

        while (Practice.getInstance().getArenaStorage().getArenas().containsKey(getId())) {
            setId(IDUtils.createCustomUuid(6));
        }

        setName(name);
        setDirectory(new File(arenaDirectory, getId()));

        if (!getDirectory().exists()) {
            getDirectory().mkdir();
        }

        File presetFile = new File(presetDirectoryPath, name);

        if (!presetFile.exists()) {
            presetFile.mkdirs();
        }

        setPresetDirectory(presetFile);
        setArea(Cuboid.loadProperties(getPresetDirectory()));
    }

    public boolean copyTo(File destination) {
        try {

            File source = new File(getPresetDirectory().getPath());
            CompressionUtil.copy(source, destination, null);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
