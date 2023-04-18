package br.com.armagedon.database.mongo.storage;

import br.com.armagedon.Core;
import br.com.armagedon.database.mongo.collections.CollectionProps;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoIterable;

public class MongoStorage {

    private final String MONGO_DATABASE_NAME = Core.MONGODB.getMONGO_DATABASE_NAME();

    public MongoStorage() {
        loadCollections();
    }


    public void loadCollections() {
        try (MongoClient client = MongoClients.create(Core.MONGODB.getMONGO_URI())) {

            for (CollectionProps collection : CollectionProps.values()) {

                if (!collectionExists(collection)) {

                    client.getDatabase(MONGO_DATABASE_NAME).createCollection(collection.getName());
                }
            }
        }
    }

    private boolean collectionExists(CollectionProps collection) {
        try (MongoClient client = MongoClients.create(Core.MONGODB.getMONGO_URI())) {
            MongoIterable<String> collectionNames = client.getDatabase(MONGO_DATABASE_NAME).listCollectionNames();

            for (String name : collectionNames) {
                if (name.equalsIgnoreCase(collection.getName())) {
                    return true;
                }
            }
            return false;
        }

    }
}
