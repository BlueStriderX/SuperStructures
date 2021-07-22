package thederpgamer.superstructures.utils;

import api.common.GameClient;
import api.common.GameServer;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.StellarSystem;
import thederpgamer.superstructures.data.structures.DysonSphereData;
import thederpgamer.superstructures.manager.ConfigManager;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereUtils {

    public static boolean isValidForDysonSphere(SegmentPiece segmentPiece) {
        Sector sector = GameServer.getUniverse().getSector(segmentPiece.getSegmentController().getSectorId());
        return (sector._getDistanceToSun() <= (ConfigManager.getMainConfig().getDouble("max-dyson-sphere-station-distance"))) && !hasDysonSphere(sector._getSystem()) && segmentPiece.getSegmentController().getType().equals(SimpleTransformableSendableObject.EntityType.SPACE_STATION);
    }

    public static boolean hasDysonSphere(StellarSystem system) {
        if(DataUtils.hasSuperStructure(system) && DataUtils.getStructure(system) instanceof DysonSphereData) return true;
        else {
            DataUtils.queueRemoval(system.getPos());
            return false;
        }
    }

    public static boolean isClientInDrawRange(float maxDistance) {
        return GameClient.getClientState().getCurrentGalaxy().getSunDistance(GameClient.getClientPlayerState().getCurrentSector()) <= maxDistance;
    }
}
