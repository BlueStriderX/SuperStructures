package thederpgamer.superstructures.graphics.gui.dysonsphere;

import api.utils.gui.GUIInputDialog;
import org.lwjgl.input.Keyboard;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.input.KeyEventInterface;
import thederpgamer.superstructures.data.structures.SuperStructureData;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/22/2021
 */
public class DysonSphereModuleTypeSelectionDialog extends GUIInputDialog {

    private SuperStructureData structureData;
    private int index;

    public DysonSphereModuleTypeSelectionDialog(SuperStructureData structureData, int index) {
        this.structureData = structureData;
        this.index = index;
    }

    @Override
    public DysonSphereModuleTypeSelectionPanel createPanel() {
        return new DysonSphereModuleTypeSelectionPanel(getState(), this, this, structureData, index);
    }

    @Override
    public void callback(GUIElement callingElement, MouseEvent mouseEvent) {
        if(!isOccluded() && mouseEvent.pressedLeftMouse() && callingElement.getUserPointer() != null) {
            switch((String) callingElement.getUserPointer()) {
                case "X":
                case "CANCEL":
                case "OK":
                    deactivate();
                    break;
            }
        }
    }

    @Override
    public void activate() {
        if(getInputPanel() != null) {
            ((DysonSphereModuleTypeSelectionPanel) getInputPanel()).structureData = structureData;
            ((DysonSphereModuleTypeSelectionPanel) getInputPanel()).index = index;
        }
        super.activate();
    }

    @Override
    public void handleKeyEvent(KeyEventInterface keyEvent) {
        super.handleKeyEvent(keyEvent);
        if(keyEvent.isPressed() && keyEvent.getKey() == Keyboard.KEY_ESCAPE) deactivate();
    }
}
