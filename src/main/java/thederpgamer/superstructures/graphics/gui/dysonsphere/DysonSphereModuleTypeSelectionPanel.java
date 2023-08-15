package thederpgamer.superstructures.graphics.gui.dysonsphere;

import api.common.GameClient;
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
import thederpgamer.superstructures.utils.PlayerUtils;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/22/2021
 */
public class DysonSphereModuleTypeSelectionPanel extends GUIInputDialogPanel {

    private final DysonSphereModuleTypeSelectionDialog dialog;
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
        GUITile resourceTile = tilePane.addButtonTile("RESOURCE", "Generates resources by siphoning them from the star.", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
            @Override
            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
                if(mouseEvent.pressedLeftMouse()) {
                    getState().getController().queueUIAudio("0022_menu_ui - enter");
                    queueModuleConstruction(new DysonSphereSiphonModuleData(structureData));
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

        GUITile foundryTile = tilePane.addButtonTile("FOUNDRY", "Manufactures components and materials on a massive scale using the heat from it's star.", GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
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
    }

    private void queueModuleConstruction(StructureModuleData moduleData) {
        if(PlayerUtils.adminMode()) {
            moduleData.setStatus(StructureModuleData.NONE);
            api.utils.game.PlayerUtils.sendMessage(GameClient.getClientPlayerState(), "[DEBUG]: Admin mode used to construct a new dyson sphere module in " + GameClient.getClientPlayerState().getCurrentSector().toString() + ":\n" + moduleData);
        } else {
            for(StructureModuleData data : structureData.modules) {
                if(data.getStatus() != StructureModuleData.NONE) {
                    new SimplePopup(getState(), "Cannot Construct", "There is already a module construction or upgrade in process!").activate();
                    return;
                }
            }
            moduleData.setStatus(StructureModuleData.CONSTRUCTION);
            //Todo: Queue module construction
        }
        moduleData.setLevel(1);
        structureData.modules[index] = moduleData;
        ((DysonSphereMenuPanel) SuperStructures.getInstance().dysonSphereControlManager.getMenuPanel()).refreshTabs();
    }
}