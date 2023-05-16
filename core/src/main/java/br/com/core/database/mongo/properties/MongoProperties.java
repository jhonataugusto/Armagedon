package br.com.core.database.mongo.properties;

import lombok.Data;

@Data
public class MongoProperties {
    private String host;
    private int port;
    private String username;
    private String password;

    public MongoProperties(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public MongoProperties(String host, int port) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }
}
