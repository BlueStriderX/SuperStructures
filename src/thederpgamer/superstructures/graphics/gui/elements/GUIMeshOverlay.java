package thederpgamer.superstructures.graphics.gui.elements;

import com.bulletphysics.linearmath.Transform;
import org.lwjgl.opengl.GL11;
import org.schema.game.client.data.GameClientState;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.DrawableScene;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.shader.Shader;
import org.schema.schine.graphicsengine.shader.ShaderLibrary;
import org.schema.schine.graphicsengine.shader.Shaderable;
import org.schema.schine.graphicsengine.texture.TextureLoader;
import org.schema.schine.input.InputState;
import javax.vecmath.Vector3f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/24/2021
 */
public class GUIMeshOverlay extends GUIOverlay implements Shaderable {

    private final Mesh mesh;
    private final GUIElement dependent;
    private int displayWidth;
    private int spriteHeight;
    private float displayScale;
    private float time;
    private boolean initialized = false;
    private Vector3f rotation;

    public GUIMeshOverlay(InputState state, Mesh mesh, GUIElement dependent, int displayWidth, int displayHeight, float displayScale) {
        super(new Sprite(displayWidth, displayHeight), state);
        this.mesh = mesh;
        this.dependent = dependent;
        this.displayWidth = displayWidth;
        this.spriteHeight = displayHeight;
        this.displayScale = displayScale;
        this.rotation = new Vector3f();
    }

    @Override
    public void onInit() {
        sprite.getMaterial().setTexture(TextureLoader.getEmptyTexture(displayWidth, spriteHeight));
        sprite.setHeight(displayWidth);
        sprite.setWidth(spriteHeight);
        sprite.onInit();
        rotation = new Vector3f();
        initialized = true;
    }

    @Override
    public void draw() {
        if(!initialized) onInit();
        if(dependent.isActive() && dependent.isOnScreen()) {
            Transform transform = new Transform();
            transform.setIdentity();

            GlUtil.glEnable(GL11.GL_DEPTH_TEST);
            GlUtil.glDepthMask(true);

            ShaderLibrary.scanlineShader.setShaderInterface(this);
            ShaderLibrary.scanlineShader.load();

            Vector3f menuPos = Controller.getCamera().getPos();
            Vector3f posOnScreen = ((GameClientState) getState()).getScene().getWorldToScreenConverter().convert(menuPos, new Vector3f(), false);
            transform.origin.set(posOnScreen);
            transform.basis.rotX(rotation.x);
            transform.basis.rotY(rotation.y);
            transform.basis.rotZ(rotation.z);

            mesh.loadVBO(true);
            GlUtil.glPushMatrix();
            GlUtil.glMultMatrix(transform);

            GlUtil.scaleModelview(displayScale, displayScale, displayScale);
            mesh.renderVBO();
            GlUtil.glPopMatrix();

            mesh.unloadVBO(true);
            ShaderLibrary.scanlineShader.unload();
        } else mesh.cleanUp();
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

    public void rotateMesh(Vector3f rotation) {
        rotateMesh(rotation.x, rotation.y, rotation.z);
    }

    public void rotateMesh(float xRotation, float yRotation, float zRotation) {
        rotation.set(xRotation, yRotation, zRotation);
    }

    @Override
    public void update(Timer timer) {
        time += timer.getDelta() * 2.0f;
    }

    @Override
    public void updateShader(DrawableScene drawableScene) {

    }

    @Override
    public void updateShaderParameters(Shader shader) {
        GlUtil.updateShaderFloat(shader, "uTime", time);
        GlUtil.updateShaderVector2f(shader, "uResolution", 20, 1000);
        GlUtil.updateShaderInt(shader, "uDiffuseTexture", 0);
    }

    @Override
    public void onExit() {

    }
}
