package thederpgamer.superstructures.utils;

import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GlUtil;

import javax.vecmath.Vector3f;
import java.nio.FloatBuffer;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [09/28/2021]
 */
public class MathUtils {

    public static Vector3f convertScreenPostoWorld(Vector3f screenPos) {
        //Unproject position
        Matrix4f modelViewMatrix = GlUtil.retrieveModelviewMatrix(new Matrix4f());
        Matrix4f projectionMatrix = GlUtil.retrieveProjectionMatrix(new Matrix4f());

        //Convert model view and projection matrices to float buffers
        FloatBuffer modelViewMatrixBuffer = FloatBuffer.allocate(16);
        FloatBuffer projectionMatrixBuffer = FloatBuffer.allocate(16);
        FloatBuffer positionBuffer = FloatBuffer.allocate(3);
        for(int i = 0; i < 16; i ++) {
            modelViewMatrixBuffer.put(i, modelViewMatrix.m00 + i);
            projectionMatrixBuffer.put(i, projectionMatrix.m00 + i);
        }
        positionBuffer.put(0, screenPos.x);
        positionBuffer.put(1, screenPos.y);
        positionBuffer.put(2, screenPos.z);
        GLU.gluUnProject(screenPos.x, screenPos.y, screenPos.z, modelViewMatrixBuffer, projectionMatrixBuffer, Controller.viewport, positionBuffer);

        //Get unprojected position
        screenPos.x = positionBuffer.get(0);
        screenPos.y = positionBuffer.get(1);
        screenPos.z = positionBuffer.get(2);
        return screenPos;
    }

    public static Vector3f calculateMin(Vector3f[] vectors) {
        Vector3f min = new Vector3f(vectors[0]);
        for(Vector3f vector : vectors) {
            if(vector.x < min.x) min.x = vector.x;
            if(vector.y < min.y) min.y = vector.y;
            if(vector.z < min.z) min.z = vector.z;
        }
        return min;
    }

    public static Vector3f calculateMax(Vector3f[] vectors) {
        Vector3f max = new Vector3f(vectors[0]);
        for(Vector3f vector : vectors) {
            if(vector.x > max.x) max.x = vector.x;
            if(vector.y > max.y) max.y = vector.y;
            if(vector.z > max.z) max.z = vector.z;
        }
        return max;
    }

    public static void rotateAroundAxis(Vector3f pos, Vector3f rot, Vector3f axis) {
        //Rotate around axis
        Vector3f axisRot = new Vector3f(axis);
        axisRot.scale(rot.length());
        pos.add(axisRot);
    }
}
