package thederpgamer.superstructures.data.structures;

import api.common.GameClient;
import api.common.GameServer;
import api.network.PacketReadBuffer;
import com.bulletphysics.linearmath.Transform;
import org.lwjgl.opengl.GL11;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.graphicsengine.core.DrawableScene;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.shader.Shader;
import org.schema.schine.graphicsengine.shader.ShaderLibrary;
import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.modules.dysonsphere.DysonSphereEmptyModuleData;
import thederpgamer.superstructures.manager.ResourceManager;
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

	public Mesh frameMesh;
	public final Mesh[] modulesMeshes = new Mesh[12];
	public Transform starTransform = new Transform();
	public Vector3i centerSector = new Vector3i();

	public float updateTimer;
	public boolean initialized;

	public DysonSphereData(Vector3i sunPos, SegmentPiece segmentPiece) {
		super(sunPos, segmentPiece, 12);
		for(int i = 0; i < modules.length; i ++) modules[i] = new DysonSphereEmptyModuleData(this);
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
		frameMesh.setScale(scale);
		frameMesh.setTransform(starTransform);
		for(Mesh mesh : modulesMeshes) {
			if(mesh != null) {
				mesh.setScale(scale);
				mesh.setTransform(starTransform);
			}
		}
		updateTimer = 100.0f;
	}

	@Override
	public void onInit() {
		if(segmentController.isOnServer()) return;
		starTransform.setIdentity();
		GameClient.getClientController().getClientChannel().getGalaxyManagerClient().getSystemOnClient(sector).getSunSectorPosAbs(GameClient.getClientState().getCurrentGalaxy(), centerSector);
		frameMesh = ResourceManager.getMesh("dyson_sphere_frame");
		frameMesh.onInit();
		for(int i = 0; i < 12; i ++) {
//			modulesMeshes[i] = ResourceManager.getMesh("dyson_sphere_empty_module_" + i);
			modulesMeshes[i] = ResourceManager.getMesh("dyson_sphere_empty_module_0");
			modulesMeshes[i].onInit();
		}
		initialized = true;
	}

	@Override
	public void draw() {
		if(!initialized) onInit();
		try {
			GlUtil.glEnable(GL11.GL_BLEND);
			GlUtil.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlUtil.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

			ShaderLibrary.shardShader.setShaderInterface(this);
			ShaderLibrary.shardShader.load();
			frameMesh.draw();
			ShaderLibrary.shardShader.unload();

			for(int i = 0; i < 12; i++) {
				if(modulesMeshes[i] != null && modules[i] != null) {
					if(modules[i].getStatus() != StructureModuleData.NONE) {
						ShaderLibrary.scanlineShader.setShaderInterface(this);
						ShaderLibrary.scanlineShader.load();
					} else {
						ShaderLibrary.shardShader.setShaderInterface(this);
						ShaderLibrary.shardShader.load();
					}
					modulesMeshes[i].draw();
					if(modules[i].getStatus() != StructureModuleData.NONE) ShaderLibrary.scanlineShader.unload();
					else ShaderLibrary.shardShader.unload();
				}
			}

			GlUtil.glDisable(GL11.GL_BLEND);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void cleanUp() {
		if(frameMesh != null) frameMesh.cleanUp();
		for(Mesh mesh : modulesMeshes) if(mesh != null) mesh.cleanUp();
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
			if(segmentController.isOnServer()) return SunType.getFromSystem(GameServer.getServerState().getUniverse().getGalaxyFromSystemPos(system), system);
			else return SunType.getFromSystem(GameClient.getClientState().getCurrentGalaxy(), system); //Todo: Probably want to get a specific galaxy rather than just the current one.
		} catch(Exception exception) {
			exception.printStackTrace();
			return SunType.VOID;
		}
	}
}