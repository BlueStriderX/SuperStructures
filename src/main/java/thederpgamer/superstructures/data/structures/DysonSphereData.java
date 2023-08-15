package thederpgamer.superstructures.data.structures;

import api.common.GameClient;
import api.common.GameCommon;
import api.common.GameServer;
import api.network.PacketReadBuffer;
import com.bulletphysics.linearmath.Transform;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.graphicsengine.core.DrawableScene;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.shader.Shader;
import thederpgamer.superstructures.graphics.mesh.DysonSphereMultiMesh;
import thederpgamer.superstructures.utils.StructureUtils;

import javax.vecmath.Vector3f;
import java.io.IOException;

/**
 * Data class for Dyson Spheres.
 *
 * @author TheDerpGamer
 */
public class DysonSphereData extends SuperStructureData {

	private static final int sectorSize = (int) ServerConfig.SECTOR_SIZE.getCurrentState();
	private static final int sectorSizeHalf = sectorSize / 2;
	private static final Vector3f scale = new Vector3f(sectorSizeHalf, sectorSizeHalf, sectorSizeHalf);

	public DysonSphereMultiMesh mesh;
	public Transform starTransform;
	public Vector3i centerSector;

	public float updateTimer;
	public boolean initialized;

	public DysonSphereData(Vector3i sunPos, SegmentPiece segmentPiece) {
		super(sunPos, segmentPiece, 12);
	}

	public DysonSphereData(PacketReadBuffer packetReadBuffer) throws IOException {
		super(packetReadBuffer);
	}

	/**
	 * Updates the mesh by setting the scale and transform.
	 * If the mesh is not initialized, calls onInit().
	 * <br/>
	 * The method first checks if the mesh is initialized,
	 * and if not, it calls the onInit() method.
	 * <br/>
	 * It then retrieves the current sector using the
	 * segmentController.getSector() method and compares
	 * it with the center sector.
	 * If they are equal, it sets the starTransform origin to (0, 0, 0),
	 * otherwise it calculates the difference between
	 * the center sector and the current sector and sets
	 * the starTransform origin to the difference.
	 * <br/>
	 * A scale vector of (500.0f, 500.0f, 500.0f) is
	 * created and used to set the scale of the mesh's
	 * frame and transform.
	 * It then iterates over the
	 * mesh.meshArray and for each mesh that is not null,
	 * sets the scale and transform using the scale vector
	 * and starTransform respectively.
	 * <br/>
	 * Finally, the updateTimer is set to 100.0f.
	 */
	public void updateMesh() {
		if(!initialized) onInit();
		Vector3i currentSector = segmentController.getSector(new Vector3i());
		if(currentSector.equals(centerSector)) starTransform.origin.set(0, 0, 0);
		else {
			Vector3f difference = new Vector3f(centerSector.x - currentSector.x, centerSector.y - currentSector.y, centerSector.z - currentSector.z);
			starTransform.origin.set(difference);
		}
		mesh.frame.setScale(scale);
		mesh.frame.setTransform(starTransform);
		for(Mesh mesh : mesh.meshArray) {
			if(mesh != null) {
				mesh.setScale(scale);
				mesh.setTransform(starTransform);
			}
		}
		updateTimer = 100.0f;
	}

	@Override
	public void onInit() {
		starTransform = new Transform();
		starTransform.setIdentity();
		system = GameClient.getClientState().getCurrentClientSystem().getPos();
		centerSector = GameClient.getClientState().getCurrentClientSystem().getSunSectorPosAbs(GameClient.getClientState().getCurrentGalaxy(), new Vector3i());
		mesh = StructureUtils.createMultiMesh(this);
		mesh.onInit();
		initialized = true;
	}

	@Override
	public void draw() {
		if(!initialized) onInit();
		mesh.draw();
	}

	@Override
	public void cleanUp() {
		if(mesh != null) mesh.cleanUp();
	}

	@Override
	public boolean isInvisible() {
		return !StructureUtils.isClientInDrawRange(20000);
	}

	@Override
	public void onExit() {

	}

	@Override
	public void updateShader(DrawableScene drawableScene) {

	}

	@Override
	public void updateShaderParameters(Shader shader) {

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