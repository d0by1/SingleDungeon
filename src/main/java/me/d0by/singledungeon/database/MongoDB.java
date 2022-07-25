package me.d0by.singledungeon.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import me.d0by.singledungeon.Config;
import me.d0by.singledungeon.profile.Profile;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

public class MongoDB implements DB {

    private static final ReplaceOptions REPLACE_OPTIONS = new ReplaceOptions().upsert(true);
    private MongoClient client;

    public MongoDB() {
        this.connect();
    }

    @Override
    public void connect() {
        client = MongoClients.create(String.format("mongodb://%s:%d", Config.MONGODB_HOST, Config.MONGODB_PORT));
    }

    @Override
    public void disconnect() {
        client.close();
    }

    public MongoDatabase getDatabase() {
        return client.getDatabase(Config.MONGODB_DATABASE);
    }

    public MongoCollection<Document> getCollection() {
        return getDatabase().getCollection(Config.MONGODB_COLLECTION);
    }

    @Override
    public void loadProfile(@NotNull Profile profile) {
        Document document = getCollection().find(new Document("nickname", profile.getName())).first();
        if (document != null) {
            profile.setGames(document.getInteger("games"));
            profile.setWins(document.getInteger("wins"));
            profile.setKills(document.getInteger("kills"));
        }
    }

    @Override
    public void saveProfile(@NotNull Profile profile) {
        Document document = new Document("nickname", profile.getName())
                .append("games", profile.getGames())
                .append("wins", profile.getWins())
                .append("kills", profile.getKills());
        getCollection().replaceOne(new Document("nickname", profile.getName()), document, REPLACE_OPTIONS);
    }

}
