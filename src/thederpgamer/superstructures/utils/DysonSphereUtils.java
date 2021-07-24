package thederpgamer.superstructures.utils;

import api.common.GameClient;
import api.common.GameServer;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.graphicsengine.forms.Mesh;
import thederpgamer.superstructures.data.structures.DysonSphereData;
import thederpgamer.superstructures.data.structures.SuperStructureData;
import thederpgamer.superstructures.manager.ConfigManager;
import thederpgamer.superstructures.manager.ResourceManager;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereUtils {

    public static boolean isValidForDysonSphere(SegmentPiece segmentPiece) {
        Sector sector = GameServer.getUniverse().getSector(segmentPiece.getSegmentController().getSectorId());
        return ((sector._getDistanceToSun() * (int) ServerConfig.SECTOR_SIZE.getCurrentState()) <= (ConfigManager.getMainConfig().getDouble("max-dyson-sphere-station-distance"))) && segmentPiece.getSegmentController().getType().equals(SimpleTransformableSendableObject.EntityType.SPACE_STATION);
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

    public static Mesh createMesh(SuperStructureData structureData) {
        //Mesh[] meshArray = new Mesh[12];
        for(int i = 0; i < 12; i ++) {
            try {
                //meshArray[i] = ResourceManager.getMesh(structureData.modules[i].getMeshName());
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
        //Mesh combinedMesh =
        return ResourceManager.getMesh( "dyson-sphere-full"); //Todo: Combine each individual module mesh into one combined mesh
    }
}
