package br.com.core.data.object;

import lombok.Data;

@Data
public class StatisticsDAO {
    private String gameMode_name;
    private int unranked_matches;
    private int ranked_matches;

    private int unranked_wins;
    private int unranked_losses;

    private int ranked_wins;
    private int ranked_loses;
}
