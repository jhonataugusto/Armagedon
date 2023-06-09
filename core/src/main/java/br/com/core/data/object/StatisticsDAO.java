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
    private String mode_name = "null";
    private int unranked_matches = 0;
    private int ranked_matches = 0;

    private int unranked_wins = 0;
    private int unranked_losses = 0;

    private int ranked_wins = 0;
    private int ranked_loses = 0;

    public StatisticsDAO(String mode_name) {
        this.mode_name = mode_name;
    }
}
