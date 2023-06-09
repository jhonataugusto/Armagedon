/*
 *
 * This file is part of Bukkit - https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse
 * Copyright (C) 2011 Bukkit author and contributors
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
 *
 */

package io.github.retrooper.packetevents.utils.boundingbox;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Objects;


public class RayTraceResult {
    private final Vector hitPosition;
    private final Block hitBlock;
    private final BlockFace hitBlockFace;
    private final Entity hitEntity;

    private RayTraceResult(Vector hitPosition, Block hitBlock, BlockFace hitBlockFace, Entity hitEntity) {
        Validate.notNull(hitPosition, "Hit position is null!");
        this.hitPosition = hitPosition.clone();
        this.hitBlock = hitBlock;
        this.hitBlockFace = hitBlockFace;
        this.hitEntity = hitEntity;
    }

    public RayTraceResult(Vector hitPosition) {
        this(hitPosition, (Block) null, (BlockFace) null, (Entity) null);
    }

    public RayTraceResult(Vector hitPosition, BlockFace hitBlockFace) {
        this(hitPosition, (Block) null, hitBlockFace, (Entity) null);
    }

    public RayTraceResult(Vector hitPosition, Block hitBlock, BlockFace hitBlockFace) {
        this(hitPosition, hitBlock, hitBlockFace, (Entity) null);
    }

    public RayTraceResult(Vector hitPosition, Entity hitEntity) {
        this(hitPosition, (Block) null, (BlockFace) null, hitEntity);
    }

    public RayTraceResult(Vector hitPosition, Entity hitEntity, BlockFace hitBlockFace) {
        this(hitPosition, (Block) null, hitBlockFace, hitEntity);
    }


    public Vector getHitPosition() {
        return this.hitPosition.clone();
    }


    public Block getHitBlock() {
        return this.hitBlock;
    }


    public BlockFace getHitBlockFace() {
        return this.hitBlockFace;
    }


    public Entity getHitEntity() {
        return this.hitEntity;
    }

    public int hashCode() {
        int result = 31 + this.hitPosition.hashCode();
        result = 31 * result + (this.hitBlock == null ? 0 : this.hitBlock.hashCode());
        result = 31 * result + (this.hitBlockFace == null ? 0 : this.hitBlockFace.hashCode());
        result = 31 * result + (this.hitEntity == null ? 0 : this.hitEntity.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof RayTraceResult)) {
            return false;
        } else {
            RayTraceResult other = (RayTraceResult) obj;
            if (!this.hitPosition.equals(other.hitPosition)) {
                return false;
            } else if (!Objects.equals(this.hitBlock, other.hitBlock)) {
                return false;
            } else if (!Objects.equals(this.hitBlockFace, other.hitBlockFace)) {
                return false;
            } else {
                return Objects.equals(this.hitEntity, other.hitEntity);
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RayTraceResult [hitPosition=");
        builder.append(this.hitPosition);
        builder.append(", hitBlock=");
        builder.append(this.hitBlock);
        builder.append(", hitBlockFace=");
        builder.append(this.hitBlockFace);
        builder.append(", hitEntity=");
        builder.append(this.hitEntity);
        builder.append("]");
        return builder.toString();
    }
}