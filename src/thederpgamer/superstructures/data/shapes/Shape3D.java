package thederpgamer.superstructures.data.shapes;

import com.bulletphysics.linearmath.Transform;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.core.GlUtil;
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
public class Shape3D implements Drawable {

    public static final int NONE = 0;
    public static final int WIREFRAME = 1;

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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
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
                    transform.getOpenGLMatrix(glMat);
                    bb.rewind();
                    bb.put(glMat);
                    bb.rewind();
                    GL11.glMultMatrix(bb);

                    GlUtil.glPushMatrix();
                    GlUtil.glDisable(GL11.GL_TEXTURE_2D);
                    GlUtil.glEnable(GL11.GL_COLOR_MATERIAL);
                    GlUtil.glDisable(GL11.GL_LIGHTING);
                    int i = 0;
                    int j = 0;
                    for(Vector3f[] edge : edges) {
                        GL11.glBegin(GL11.GL_LINES);
                        GlUtil.glColor4f(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
                        //GlUtil.glColor4f(getFaceColor(j)); Todo: It doesn't seem to draw the edges in order >:(
                        GL11.glVertex3f(edge[0].x, edge[0].y, edge[0].z);
                        GL11.glVertex3f(edge[1].x, edge[1].y, edge[1].z);
                        GL11.glEnd();
                        if(i == 4) {
                            j ++;
                            i = 0;
                        } else i ++;
                    }
                    GlUtil.glDisable(GL11.GL_COLOR_MATERIAL);
                    GlUtil.glEnable(GL11.GL_LIGHTING);
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
}