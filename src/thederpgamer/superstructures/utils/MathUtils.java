package thederpgamer.superstructures.utils;

import javax.vecmath.Vector3f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [09/28/2021]
 */
public class MathUtils {

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
}
