package br.com.bungee.server;

import br.com.core.data.ServerData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Server {
    private ServerData data;

    public Server(String json) {
        this.data = ServerData.fromJson(json);
    }

    public Server(ServerData data) {
        this.data = data;
    }

    public String getName(){
        return getData().getName();
    }

    public int getCurrentPlayers(){
        return getData().getCurrentPlayers();
    }

    public boolean isOnline(){
        return getData().isOnline();
    }
}
