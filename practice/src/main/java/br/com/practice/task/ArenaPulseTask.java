package br.com.practice.task;

import br.com.practice.Practice;
import br.com.practice.events.arena.pulse.ArenaPulseEvent;
import lombok.Getter;

@Getter
public class ArenaPulseTask implements Runnable {

    private final Practice instance;


    public ArenaPulseTask(Practice plugin) {
        this.instance = plugin;
    }

    @Override
    public void run() {
        ArenaPulseEvent event = new ArenaPulseEvent();
        Practice.getInstance().getServer().getPluginManager().callEvent(event);
    }
}
