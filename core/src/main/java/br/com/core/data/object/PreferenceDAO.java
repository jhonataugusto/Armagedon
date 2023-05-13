package br.com.core.data.object;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * uma classe para serialização e deserialização de dados para universalizar a comunicação de Objetos no Bukkit, Mongo e Redis.
 * resumidamente: facilitar a transferencia via JSON e deixar o código mais organizado
 */
@Data
@AllArgsConstructor
public class PreferenceDAO {
    private String name;
    private String type;
    private boolean active;
}
