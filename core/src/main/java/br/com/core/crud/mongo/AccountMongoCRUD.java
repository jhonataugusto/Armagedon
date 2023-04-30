package br.com.core.crud.mongo;

import br.com.core.Core;
import br.com.core.database.mongo.collections.CollectionProps;
import br.com.core.data.AccountData;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

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

    public static List<AccountData> getTopAccounts(int limit, String gamemodeName) {
        try (MongoClient client = MongoClients.create(URI)) {
            MongoCollection<Document> collection = client.getDatabase(MONGO_DATABASE_NAME).getCollection(MONGO_COLLECTION_NAME);

            List<Bson> pipeline = Arrays.asList(
                    Aggregates.match(Filters.exists("elos.name")),
                    Aggregates.unwind("$elos"),
                    Aggregates.match(Filters.eq("elos.name", gamemodeName)),
                    Aggregates.sort(Sorts.descending("elos.elo")),
                    Aggregates.limit(limit),
                    Aggregates.lookup(CollectionProps.USERS.getName(), "uuid", "uuid", CollectionProps.USERS.getName()),
                    Aggregates.unwind("$" + CollectionProps.USERS.getName()),
                    Aggregates.replaceRoot("$" + CollectionProps.USERS.getName())
            );

            List<AccountData> accounts = new ArrayList<>();
            List<Document> documents = collection.aggregate(pipeline).into(new ArrayList<>());
            for (Document document : documents) {
                accounts.add(new AccountData(document.toJson()));
            }

            return accounts;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}

