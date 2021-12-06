package thederpgamer.superstructures.graphics.mesh;

import org.lwjgl.input.Mouse;
import org.schema.common.util.linAlg.Quat4fTools;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.Mesh;

import javax.vecmath.Matrix3f;
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
    
    private final Matrix3f horizontalMatrix = new Matrix3f();
    private final Matrix3f verticalMatrix = new Matrix3f();

    private final Quat4f horizontalRotation = new Quat4f();
    private final Quat4f verticalRotation = new Quat4f();
    private final Quat4f finalRotation = new Quat4f();
    private final Vector4f finalVectorRotation = new Vector4f();

    private boolean initialized = false;

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
        if(Mouse.isButtonDown(1)) {
            mouseRotation.x += lastMousePos.x - Mouse.getEventX();
            mouseRotation.y += lastMousePos.y - Mouse.getEventY();
        }
        lastMousePos.x = Mouse.getEventX();
        lastMousePos.y = Mouse.getEventY();

        horizontalMatrix.setIdentity();
        horizontalMatrix.rotY(-mouseRotation.x * 0.0065f);

        verticalMatrix.setIdentity();
        verticalMatrix.rotX(mouseRotation.y * 0.0065f);

        Quat4fTools.set(horizontalMatrix, horizontalRotation);
        //Quat4fTools.set(verticalMatrix, verticalRotation);

        //finalRotation.set(0.0f, 0.0f, 0.0f, 0.0f);
        //finalRotation.mul(horizontalRotation, verticalRotation);
        finalVectorRotation.set(horizontalRotation);
        setQuatRot(finalVectorRotation);

        GlUtil.enableBlend(true);
        if(frame != null) frame.draw();

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
}