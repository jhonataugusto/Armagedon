package br.com.armagedon.server.data;

import br.com.armagedon.Core;
import lombok.Data;

@Data
public class ServerData {
    private String name;
    private boolean online;
    private int players;



    private ServerData fromJson(String json) {
        return Core.GSON.fromJson(json, ServerData.class);
    }

    public String toJson(){
        return Core.GSON.toJson(this);
    }
}
