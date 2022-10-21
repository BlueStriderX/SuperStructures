package thederpgamer.superstructures.data.structures;

import api.common.GameServer;
import api.network.PacketReadBuffer;
import com.bulletphysics.linearmath.Transform;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.forms.Mesh;
import thederpgamer.superstructures.manager.ResourceManager;

import javax.vecmath.Vector3f;
import java.io.IOException;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereData extends SuperStructureData implements Drawable {

    public Mesh frameMesh;
    public Mesh[] moduleMeshes;
    public Transform starTransform;
    public Vector3i centerSector;
    public StellarSystem system;

    public float updateTimer = 0.0f;
    public boolean initialized = false;

    public DysonSphereData(Vector3i sunPos, SegmentPiece segmentPiece) {
        super(sunPos, segmentPiece, 12);
    }

    public DysonSphereData(PacketReadBuffer packetReadBuffer) throws IOException {
        super(packetReadBuffer);
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
}