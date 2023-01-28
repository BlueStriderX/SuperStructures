package thederpgamer.superstructures.graphics.mesh;

import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.Mesh;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [09/28/2021]
 */
public class DysonSphereMultiMesh extends MultiMesh {

	private final Vector3f lastMousePos = new Vector3f();
	private final Vector3f mouseRotation = new Vector3f();

	private final Quat4f currentRotation = new Quat4f();
	private final Quat4f quatRotation = new Quat4f();
	private final Vector4f vectorRotation = new Vector4f();
	private final Vector4f finalRotation = new Vector4f();

	private boolean initialized;

	public DysonSphereMultiMesh(Mesh frame, Mesh[] meshArray) {
		super(frame, meshArray);
	}

	@Override
	public void onInit() {
		super.onInit();
		initialized = true;
	}

	@Override
	public void draw() {
		if(!initialized) onInit();
        /*
        if(Mouse.isButtonDown(1)) {
            mouseRotation.x += lastMousePos.x - Mouse.getEventX();
            mouseRotation.y += lastMousePos.y - Mouse.getEventY();
        }
        lastMousePos.x = Mouse.getEventX();
        lastMousePos.y = Mouse.getEventY();

        float magnitude = (float) (Math.sqrt(mouseRotation.x * mouseRotation.x + mouseRotation.y * mouseRotation.y) * 0.01f);
        vectorRotation.set(mouseRotation.y, -mouseRotation.x, 0.0f, magnitude);
        quatRotation.set(vectorRotation); //Convert to Quat4f
        currentRotation.set(getRot4()); //Get current rotation

        quatRotation.mul(currentRotation); //Apply current rotation
        finalRotation.set(quatRotation); //Convert to Vector4f
        setQuatRot(finalRotation); //Set rotation
         */
		GlUtil.enableBlend(true);
		if(frame != null) frame.draw();
		//Todo: Separate draw function for gui version
		for(int i = 0; i < 12; i ++) {
			if(meshArray[i] != null) {
				meshArray[i].draw();
                /*
                highlightOverlays[i].transform();
                highlightOverlays[i].checkMouseInside();
                //highlightOverlays[i].checkMouseInsideWithTransform();
                if(highlightOverlays[i].isInside()) meshArray[i].setMaterial(ResourceManager.getSprite("highlight-overlay").getMaterial());
                else meshArray[i].setMaterial(ResourceManager.getSprite("regular-overlay").getMaterial());
                */
			}
		}
	}

	@Override
	public void cleanUp() {
		if(initialized) {
			if(frame != null) frame.cleanUp();
			for(Mesh mesh : meshArray) if(mesh != null) mesh.cleanUp();
		}
	}

	/**
	 * Aligns the sub mesh with the rotation of the frame relative to the dodecahedron face that the sub mesh is on.
	 * @param mesh The sub mesh to align.
	 * @param index The index of the sub mesh in the mesh array.
	 */
	@Override
	public void alignMesh(Mesh mesh, int index) {
		mesh.setPos(frame.getPos());
		mesh.setQuatRot(frame.getRot4());
		mesh.rotateBy((float) 1.5707963267948966, (float) 1.5707963267948966, (float) 1.5707963267948966); //Rotate the mesh 90 degrees on all axes
		mesh.rotateBy(0, 0, (float) Math.toRadians(72 * index)); //Rotate the mesh 72 degrees on the z axis for each index
	}
}