package br.com.core.data.object;

import lombok.Data;

@Data
public class RankDAO {
    private String name;
    private String expiration;

    public RankDAO(String name, long expiration) {
        this.name = name;
        this.expiration = String.valueOf(expiration);
    }

    public long getExpiration(){
        return Long.parseLong(expiration);
    }
}
