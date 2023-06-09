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

package io.github.retrooper.packetevents.packetwrappers.handshaking.setprotocol;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public class WrappedPacketHandshakingInSetProtocol extends WrappedPacket {
    private static boolean v_1_17;
    public WrappedPacketHandshakingInSetProtocol(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }

    public int getProtocolVersion() {
        return readInt(v_1_17 ? 1 : 0);
    }

    public void setProtocolVersion(int protocolVersion) {
        writeInt(v_1_17 ? 1 : 0, protocolVersion);
    }

    public int getPort() {
        return readInt(v_1_17 ? 2 : 1);
    }

    public void setPort(int port) {
        writeInt(v_1_17 ? 2 : 1, port);
    }

    public String getHostName() {
        return readString(0);
    }

    public void setHostName(String hostName) {
        writeString(0, hostName);
    }
}
