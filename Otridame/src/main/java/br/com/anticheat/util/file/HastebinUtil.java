package br.com.anticheat.util.file;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HastebinUtil {

    private static final String PASTE_URL = "https://hasteb.in/";
    private static final String PASTE_UPLOAD_URL = PASTE_URL + "documents";

    public static String uploadPaste(final String contents) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(PASTE_UPLOAD_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Karhu");
            try (OutputStream os = connection.getOutputStream()) {
                os.write(contents.getBytes(Charsets.UTF_8));
            }

            Gson gson = new Gson();

            // Read URL
            JsonObject object = gson.fromJson(new InputStreamReader(connection.getInputStream(), Charsets.UTF_8), JsonObject.class);
            String pasteUrl = PASTE_URL + object.get("key").getAsString();
            connection.disconnect();
            return pasteUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
