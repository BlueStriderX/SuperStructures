package thederpgamer.superstructures.networking.server;

import api.common.GameClient;
import api.network.Packet;
import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.network.objects.Sendable;
import thederpgamer.superstructures.manager.GraphicsManager;
import thederpgamer.superstructures.structures.StructureType;
import thederpgamer.superstructures.utils.StructureUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * Packet for telling a client to open a GUI.
 * <p>[SERVER -> CLIENT]</p>
 *
 * @author TheDerpGamer
 */
public class DysonSphereOpenGUIPacket extends Packet {

	private SegmentPiece segmentPiece;

	/**
	 * Default constructor used to instantiate this packet on the client.
	 * <p><b>Do not use this constructor on the server.</b></p>
	 */
	public DysonSphereOpenGUIPacket() {

	}

	/**
	 * Server constructor. Instantiates this packet on the server.
	 * @param segmentPiece the segment piece to open the GUI for
	 */
	public DysonSphereOpenGUIPacket(SegmentPiece segmentPiece) {
		this.segmentPiece = segmentPiece;
	}

	@Override
	public void readPacketData(PacketReadBuffer packetReadBuffer) throws IOException {
		Sendable sendable = packetReadBuffer.readSendable();
		long index = packetReadBuffer.readLong();
		StructureType type = StructureType.values()[packetReadBuffer.readInt()];
		if(sendable instanceof SegmentController) {
			SegmentController segmentController = (SegmentController) sendable;
			if(segmentController.getSegmentBuffer().existsPointUnsave(index) && StructureUtils.isTypeStructureController(segmentController.getSegmentBuffer().getPointUnsave(index).getType())) {
				if(type.getType() == segmentController.getSegmentBuffer().getPointUnsave(index).getType()) segmentPiece = segmentController.getSegmentBuffer().getPointUnsave(index);
				else throw new IOException("Segment piece type does not match structure type: " + segmentController.getSegmentBuffer().getPointUnsave(index).getType() + " != " + type.getType());
			} else throw new IOException("Received invalid segment piece from server: " + segmentController);
		} else throw new IOException("Received invalid sendable from server: " + sendable.toString());
	}

	@Override
	public void writePacketData(PacketWriteBuffer packetWriteBuffer) throws IOException {
		packetWriteBuffer.writeSendable(segmentPiece.getSegmentController());
		packetWriteBuffer.writeLong(segmentPiece.getAbsoluteIndex());
		packetWriteBuffer.writeInt(Objects.requireNonNull(StructureType.getType(segmentPiece.getType())).ordinal());
	}

	@Override
	public void processPacketOnClient() {
		StructureType type = StructureType.getType(segmentPiece.getType());
		switch(type) {
			case DYSON_SPHERE:
				GraphicsManager.getInstance().openDysonSphereControlManager(segmentPiece, GameClient.getClientPlayerState());
				break;
		}
	}

	@Override
	public void processPacketOnServer(PlayerState playerState) {

	}
}
