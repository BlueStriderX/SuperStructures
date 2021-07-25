package thederpgamer.superstructures.graphics.gui.elements;

import api.DebugFile;
import com.bulletphysics.linearmath.Transform;
import org.schema.game.common.data.Dodecahedron;
import org.schema.schine.graphicsengine.core.DrawableScene;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.shader.Shader;
import org.schema.schine.graphicsengine.shader.ShaderLibrary;
import org.schema.schine.graphicsengine.shader.Shaderable;
import org.schema.schine.input.InputState;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/24/2021
 */
public class GUIDodecahedronMeshDrawer extends GUIAncor implements Shaderable {

    private Dodecahedron dodecahedron;
    private GUIElement dependent;
    public Vector3f rotation;
    private float time = 0;

    public GUIDodecahedronMeshDrawer(InputState state, float radius, GUIElement dependent) {
        super(state);
        this.dodecahedron = new Dodecahedron(radius);
        this.dodecahedron.create();
        this.dependent = dependent;
        this.rotation = new Vector3f();
    }

    @Override
    public void draw() {
        if(dependent.isOnScreen() && dependent.isActive()) {
            ShaderLibrary.scanlineShader.setShaderInterface(this);
            ShaderLibrary.scanlineShader.load();
            for(int i = 0; i < 12; i ++) {
                Transform transform = dodecahedron.getTransform(i, new Transform(), -0.5f, 0.5f);
                transform.inverse();
                Vector3f[] polygons = dodecahedron.getPolygon(i);
                Matrix3f matrix = new Matrix3f();
                matrix.rotX(rotation.x);
                matrix.rotY(rotation.y);
                matrix.rotZ(rotation.z);
                if(rotation.length() > 0) DebugFile.log("[DEBUG]: " + rotation.toString());
                for(Vector3f p : polygons) {
                    transform.transform(p);
                    p.y = 0;
                    matrix.transform(p);
                }
                System.arraycopy(polygons, 0, dodecahedron.poly[i], 0, polygons.length);
            }
            GlUtil.glColor4f(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
            dodecahedron.draw();
            ShaderLibrary.scanlineShader.unload();
        } else dodecahedron.cleanUp();
    }

    @Override
    public void update(Timer timer) {
        time += timer.getDelta() * 2f;
    }

    @Override
    public void updateShader(DrawableScene scene) {

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
