/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.abilities;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutAbilities extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor, playerAbilitiesConstructor;
    private static Class<?> playerAbilitiesClass;
    private boolean vulnerable, flying, allowFlight, instantBuild;
    private float flySpeed, walkSpeed;

    public WrappedPacketOutAbilities(final NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutAbilities(final boolean isVulnerable, final boolean isFlying, final boolean allowFlight, final boolean canBuildInstantly,
                                     final float flySpeed, final float walkSpeed) {
        this.vulnerable = isVulnerable;
        this.flying = isFlying;
        this.allowFlight = allowFlight;
        this.instantBuild = canBuildInstantly;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    @Override
    protected void load() {
        try {
            playerAbilitiesClass = NMSUtils.getNMSClass("PlayerAbilities");
        } catch (ClassNotFoundException e) {
            playerAbilitiesClass = NMSUtils.getNMClassWithoutException("world.entity.player.PlayerAbilities");
        }

        if (playerAbilitiesClass != null) {
            try {
                playerAbilitiesConstructor = playerAbilitiesClass.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        try {
            packetConstructor = PacketTypeClasses.Play.Server.ABILITIES.getConstructor(playerAbilitiesClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public boolean isVulnerable() {
        if (packet != null) {
            return readBoolean(0);
        } else {
            return vulnerable;
        }
    }

    public void setVulnerable(boolean isVulnerable) {
        if (packet != null) {
            writeBoolean(0, isVulnerable);
        } else {
            this.vulnerable = isVulnerable;
        }
    }

    public boolean isFlying() {
        if (packet != null) {
            return readBoolean(1);
        } else {
            return flying;
        }
    }

    public void setFlying(boolean isFlying) {
        if (packet != null) {
            writeBoolean(1, isFlying);
        } else {
            this.flying = isFlying;
        }
    }

    public boolean isFlightAllowed() {
        if (packet != null) {
            return readBoolean(2);
        } else {
            return allowFlight;
        }
    }

    public void setFlightAllowed(boolean isFlightAllowed) {
        if (packet != null) {
            writeBoolean(2, isFlightAllowed);
        } else {
            this.allowFlight = isFlightAllowed;
        }
    }

    public boolean canBuildInstantly() {
        if (packet != null) {
            return readBoolean(3);
        } else {
            return instantBuild;
        }
    }

    public void setCanBuildInstantly(boolean canBuildInstantly) {
        if (packet != null) {
            writeBoolean(3, canBuildInstantly);
        } else {
            this.instantBuild = canBuildInstantly;
        }
    }

    public float getFlySpeed() {
        if (packet != null) {
            return readFloat(0);
        } else {
            return flySpeed;
        }
    }

    public void setFlySpeed(float flySpeed) {
        if (packet != null) {
            writeFloat(0, flySpeed);
        } else {
            this.flySpeed = flySpeed;
        }
    }

    public float getWalkSpeed() {
        if (packet != null) {
            return readFloat(1);
        } else {
            return walkSpeed;
        }
    }

    public void setWalkSpeed(float walkSpeed) {
        if (packet != null) {
            writeFloat(1, walkSpeed);
        } else {
            this.walkSpeed = walkSpeed;
        }
    }

    private Object getPlayerAbilities(boolean vulnerable, boolean flying, boolean flightAllowed, boolean canBuildInstantly, float flySpeed, float walkSpeed) {
        Object instance = null;
        try {
            instance = playerAbilitiesConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        WrappedPacket wrapper = new WrappedPacket(new NMSPacket(instance));
        wrapper.writeBoolean(0, vulnerable);
        wrapper.writeBoolean(1, flying);
        wrapper.writeBoolean(2, flightAllowed);
        wrapper.writeBoolean(3, canBuildInstantly);
        wrapper.writeFloat(0, flySpeed);
        wrapper.writeFloat(1, walkSpeed);
        return instance;
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getPlayerAbilities(isVulnerable(), isFlying(), isFlightAllowed(), canBuildInstantly(), getFlySpeed(), getWalkSpeed()));
    }
}
