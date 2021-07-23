package thederpgamer.superstructures.data.shapes;

import api.common.GameClient;
import com.bulletphysics.linearmath.Transform;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Matrix4f;
import org.schema.game.client.data.GameClientState;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.modules.dysonsphere.DysonSphereEmptyModuleData;
import thederpgamer.superstructures.data.structures.SuperStructureData;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.nio.FloatBuffer;
import java.util.Random;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class Shape3D extends GUIElement implements Drawable {

    public static final int NONE = 0;
    public static final int WIREFRAME = 1;
    public static final int GUI = 2;

    private String name;
    private Vector3f[] vertices;
    private Vector3f[][] edges;
    private Vector3f[][] faces;

    private int drawMode = NONE;
    private Transform transform;
    private float scale;
    private Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    static FloatBuffer bb = BufferUtils.createFloatBuffer(16);
    private static float[] glMat = new float[16];

    private SuperStructureData structureData;

    public Shape3D(String name, Vector3f[] vertices, Vector3f[][] edges, Vector3f[][] faces) {
        super(GameClient.getClientState());
        this.name = name;
        this.vertices = vertices;
        this.edges = edges;
        this.faces = faces;
    }

    public SuperStructureData getStructureData() {
        return structureData;
    }

    public void setStructureData(SuperStructureData structureData) {
        this.structureData = structureData;
    }

    public String getName() {
        return name;
    }

    public Vector3f[] getVertices() {
        return vertices;
    }

    public Vector3f[][] getEdges() {
        return edges;
    }

    public Vector3f[][] getFaces() {
        return faces;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public float getShapeScale() {
        return scale;
    }

    public void setShapeScale(float scale) {
        this.scale = scale;
    }

    public int getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(int drawMode) {
        this.drawMode = drawMode;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    @Override
    public void onInit() {
        if(scale != 0 && transform != null) {
            for(Vector3f vertex : vertices) vertex.scale(scale);
            for(Vector3f[] edgeArray : edges) for(Vector3f edge : edgeArray) edge.scale(scale);
            for(Vector3f[] faceArray : faces) for(Vector3f face : faceArray) face.scale(scale);
        }
    }

    @Override
    public void draw() {
        if(scale != 0 && transform != null) {
            switch(drawMode) {
                case NONE:
                    break;
                case WIREFRAME:
                    transform.setFromOpenGLMatrix(glMat);
                    bb.rewind();
                    bb.put(glMat);
                    bb.rewind();
                    GL11.glMultMatrix(bb);

                    GlUtil.glPushMatrix();
                    GlUtil.glDisable(GL11.GL_TEXTURE_2D);
                    GlUtil.glEnable(GL11.GL_COLOR_MATERIAL);
                    GlUtil.glDisable(GL11.GL_LIGHTING);
                    GlUtil.glColor4f(color);
                    for(Vector3f[] edge : edges) {
                        GL11.glBegin(GL11.GL_LINES);
                        GL11.glVertex3f(edge[0].x, edge[0].y, edge[0].z);
                        GL11.glVertex3f(edge[1].x, edge[1].y, edge[1].z);
                        GL11.glEnd();
                    }
                    GlUtil.glDisable(GL11.GL_COLOR_MATERIAL);
                    GlUtil.glEnable(GL11.GL_LIGHTING);
                    GlUtil.glEnable(GL11.GL_TEXTURE_2D);
                    GlUtil.glPopMatrix();
                    break;
                case GUI:
                    Matrix4f modelviewMatrix = Controller.modelviewMatrix;
                    bb.rewind();
                    modelviewMatrix.store(bb);
                    bb.rewind();
                    bb.get(glMat);
                    transform.setFromOpenGLMatrix(glMat);
                    transform.origin.set(0, 0, 0);
                    GlUtil.glPushMatrix();
                    setInside(false);
                    transform();

                    GlUtil.glDisable(GL11.GL_DEPTH_TEST);
                    GlUtil.glEnable(GL11.GL_TEXTURE_2D);
                    GlUtil.glEnable(GL11.GL_LIGHTING);
                    GlUtil.glEnable(GL11.GL_BLEND);
                    GlUtil.glDepthMask(false);
                    GUIElement.enableOrthogonal3d();
                    GlUtil.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
                    GlUtil.glMultMatrix(transform);
                    GlUtil.glPushMatrix();
                    int x = 0;
                    int y = 0;
                    GlUtil.translateModelview(x, y, 100);
                    GlUtil.scaleModelview(scale, -scale, scale);
                    GlUtil.glMultMatrix(transform);
                    Transform m = new Transform();
                    m.setIdentity();
                    if(((GameClientState) getState()).getCurrentPlayerObject() != null) m.basis.set(((GameClientState) getState()).getCurrentPlayerObject().getWorldTransform().basis);
                    GlUtil.glMultMatrix(m);

                    GlUtil.glDisable(GL11.GL_TEXTURE_2D);
                    GlUtil.glEnable(GL11.GL_COLOR_MATERIAL);
                    GlUtil.glDisable(GL11.GL_LIGHTING);
                    GlUtil.glColor4f(color);
                    for(Vector3f[] edge : edges) {
                        GL11.glBegin(GL11.GL_LINES);
                        GL11.glVertex3f(edge[0].x, edge[0].y, edge[0].z);
                        GL11.glVertex3f(edge[1].x, edge[1].y, edge[1].z);
                        GL11.glEnd();
                    }
                    GlUtil.glDisable(GL11.GL_COLOR_MATERIAL);
                    GlUtil.glEnable(GL11.GL_LIGHTING);
                    GlUtil.glEnable(GL11.GL_TEXTURE_2D);
                    GlUtil.glPopMatrix();

                    GUIElement.disableOrthogonal();
                    GlUtil.glEnable(GL11.GL_LIGHTING);
                    GlUtil.glDisable(GL11.GL_NORMALIZE);
                    GlUtil.glEnable(GL11.GL_DEPTH_TEST);
                    GlUtil.glDepthMask(true);
                    GlUtil.glPopMatrix();
                    break;
            }
        }
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    private Vector4f getRandomColor() {
        Random rand = new Random();
        float x = rand.nextFloat();
        float y = rand.nextFloat();
        float z = rand.nextFloat();
        return new Vector4f(x, y, z, 0.35f);
    }

    private Vector4f getFaceColor(int index) {
        if(structureData != null && structureData.modules != null) {
            StructureModuleData moduleData = structureData.modules[index];
            if(moduleData != null) {
                if(moduleData instanceof DysonSphereEmptyModuleData) return new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
                switch(moduleData.status) {
                    case StructureModuleData.NONE: return new Vector4f(0.0f, 0.0f, 1.0f, 1.0f);
                    case StructureModuleData.CONSTRUCTION: return new Vector4f(1.0f, 1.0f, 0.0f, 1.0f);
                    case StructureModuleData.UPGRADE: return new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);
                    case StructureModuleData.REPAIR: return new Vector4f(1.0f, 0.8f, 0.0f, 1.0f);
                }
            }
        }
        return new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public float getWidth() {
        return scale;
    }

    @Override
    public float getHeight() {
        return scale;
    }
}