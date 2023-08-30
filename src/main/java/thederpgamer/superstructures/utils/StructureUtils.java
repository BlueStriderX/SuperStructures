package thederpgamer.superstructures.utils;

import api.common.GameClient;
import api.common.GameServer;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.VoidSystem;
import org.schema.game.server.data.Galaxy;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.graphicsengine.forms.Mesh;
import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.modules.dysonsphere.DysonSphereEmptyModuleData;
import thederpgamer.superstructures.data.structures.DysonSphereData;
import thederpgamer.superstructures.data.structures.SuperStructureData;
import thederpgamer.superstructures.graphics.gui.elements.GUIMeshOverlay;
import thederpgamer.superstructures.graphics.mesh.DysonSphereMultiMesh;
import thederpgamer.superstructures.manager.ConfigManager;
import thederpgamer.superstructures.manager.GraphicsManager;
import thederpgamer.superstructures.manager.ResourceManager;
import thederpgamer.superstructures.structures.StructureType;

/**
 * Various utility methods for super structures.
 *
 * @author TheDerpGamer
 */
public class StructureUtils {

	private static final int sectorSize = (int) ServerConfig.SECTOR_SIZE.getCurrentState();
	private static final int sectorSizeHalf = sectorSize / 2;

	public static boolean isTypeStructureController(short type) {
		return StructureType.getType(type) != null;
	}

	public static boolean isValid(SegmentPiece segmentPiece) {
		return isTypeStructureController(segmentPiece.getInfo().getId()) && StructureType.isValid(segmentPiece);
	}

	public static boolean isClientInDrawRange(float maxDistance) {
		return GameClient.getClientState().getCurrentGalaxy().getSunDistance(GameClient.getClientPlayerState().getCurrentSector()) <= maxDistance;
	}

	public static DysonSphereData generateStructureData(SegmentPiece segmentPiece) {
		Vector3i system = new Vector3i();
		segmentPiece.getSegmentController().getSystem(system);
		if(segmentPiece.getSegmentController().isOnServer()) {
			Galaxy galaxy = GameServer.getServerState().getUniverse().getGalaxyFromSystemPos(system);
			return new DysonSphereData(VoidSystem.getSunSectorPosAbs(galaxy, system, new Vector3i()), segmentPiece);
		} else return new DysonSphereData(VoidSystem.getSunSectorPosAbs(GameClient.getClientState().getCurrentGalaxy(), system, new Vector3i()), segmentPiece);
	}

	public static DysonSphereMultiMesh createMultiMesh(SuperStructureData structureData) {
		Mesh[] meshArray = new Mesh[12];
		for(int i = 0; i < meshArray.length; i ++) {
			try {
				StructureModuleData moduleData = structureData.modules[i];
				if(moduleData == null) structureData.modules[i] = new DysonSphereEmptyModuleData(structureData);
//				meshArray[i] = ResourceManager.getMesh("dyson_sphere_empty_module_" + i);
				meshArray[i] = ResourceManager.getMesh("dyson_sphere_empty_module_0");
			} catch(Exception exception) {
				exception.printStackTrace();
			}
		}
		return new DysonSphereMultiMesh(ResourceManager.getMesh("dyson_sphere_frame"), meshArray);
	}

	public static void updateMesh(GUIMeshOverlay meshOverlay, SuperStructureData structureData) {
		if(meshOverlay.mesh instanceof DysonSphereMultiMesh) {
			DysonSphereMultiMesh mesh = (DysonSphereMultiMesh) meshOverlay.mesh;
			for(int i = 0; i < 12; i ++) {
				if(mesh.meshArray[i] != null) {
					if(structureData.modules[i] instanceof DysonSphereEmptyModuleData) mesh.meshArray[i].setMaterial(ResourceManager.getSprite("empty-overlay").getMaterial());
					else {
						switch(structureData.modules[i].getStatus()) {
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

	public static void openGUI(SegmentPiece segmentPiece, PlayerState playerState) {
		StructureType type = StructureType.getType(segmentPiece.getInfo().getId());
		switch(type) {
			case DYSON_SPHERE:
				GraphicsManager.getInstance().openDysonSphereControlManager(segmentPiece, playerState);
				break;
		}
	}

	public static boolean inDrawRange(StructureType structureType) {
		switch(structureType) {
			case DYSON_SPHERE:
				return GameClient.getClientState().getCurrentGalaxy().getSunDistance(GameClient.getClientPlayerState().getCurrentSector()) <= ConfigManager.getMainConfig().getInt("max-dyson-sphere-station-distance");
			default:
				return false;
		}
	}
}