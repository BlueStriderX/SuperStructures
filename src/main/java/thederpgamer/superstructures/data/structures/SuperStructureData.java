package thederpgamer.superstructures.data.structures;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.SegmentPiece;
import thederpgamer.superstructures.data.DataSerializer;
import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.utils.EntityUtils;

import java.io.IOException;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class SuperStructureData implements DataSerializer {

    public Vector3i system;
    public Vector3i sector;
    public Vector3i sunSector;
    public StructureModuleData[] modules;
    public SegmentController segmentController;
    public SegmentPiece segmentPiece;
	public SSLogEvent[] logs;

	public SuperStructureData(Vector3i sunSector, SegmentPiece segmentPiece, int maxModules) {
        this.sunSector = sunSector;
        this.sector = segmentPiece.getSegmentController().getSector(new Vector3i());
        this.system = segmentPiece.getSegmentController().getSystem(new Vector3i());
        this.segmentPiece = segmentPiece;
        this.segmentController = segmentPiece.getSegmentController();
        this.modules = new StructureModuleData[maxModules];
        this.logs = new SSLogEvent[0];
    }

    public SuperStructureData(PacketReadBuffer packetReadBuffer) throws IOException {
        deserialize(packetReadBuffer);
    }

    @Override
    public void serialize(PacketWriteBuffer packetWriteBuffer) throws IOException {
        packetWriteBuffer.writeVector(system);
        packetWriteBuffer.writeVector(sector);
        packetWriteBuffer.writeVector(sunSector);
        packetWriteBuffer.writeLong(segmentPiece.getAbsoluteIndex());
        packetWriteBuffer.writeString(segmentController.getUniqueIdentifier());
        packetWriteBuffer.writeInt(modules.length);
        if(modules.length > 0) {
            for(StructureModuleData module : modules) if(module != null) module.serialize(packetWriteBuffer);
        }

        packetWriteBuffer.writeInt(logs.length);
        if(logs.length > 0) {
            for(SSLogEvent log : logs) log.serialize(packetWriteBuffer);
        }
    }

    @Override
    public void deserialize(PacketReadBuffer packetReadBuffer) throws IOException {
        try {
            system = packetReadBuffer.readVector();
            sector = packetReadBuffer.readVector();
            sunSector = packetReadBuffer.readVector();
            long index = packetReadBuffer.readLong();
            String entityUID = packetReadBuffer.readString();
            modules = new StructureModuleData[packetReadBuffer.readInt()];
            for(int i = 0; i < modules.length; i ++) {
                try {
                    modules[i] = new StructureModuleData(packetReadBuffer);
                } catch(Exception ignored) { }
            }
            segmentController = EntityUtils.getEntityFromUID(entityUID);
            if(segmentController == null) {
                //Todo: Delete this data due to non-existent segment controller
            } else segmentPiece = segmentController.getSegmentBuffer().getPointUnsave(index);
            logs = new SSLogEvent[packetReadBuffer.readInt()];
            for(int i = 0; i < logs.length; i ++) {
                try {
                    logs[i] = new SSLogEvent(packetReadBuffer);
                } catch(Exception ignored) { }
            }
        } catch(Exception exception) {
            //Ignore
        }
    }

    public void log(int type, String message) {
        SSLogEvent[] newLogs = new SSLogEvent[logs.length + 1];
        System.arraycopy(logs, 0, newLogs, 0, logs.length);
        newLogs[logs.length] = new SSLogEvent(type, message);
        logs = newLogs;
    }
}
