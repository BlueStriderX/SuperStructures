package thederpgamer.superstructures.data.modules;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import it.unimi.dsi.fastutil.shorts.Short2IntOpenHashMap;
import thederpgamer.superstructures.data.DataSerializer;
import thederpgamer.superstructures.data.structures.SuperStructureData;

import java.io.IOException;

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

    private SuperStructureData structureData;
    private String name;
    private String description;
    private int level;
    private int maxLevel;
    private int status;
    private final Short2IntOpenHashMap constructionMap = new Short2IntOpenHashMap();

    public StructureModuleData(SuperStructureData structureData, String name, String description, int maxLevel) {
        this.structureData = structureData;
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
        level = 0;
        status = NONE;
    }

	public StructureModuleData(PacketReadBuffer packetReadBuffer) throws IOException {
        deserialize(packetReadBuffer);
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void serialize(PacketWriteBuffer packetWriteBuffer) throws IOException {
        packetWriteBuffer.writeString(name);
        packetWriteBuffer.writeString(description);
        packetWriteBuffer.writeInt(level);
        packetWriteBuffer.writeInt(maxLevel);
        packetWriteBuffer.writeInt(status);
        packetWriteBuffer.writeInt(constructionMap.size());
        for(Short key : constructionMap.keySet()) {
            packetWriteBuffer.writeShort(key);
            packetWriteBuffer.writeInt(constructionMap.get(key));
        }
    }

    @Override
    public void deserialize(PacketReadBuffer packetReadBuffer) throws IOException {
        name = packetReadBuffer.readString();
        description = packetReadBuffer.readString();
        level = packetReadBuffer.readInt();
        maxLevel = packetReadBuffer.readInt();
        status = packetReadBuffer.readInt();
        int size = packetReadBuffer.readInt();
        for(int i = 0; i < size; i ++) constructionMap.put(packetReadBuffer.readShort(), packetReadBuffer.readInt());
    }

    public SuperStructureData getStructureData() {
        return structureData;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Short2IntOpenHashMap getConstructionMap() {
        return constructionMap;
    }
}
