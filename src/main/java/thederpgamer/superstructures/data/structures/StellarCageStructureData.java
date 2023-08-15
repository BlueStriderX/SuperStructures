package thederpgamer.superstructures.data.structures;

import api.common.GameClient;
import api.common.GameCommon;
import api.common.GameServer;
import api.network.PacketReadBuffer;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;

/**
 * [Description]
 *
 * @author TheDerpGamer (TheDerpGamer#0027)
 */
public class StellarCageStructureData extends SuperStructureData {

	public StellarCageStructureData(Vector3i sunSector, SegmentPiece segmentPiece, int maxModules) {
		super(sunSector, segmentPiece, maxModules);
	}

	public StellarCageStructureData(PacketReadBuffer readBuffer) throws Exception {
		super(readBuffer);
	}

	public SunType getSunType() {
		try {
			if((GameCommon.isDedicatedServer() || GameCommon.isOnSinglePlayer()) && GameServer.getServerState() != null) return SunType.getFromSystem(GameServer.getServerState().getUniverse().getGalaxyFromSystemPos(system), system);
			else if(GameClient.getClientState() != null) return SunType.getFromSystem(GameClient.getClientState().getCurrentGalaxy(), system); //Todo: Probably want to get a specific galaxy rather than just the current one.
			else return SunType.VOID;
		} catch(Exception exception) {
			exception.printStackTrace();
			return SunType.VOID;
		}
	}
}