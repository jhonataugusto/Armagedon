package br.com.anticheat.event;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;

public class Event extends PacketEvent {
    public boolean isAsyncByDefault() {
        return false;
    }
    @Override
    public void call(PacketListenerAbstract listener) {

    }
}
