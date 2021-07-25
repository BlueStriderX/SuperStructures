package thederpgamer.superstructures.graphics.gui.elements;

import com.bulletphysics.linearmath.Transform;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIMainWindow;
import org.schema.schine.graphicsengine.texture.TextureLoader;
import org.schema.schine.input.InputState;
import javax.vecmath.Vector3f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/24/2021
 */
public class GUIMeshOverlay extends GUIOverlay {

    private final Mesh mesh;
    private final GUIMainWindow window;
    private int displayWidth;
    private int displayHeight;
    private float displayScale;
    private Vector3f rotation;
    private final float sensitivity;
    private boolean initialized = false;

    public GUIMeshOverlay(InputState state, Mesh mesh, GUIMainWindow window, int displayWidth, int displayHeight, float displayScale) {
        super(new Sprite(displayWidth, displayHeight), state);
        this.mesh = mesh;
        this.window = window;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.displayScale = displayScale;
        this.rotation = new Vector3f();
        this.sensitivity = (float) EngineSettings.M_MOUSE_SENSITIVITY.getCurrentState();
    }

    @Override
    public void onInit() {
        sprite.getMaterial().setTexture(TextureLoader.getEmptyTexture(displayWidth, displayHeight));
        sprite.setHeight(displayWidth);
        sprite.setWidth(displayHeight);
        sprite.onInit();
        rotation = new Vector3f();
        initialized = true;
    }

    @Override
    public void draw() {
        if(!initialized) onInit();
        if(window.getSelectedTab() == 0) {
            setMouseUpdateEnabled(true);
            if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                rotation.x += (-Mouse.getDX() * sensitivity) * 10;
                rotation.y += (Mouse.getDY() * sensitivity) * 10;
            }

            Transform transform = new Transform();
            transform.setIdentity();
            transform.origin.x = window.getPos().x + (window.getInnerWidth() / 4.0f);
            transform.origin.y = window.getPos().y + (window.getInnerHeigth() / 2.0f);

            GlUtil.glPushMatrix();
            GlUtil.glMultMatrix(transform);
            GlUtil.scaleModelview(displayScale, -displayScale, displayScale);
            GlUtil.rotateModelview(rotation.x, rotation.y, rotation.z, 0.0f);
            GlUtil.glEnable(GL11.GL_BLEND);
            GlUtil.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlUtil.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlUtil.glEnable(GL11.GL_COLOR_MATERIAL);
            mesh.draw();

            GlUtil.glPopMatrix();
            GlUtil.glDisable(GL11.GL_COLOR_MATERIAL);
            GlUtil.glDisable(GL11.GL_BLEND);
        } else {
            mesh.cleanUp();
            rotation.x = 0.0f;
            rotation.y = 0.0f;
            setMouseUpdateEnabled(false);
        }
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
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
}
