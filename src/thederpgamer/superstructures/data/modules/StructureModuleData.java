package thederpgamer.superstructures.data.modules;

import api.common.GameCommon;
import thederpgamer.superstructures.data.entity.structure.SuperStructureModule;
import java.io.Serializable;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class StructureModuleData implements Serializable {

    public String name;
    public int level;
    public final int maxLevel;
    public short[] blockIds;
    public int moduleEntityId;

    public StructureModuleData(String name, int moduleEntityId, int maxLevel, int blockTypeCount) {
        this.name = name;
        this.moduleEntityId = moduleEntityId;
        this.maxLevel = maxLevel;
        this.level = 0;
        this.blockIds = new short[blockTypeCount];
    }

    public SuperStructureModule getEntity() {
        return (SuperStructureModule) GameCommon.getGameObject(moduleEntityId);
    }
}
