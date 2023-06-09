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

package io.github.retrooper.packetevents.utils.gameprofile;

import com.mojang.authlib.GameProfile;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Skin;
import com.mojang.authlib.properties.Property;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * 1.7.10 Mojang Game Profile util using the 1.7.10 Mojang API import location.
 *
 * @author retrooper
 * @since 1.6.8.2
 */
class GameProfileUtil_7 {
    /**
     * Create a new Mojang Game Profile object using the 1.7.10 Mojang API import.
     *
     * @param uuid     UUID
     * @param username Username
     * @return 1.7.10 Mojang Game Profile.
     */
    public static Object getGameProfile(UUID uuid, String username) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Object entityHuman = NMSUtils.entityHumanClass.cast(NMSUtils.getEntityPlayer(player));
            WrappedPacket wrappedEntityPlayer = new WrappedPacket(new NMSPacket(entityHuman), NMSUtils.entityHumanClass);
            return wrappedEntityPlayer.readObject(0, GameProfile.class);
        } else {
            return new GameProfile(uuid, username);
        }
    }

    /**
     * Create a Wrapper for the Mojang Game Profile object.
     *
     * @param gameProfile Mojang Game profile
     * @return {@link WrappedGameProfile}
     */
    public static WrappedGameProfile getWrappedGameProfile(Object gameProfile) {
        GameProfile gp = (GameProfile) gameProfile;
        return new WrappedGameProfile(gp.getId(), gp.getName());
    }

    public static void setGameProfileSkin(Object gameProfile, Skin skin) {
        GameProfile gp = (GameProfile) gameProfile;
        gp.getProperties().put("textures", new Property(skin.getValue(), skin.getSignature()));
    }

    public static Skin getGameProfileSkin(Object gameProfile) {
        Property property = ((GameProfile) gameProfile).getProperties().get("textures").iterator().next();
        String value = property.getValue();
        String signature = property.getSignature();
        return new Skin(value, signature);
    }
}
