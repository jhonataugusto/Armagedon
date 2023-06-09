package br.com.anticheat.commands;

import br.com.anticheat.AntiCheat;

public class ACCommand {

    public AntiCheat main = AntiCheat.getInstance();

    public ACCommand() {
        main.getFramework().registerCommands(this);
    }
}
