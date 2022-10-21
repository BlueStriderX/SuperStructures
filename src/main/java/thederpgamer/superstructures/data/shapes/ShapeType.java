package thederpgamer.superstructures.data.shapes;

import javax.vecmath.Vector3f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [12/05/2021]
 */
public enum ShapeType {
    DODECAHEDRON(
            new Vector3f[] {
                    new Vector3f(0.0f, 0.68f, 0.42f),
                    new Vector3f(-0.68f, 0.42f, 0.0f),
                    new Vector3f(-0.42f, 0.0f, 0.68f),
                    new Vector3f(-0.68f, -0.42f, 0.0f),
                    new Vector3f(0.00f, -0.68f, 0.42f),
                    new Vector3f(0.42f, 0.0f, 0.68f),
                    new Vector3f(0.68f, 0.42f, 0.0f),
                    new Vector3f(0.0f, 0.68f, -0.42f),
                    new Vector3f(0.68f, -0.42f, 0.0f),
                    new Vector3f(0.42f, 0.0f, -0.68f),
                    new Vector3f(-0.42f, 0.0f, -0.68f),
                    new Vector3f(0.0f, -0.68f, -0.42f)
            },
            new Vector3f[] {
                    new Vector3f(-144.0f, -32.0f, 90.0f),
                    new Vector3f(-108.0f, 0.0f, 148.0f),
                    new Vector3f(-90.0f, -58.0f, -180.0f),
                    new Vector3f(-72.0f, 0.0f, -148.0f),
                    new Vector3f(162.0f, -32.0f, -90.0f),
                    new Vector3f(54.0f, -58.0f, 0.0f),
                    new Vector3f(72.0f, 0.0f, 32.0f),
                    new Vector3f(18.0f, 32.0f, 90.0f),
                    new Vector3f(108.0f, 0.0f, -32.0f),
                    new Vector3f(90.0f, 58.0f, 0.0f),
                    new Vector3f(4.0f, 122.0f, 0.0f),
                    new Vector3f(-18.0f, 32.0f, -90.0f)
            });

    public Vector3f[] facePositions;
    public Vector3f[] faceRotations;

    ShapeType(Vector3f[] facePositions, Vector3f[] faceRotations) {
        this.facePositions = facePositions;
        this.faceRotations = faceRotations;
    }
}
