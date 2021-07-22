package thederpgamer.superstructures.graphics.gui.dysonsphere;

import api.common.GameClient;
import api.utils.game.PlayerUtils;
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
 * @since 07/21/2021
 */
public class DysonSphereMenuPanel extends GUIMenuPanel {

    public SuperStructureData structureData;
    private GUIContentPane statusTab;
    private GUIContentPane moduleTab;
    private GUIContentPane settingsTab;

    private GUITilePane<StructureModuleData> modulePane;

    public DysonSphereMenuPanel(InputState inputState, SuperStructureData structureData) {
        super(inputState, "DysonSphereMenuPanel", 800, 500);
        this.structureData = structureData;
    }

    @Override
    public void recreateTabs() {
        guiWindow.clearTabs();

        statusTab = guiWindow.addTab("STATUS");
        statusTab.setTextBoxHeightLast(500);
        createStatusTab(statusTab);

        moduleTab = guiWindow.addTab("MODULES");
        moduleTab.setTextBoxHeightLast(500);
        createModuleTab(moduleTab);

        settingsTab = guiWindow.addTab("SETTINGS");
        settingsTab.setTextBoxHeightLast(500);
        createSettingsTab(settingsTab);
    }

    public void redrawModules() {
        modulePane.clear();
        for(int i = 0; i < structureData.modules.length; i ++) createModuleTile(modulePane, i);
    }

    private void createStatusTab(GUIContentPane statusTab) {
        //Todo: Status tab
    }

    private void createModuleTab(GUIContentPane moduleTab) {
        modulePane = new GUITilePane<>(getState(), guiWindow, 132, 165);
        modulePane.onInit();
        for(int i = 0; i < structureData.modules.length; i ++) createModuleTile(modulePane, i);
        moduleTab.getContent(0).attach(modulePane);
    }


    private void createSettingsTab(GUIContentPane settingsTab) {
        //Todo: Settings tab
    }

    private void createModuleTile(GUITilePane<StructureModuleData> modulePane, final int index) {
        final StructureModuleData[] module = new StructureModuleData[] {structureData.modules[index]};
        if(module[0] == null) {
            structureData.modules[index] = new DysonSphereEmptyModuleData(structureData);
            module[0] = structureData.modules[index];
        }

        if(module[0] instanceof DysonSphereEmptyModuleData) {
            GUITile tile = modulePane.addButtonTile("EMPTY", "Add new module", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) (new DysonSphereModuleTypeSelectionDialog(structureData, index)).activate();
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
            GUIOverlay spriteOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-empty-module-icon"), getState());
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
                            getState().getController().queueUIAudio("0022_menu_ui - enter");
                            queueModuleUpgrade(module[0], index);
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
            GUIOverlay spriteOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-power-module-icon"), getState());
            spriteOverlay.onInit();
            spriteOverlay.getSprite().setWidth(130);
            spriteOverlay.getSprite().setHeight(115);
            tile.attach(spriteOverlay);
            spriteOverlay.getPos().x += 5;
            spriteOverlay.getPos().y += 50;
        } else if(module[0] instanceof DysonSphereResourceModuleData) {
            String desc = (module[0].level < module[0].maxLevel) ? ("Upgrade module to level " + (module[0].level + 1)) : "Max level reached";
            GUITile tile = modulePane.addButtonTile("RESOURCE", desc, GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        if(module[0].level < module[0].maxLevel) {
                            getState().getController().queueUIAudio("0022_menu_ui - enter");
                            queueModuleUpgrade(module[0], index);
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
            GUIOverlay spriteOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-resource-module-icon"), getState());
            spriteOverlay.onInit();
            spriteOverlay.getSprite().setWidth(130);
            spriteOverlay.getSprite().setHeight(115);
            tile.attach(spriteOverlay);
            spriteOverlay.getPos().x += 5;
            spriteOverlay.getPos().y += 50;
        } else if(module[0] instanceof DysonSphereFoundryModuleData) {
            String desc = (module[0].level < module[0].maxLevel) ? ("Upgrade module to level " + (module[0].level + 1)) : "Max level reached";
            GUITile tile = modulePane.addButtonTile("FOUNDRY", desc, GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        if(module[0].level < module[0].maxLevel) {
                            getState().getController().queueUIAudio("0022_menu_ui - enter");
                            queueModuleUpgrade(module[0], index);
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
            GUIOverlay spriteOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-foundry-module-icon"), getState());
            spriteOverlay.onInit();
            spriteOverlay.getSprite().setWidth(130);
            spriteOverlay.getSprite().setHeight(115);
            tile.attach(spriteOverlay);
            spriteOverlay.getPos().x += 5;
            spriteOverlay.getPos().y += 50;
        } else if(module[0] instanceof DysonSphereShipyardModuleData) {
            String desc = (module[0].level < module[0].maxLevel) ? ("Upgrade module to level " + (module[0].level + 1)) : "Max level reached";
            GUITile tile = modulePane.addButtonTile("SHIPYARD", desc, GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        if(module[0].level < module[0].maxLevel) {
                            getState().getController().queueUIAudio("0022_menu_ui - enter");
                            queueModuleUpgrade(module[0], index);
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
            GUIOverlay spriteOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-shipyard-module-icon"), getState());
            spriteOverlay.onInit();
            spriteOverlay.getSprite().setWidth(130);
            spriteOverlay.getSprite().setHeight(115);
            tile.attach(spriteOverlay);
            spriteOverlay.getPos().x += 5;
            spriteOverlay.getPos().y += 50;
        } else if(module[0] instanceof DysonSphereDefenseModuleData) {
            String desc = (module[0].level < module[0].maxLevel) ? ("Upgrade module to level " + (module[0].level + 1)) : "Max level reached";
            GUITile tile = modulePane.addButtonTile("DEFENSE", desc, GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        if(module[0].level < module[0].maxLevel) {
                            getState().getController().queueUIAudio("0022_menu_ui - enter");
                            queueModuleUpgrade(module[0], index);
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
            GUIOverlay spriteOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-defense-module-icon"), getState());
            spriteOverlay.onInit();
            spriteOverlay.getSprite().setWidth(130);
            spriteOverlay.getSprite().setHeight(115);
            tile.attach(spriteOverlay);
            spriteOverlay.getPos().x += 5;
            spriteOverlay.getPos().y += 50;
        } else if(module[0] instanceof DysonSphereOffenseModuleData) {
            String desc = (module[0].level < module[0].maxLevel) ? ("Upgrade module to level " + (module[0].level + 1)) : "Max level reached";
            GUITile tile = modulePane.addButtonTile("OFFENSE", desc, GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        if(module[0].level < module[0].maxLevel) {
                            getState().getController().queueUIAudio("0022_menu_ui - enter");
                            queueModuleUpgrade(module[0], index);
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
            GUIOverlay spriteOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-offense-module-icon"), getState());
            spriteOverlay.onInit();
            spriteOverlay.getSprite().setWidth(130);
            spriteOverlay.getSprite().setHeight(115);
            tile.attach(spriteOverlay);
            spriteOverlay.getPos().x += 5;
            spriteOverlay.getPos().y += 50;
        } else if(module[0] instanceof DysonSphereSupportModuleData) {
            String desc = (module[0].level < module[0].maxLevel) ? ("Upgrade module to level " + (module[0].level + 1)) : "Max level reached";
            GUITile tile = modulePane.addButtonTile("SUPPORT", desc, GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
                @Override
                public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                    if(mouseEvent.pressedLeftMouse()) {
                        if(module[0].level < module[0].maxLevel) {
                            getState().getController().queueUIAudio("0022_menu_ui - enter");
                            queueModuleUpgrade(module[0], index);
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
            GUIOverlay spriteOverlay = new GUIOverlay(ResourceManager.getSprite("super-structure-support-module-icon"), getState());
            spriteOverlay.onInit();
            spriteOverlay.getSprite().setWidth(130);
            spriteOverlay.getSprite().setHeight(115);
            tile.attach(spriteOverlay);
            spriteOverlay.getPos().x += 5;
            spriteOverlay.getPos().y += 50;
        }
    }

    private void queueModuleUpgrade(StructureModuleData moduleData, int index) {
        if(DataUtils.adminMode()) {
            PlayerUtils.sendMessage(GameClient.getClientPlayerState(), "[DEBUG]: Admin mode used to upgrade a dyson sphere module in " + GameClient.getClientPlayerState().getCurrentSector().toString() + ":\n" + moduleData.toString());
        } else {
            //Todo: Queue module construction
        }
        structureData.modules[index] = moduleData;
        ((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).redrawModules();
        DataUtils.queueUpdate(structureData);
    }
}