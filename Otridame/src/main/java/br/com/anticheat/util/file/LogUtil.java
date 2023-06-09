package br.com.anticheat.util.file;

import br.com.anticheat.AntiCheat;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogUtil {

    public static boolean logToFile(TextFile file, String text) {
        try {
            //yyyy-MM-dd HH:mm:ss
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            file.addLine(text.replace("%time%", date));
            file.write();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean logToMySQL(String text) {
        try {

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static class TextFile {

        private File file;
        private String name;
        private List<String> lines = new ArrayList<>();

        public TextFile(String name, String path) {
            this.file = new File(AntiCheat.getInstance().getDataFolder() + path);
            this.file.mkdirs();
            this.file = new File(AntiCheat.getInstance().getDataFolder() + path, name + ".txt");
            try {
                if (!this.file.exists()) this.file.createNewFile();
            } catch (Exception e) {
            }
            this.name = name;
            this.readTextFile();
        }

        public void clear() {
            this.lines.clear();
        }

        public void addLine(String line) {
            this.lines.add(line);
        }

        public void write() {
            try {
                FileWriter fw = new FileWriter(this.file, false);
                BufferedWriter bw = new BufferedWriter(fw);
                for (String line : this.lines) {
                    bw.write(line);
                    bw.newLine();
                }
                bw.close();
                fw.close();
            } catch (Exception e) {
            }
        }

        public void readTextFile() {
            this.lines.clear();
            try {
                String line;
                FileReader fr = new FileReader(this.file);
                BufferedReader br = new BufferedReader(fr);
                while ((line = br.readLine()) != null) {
                    this.lines.add(line);
                }
                br.close();
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getText() {
            String text = "";
            int i = 0;
            while (i < this.lines.size()) {
                String line = this.lines.get(i);
                text = text + line + (this.lines.size() - 1 == i ? "" : "\n");
                ++i;
            }
            return text;
        }

        public List<String> getLines() {
            return lines;
        }

    }

}
