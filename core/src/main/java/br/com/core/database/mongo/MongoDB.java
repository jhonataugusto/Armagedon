package br.com.core.database.mongo;

import br.com.core.Core;
import br.com.core.database.mongo.properties.MongoProperties;
import com.google.gson.Gson;
import lombok.Data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Data
public class MongoDB {
    private final String MONGO_DATABASE_NAME = Core.SERVER_NAME.toLowerCase();
    private final String DEFAULT_HOST = "localhost";
    private final int DEFAULT_PORT = 27017;
    private final String DEFAULT_USERNAME = "username";
    private final String DEFAULT_PASSWORD = "password";
    private File folder = new File("../database");
    private File file = new File(folder + "/mongo_properties.json");
    private String MONGO_URI = load();

    private MongoProperties properties;

    public String load() {

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                properties = Core.GSON.fromJson(reader, MongoProperties.class);
            } catch (IOException exception) {
                System.out.println("Error reading properties file");
                exception.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
                properties = new MongoProperties(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_USERNAME, DEFAULT_PASSWORD);
                saveProperties();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return this.MONGO_URI = "mongodb://" + properties.getUsername() + ":" + properties.getPassword() + "@" + properties.getHost() + ":" + properties.getPort();
    }

    private void saveProperties() {
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            gson.toJson(properties, writer);
        } catch (IOException e) {
            System.out.println("Error saving properties file");
            e.printStackTrace();
        }
    }
}
