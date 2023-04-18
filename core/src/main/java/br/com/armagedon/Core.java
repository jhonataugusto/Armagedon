package br.com.armagedon;

import br.com.armagedon.database.mongo.MongoDB;
import br.com.armagedon.database.mongo.storage.MongoStorage;
import com.google.gson.Gson;
import lombok.Data;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

@Data
public class Core {
    public static final String SERVER_NAME = System.getProperty("server_name", "Armagedon");
    public static final String SERVER_WEBSITE = System.getProperty("server_website", "armagedon.com.br");
    public static final String SERVER_DISCORD = System.getProperty("server_discord", "discord.gg/armagedon");
    public static final String SERVER_STORE = System.getProperty("server_store", "loja.armagedon.com.br");
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###,###,###.##");
    public static final DecimalFormat SIMPLE_DECIMAL_FORMAT = build();
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final Gson GSON = new Gson();
    public static final MongoDB MONGODB = new MongoDB();
    public static final Logger MONGO_LOGGER = Logger.getLogger("org.mongodb.driver");
    public final MongoStorage MONGO_STORAGE = new MongoStorage();
    private static final String JEDIS_HOST = "localhost";
    private static final int JEDIS_PORT = 6379;
    public static final JedisPool JEDIS_POOL = new JedisPool(new JedisPoolConfig(), JEDIS_HOST, JEDIS_PORT);

    private static DecimalFormat build() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();

        decimalFormatSymbols.setDecimalSeparator(',');

        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        return decimalFormat;
    }
}
