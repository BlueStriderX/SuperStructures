package thederpgamer.superstructures.data.structures;

import api.common.GameClient;
import api.common.GameServer;
import api.network.PacketReadBuffer;
import com.bulletphysics.linearmath.MatrixUtil;
import com.bulletphysics.linearmath.Transform;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.core.GraphicsContext;
import org.schema.schine.graphicsengine.forms.Mesh;
import thederpgamer.superstructures.manager.ResourceManager;

import javax.vecmath.Matrix3f;
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

    private static Vector3f[] dodecahedronVertices;

    public DysonSphereData(Vector3i sunPos, SegmentPiece segmentPiece) {
        super(sunPos, segmentPiece, 12);
    }

    public DysonSphereData(PacketReadBuffer packetReadBuffer) throws IOException {
        super(packetReadBuffer);
    }

    public void updateMesh() {
        if(!initialized) onInit();
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

                //Rotate mesh to face outwards from the center of the star
                Vector3f direction = new Vector3f(moduleMeshes[i].getTransform().origin);
                direction.normalize();
                //Create new matrix3f to store rotation
                Matrix3f rotation = new Matrix3f();
                rotation.setIdentity();
                MatrixUtil.setEulerZYX(rotation, (float) Math.PI / 2.0f, (float) Math.PI / 2.0f, 0.0f);
                //Rotate the mesh
                moduleMeshes[i].getTransform().basis.mul(rotation);

                //Position the mesh so it acts as a face of the dodechahedron
                moduleMeshes[i].getTransform().origin.set(0.0f, 0.0f, 0.0f);
                moduleMeshes[i].getTransform().origin.scaleAdd(250.0f, getDodecahedronVertex(i), moduleMeshes[i].getTransform().origin);
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
        frameMesh = ResourceManager.getMesh("dyson_sphere_frame");
        moduleMeshes = new Mesh[12];
        for(int i = 0; i < 12; i ++) {
            try {
                moduleMeshes[i] = ResourceManager.getMesh("dyson_sphere_empty_module_0");
            } catch(Exception ignored) { }
        }

        initialized = true;
    }

    @Override
    public void draw() {
        if(!initialized) onInit();
        Vector3f currentSector = new Vector3f(GameClient.getClientPlayerState().getCurrentSector().toVector3f());
        Vector3f difference = new Vector3f(centerSector.x - currentSector.x, centerSector.y - currentSector.y, centerSector.z - currentSector.z);

        difference.scale(1000.0f);
        Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set(difference);
        frameMesh.setScale(new Vector3f(500.0f, 500.0f, 500.0f));
        frameMesh.setTransform(transform);
        for(int i = 0; i < 12; i ++) {
            if(moduleMeshes[i] != null) {
                moduleMeshes[i].setScale(new Vector3f(500.0f, 500.0f, 500.0f));
                moduleMeshes[i].setTransform(transform);
            }
        }

        frameMesh.transform();
        frameMesh.draw();
        for(int i = 0; i < 12; i ++) {
            if(moduleMeshes[i] != null) {
                moduleMeshes[i].transform();
                moduleMeshes[i].draw();
            }
        }
    }

    @Override
    public void cleanUp() {
        if(GraphicsContext.initialized) {
            if(frameMesh != null) frameMesh.cleanUp();
            for(int i = 0; i < 12; i ++) {
                if(moduleMeshes[i] != null) moduleMeshes[i].cleanUp();
            }
        }
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    public static Vector3f getDodecahedronVertex(int index) {
        //Check if dodecahedron array is null, if it is initialize it
        if(dodecahedronVertices == null) {
            dodecahedronVertices = new Vector3f[12];
            float t = (float) ((1.0 + Math.sqrt(5.0)) / 2.0);
            dodecahedronVertices[0] = new Vector3f(-1.0f, t, 0.0f);
            dodecahedronVertices[1] = new Vector3f(1.0f, t, 0.0f);
            dodecahedronVertices[2] = new Vector3f(-1.0f, -t, 0.0f);
            dodecahedronVertices[3] = new Vector3f(1.0f, -t, 0.0f);
            dodecahedronVertices[4] = new Vector3f(0.0f, -1.0f, t);
            dodecahedronVertices[5] = new Vector3f(0.0f, 1.0f, t);
            dodecahedronVertices[6] = new Vector3f(0.0f, -1.0f, -t);
            dodecahedronVertices[7] = new Vector3f(0.0f, 1.0f, -t);
            dodecahedronVertices[8] = new Vector3f(t, 0.0f, -1.0f);
            dodecahedronVertices[9] = new Vector3f(t, 0.0f, 1.0f);
            dodecahedronVertices[10] = new Vector3f(-t, 0.0f, -1.0f);
            dodecahedronVertices[11] = new Vector3f(-t, 0.0f, 1.0f);
        }
        return dodecahedronVertices[index];
    }
}