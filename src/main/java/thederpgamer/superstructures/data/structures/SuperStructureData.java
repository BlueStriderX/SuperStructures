package thederpgamer.superstructures.data.structures;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.shader.Shaderable;
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
public abstract class SuperStructureData implements DataSerializer, Drawable, Shaderable {

    public Vector3i system = new Vector3i();
    public Vector3i sector = new Vector3i();
    public Vector3i sunSector = new Vector3i();
    public StructureModuleData[] modules = new StructureModuleData[12];
    public SegmentController segmentController;
    public SegmentPiece segmentPiece;
    public float updateTimer;

    protected SuperStructureData(Vector3i sunSector, SegmentPiece segmentPiece, int maxModules) {
        this.sunSector = sunSector;
        segmentPiece.getSegmentController().getSector(sector);
        segmentPiece.getSegmentController().getSystem(system);
        this.segmentPiece = segmentPiece;
        segmentController = segmentPiece.getSegmentController();
        modules = new StructureModuleData[maxModules];
    }

    protected SuperStructureData(PacketReadBuffer packetReadBuffer) throws IOException {
        deserialize(packetReadBuffer);
    }

    @Override
    public void serialize(PacketWriteBuffer packetWriteBuffer) throws IOException {
        packetWriteBuffer.writeVector(system);
        packetWriteBuffer.writeVector(sector);
        packetWriteBuffer.writeVector(sunSector);
        packetWriteBuffer.writeLong(segmentPiece.getAbsoluteIndex());
        packetWriteBuffer.writeLong(segmentController.getDbId());
        packetWriteBuffer.writeInt(modules.length);
        if(modules.length > 0) {
            for(StructureModuleData module : modules) if(module != null) module.serialize(packetWriteBuffer);
        }
    }

    @Override
    public void deserialize(PacketReadBuffer packetReadBuffer) throws IOException {
        if(packetReadBuffer.available() <= 0) return;
        system = packetReadBuffer.readVector();
        sector = packetReadBuffer.readVector();
        sunSector = packetReadBuffer.readVector();
        long index = packetReadBuffer.readLong();
        long entityId = packetReadBuffer.readLong();
        modules = new StructureModuleData[packetReadBuffer.readInt()];
        for(int i = 0; i < modules.length; i ++) {
            try {
                modules[i] = new StructureModuleData(packetReadBuffer);
            } catch(Exception ignored) { }
        }
        segmentController = EntityUtils.getEntityFromID(entityId);
        if(segmentController == null) {
            //Todo: Delete this data due to non-existent segment controller
        } else segmentPiece = segmentController.getSegmentBuffer().getPointUnsave(index);
    }

    public abstract void updateMesh();
}
