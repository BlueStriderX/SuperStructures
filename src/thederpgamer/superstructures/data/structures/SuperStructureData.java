package thederpgamer.superstructures.data.structures;

import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Sector;
import thederpgamer.superstructures.data.modules.StructureModuleData;
import java.io.Serializable;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class SuperStructureData implements Serializable {

    public Vector3i systemPos;
    public Vector3i sectorPos;
    public long index;
    public int entityId;
    public StructureModuleData[] modules;

    public SuperStructureData(Sector sector, SegmentPiece segmentPiece, int maxModules) {
        sectorPos = sector.pos;
        systemPos = sector._getSystem().getPos();
        index = segmentPiece.getAbsoluteIndex();
        entityId = segmentPiece.getSegmentController().getId();
        modules = new StructureModuleData[maxModules];
    }

    public StructureModuleData getModule(int entityId) {
        for(StructureModuleData module : modules) if(module.moduleEntityId == entityId) return module;
        return null;
    }
}
