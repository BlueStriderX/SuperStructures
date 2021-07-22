package thederpgamer.superstructures.data.modules;

import api.common.GameCommon;
import thederpgamer.superstructures.data.entity.structure.SuperStructureModule;
import thederpgamer.superstructures.data.structures.SuperStructureData;

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
    public int controllerEntityId;

    public StructureModuleData(String name, int maxLevel, int blockTypeCount, SuperStructureData structureData) {
        this.name = name;
        this.moduleEntityId = -1;
        this.maxLevel = maxLevel;
        this.level = 0;
        this.blockIds = new short[blockTypeCount];
        this.controllerEntityId = structureData.entityId;
    }

    public SuperStructureModule getEntity() {
        return (SuperStructureModule) GameCommon.getGameObject(moduleEntityId);
    }
}
