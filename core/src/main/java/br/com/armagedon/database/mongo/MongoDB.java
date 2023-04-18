package br.com.armagedon.database.mongo;

import br.com.armagedon.Core;
import br.com.armagedon.database.mongo.properties.MongoProperties;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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
                properties = new MongoProperties(DEFAULT_HOST, DEFAULT_PORT);
                saveProperties();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return this.MONGO_URI = "mongodb://" + properties.getHost() + ":" + properties.getPort();
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
