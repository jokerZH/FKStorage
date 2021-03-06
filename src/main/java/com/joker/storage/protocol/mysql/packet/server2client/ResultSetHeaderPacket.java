/*
 * Copyright 1999-2012 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joker.storage.protocol.mysql.packet.server2client;

import java.nio.ByteBuffer;
import java.sql.SQLException;

import com.joker.storage.protocol.mysql.connection.MysqlConnection;
import com.joker.storage.protocol.mysql.packet.BasePacket;
import com.joker.storage.protocol.mysql.utils.BufferUtil;

/**
 * From server to client after coBufferUtil.nd, if no error and result set -- that is,
 * if the coBufferUtil.nd was a query which returned a result set. The Result Set Header
 * Packet is the first of several, possibly many, packets that the server sends
 * for result sets. The order of packets for a result set is:
 * 
 * <pre>
 * (Result Set Header Packet)   the number of columns
 * (Field Packets)              column descriptors
 * (EOF Packet)                 marker: end of Field Packets
 * (Row Data Packets)           row contents
 * (EOF Packet)                 marker: end of Data Packets
 * 
 * Bytes                        Name
 * -----                        ----
 * 1-9   (Length-Coded-Binary)  field_count
 * 1-9   (Length-Coded-Binary)  extra
 * 
 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol#Result_Set_Header_Packet
 * </pre>
 * 
 * @author xianmao.hexm 2010-7-22 下午05:59:55
 */
public class ResultSetHeaderPacket extends BasePacket {

    public int fieldCount;
    public long extra;


    public ResultSetHeaderPacket(byte packetId, int fieldCount, long extra) throws SQLException {
        this.packetId = packetId;
        this.fieldCount = fieldCount;
        this.extra = extra;
        this.packetLength = calcPacketSize();
    }

    @Override
    public void read(ByteBuffer buffer) {
        super.read(buffer);

        this.fieldCount = (int) BufferUtil.readLength(buffer);
        if (buffer.hasRemaining()) {
            this.extra = BufferUtil.readLength(buffer);
        }
    }

    @Override
    public ByteBuffer write(ByteBuffer buffer, MysqlConnection c) {
        BufferUtil.checkWriteBuffer(buffer, calcPacketSize(), c);
        super.write(buffer, c);

        BufferUtil.writeLength(buffer, fieldCount);
        if (extra > 0) {
            BufferUtil.writeLength(buffer, extra);
        }
        return buffer;
    }

    @Override
    public int calcPacketSize() {
        int size = super.calcPacketSize();
        size += BufferUtil.getLength(fieldCount);
        if (extra > 0) {
            size += BufferUtil.getLength(extra);
        }
        return size;
    }

    @Override
    protected String getPacketInfo() {
        return "MySQL ResultSetHeader Packet";
    }

}
