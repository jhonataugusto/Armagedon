package br.com.anticheat.check.impl.timer;


import br.com.anticheat.AntiCheat;
import io.github.retrooper.packetevents.event.PacketEvent;
import br.com.anticheat.check.api.Category;
import br.com.anticheat.check.api.Check;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.event.FlyingEvent;
import br.com.anticheat.event.PositionEvent;
import br.com.anticheat.util.MathUtil;
import org.bukkit.entity.Player;

import java.util.Deque;
import java.util.LinkedList;

public class TimerA extends Check {

    private long lastTime, lastFlag;
    private double buffer;

    private final Deque<Long> samples = new LinkedList();


    public TimerA(final PlayerData playerData) {
        super(Category.PACKET, playerData);
    }

    @Override
    public void handle(PacketEvent event, Player player) {
        if (event instanceof FlyingEvent && !AntiCheat.getInstance().isServerLagging()) {
            long now = System.currentTimeMillis();

            if (playerData.getTotalTicks() < 300 && playerData.isLagging2(System.currentTimeMillis(), 100L) && playerData.getTotalTicks() - playerData.getLastDroppedPackets() < 5) return;

            samples.add(now - lastTime);

            if (samples.size() >= 40) {
                final double avg = samples.stream().mapToDouble(d -> d).average().orElse(0.0);
                final double speed = 50 / avg;

                if (speed > (1.009 + MathUtil.getPingToTimer(playerData.getTransPing() + 50))) {
                    if ((buffer += 0.75) > 3.5) {
                        handleFlag(player, "Timer A", "§b* §fTimer=§b" + speed + "\n§b* §fAVG=§b" + avg, getBanVL("TimerA"), 100000L);
                    }
                } else {
                    buffer = Math.max(buffer - 1.25, 0.0);
                }

                samples.clear();

            }
            lastTime = System.currentTimeMillis();


        } else if (event instanceof PositionEvent) {
            samples.add(150L);
        }
    }
}

