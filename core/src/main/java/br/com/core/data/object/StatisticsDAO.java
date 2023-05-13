package br.com.core.data.object;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * uma classe para serialização e deserialização de dados para universalizar a comunicação de Objetos no Bukkit, Mongo e Redis.
 * resumidamente: facilitar a transferencia via JSON e deixar o código mais organizado
 * */
@Data
@AllArgsConstructor
public class StatisticsDAO {
    private String gameMode_name;
    private int unranked_matches;
    private int ranked_matches;

    private int unranked_wins;
    private int unranked_losses;

    private int ranked_wins;
    private int ranked_loses;
}
