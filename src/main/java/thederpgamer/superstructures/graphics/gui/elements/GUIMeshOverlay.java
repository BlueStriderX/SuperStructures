package thederpgamer.superstructures.graphics.gui.elements;

import org.lwjgl.opengl.GL11;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.AbstractSceneNode;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.input.InputState;
import thederpgamer.superstructures.graphics.mesh.MultiMesh;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 2.0 - [09/28/2021]
 */
public class GUIMeshOverlay extends GUIElement {

    public AbstractSceneNode mesh;
    private final GUIElement panel;
    private final float displayScale;
    public boolean drawMesh;
    private boolean initialized;

    public GUIMeshOverlay(InputState inputState, Mesh mesh, GUIElement panel, float scale) {
        super(inputState);
        this.mesh = mesh;
        this.panel = panel;
        displayScale = scale;
    }

    public GUIMeshOverlay(InputState inputState, MultiMesh mesh, GUIElement panel, float scale) {
        super(inputState);
        this.mesh = mesh;
        this.panel = panel;
        displayScale = scale;
    }

    @Override
    public void onInit() {
        mesh.onInit();
        initialized = true;
    }

    @Override
    public void draw() {
        if(!initialized) onInit();
        if(drawMesh) {
            GUIElement.enableOrthogonal3d();

            GlUtil.glPushMatrix();
            GlUtil.translateModelview(panel.getPos().x + (panel.getWidth() / 2.0f), panel.getPos().y + (panel.getHeight() / 1.5f), 0);
            GlUtil.scaleModelview(displayScale, -displayScale, displayScale);

            GlUtil.glDepthMask(true);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GlUtil.glEnable(GL11.GL_DEPTH_TEST);
            GlUtil.glEnable(GL11.GL_NORMALIZE);
            GlUtil.glDisable(GL11.GL_LIGHTING);
            GlUtil.glDisable(GL11.GL_CULL_FACE);
            GlUtil.glEnable(GL11.GL_BLEND);

            GlUtil.glPushMatrix();
            mesh.transform();
            mesh.draw();
            GlUtil.glPopMatrix();

            GlUtil.glEnable(GL11.GL_LIGHTING);
            GlUtil.glDisable(GL11.GL_NORMALIZE);
            GlUtil.glEnable(GL11.GL_DEPTH_TEST);
            GlUtil.glPopMatrix();
            GUIElement.disableOrthogonal();
        }
    }

    @Override
    public void cleanUp() {
        mesh.cleanUp();
    }

    @Override
    public float getWidth() {
        return 256;
    }

    @Override
    public float getHeight() {
        return 256;
    }
}