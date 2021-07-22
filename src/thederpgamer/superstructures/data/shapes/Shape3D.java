package thederpgamer.superstructures.data.shapes;

import com.bulletphysics.linearmath.Transform;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.core.GlUtil;
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
    public static final int FILLED = 2;

    private String name;
    private Vector3f[] vertices;
    private Vector3f[][] edges;
    private Vector3f[][] faces;

    private int drawMode = NONE;
    private Transform transform;
    private float scale;
    private Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    public Vector4f[] faceColors;

    static FloatBuffer bb = BufferUtils.createFloatBuffer(16);
    private static float[] glMat = new float[16];

    public Shape3D(String name, Vector3f[] vertices, Vector3f[][] edges, Vector3f[][] faces) {
        this.name = name;
        this.vertices = vertices;
        this.edges = edges;
        this.faces = faces;
        this.faceColors = new Vector4f[faces.length];
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
                    GlUtil.glColor4f(color);
                    for(Vector3f[] edge : edges) {
                        GL11.glBegin(GL11.GL_LINES);
                        GL11.glVertex3f(edge[0].x, edge[0].y, edge[0].z);
                        GL11.glVertex3f(edge[1].x, edge[1].y, edge[1].z);
                        GL11.glEnd();
                    }
                    GlUtil.glDisable(GL11.GL_COLOR_MATERIAL);
                    GlUtil.glEnable(GL11.GL_LIGHTING);
                    GlUtil.glPopMatrix();
                    break;
                case FILLED:
                    transform.getOpenGLMatrix(glMat);
                    bb.rewind();
                    bb.put(glMat);
                    bb.rewind();
                    GL11.glMultMatrix(bb);

                    GlUtil.glPushMatrix();
                    GlUtil.glDisable(GL11.GL_TEXTURE_2D);
                    GlUtil.glEnable(GL11.GL_COLOR_MATERIAL);
                    GlUtil.glDisable(GL11.GL_LIGHTING);
                    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    for(int i = 0; i < faces.length; i ++) {
                        GlUtil.glColor4f(faceColors[i]);
                        for(Vector3f v : faces[i]) GL11.glVertex3f(v.x, v.y, v.z);
                    }
                    GL11.glEnd();
                    GL11.glCullFace(GL11.GL_BACK);
                    GlUtil.glDisable(GL11.GL_COLOR_MATERIAL);
                    GlUtil.glEnable(GL11.GL_LIGHTING);
                    GlUtil.glPopMatrix();

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
}