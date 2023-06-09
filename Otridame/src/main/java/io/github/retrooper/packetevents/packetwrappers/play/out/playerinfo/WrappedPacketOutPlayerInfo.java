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

package io.github.retrooper.packetevents.packetwrappers.play.out.playerinfo;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.gameprofile.GameProfileUtil;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.GameMode;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class WrappedPacketOutPlayerInfo extends WrappedPacket implements SendableWrapper {
    private static boolean v_1_7_10, v_1_17;
    private static Class<? extends Enum<?>> enumPlayerInfoActionClass;
    private static Constructor<?> packetConstructor, playerInfoDataConstructor;
    private static byte constructorMode = 0;
    private PlayerInfoAction action;
    private PlayerInfo[] playerInfoArray = new PlayerInfo[0];

    public WrappedPacketOutPlayerInfo(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutPlayerInfo(PlayerInfoAction action, PlayerInfo... playerInfoArray) {
        this.action = action;
        this.playerInfoArray = playerInfoArray;
    }

    @Override
    protected void load() {
        v_1_7_10 = version.isOlderThan(ServerVersion.v_1_8);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        enumPlayerInfoActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Server.PLAYER_INFO, "EnumPlayerInfoAction");
        try {
            if (v_1_17) {
                packetConstructor = PacketTypeClasses.Play.Server.PLAYER_INFO.getConstructor(NMSUtils.packetDataSerializerClass);
            } else {
                packetConstructor = PacketTypeClasses.Play.Server.PLAYER_INFO.getConstructor();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Class<?> playerInfoDataClass = SubclassUtil.getSubClass(PacketTypeClasses.Play.Server.PLAYER_INFO, "PlayerInfoData");
        if (playerInfoDataClass != null) {
            try {
                playerInfoDataConstructor = playerInfoDataClass.getConstructor(NMSUtils.gameProfileClass, int.class, NMSUtils.enumGameModeClass, NMSUtils.iChatBaseComponentClass);
            } catch (NoSuchMethodException e) {
                try {
                    playerInfoDataConstructor = playerInfoDataClass.getConstructor(PacketTypeClasses.Play.Server.PLAYER_INFO, NMSUtils.gameProfileClass, int.class, NMSUtils.enumGameModeClass, NMSUtils.iChatBaseComponentClass);
                    constructorMode = 1;
                } catch (NoSuchMethodException e2) {
                    e.printStackTrace();
                    e2.printStackTrace();
                }

            }
        }
    }

    public PlayerInfoAction getAction() {
        if (packet != null) {
            int index;
            if (v_1_7_10) {
                index = readInt(0);
            } else {
                Enum<?> enumConst = readEnumConstant(0, enumPlayerInfoActionClass);
                index = enumConst.ordinal();
            }
            return PlayerInfoAction.values()[index];
        } else {
            return action;
        }
    }

    public void setAction(PlayerInfoAction action) {
        if (packet != null) {
            int index = action.ordinal();
            if (v_1_7_10) {
                writeInt(0, index);
            } else {
                Enum<?> enumConst = EnumUtil.valueByIndex(enumPlayerInfoActionClass, action.ordinal());
                writeEnumConstant(0, enumConst);
            }
        } else {
            this.action = action;
        }
    }

    //TODO Technically we would need to support profile signiture data for complete 1.19.1 support.
    public PlayerInfo[] getPlayerInfo() {
        if (packet != null) {
            PlayerInfo[] playerInfoArray = new PlayerInfo[1];
            if (v_1_7_10) {
                String username = readString(0);
                Object mojangGameProfile = readObject(0, NMSUtils.gameProfileClass);
                WrappedGameProfile gameProfile = GameProfileUtil.getWrappedGameProfile(mojangGameProfile);
                GameMode gameMode = GameMode.values()[readInt(1)];
                int ping = readInt(2);
                playerInfoArray[0] = new PlayerInfo(username, gameProfile, gameMode, ping);
            } else {
                List<Object> nmsPlayerInfoDataList = readList(0);
                playerInfoArray = new PlayerInfo[nmsPlayerInfoDataList.size()];
                for (int i = 0; i < nmsPlayerInfoDataList.size(); i++) {
                    Object nmsPlayerInfoData = nmsPlayerInfoDataList.get(i);
                    WrappedPacket nmsPlayerInfoDataWrapper = new WrappedPacket(new NMSPacket(nmsPlayerInfoData));
                    String username = nmsPlayerInfoDataWrapper.readIChatBaseComponent(0);
                    Object mojangGameProfile = nmsPlayerInfoDataWrapper.readObject(0, NMSUtils.gameProfileClass);
                    WrappedGameProfile gameProfile = GameProfileUtil.getWrappedGameProfile(mojangGameProfile);
                    GameMode gameMode = nmsPlayerInfoDataWrapper.readGameMode(0);
                    int ping = nmsPlayerInfoDataWrapper.readInt(0);
                    playerInfoArray[i] = new PlayerInfo(username, gameProfile, gameMode, ping);
                }
            }
            return playerInfoArray;
        } else {
            return playerInfoArray;
        }
    }

    public void setPlayerInfo(PlayerInfo... playerInfoArray) throws UnsupportedOperationException {
        if (v_1_7_10 && playerInfoArray.length > 1) {
            throw new UnsupportedOperationException("The player info list size cannot be greater than 1 on 1.7.10 servers!");
        }
        if (packet != null) {
            if (v_1_7_10) {
                PlayerInfo playerInfo = playerInfoArray[0];
                writeString(0, playerInfo.username);
                Object mojangGameProfile = GameProfileUtil.getGameProfile(playerInfo.gameProfile.getId(), playerInfo.gameProfile.getName());
                writeObject(0, mojangGameProfile);
                writeInt(1, playerInfo.gameMode.ordinal());
                writeInt(2, playerInfo.ping);
            } else {
                List<Object> nmsPlayerInfoList = new ArrayList<>();

                for (PlayerInfo playerInfo : playerInfoArray) {
                    Object usernameIChatBaseComponent = NMSUtils.generateIChatBaseComponent(NMSUtils.fromStringToJSON(playerInfo.username));
                    Object mojangGameProfile = GameProfileUtil.getGameProfile(playerInfo.gameProfile.getId(), playerInfo.gameProfile.getName());
                    Enum<?> nmsGameModeEnumConstant = EnumUtil.valueByIndex(NMSUtils.enumGameModeClass, playerInfo.gameMode.ordinal());
                    int ping = playerInfo.ping;
                    try {
                        if (constructorMode == 0) {
                            nmsPlayerInfoList.add(playerInfoDataConstructor.newInstance(mojangGameProfile, ping, nmsGameModeEnumConstant, usernameIChatBaseComponent));

                        } else if (constructorMode == 1) {
                            nmsPlayerInfoList.add(playerInfoDataConstructor.newInstance(null, mojangGameProfile, ping, nmsGameModeEnumConstant, usernameIChatBaseComponent));
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                writeList(0, nmsPlayerInfoList);
            }
        } else {
            this.playerInfoArray = playerInfoArray;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (v_1_17) {
            Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
            Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(byteBuf);
            packetInstance = packetConstructor.newInstance(packetDataSerializer);
        } else {
            packetInstance = packetConstructor.newInstance();
        }
        WrappedPacketOutPlayerInfo playerInfoWrapper = new WrappedPacketOutPlayerInfo(new NMSPacket(packetInstance));
        PlayerInfo[] playerInfos = getPlayerInfo();
        if (playerInfos.length != 0) {
            playerInfoWrapper.setPlayerInfo(playerInfos);
        }
        playerInfoWrapper.setAction(getAction());
        return packetInstance;
    }

    @Override
    public boolean isSupported() {
        //1.19.3 removed this packet and replaced it with PlayerInfoRemove & PlayerInfoUpdate
        //TODO Since this is a minimal update, we won't add those. v1.8 will eventually be discontinued
        //so please consider updating to 2.0
        return version.isOlderThanOrEquals(ServerVersion.v_1_19_2);
    }

    public enum PlayerInfoAction {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    public static class PlayerInfo {
        private String username;
        private WrappedGameProfile gameProfile;
        private GameMode gameMode;
        private int ping;

        public PlayerInfo(String username, WrappedGameProfile gameProfile, GameMode gameMode, int ping) {
            this.username = username;
            this.gameProfile = gameProfile;
            this.gameMode = gameMode;
            this.ping = ping;
        }

        public PlayerInfo(String username, WrappedGameProfile gameProfile, org.bukkit.GameMode gameMode, int ping) {
            this.username = username;
            this.gameProfile = gameProfile;
            this.gameMode = GameMode.valueOf(gameMode.name());
            this.ping = ping;
        }

        public WrappedGameProfile getGameProfile() {
            return gameProfile;
        }

        public void setGameProfile(WrappedGameProfile gameProfile) {
            this.gameProfile = gameProfile;
        }

        public GameMode getGameMode() {
            return gameMode;
        }

        public void setGameMode(GameMode gameMode) {
            this.gameMode = gameMode;
        }

        public int getPing() {
            return ping;
        }

        public void setPing(int ping) {
            this.ping = ping;
        }


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
