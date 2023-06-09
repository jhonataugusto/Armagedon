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

package io.github.retrooper.packetevents.packetwrappers.play.out.entityheadrotation;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;

public class WrappedPacketOutEntityHeadRotation extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private static final float ROTATION_FACTOR = 256.0F / 360.0F;
    private float yaw;

    public WrappedPacketOutEntityHeadRotation(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutEntityHeadRotation(int entityID, float yaw) {
        this.entityID = entityID;
        this.yaw = yaw;
    }

    public WrappedPacketOutEntityHeadRotation(Entity entity, float yaw) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.yaw = yaw;
    }

    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (v_1_17) {
                packetConstructor = PacketTypeClasses.Play.Server.ENTITY_HEAD_ROTATION.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                packetConstructor = PacketTypeClasses.Play.Server.ENTITY_HEAD_ROTATION.getConstructor();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public float getYaw() {
        if (packet != null) {
            return readByte(0) / ROTATION_FACTOR;
        } else {
            return yaw;
        }
    }

    public void setYaw(float yaw) {
        if (packet != null) {
            writeByte(0, (byte) (yaw * ROTATION_FACTOR));
        } else {
            this.yaw = yaw;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (v_1_17) {
            Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] {0, 0}));
            packetInstance = packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = packetConstructor.newInstance();
        }
        WrappedPacketOutEntityHeadRotation wrappedPacketOutEntityHeadRotation = new WrappedPacketOutEntityHeadRotation(new NMSPacket(packetInstance));
        wrappedPacketOutEntityHeadRotation.setEntityId(getEntityId());
        wrappedPacketOutEntityHeadRotation.setYaw(getYaw());
        return packetInstance;
    }
}
