package thederpgamer.superstructures.graphics.gui.dysonsphere;

import api.common.GameClient;
import api.utils.game.PlayerUtils;
import api.utils.gui.GUIMenuPanel;
import api.utils.gui.SimplePopup;
import org.newdawn.slick.Color;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.*;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIContentPane;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalArea;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITile;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITilePane;
import org.schema.schine.input.InputState;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.modules.dysonsphere.*;
import thederpgamer.superstructures.data.structures.SSLogEvent;
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
	private GUIContentPane overviewTab;
	private GUIContentPane moduleTab;

	private GUITilePane<StructureModuleData> modulePane;

	private boolean initialized = false;

	public DysonSphereMenuPanel(InputState inputState, SuperStructureData structureData) {
		super(inputState, "DysonSphereMenuPanel", (int) (GLFrame.getWidth() / 1.5f), (int) (GLFrame.getHeight() / 1.5f));
		this.structureData = structureData;
	}

	@Override
	public void onInit() {
		super.onInit();
		initialized = true;
	}

	@Override
	public void draw() {
		if(!initialized) onInit();
		super.draw();
	}

	@Override
	public void recreateTabs() {
		int tab = guiWindow.getSelectedTab();
		guiWindow.clearTabs();

		overviewTab = guiWindow.addTab("Overview");
		overviewTab.setTextBoxHeightLast((int) (GLFrame.getHeight() / 1.5f));
		createOverviewTab(overviewTab);

		moduleTab = guiWindow.addTab("MODULES");
		moduleTab.setTextBoxHeightLast((int) (GLFrame.getHeight() / 1.5f));
		createModuleTab(moduleTab);

		guiWindow.setSelectedTab(tab);
	}

	public void refreshTabs() {
		modulePane.clear();
		for(int i = 0; i < structureData.modules.length; i ++) createModuleTile(modulePane, i);
	}

	private void createOverviewTab(GUIContentPane overviewTab) {
		overviewTab.addDivider((int) (overviewTab.getWidth() / 2));
		overviewTab.addNewTextBox(1, (int) (overviewTab.getHeight() / 2));

		{ //Settings Pane
			GUIAncor settingsPane = overviewTab.getContent(0, 0);

		}

		{ //Log Pane
			GUIAncor logPane = overviewTab.getContent(1, 0);
			GUIElementList logList = new GUIElementList(getState());
			for(SSLogEvent log : structureData.logs) {
				GUITextOverlay logText = new GUITextOverlay(10, 10, getState());
				logText.setTextSimple(log.toString());
				switch(log.getType()) {
					case SSLogEvent.LOG_EVENT_WARNING:
						logText.setColor(Color.orange);
						break;
					case SSLogEvent.LOG_EVENT_ERROR:
						logText.setColor(Color.red);
						break;
				}
				GUIListElement logElement = new GUIListElement(logText, getState());
				logList.add(logElement);
			}
			logList.onInit();
			logPane.attach(logList);
		}

		{ //Stats Pane
			GUIAncor statsPane = overviewTab.getContent(1, 1);
		}
	}

	private void createModuleTab(GUIContentPane moduleTab) {
		modulePane = new GUITilePane<>(getState(), guiWindow, 132, 180);
		modulePane.onInit();
		if(structureData != null && structureData.modules != null) for(int i = 0; i < structureData.modules.length; i ++) createModuleTile(modulePane, i);
		moduleTab.getContent(0).attach(modulePane);
	}

	private void createModuleTile(GUITilePane<StructureModuleData> modulePane, final int index) {
		final StructureModuleData[] module = new StructureModuleData[] {structureData.modules[index]};
		if(module[0] == null) {
			structureData.modules[index] = new DysonSphereEmptyModuleData(structureData);
			module[0] = structureData.modules[index];
		}

		GUIHorizontalArea.HButtonColor buttonColor = GUIHorizontalArea.HButtonColor.BLUE;
		String desc = module[0].getDesc();
		if(module[0].status == StructureModuleData.CONSTRUCTION) {
			buttonColor = GUIHorizontalArea.HButtonColor.YELLOW;
			desc = "Constructing module";
		} else if(module[0].status == StructureModuleData.UPGRADE) {
			buttonColor = GUIHorizontalArea.HButtonColor.GREEN;
			desc = "Upgrading module to level " + (module[0].level + 1);
		}
		else if(module[0].status == StructureModuleData.REPAIR) buttonColor = GUIHorizontalArea.HButtonColor.ORANGE;

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
			spriteOverlay.getPos().y += 70;
		} else if(module[0] instanceof DysonSpherePowerModuleData) {
			GUITile tile = modulePane.addButtonTile("POWER - " + module[0].level, desc, buttonColor, new GUICallback() {
				@Override
				public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
					if(mouseEvent.pressedLeftMouse()) {
						if(module[0].level < module[0].maxLevel) {
							getState().getController().queueUIAudio("0022_menu_ui - enter");
							queueModuleUpgrade(module[0], index);
							structureData.log(SSLogEvent.LOG_EVENT_INFO, "Module upgrade queued");
						} else {
							getState().getController().queueUIAudio("0022_menu_ui - error 1");
							new SimplePopup(getState(), "Cannot Upgrade", "This module has already reached it's maximum level!");
							structureData.log(SSLogEvent.LOG_EVENT_ERROR, "Module upgrade failed - max level reached");
						}
					} else if(mouseEvent.pressedRightMouse()) {
						getState().getController().queueUIAudio("0022_menu_ui - cancel");
						if(module[0].status == StructureModuleData.CONSTRUCTION) module[0] = new DysonSphereEmptyModuleData(structureData);
						module[0].status = StructureModuleData.NONE;
						structureData.modules[index] = module[0];
						structureData.log(SSLogEvent.LOG_EVENT_INFO, "Module construction cancelled");
						((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).refreshTabs();
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
			spriteOverlay.getPos().y += 70;
		} else if(module[0] instanceof DysonSphereResourceModuleData) {
			GUITile tile = modulePane.addButtonTile("RESOURCE - " + module[0].level, desc, buttonColor, new GUICallback() {
				@Override
				public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
					if(mouseEvent.pressedLeftMouse()) {
						if(module[0].level < module[0].maxLevel) {
							getState().getController().queueUIAudio("0022_menu_ui - enter");
							queueModuleUpgrade(module[0], index);
						} else {
							getState().getController().queueUIAudio("0022_menu_ui - error 1");
							new SimplePopup(getState(), "Cannot Upgrade", "This module has already reached it's maximum level!");
						}
					} else if(mouseEvent.pressedRightMouse()) {
						getState().getController().queueUIAudio("0022_menu_ui - cancel");
						if(module[0].status == StructureModuleData.CONSTRUCTION) module[0] = new DysonSphereEmptyModuleData(structureData);
						module[0].status = StructureModuleData.NONE;
						structureData.modules[index] = module[0];
						((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).refreshTabs();
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
			spriteOverlay.getPos().y += 70;
		} else if(module[0] instanceof DysonSphereFoundryModuleData) {
			GUITile tile = modulePane.addButtonTile("FOUNDRY - " + module[0].level, desc, buttonColor, new GUICallback() {
				@Override
				public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
					if(mouseEvent.pressedLeftMouse()) {
						if(module[0].level < module[0].maxLevel) {
							getState().getController().queueUIAudio("0022_menu_ui - enter");
							queueModuleUpgrade(module[0], index);
						} else {
							getState().getController().queueUIAudio("0022_menu_ui - error 1");
							new SimplePopup(getState(), "Cannot Upgrade", "This module has already reached it's maximum level!");
						}
					} else if(mouseEvent.pressedRightMouse()) {
						getState().getController().queueUIAudio("0022_menu_ui - cancel");
						if(module[0].status == StructureModuleData.CONSTRUCTION) module[0] = new DysonSphereEmptyModuleData(structureData);
						module[0].status = StructureModuleData.NONE;
						structureData.modules[index] = module[0];
						((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).refreshTabs();
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
			spriteOverlay.getPos().y += 70;
		} else if(module[0] instanceof DysonSphereShipyardModuleData) {
			GUITile tile = modulePane.addButtonTile("SHIPYARD - " + module[0].level, desc, buttonColor, new GUICallback() {
				@Override
				public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
					if(mouseEvent.pressedLeftMouse()) {
						if(module[0].level < module[0].maxLevel) {
							getState().getController().queueUIAudio("0022_menu_ui - enter");
							queueModuleUpgrade(module[0], index);
						} else {
							getState().getController().queueUIAudio("0022_menu_ui - error 1");
							new SimplePopup(getState(), "Cannot Upgrade", "This module has already reached it's maximum level!");
						}
					} else if(mouseEvent.pressedRightMouse()) {
						getState().getController().queueUIAudio("0022_menu_ui - cancel");
						if(module[0].status == StructureModuleData.CONSTRUCTION) module[0] = new DysonSphereEmptyModuleData(structureData);
						module[0].status = StructureModuleData.NONE;
						structureData.modules[index] = module[0];
						((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).refreshTabs();
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
			spriteOverlay.getPos().y += 70;
		} else if(module[0] instanceof DysonSphereDefenseModuleData) {
			GUITile tile = modulePane.addButtonTile("DEFENSE - " + module[0].level, desc, buttonColor, new GUICallback() {
				@Override
				public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
					if(mouseEvent.pressedLeftMouse()) {
						if(module[0].level < module[0].maxLevel) {
							getState().getController().queueUIAudio("0022_menu_ui - enter");
							queueModuleUpgrade(module[0], index);
						} else {
							getState().getController().queueUIAudio("0022_menu_ui - error 1");
							new SimplePopup(getState(), "Cannot Upgrade", "This module has already reached it's maximum level!");
						}
					} else if(mouseEvent.pressedRightMouse()) {
						getState().getController().queueUIAudio("0022_menu_ui - cancel");
						if(module[0].status == StructureModuleData.CONSTRUCTION) module[0] = new DysonSphereEmptyModuleData(structureData);
						module[0].status = StructureModuleData.NONE;
						structureData.modules[index] = module[0];
						((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).refreshTabs();
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
			spriteOverlay.getPos().y += 70;
		} else if(module[0] instanceof DysonSphereOffenseModuleData) {
			GUITile tile = modulePane.addButtonTile("OFFENSE - " + module[0].level, desc, buttonColor, new GUICallback() {
				@Override
				public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
					if(mouseEvent.pressedLeftMouse()) {
						if(module[0].level < module[0].maxLevel) {
							getState().getController().queueUIAudio("0022_menu_ui - enter");
							queueModuleUpgrade(module[0], index);
						} else {
							getState().getController().queueUIAudio("0022_menu_ui - error 1");
							new SimplePopup(getState(), "Cannot Upgrade", "This module has already reached it's maximum level!");
						}
					} else if(mouseEvent.pressedRightMouse()) {
						getState().getController().queueUIAudio("0022_menu_ui - cancel");
						if(module[0].status == StructureModuleData.CONSTRUCTION) module[0] = new DysonSphereEmptyModuleData(structureData);
						module[0].status = StructureModuleData.NONE;
						structureData.modules[index] = module[0];
						((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).refreshTabs();
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
			spriteOverlay.getPos().y += 70;
		} else if(module[0] instanceof DysonSphereSupportModuleData) {
			GUITile tile = modulePane.addButtonTile("SUPPORT - " + module[0].level, desc, buttonColor, new GUICallback() {
				@Override
				public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
					if(mouseEvent.pressedLeftMouse()) {
						if(module[0].level < module[0].maxLevel) {
							getState().getController().queueUIAudio("0022_menu_ui - enter");
							queueModuleUpgrade(module[0], index);
						} else {
							getState().getController().queueUIAudio("0022_menu_ui - error 1");
							new SimplePopup(getState(), "Cannot Upgrade", "This module has already reached it's maximum level!");
						}
					} else if(mouseEvent.pressedRightMouse()) {
						getState().getController().queueUIAudio("0022_menu_ui - cancel");
						if(module[0].status == StructureModuleData.CONSTRUCTION) module[0] = new DysonSphereEmptyModuleData(structureData);
						module[0].status = StructureModuleData.NONE;
						structureData.modules[index] = module[0];
						((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).refreshTabs();
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
			spriteOverlay.getPos().y += 70;
		}
	}

	private void queueModuleUpgrade(StructureModuleData moduleData, int index) {
		if(DataUtils.adminMode()) {
			moduleData.status = StructureModuleData.NONE;
			PlayerUtils.sendMessage(GameClient.getClientPlayerState(), "[DEBUG]: Admin mode used to upgrade a dyson sphere module in " + GameClient.getClientPlayerState().getCurrentSector().toString() + ":\n" + moduleData.toString());
		} else {
			for(StructureModuleData data : structureData.modules) {
				if(data.status != StructureModuleData.NONE) {
					new SimplePopup(getState(), "Cannot Upgrade", "There is already a module construction or upgrade in process!").activate();
					return;
				}
			}
			//Todo: Queue module construction
			moduleData.status = StructureModuleData.UPGRADE;
		}
		structureData.modules[index] = moduleData;
		((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).refreshTabs();
	}
}