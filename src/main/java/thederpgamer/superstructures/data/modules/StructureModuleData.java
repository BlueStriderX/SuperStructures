package thederpgamer.superstructures.data.modules;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import thederpgamer.superstructures.data.DataSerializer;

import java.io.IOException;
import java.util.HashMap;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class StructureModuleData implements DataSerializer {

    public static final int NONE = 0;
    public static final int CONSTRUCTION = 1;
    public static final int UPGRADE = 2;
    public static final int REPAIR = 3;

    public String name;
    public int level;
    public int maxLevel;
    public short[] blockIds;
    public int status;
    public HashMap<Short, Integer> constructionMap;

    public StructureModuleData(String name, int maxLevel, int blockTypeCount) {
        this.name = name;
        this.maxLevel = maxLevel;
        level = 0;
        blockIds = new short[blockTypeCount];
        constructionMap = new HashMap<>();
        status = NONE;
    }

    public StructureModuleData(PacketReadBuffer packetReadBuffer) throws IOException {
        deserialize(packetReadBuffer);
    }

    public String getDesc() {
        return "";
    }

    @Override
    public void serialize(PacketWriteBuffer packetWriteBuffer) throws IOException {
        packetWriteBuffer.writeString(name);
        packetWriteBuffer.writeInt(level);
        packetWriteBuffer.writeInt(maxLevel);
        packetWriteBuffer.writeInt(blockIds.length);
        if(blockIds.length > 0) for(short s : blockIds) packetWriteBuffer.writeShort(s);
        packetWriteBuffer.writeInt(status);
        packetWriteBuffer.writeObject(constructionMap);
    }

    @Override
    public void deserialize(PacketReadBuffer packetReadBuffer) throws IOException {
        name = packetReadBuffer.readString();
        level = packetReadBuffer.readInt();
        maxLevel = packetReadBuffer.readInt();
        int size = packetReadBuffer.readInt();
        if(size > 0) {
            blockIds = new short[size];
            for(int i = 0; i < size; i ++) blockIds[i] = packetReadBuffer.readShort();
        }
        status = packetReadBuffer.readInt();
        constructionMap = packetReadBuffer.readObject(constructionMap.getClass());
    }
}
