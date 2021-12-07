package thederpgamer.superstructures.utils;

import api.common.GameClient;
import api.common.GameServer;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.VoidSystem;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.graphicsengine.forms.Mesh;
import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.modules.dysonsphere.DysonSphereEmptyModuleData;
import thederpgamer.superstructures.data.structures.DysonSphereData;
import thederpgamer.superstructures.data.structures.SuperStructureData;
import thederpgamer.superstructures.graphics.gui.elements.GUIMeshOverlay;
import thederpgamer.superstructures.graphics.mesh.DysonSphereMultiMesh;
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

    public static boolean inDrawRange() {
        return GameClient.getClientState().getCurrentGalaxy().getSunDistance(GameClient.getClientPlayerState().getCurrentSector()) <= ConfigManager.getMainConfig().getInt("max-dyson-sphere-station-distance");
    }

    /*
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

     */

    public static DysonSphereData generateStructureData(SegmentPiece segmentPiece) {
        return new DysonSphereData(VoidSystem.getSunSectorPosAbs(GameClient.getClientState().getCurrentGalaxy(), segmentPiece.getSegmentController().getSystem(new Vector3i()), new Vector3i()), segmentPiece);
    }

    public static DysonSphereMultiMesh createMultiMesh(SuperStructureData structureData) {
        Mesh[] meshArray = new Mesh[12];
        for(int i = 0; i < meshArray.length; i ++) {
            try {
                meshArray[i] = ResourceManager.getMesh("dyson_sphere_empty_module_" + i);
            } catch(Exception ignored) { }
        }
        return new DysonSphereMultiMesh(ResourceManager.getMesh("dyson_sphere_frame"), meshArray);
    }

    public static void updateMesh(GUIMeshOverlay meshOverlay, SuperStructureData structureData) {
        if(meshOverlay.mesh instanceof DysonSphereMultiMesh) {
            DysonSphereMultiMesh mesh = (DysonSphereMultiMesh) meshOverlay.mesh;
            for(int i = 0; i < 12; i ++) {
                if(mesh.meshArray[i] != null) {
                    if(structureData.modules[i] instanceof DysonSphereEmptyModuleData) {
                        mesh.meshArray[i].setMaterial(ResourceManager.getSprite("empty-overlay").getMaterial());
                    } else {
                        switch(structureData.modules[i].status) {
                            case StructureModuleData.NONE:
                                mesh.meshArray[i].setMaterial(ResourceManager.getSprite("regular-overlay").getMaterial());
                                break;
                            case StructureModuleData.CONSTRUCTION:
                                mesh.meshArray[i].setMaterial(ResourceManager.getSprite("construction-overlay").getMaterial());
                                break;
                            case StructureModuleData.REPAIR:
                                mesh.meshArray[i].setMaterial(ResourceManager.getSprite("repair-overlay").getMaterial());
                                break;
                            case StructureModuleData.UPGRADE:
                                mesh.meshArray[i].setMaterial(ResourceManager.getSprite("upgrade-overlay").getMaterial());
                                break;
                        }
                    }
                }
            }
        }
    }
}