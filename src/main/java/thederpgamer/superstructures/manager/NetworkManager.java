package thederpgamer.superstructures.manager;

import api.network.packets.PacketUtil;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.PlayerState;
import thederpgamer.superstructures.structures.StructureType;

/**
 * Manages network packets.
 *
 * @author TheDerpGamer
 */
public class NetworkManager {

	public static void sendOpenGUIPacket(SegmentPiece segmentPiece, PlayerState playerState) {
		PacketUtil.sendPacket(playerState, StructureType.instantiateOpenGUIPacket(segmentPiece));
	}
}
