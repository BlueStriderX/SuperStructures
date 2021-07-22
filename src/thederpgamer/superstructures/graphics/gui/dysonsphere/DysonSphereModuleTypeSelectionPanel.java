package thederpgamer.superstructures.graphics.gui.dysonsphere;

import api.common.GameClient;
import api.utils.game.PlayerUtils;
import api.utils.gui.GUIInputDialogPanel;
import api.utils.gui.SimplePopup;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.GUIActivationCallback;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.forms.gui.newgui.*;
import org.schema.schine.input.InputState;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.modules.dysonsphere.*;
import thederpgamer.superstructures.data.structures.SuperStructureData;
import thederpgamer.superstructures.manager.ResourceManager;
import thederpgamer.superstructures.utils.DataUtils;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/22/2021
 */
public class DysonSphereModuleTypeSelectionPanel extends GUIInputDialogPanel {

    private DysonSphereModuleTypeSelectionDialog dialog;
    public SuperStructureData structureData;
    public int index;

    public DysonSphereModuleTypeSelectionPanel(InputState state, GUICallback guiCallback,  DysonSphereModuleTypeSelectionDialog dialog, SuperStructureData structureData, int index) {
        super(state, "DysonSphereModuleTypeSelectionPanel", "Construct new module", "Select a module type to construct.", 970, 400, guiCallback);
        this.dialog = dialog;
        this.structureData = structureData;
        this.index = index;
    }

    @Override
    public void onInit() {
        super.onInit();
        GUIContentPane contentPane = ((GUIDialogWindow) background).getMainContentPane();
        contentPane.setTextBoxHeightLast(350);

        GUITilePane<GUIOverlay> modulePane = new GUITilePane<>(getState(), background, 132, 250);
        modulePane.onInit();
        createModuleTiles(modulePane);
        getContent().attach(modulePane);
        modulePane.getPos().y += 30;
    }

    private void createModuleTiles(GUITilePane<GUIOverlay> tilePane) {
        GUITile powerTile = tilePane.addButtonTile("POWER", "Generates solar energy for the controller station.", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
            @Override
            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                if(mouseEvent.pressedLeftMouse()) {
                    getState().getController().queueUIAudio("0022_menu_ui - enter");
                    queueModuleConstruction(new DysonSpherePowerModuleData(structureData));
                    dialog.deactivate();
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        }, new GUIActivationCallback() {
            @Override
            public boolean isVisible(InputState inputState) {
                return true;
            }

            @Override
            public boolean isActive(InputState inputState) {
                return true;
            }
        });
        GUIOverlay powerOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-power-module-icon"), getState());
        powerOverlay.onInit();
        powerOverlay.getSprite().setWidth(130);
        powerOverlay.getSprite().setHeight(115);
        powerTile.attach(powerOverlay);
        powerOverlay.getPos().x += 5;
        powerOverlay.getPos().y += 130;

        GUITile resourceTile = tilePane.addButtonTile("RESOURCE", "Generates resources by siphoning them from the star.", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
            @Override
            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                if(mouseEvent.pressedLeftMouse()) {
                    getState().getController().queueUIAudio("0022_menu_ui - enter");
                    queueModuleConstruction(new DysonSphereResourceModuleData(structureData));
                    dialog.deactivate();
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        }, new GUIActivationCallback() {
            @Override
            public boolean isVisible(InputState inputState) {
                return true;
            }

            @Override
            public boolean isActive(InputState inputState) {
                return true;
            }
        });
        GUIOverlay resourceOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-resource-module-icon"), getState());
        resourceOverlay.onInit();
        resourceOverlay.getSprite().setWidth(130);
        resourceOverlay.getSprite().setHeight(115);
        resourceTile.attach(resourceOverlay);
        resourceOverlay.getPos().x += 5;
        resourceOverlay.getPos().y += 130;

        GUITile foundryTile = tilePane.addButtonTile("FOUNDRY", "Manufactures components and materials on a massive scale.", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
            @Override
            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                if(mouseEvent.pressedLeftMouse()) {
                    getState().getController().queueUIAudio("0022_menu_ui - enter");
                    queueModuleConstruction(new DysonSphereFoundryModuleData(structureData));
                    dialog.deactivate();
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        }, new GUIActivationCallback() {
            @Override
            public boolean isVisible(InputState inputState) {
                return true;
            }

            @Override
            public boolean isActive(InputState inputState) {
                return true;
            }
        });
        GUIOverlay foundryOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-foundry-module-icon"), getState());
        foundryOverlay.onInit();
        foundryOverlay.getSprite().setWidth(130);
        foundryOverlay.getSprite().setHeight(115);
        foundryTile.attach(foundryOverlay);
        foundryOverlay.getPos().x += 5;
        foundryOverlay.getPos().y += 130;

        GUITile shipyardTile = tilePane.addButtonTile("SHIPYARD", "Assembles ships on a large industrial scale using the power of it's star.", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
            @Override
            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                if(mouseEvent.pressedLeftMouse()) {
                    getState().getController().queueUIAudio("0022_menu_ui - enter");
                    queueModuleConstruction(new DysonSphereShipyardModuleData(structureData));
                    dialog.deactivate();
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        }, new GUIActivationCallback() {
            @Override
            public boolean isVisible(InputState inputState) {
                return true;
            }

            @Override
            public boolean isActive(InputState inputState) {
                return true;
            }
        });
        GUIOverlay shipyardOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-shipyard-module-icon"), getState());
        shipyardOverlay.onInit();
        shipyardOverlay.getSprite().setWidth(130);
        shipyardOverlay.getSprite().setHeight(115);
        shipyardTile.attach(shipyardOverlay);
        shipyardOverlay.getPos().x += 5;
        shipyardOverlay.getPos().y += 130;

        GUITile defenseTile = tilePane.addButtonTile("DEFENSE", "Enhances the defensive capabilities of the dyson sphere.", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
            @Override
            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                if(mouseEvent.pressedLeftMouse()) {
                    getState().getController().queueUIAudio("0022_menu_ui - enter");
                    queueModuleConstruction(new DysonSphereDefenseModuleData(structureData));
                    dialog.deactivate();
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        }, new GUIActivationCallback() {
            @Override
            public boolean isVisible(InputState inputState) {
                return true;
            }

            @Override
            public boolean isActive(InputState inputState) {
                return true;
            }
        });
        GUIOverlay defenseOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-defense-module-icon"), getState());
        defenseOverlay.onInit();
        defenseOverlay.getSprite().setWidth(130);
        defenseOverlay.getSprite().setHeight(115);
        defenseTile.attach(defenseOverlay);
        defenseOverlay.getPos().x += 5;
        defenseOverlay.getPos().y += 130;

        GUITile offenseTile = tilePane.addButtonTile("OFFENSE", "Enhances the offensive capabilities of the dyson sphere.", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
            @Override
            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                if(mouseEvent.pressedLeftMouse()) {
                    getState().getController().queueUIAudio("0022_menu_ui - enter");
                    queueModuleConstruction(new DysonSphereOffenseModuleData(structureData));
                    dialog.deactivate();
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        }, new GUIActivationCallback() {
            @Override
            public boolean isVisible(InputState inputState) {
                return true;
            }

            @Override
            public boolean isActive(InputState inputState) {
                return true;
            }
        });
        GUIOverlay offenseOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-offense-module-icon"), getState());
        offenseOverlay.onInit();
        offenseOverlay.getSprite().setWidth(130);
        offenseOverlay.getSprite().setHeight(115);
        offenseTile.attach(offenseOverlay);
        offenseOverlay.getPos().x += 5;
        offenseOverlay.getPos().y += 130;

        GUITile supportTile = tilePane.addButtonTile("SUPPORT", "Allows the dyson sphere to support nearby friendly ships.", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
            @Override
            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                if(mouseEvent.pressedLeftMouse()) {
                    getState().getController().queueUIAudio("0022_menu_ui - enter");
                    queueModuleConstruction(new DysonSphereSupportModuleData(structureData));
                    dialog.deactivate();
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        }, new GUIActivationCallback() {
            @Override
            public boolean isVisible(InputState inputState) {
                return true;
            }

            @Override
            public boolean isActive(InputState inputState) {
                return true;
            }
        });
        GUIOverlay supportOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-support-module-icon"), getState());
        supportOverlay.onInit();
        supportOverlay.getSprite().setWidth(130);
        supportOverlay.getSprite().setHeight(115);
        supportTile.attach(supportOverlay);
        supportOverlay.getPos().x += 5;
        supportOverlay.getPos().y += 130;
    }

    private void queueModuleConstruction(StructureModuleData moduleData) {
        if(DataUtils.adminMode()) {
            moduleData.status = StructureModuleData.NONE;
            PlayerUtils.sendMessage(GameClient.getClientPlayerState(), "[DEBUG]: Admin mode used to construct a new dyson sphere module in " + GameClient.getClientPlayerState().getCurrentSector().toString() + ":\n" + moduleData.toString());
        } else {
            for(StructureModuleData data : structureData.modules) {
                if(data.status != StructureModuleData.NONE) {
                    new SimplePopup(getState(), "Cannot Construct", "There is already a module construction or upgrade in process!").activate();
                    return;
                }
            }
            moduleData.status = StructureModuleData.CONSTRUCTION;
            //Todo: Queue module construction
        }
        moduleData.level = 1;
        structureData.modules[index] = moduleData;
        ((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).redrawModules();
        DataUtils.queueUpdate(structureData);
    }
}
