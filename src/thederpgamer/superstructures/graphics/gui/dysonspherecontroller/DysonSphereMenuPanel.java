package thederpgamer.superstructures.graphics.gui.dysonspherecontroller;

import api.utils.gui.GUIMenuPanel;
import api.utils.gui.SimplePopup;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.GUIActivationCallback;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIContentPane;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalArea;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITile;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITilePane;
import org.schema.schine.input.InputState;
import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.modules.dysonsphere.DysonSphereEmptyModuleData;
import thederpgamer.superstructures.data.modules.dysonsphere.DysonSpherePowerModuleData;
import thederpgamer.superstructures.data.structures.SuperStructureData;
import thederpgamer.superstructures.manager.ResourceManager;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereMenuPanel extends GUIMenuPanel {

    private SuperStructureData structureData;

    public DysonSphereMenuPanel(InputState inputState, SuperStructureData structureData) {
        super(inputState, "DysonSphereMenuPanel", 800, 500);
        this.structureData = structureData;
    }

    @Override
    public void recreateTabs() {
        guiWindow.clearTabs();

        GUIContentPane statusTab = guiWindow.addTab("STATUS");
        statusTab.setTextBoxHeightLast(500);
        createStatusTab(statusTab);

        GUIContentPane moduleTab = guiWindow.addTab("MODULES");
        moduleTab.setTextBoxHeightLast(500);
        createModuleTab(moduleTab);

        GUIContentPane settingsTab = guiWindow.addTab("SETTINGS");
        settingsTab.setTextBoxHeightLast(500);
        createSettingsTab(settingsTab);
    }

    private void createStatusTab(GUIContentPane statusTab) {
        //Todo: Status tab
    }

    private void createModuleTab(GUIContentPane moduleTab) {
        GUITilePane<StructureModuleData> modulePane = new GUITilePane<>(getState(), guiWindow, 132, 165);
        modulePane.onInit();
        for(int i = 0; i < structureData.modules.length; i ++) createModuleTile(modulePane, i);
        moduleTab.getContent(0).attach(modulePane);
    }

    private void createSettingsTab(GUIContentPane settingsTab) {
        //Todo: Settings tab
    }

    private void createModuleTile(GUITilePane<StructureModuleData> modulePane, int index) {
        final StructureModuleData[] module = new StructureModuleData[] {structureData.modules[index]};
        if(module[0] == null) {
            structureData.modules[index] = new DysonSphereEmptyModuleData();
            module[0] = structureData.modules[index];
        }

        if(module[0] instanceof DysonSphereEmptyModuleData) {
            GUITile tile = modulePane.addButtonTile("EMPTY", "Add new module", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        //Todo: Choose module type menu
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
            GUIOverlay spriteOverlay = new GUIOverlay(ResourceManager.getSprite("dyson-sphere-empty-module-icon"), getState());
            spriteOverlay.onInit();
            spriteOverlay.getSprite().setWidth(130);
            spriteOverlay.getSprite().setHeight(115);
            tile.attach(spriteOverlay);
            spriteOverlay.getPos().x += 5;
            spriteOverlay.getPos().y += 50;
        } else if(module[0] instanceof DysonSpherePowerModuleData) {
            String desc = (module[0].level < module[0].maxLevel) ? ("Upgrade module to level " + (module[0].level + 1)) : "Max level reached";
            GUITile tile = modulePane.addButtonTile("POWER", desc, GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        if(module[0].level < module[0].maxLevel) {
                            //Todo: Queue module upgrade
                        } else {
                            getState().getController().queueUIAudio("0022_menu_ui - error 1");
                            new SimplePopup(getState(), "Max level reached", "This module has already reached it's maximum level!");
                        }
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
            GUIOverlay spriteOverlay = new GUIOverlay(ResourceManager.getSprite("dyson-sphere-power-module-icon"), getState());
            spriteOverlay.onInit();
            spriteOverlay.getSprite().setWidth(130);
            spriteOverlay.getSprite().setHeight(115);
            tile.attach(spriteOverlay);
            spriteOverlay.getPos().x += 5;
            spriteOverlay.getPos().y += 50;
        }
    }
}
