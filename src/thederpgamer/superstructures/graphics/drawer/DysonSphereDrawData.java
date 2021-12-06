package thederpgamer.superstructures.graphics.drawer;

import api.common.GameServer;
import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import com.bulletphysics.linearmath.Transform;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.forms.Mesh;
import thederpgamer.superstructures.data.DataSerializer;
import thederpgamer.superstructures.data.structures.SuperStructureData;
import thederpgamer.superstructures.manager.ResourceManager;
import thederpgamer.superstructures.utils.DataUtils;

import javax.vecmath.Vector3f;
import java.io.IOException;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [12/06/2021]
 */
public class DysonSphereDrawData implements Drawable, DataSerializer {

    public Mesh frameMesh;
    public Mesh[] moduleMeshes;
    public Transform starTransform;
    public Vector3i centerSector;
    public StellarSystem system;

    public long index;
    public SegmentController segmentController;
    public SuperStructureData structureData;

    public float updateTimer = 0.0f;
    public boolean initialized = false;

    public DysonSphereDrawData(long index, SegmentController segmentController, SuperStructureData structureData) {
        this.index = index;
        this.segmentController = segmentController;
        this.structureData = structureData;
    }

    public DysonSphereDrawData(PacketReadBuffer readBuffer) throws IOException {
        deserialize(readBuffer);
    }

    @Override
    public void onInit() {
        starTransform = new Transform();
        starTransform.setIdentity();
        try {
            Vector3i currentSector = segmentController.getSector(new Vector3i());
            Sector sector = GameServer.getUniverse().getSector(currentSector);
            system = sector._getSystem();
            centerSector = system.getAbsoluteSectorPos(system.getIndex(new Vector3i(0, 0, 0)), new Vector3i());
        } catch(IOException exception) {
            exception.printStackTrace();
        }
        frameMesh = ResourceManager.getMesh("dyson-sphere-frame");
        moduleMeshes = new Mesh[12];
        for(int i = 0; i < 12; i ++) {
            try {
                moduleMeshes[i] = ResourceManager.getMesh("dyson_sphere_empty_module_" + i);
            } catch(Exception ignored) { }
        }
        initialized = true;
    }

    @Override
    public void draw() {
        if(!initialized) onInit();
    }

    @Override
    public void cleanUp() {
        if(frameMesh != null) frameMesh.cleanUp();
        for(int i = 0; i < 12; i ++) {
            if(moduleMeshes[i] != null) moduleMeshes[i].cleanUp();
        }
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    public void updateMesh() {
        Vector3i currentSector = segmentController.getSector(new Vector3i());
        Vector3f difference = new Vector3f(centerSector.x - currentSector.x, centerSector.y - currentSector.y, centerSector.z - currentSector.z);
        starTransform.origin.set(difference);

        Vector3f scale = new Vector3f(500.0f, 500.0f, 500.0f);
        frameMesh.setScale(scale);
        frameMesh.setTransform(starTransform);
        for(int i = 0; i < 12; i ++) {
            if(moduleMeshes[i] != null) {
                moduleMeshes[i].setScale(scale);
                moduleMeshes[i].setTransform(starTransform);
            }
        }
        updateTimer = 100.0f;
    }

    @Override
    public void serialize(PacketWriteBuffer packetWriteBuffer) throws IOException {
        packetWriteBuffer.writeLong(index);
        packetWriteBuffer.writeSendable(segmentController);
    }

    @Override
    public void deserialize(PacketReadBuffer packetReadBuffer) throws IOException {
        index = packetReadBuffer.readLong();
        segmentController = (SegmentController) packetReadBuffer.readSendable();
        structureData = DataUtils.getStructure(segmentController.getSystem(new Vector3i()));
    }
}
