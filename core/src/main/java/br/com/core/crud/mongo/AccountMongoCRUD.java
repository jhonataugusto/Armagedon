package br.com.core.crud.mongo;

import br.com.core.Core;
import br.com.core.database.mongo.collections.CollectionProps;
import br.com.core.data.AccountData;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.UUID;

public class AccountMongoCRUD {

    private static final String URI = Core.MONGODB.getMONGO_URI();
    private static final String MONGO_DATABASE_NAME = Core.MONGODB.getMONGO_DATABASE_NAME();
    private static final String MONGO_COLLECTION_NAME = CollectionProps.USERS.getName();

    public static void create(AccountData data) {
        try (MongoClient client = MongoClients.create(URI)) {
            Document document = Document.parse(data.toJson());
            client.getDatabase(MONGO_DATABASE_NAME).getCollection(MONGO_COLLECTION_NAME).insertOne(document);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static AccountData get(UUID uuid) {
        try (MongoClient client = MongoClients.create(URI)) {

            Document document = client.getDatabase(MONGO_DATABASE_NAME).getCollection(MONGO_COLLECTION_NAME).find(Filters.eq("uuid", uuid.toString())).first();

            if (document != null) {
                return new AccountData(document.toJson());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static AccountData get(String name) {
        try (MongoClient client = MongoClients.create(URI)) {

            Document document = client.getDatabase(MONGO_DATABASE_NAME).getCollection(MONGO_COLLECTION_NAME).find(Filters.eq("name", name)).first();

            if (document != null) {
                return new AccountData(document.toJson());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static void save(AccountData data) {
        try (MongoClient client = MongoClients.create(URI)) {
            Document filter = new Document("uuid", data.getUuid().toString());
            Document update = new Document("$set", Document.parse(data.toJson()));
            client.getDatabase(MONGO_DATABASE_NAME).getCollection(MONGO_COLLECTION_NAME).updateOne(filter, update);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void delete(AccountData data) {
        try (MongoClient client = MongoClients.create(URI)) {
            Document filter = new Document("uuid", data.getUuid().toString());
            client.getDatabase(MONGO_DATABASE_NAME).getCollection(MONGO_COLLECTION_NAME).deleteOne(filter);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

