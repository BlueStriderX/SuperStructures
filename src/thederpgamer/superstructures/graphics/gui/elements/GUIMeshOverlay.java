package thederpgamer.superstructures.graphics.gui.elements;

import com.bulletphysics.linearmath.Transform;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Matrix4f;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.texture.TextureLoader;
import org.schema.schine.input.InputState;

import javax.vecmath.Vector3f;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/24/2021
 */
public class GUIMeshOverlay extends GUIOverlay {

    private Mesh mesh;
    private GUIElement dependent;
    private int displayWidth;
    private int spriteHeight;
    private float displayScale;
    private boolean initialized = false;

    private Transform transform;
    private FloatBuffer fb = BufferUtils.createFloatBuffer(16);
    private float[] ff = new float[16];

    public GUIMeshOverlay(InputState state, Mesh mesh, GUIElement dependent, int displayWidth, int displayHeight, float displayScale) {
        super(new Sprite(displayWidth, displayHeight), state);
        this.mesh = mesh;
        this.dependent = dependent;
        this.displayWidth = displayWidth;
        this.spriteHeight = displayHeight;
        this.displayScale = displayScale;
    }

    @Override
    public void onInit() {
        sprite.getMaterial().setTexture(TextureLoader.getEmptyTexture(displayWidth, spriteHeight));
        sprite.setHeight(displayWidth);
        sprite.setWidth(spriteHeight);
        sprite.onInit();
        transform = new Transform();
        initialized = true;
    }

    @Override
    public void draw() {
        if(!initialized) onInit();
        if(dependent.isActive() && dependent.isOnScreen()) {
            super.draw();
            Matrix4f modelViewMatrix = Controller.modelviewMatrix;
            fb.rewind();
            modelViewMatrix.store(fb);
            fb.rewind();
            fb.get(ff);
            transform.setFromOpenGLMatrix(ff);
            transform.origin.set(0, 0, 0);

            GUIElement.enableOrthogonal3d();
            GlUtil.glPushMatrix();
            GlUtil.translateModelview(getPos().x, getPos().y, 0);
            GlUtil.scaleModelview(displayScale, -displayScale, displayScale);
            GlUtil.glMultMatrix(transform);
            GlUtil.glDisable(GL11.GL_LIGHTING);
            GlUtil.glDisable(GL11.GL_DEPTH_TEST);
            GlUtil.glEnable(GL11.GL_NORMALIZE);
            GlUtil.glDisable(GL11.GL_CULL_FACE);
            GlUtil.glEnable(GL11.GL_DEPTH_TEST);

            try {
                setWireFrameField();
            } catch(Exception ignored) { }

            mesh.draw();
            mesh.getMaterial().attach(0);
            GlUtil.glDisable(GL11.GL_LIGHTING);
            mesh.drawVBO();
            mesh.getMaterial().detach();

            GlUtil.glPopMatrix();
            GUIElement.disableOrthogonal();
            GlUtil.glEnable(GL11.GL_LIGHTING);
            GlUtil.glDisable(GL11.GL_NORMALIZE);
            GlUtil.glEnable(GL11.GL_DEPTH_TEST);
            GlUtil.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public void setSpriteHeight(int spriteHeight) {
        this.spriteHeight = spriteHeight;
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

    public void moveMesh(Vector3f position) {
        transform.origin.set(position);
    }

    public void moveMesh(float xPosition, float yPosition, float zPosition) {
        moveMesh(new Vector3f(xPosition, yPosition, zPosition));
    }

    public void rotateMesh(Vector3f rotation) {
        rotateMesh(rotation.x, rotation.y, rotation.z);
    }

    public void rotateMesh(float xRotation, float yRotation, float zRotation) {
        transform.basis.rotX(xRotation);
        transform.basis.rotY(yRotation);
        transform.basis.rotZ(zRotation);
    }

    private void setWireFrameField() throws NoSuchFieldException, IllegalAccessException {
        Field wireFrameField = mesh.getClass().getDeclaredField("drawingWireframe");
        wireFrameField.setAccessible(true);
        wireFrameField.setBoolean(mesh, true);
    }
}
