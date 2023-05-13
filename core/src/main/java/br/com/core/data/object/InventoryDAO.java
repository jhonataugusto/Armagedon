package br.com.core.data.object;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * uma classe para serialização e deserialização de dados para universalizar a comunicação de Objetos no Bukkit, Mongo e Redis.
 * resumidamente: facilitar a transferencia via JSON e deixar o código mais organizado
 * */
@Getter
@AllArgsConstructor
public class InventoryDAO {
    private String gamemodeName;
    private String inventoryEncoded;
}
