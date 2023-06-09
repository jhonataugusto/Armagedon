package br.com.core.data.object;

import br.com.core.account.enums.rank.Rank;
import lombok.Data;

/**
 * uma classe para serialização e deserialização de dados para universalizar a comunicação de Objetos no Bukkit, Mongo e Redis.
 * resumidamente: facilitar a transferencia via JSON e deixar o código mais organizado
 */
@Data
public class RankDAO {
    private String name;
    private String expiration;

    public RankDAO(String name, long expiration) {
        this.name = name;
        this.expiration = String.valueOf(expiration);
    }

    public long getExpiration() {
        return Long.parseLong(expiration);
    }
}
