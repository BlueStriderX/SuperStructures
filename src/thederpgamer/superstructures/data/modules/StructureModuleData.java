package thederpgamer.superstructures.data.modules;

import thederpgamer.superstructures.data.structures.SuperStructureData;
import java.io.Serializable;
import java.util.HashMap;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class StructureModuleData implements Serializable {

    public static final int NONE = 0;
    public static final int CONSTRUCTION = 1;
    public static final int UPGRADE = 2;
    public static final int REPAIR = 3;

    public String name;
    public int level;
    public final int maxLevel;
    public short[] blockIds;
    public int moduleEntityId;
    public int controllerEntityId;
    public int status;
    public HashMap<Short, Integer> constructionMap;

    public StructureModuleData(String name, int maxLevel, int blockTypeCount, SuperStructureData structureData) {
        this.name = name;
        this.moduleEntityId = -1;
        this.maxLevel = maxLevel;
        this.level = 0;
        this.blockIds = new short[blockTypeCount];
        this.controllerEntityId = structureData.entityId;
        this.constructionMap = new HashMap<>();
        this.status = NONE;
    }

    public String getDesc() {
        return "";
    }

    public String getMeshName() {
        return name.toLowerCase().replace(" ", "-");
    }
}
