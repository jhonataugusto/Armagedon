package br.com.practice.util.rating;

/**
 * Classe utilitária para cálculo de rating utilizando o sistema ELO.
 */
public class RatingUtil {

    /*
    * O fator K é um parâmetro utilizado no sistema ELO para determinar o quanto o rating de um jogador irá mudar após uma partida.
    * Ele é geralmente utilizado para ajustar a taxa de mudança de rating com base na experiência ou habilidade do jogador.
    * */
    private static final int K_FACTOR = 32;

    /**
     * Construtor privado para evitar criação de instâncias.
     */
    private RatingUtil() {}

    /**
     * Calcula a pontuação esperada para um jogador em uma partida contra um oponente.
     * @param playerRating o rating do jogador.
     * @param opponentRating o rating do oponente.
     * @return a pontuação esperada do jogador.
     */
    public static double getExpectedScore(double playerRating, double opponentRating) {
        return 1.0 / (1.0 + Math.pow(10.0, (opponentRating - playerRating) / 400.0));
    }

    /**
     * Calcula a variação no rating de um jogador após uma partida.
     * @param playerRating o rating do jogador antes da partida.
     * @param opponentRating o rating do oponente antes da partida.
     * @param outcome o resultado da partida: 1 para vitória, 0.5 para empate, 0 para derrota.
     * @return a variação no rating do jogador após a partida.
     */
    public static int calculateRating(int playerRating, int opponentRating, double outcome) {
        double expectedScore = getExpectedScore(playerRating, opponentRating);
        int ratingChange = (int) Math.round(K_FACTOR * (outcome - expectedScore));
        return playerRating + ratingChange;
    }


}
