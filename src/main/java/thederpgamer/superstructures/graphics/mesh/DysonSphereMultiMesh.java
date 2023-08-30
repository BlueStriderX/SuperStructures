package thederpgamer.superstructures.graphics.mesh;

import org.lwjgl.opengl.GL11;
import org.schema.schine.graphicsengine.core.DrawableScene;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.shader.Shader;
import org.schema.schine.graphicsengine.shader.ShaderLibrary;
import org.schema.schine.graphicsengine.shader.Shaderable;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [09/28/2021]
 */
public class DysonSphereMultiMesh extends MultiMesh implements Shaderable {

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
		GlUtil.glEnable(GL11.GL_BLEND);
		GlUtil.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlUtil.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

		ShaderLibrary.shardShader.setShaderInterface(this);
		ShaderLibrary.shardShader.load();
		if(frame != null) frame.draw();
		ShaderLibrary.shardShader.unload();

		GlUtil.glDisable(GL11.GL_BLEND);
		for(int i = 0; i < 12; i ++) {
			if(meshArray[i] != null) {

				meshArray[i].draw();
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

	@Override
	public void onExit() {

	}

	@Override
	public void updateShader(DrawableScene drawableScene) {

	}

	@Override
	public void updateShaderParameters(Shader shader) {

	}
}