package br.com.anticheat.event;

import lombok.Getter;

@Getter
public class HeldItemSlotEvent extends Event {

    private final int slot;

    public HeldItemSlotEvent(int slot) {
        this.slot = slot;
    }
}
