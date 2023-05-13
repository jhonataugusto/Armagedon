package br.com.core.data.object;

import lombok.Data;

/**
 * uma classe para serialização e deserialização de dados para universalizar a comunicação de Objetos no Bukkit, Mongo e Redis.
 * resumidamente: facilitar a transferencia via JSON e deixar o código mais organizado
 * */
@Data
public class AltDAO {
    public String name;
    public String uuid;
    public String ip;
}
