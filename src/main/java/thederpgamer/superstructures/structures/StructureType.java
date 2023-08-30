package thederpgamer.superstructures.structures;

import api.common.GameServer;
import api.network.Packet;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.server.data.ServerConfig;
import thederpgamer.superstructures.elements.ElementManager;
import thederpgamer.superstructures.manager.ConfigManager;
import thederpgamer.superstructures.networking.server.DysonSphereOpenGUIPacket;

import java.util.Objects;

/**
 * Enum containing all structure types.
 *
 * @author TheDerpGamer
 */
public enum StructureType {
	DYSON_SPHERE("Dyson Sphere Controller");

	private final String name;

	StructureType(String name) {
		this.name = name;
	}

	public short getType() {
		return Objects.requireNonNull(ElementManager.getBlock(name)).getId();
	}

	public static Packet instantiateOpenGUIPacket(SegmentPiece segmentPiece) {
		StructureType type = getType(segmentPiece.getType());
		switch(type) {
			case DYSON_SPHERE:
				return new DysonSphereOpenGUIPacket(segmentPiece);
			default:
				return null;
		}
	}

	public static boolean isValid(SegmentPiece segmentPiece) {
		StructureType type = getType(segmentPiece.getType());
		SegmentController segmentController = segmentPiece.getSegmentController();
		Sector sector = GameServer.getUniverse().getSector(segmentController.getSectorId());
		switch(type) {
			case DYSON_SPHERE:
				return ((sector._getDistanceToSun() * (int) ServerConfig.SECTOR_SIZE.getCurrentState()) <= (ConfigManager.getMainConfig().getDouble("max-dyson-sphere-station-distance"))) && segmentController.getType() == SimpleTransformableSendableObject.EntityType.SPACE_STATION;
			default:
				return false;
		}
	}

	public static StructureType getType(short id) {
		for(StructureType type : values()) {
			if(type.getType() == id) return type;
		}
		return null;
	}
}
