package thederpgamer.superstructures.data.structures;

import api.common.GameClient;
import api.network.PacketReadBuffer;
import com.bulletphysics.linearmath.Transform;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.forms.Mesh;
import thederpgamer.superstructures.graphics.mesh.DysonSphereMultiMesh;
import thederpgamer.superstructures.utils.DysonSphereUtils;

import javax.vecmath.Vector3f;
import java.io.IOException;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereData extends SuperStructureData implements Drawable {

	public DysonSphereMultiMesh mesh;
	public Transform starTransform;
	public Vector3i centerSector;
	public StellarSystem system;

	public float updateTimer;
	public boolean initialized;

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
		system = GameClient.getClientState().getCurrentClientSystem();
		centerSector = system.getAbsoluteSectorPos(system.getIndex(new Vector3i(0, 0, 0)), new Vector3i());
		mesh = DysonSphereUtils.createMultiMesh(this);
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
		return !DysonSphereUtils.isClientInDrawRange(20000);
	}
}