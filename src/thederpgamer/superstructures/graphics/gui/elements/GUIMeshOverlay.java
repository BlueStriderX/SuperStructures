package thederpgamer.superstructures.graphics.gui.elements;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.schema.common.FastMath;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.MouseButton;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIMainWindow;
import org.schema.schine.graphicsengine.texture.Texture;
import org.schema.schine.input.InputState;
import thederpgamer.superstructures.manager.ResourceManager;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/24/2021
 */
public class GUIMeshOverlay extends GUIElement {

    private final Mesh mesh;
    private final GUIMainWindow window;
    private final float width;
    private final float height;
    private float displayScale;
    private boolean initialized = false;
    private final float sensitivity;

    private Vector3f vCross = new Vector3f();
    private Vector3f axis = new Vector3f();
    private Vector2f mouse = new Vector2f();
    private Vector2f mouseSum = new Vector2f();
    private Quat4f result = new Quat4f();
    private Quat4f rotMulView = new Quat4f();
    private Quat4f rotConj = new Quat4f();
    private Quat4f newRotation = new Quat4f();
    private Quat4f totalRotation = new Quat4f(0, 0, 0, 1);
    private Quat4f tmpQuat = new Quat4f();
    private static GUIMeshOverlay instance;

    public GUIMeshOverlay(InputState state, Mesh mesh, GUIMainWindow window, float width, float height, float displayScale) {
        super(state);
        this.mesh = mesh;
        this.window = window;
        this.width = width;
        this.height = height;
        this.displayScale = displayScale;
        this.sensitivity = (float) EngineSettings.M_MOUSE_SENSITIVITY.getCurrentState();
    }

    @Override
    public void onInit() {
        mesh.getMaterial().setTexture(new Texture(GL11.GL_TEXTURE_2D, ResourceManager.getTexture("dyson-sphere-frame-texture").getTextureId(), "dyson-sphere-frame-texture"));
        initialized = true;
        instance = this;
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public void draw() {
        if(!initialized) onInit();
        if(window.getSelectedTab() == 0) {
            GUIElement.enableOrthogonal3d();
            GlUtil.glPushMatrix();
            GlUtil.translateModelview(window.getPos().x + (window.getInnerWidth() / 2.0f), window.getPos().y + (window.getInnerHeigth() / 1.5f), 0);
            GlUtil.scaleModelview(displayScale, -displayScale, displayScale);

            GlUtil.glDepthMask(true);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GlUtil.glEnable(GL11.GL_DEPTH_TEST);
            GlUtil.glEnable(GL11.GL_NORMALIZE);
            GlUtil.glDisable(GL11.GL_LIGHTING);
            GlUtil.glDisable(GL11.GL_CULL_FACE);

            GlUtil.glPushMatrix();
            mesh.transform();
            mesh.draw();

            GlUtil.glPopMatrix();
            GUIElement.disableOrthogonal();

            GlUtil.glEnable(GL11.GL_LIGHTING);
            GlUtil.glDisable(GL11.GL_NORMALIZE);
            GlUtil.glEnable(GL11.GL_DEPTH_TEST);
            GlUtil.glPopMatrix();
        } else mesh.cleanUp();
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public float getDisplayScale() {
        return displayScale;
    }

    public void setDisplayScale(float displayScale) {
        this.displayScale = displayScale;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public static void rotateMesh(MouseEvent mouseEvent) {
        if(instance != null && instance.window != null && instance.window.getSelectedTab() == 0 && Mouse.isButtonDown(MouseButton.RIGHT.button)) instance.updateRotation(mouseEvent);
    }

    private void updateRotation(MouseEvent mouseEvent) {
        mouse.x = -mouseEvent.dx * sensitivity;
        mouse.y = (EngineSettings.S_MOUSE_ALL_INVERT.isOn() ? -mouseEvent.dy : mouseEvent.dy) * sensitivity;

        mouseSum.add(mouse);

        if(mouseSum.y > FastMath.HALF_PI) {
            mouseSum.y -= mouse.y;
            mouse.y = FastMath.HALF_PI - mouseSum.y;
            mouseSum.y = FastMath.HALF_PI;
        }
        if(mouseSum.y < -FastMath.HALF_PI) {
            mouseSum.y -= mouse.y;
            mouse.y = -(FastMath.HALF_PI - Math.abs(mouseSum.y));
            mouseSum.y = -FastMath.HALF_PI;
        }

        Vector3f forwardVector = new Vector3f(GlUtil.getForwardVector(new Vector3f(), mesh.getTransform()));
        Vector3f upVector = new Vector3f(GlUtil.getUpVector(new Vector3f(), mesh.getTransform()));
        Vector3f rightVector = new Vector3f(GlUtil.getRightVector(new Vector3f(), mesh.getTransform()));
        if(mouse.y != 0) {
            vCross.cross(GlUtil.getForwardVector(new Vector3f(), mesh.getTransform()), GlUtil.getUpVector(new Vector3f(), mesh.getTransform()));
            axis.set(vCross);
            axis.normalize();
            rotMesh(mouse.y, axis, forwardVector, upVector, rightVector);
        }
        rotMesh(mouse.x, new Vector3f(0, 1, 0), forwardVector, upVector, rightVector);

        GlUtil.setForwardVector(forwardVector, mesh.getTransform());
        GlUtil.setUpVector(upVector, mesh.getTransform());
        GlUtil.setRightVector(rightVector, mesh.getTransform());
    }

    private void rotMesh(float angle, Vector3f axis, Vector3f forward, Vector3f up, Vector3f right) {
        newRotation.x = axis.x * FastMath.sin(angle / 2);
        newRotation.y = axis.y * FastMath.sin(angle / 2);
        newRotation.z = axis.z * FastMath.sin(angle / 2);
        newRotation.w = FastMath.cos(angle / 2);
        rotConj.conjugate(newRotation);
        rotate(forward);
        rotate(up);
        rotate(right);
        totalRotation.mul(newRotation);
    }

    private void rotate(Vector3f v) {
        tmpQuat.set(v.x, v.y, v.z, 0);
        rotMulView.mul(newRotation, tmpQuat);
        result.mul(rotMulView, rotConj);
        v.set(result.x, result.y, result.z);
    }
}
