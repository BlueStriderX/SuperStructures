package thederpgamer.superstructures.graphics.gui.dysonspherecontroller;

import api.common.GameClient;
import api.utils.gui.GUIControlManager;
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
}
