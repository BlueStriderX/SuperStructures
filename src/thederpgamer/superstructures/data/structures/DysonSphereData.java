package thederpgamer.superstructures.data.structures;

import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Sector;
import java.io.Serializable;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereData extends SuperStructureData implements Serializable {

    public DysonSphereData(Sector sector, SegmentPiece segmentPiece) {
        super(sector, segmentPiece, 12);
    }
}