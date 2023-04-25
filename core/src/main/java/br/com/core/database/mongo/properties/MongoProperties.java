package br.com.core.database.mongo.properties;

public class MongoProperties {
    private String host;
    private int port;

    public MongoProperties(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
