package thederpgamer.superstructures.graphics.gui.dysonsphere;

import api.common.GameClient;
import api.utils.gui.GUIControlManager;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.input.KeyEventInterface;
import thederpgamer.superstructures.data.structures.SuperStructureData;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereControlManager extends GUIControlManager {

    public SuperStructureData structureData;

    public DysonSphereControlManager() {
        super(GameClient.getClientState());
    }

    @Override
    public DysonSphereMenuPanel createMenuPanel() {
        return new DysonSphereMenuPanel(getState(), structureData);
    }

    @Override
    public void handleKeyEvent(KeyEventInterface keyEvent) {
        if(isActive() && !isSuspended() && !isHinderedInteraction() && getState().getPlayerInputs().isEmpty()) super.handleKeyEvent(keyEvent);
    }

    @Override
    public void handleMouseEvent(MouseEvent mouseEvent) {
        if(isActive() && !isSuspended() && !isHinderedInteraction() && getState().getPlayerInputs().isEmpty()) super.handleMouseEvent(mouseEvent);
    }
}
