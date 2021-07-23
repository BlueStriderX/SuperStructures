package thederpgamer.superstructures.graphics.gui.elements;

import com.bulletphysics.linearmath.Transform;
import org.schema.schine.common.InputHandler;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.input.InputState;
import org.schema.schine.input.KeyEventInterface;
import thederpgamer.superstructures.data.shapes.Shape3D;

import javax.vecmath.Vector4f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/23/2021
 */
public class GUIMenuRotatable3DObject extends GUIElement implements Drawable, InputHandler {

    private Shape3D shape;
    private Vector4f color;
    private final GUIElement dependent;
    private float width;
    private float height;
    private float xRot = 0.0f;
    private float yRot = 0.0f;

    public GUIMenuRotatable3DObject(InputState state, Shape3D shape, Vector4f color, GUIElement dependent, float width, float height) {
        super(state);
        this.shape = shape;
        this.color = color;
        this.width = width;
        this.height = height;
        this.dependent = dependent;
    }

    @Override
    public void onInit() {
        shape.setTransform(dependent.getTransform());
        shape.setColor(color);
        shape.setShapeScale(5.0f);
        shape.onInit();
        dependent.attach(shape);
        shape.setInside(true);
    }

    @Override
    public void draw() {
        if(dependent.isActive() && dependent.isOnScreen()) {
            shape.setDrawMode(Shape3D.WIREFRAME);
            Controller.getMat(Controller.modelviewMatrix, shape.getTransform());
            if(xRot != 0) shape.getTransform().basis.rotX(xRot);
            if(yRot != 0) shape.getTransform().basis.rotY(yRot);
            shape.orientateInsideFrame();
            shape.orientate(ORIENTATION_HORIZONTAL_MIDDLE | ORIENTATION_VERTICAL_MIDDLE);
            shape.draw();
        } else {
            resetShapeRotation();
            shape.setDrawMode(Shape3D.NONE);
        }
    }

    @Override
    public void cleanUp() {
        if(shape != null) shape.setDrawMode(Shape3D.NONE);
    }

    @Override
    public void handleKeyEvent(KeyEventInterface keyEvent) {

    }

    @Override
    public void handleMouseEvent(MouseEvent mouseEvent) {
        if(getState().getController().getPlayerInputs().isEmpty() && mouseEvent.pressedRightMouse() && mouseEvent.state) {
            xRot = -mouseEvent.dx * (Float) EngineSettings.M_MOUSE_SENSITIVITY.getCurrentState();
            yRot = (EngineSettings.S_MOUSE_ALL_INVERT.isOn() ? -mouseEvent.dy : mouseEvent.dy) * (Float) EngineSettings.M_MOUSE_SENSITIVITY.getCurrentState();
        } else {
            xRot = 0;
            yRot = 0;
        }
    }

    public Shape3D getShape() {
        return shape;
    }

    public void setShape(Shape3D shape) {
        this.shape = shape;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public GUIElement getDependent() {
        return dependent;
    }

    @Override
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Transform getModelTransform() {
        return shape.getTransform();
    }

    public void resetShapeRotation() {
        xRot = 0.0f;
        yRot = 0.0f;
        shape.getTransform().basis.setIdentity();
    }
}
