package thederpgamer.superstructures.graphics.gui.dysonsphere;

import api.utils.gui.GUIInputDialog;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import thederpgamer.superstructures.data.structures.SuperStructureData;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/22/2021
 */
public class DysonSphereModuleTypeSelectionDialog extends GUIInputDialog {

    private SuperStructureData structureData;

    public DysonSphereModuleTypeSelectionDialog(SuperStructureData structureData) {
        super();
        this.structureData = structureData;
    }

    @Override
    public DysonSphereModuleTypeSelectionPanel createPanel() {
        return new DysonSphereModuleTypeSelectionPanel(getState(), this, structureData);
    }

    @Override
    public void callback(GUIElement callingElement, MouseEvent mouseEvent) {
        if(!isOccluded() && mouseEvent.pressedLeftMouse() && callingElement.getUserPointer() != null) {
            switch((String) callingElement.getUserPointer()) {
                case "X":
                case "CANCEL":
                    deactivate();
                    break;
                case "OK":
                    //Do stuff
                    break;
            }
        }
    }
}
