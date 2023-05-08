package br.com.bungee.util.motd;

import br.com.core.Core;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Motd {
    public static final List<String> MOTDS = new ArrayList<>();

    static {
        MOTDS.add("     nosso Discord: " + ChatColor.AQUA + Core.SERVER_DISCORD);
        MOTDS.add("     Mostre a sua habilidade no PVP!");
        MOTDS.add("     Venha lutar no nosso servidor de PVP.");
        MOTDS.add("     Você está pronto para lutar?");
        MOTDS.add("Seja o melhor lutador em nosso servidor de PVP");
        MOTDS.add("Um lugar onde você pode lutar justamente");
        MOTDS.add("para os jogadores que buscam um desafio de PVP");
        MOTDS.add("Mostre suas habilidades e vença seus adversários");
        MOTDS.add("     Prepare-se para uma batalha emocionante");
        MOTDS.add("O lugar perfeito para jogadores competitivos!");
    }
}
