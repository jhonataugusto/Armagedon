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

package io.github.retrooper.packetevents.packetwrappers.api.helper;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;

public abstract class WrappedPacketEntityAbstraction extends WrappedPacket {
    private final int entityIDFieldIndex;
    protected Entity entity;
    protected int entityID = -1;

    public WrappedPacketEntityAbstraction(NMSPacket packet, int entityIDFieldIndex) {
        super(packet);
        this.entityIDFieldIndex = entityIDFieldIndex;
    }

    public WrappedPacketEntityAbstraction(NMSPacket packet) {
        super(packet);
        this.entityIDFieldIndex = 0;
    }

    public WrappedPacketEntityAbstraction(int entityIDFieldIndex) {
        super();
        this.entityIDFieldIndex = entityIDFieldIndex;
    }

    public WrappedPacketEntityAbstraction() {
        super();
        this.entityIDFieldIndex = 0;
    }

    public int getEntityId() {
        if (entityID != -1 || packet == null) {
            return entityID;
        }
        return entityID = readInt(entityIDFieldIndex);
    }

    public void setEntityId(int entityID) {
        if (packet != null) {
            writeInt(entityIDFieldIndex, this.entityID = entityID);
        } else {
            this.entityID = entityID;
        }
        this.entity = null;
    }

    @Nullable
    public Entity getEntity(@Nullable World world) {
        if (entity != null) {
            return entity;
        }
        return PacketEvents.get().getServerUtils().getEntityById(world, getEntityId());
    }

    @Nullable
    public Entity getEntity() {
        if (entity != null) {
            return entity;
        }
        return PacketEvents.get().getServerUtils().getEntityById(getEntityId());
    }

    public void setEntity(Entity entity) {
        setEntityId(entity.getEntityId());
        this.entity = entity;
    }
}
