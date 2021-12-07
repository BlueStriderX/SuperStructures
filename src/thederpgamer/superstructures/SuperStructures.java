package thederpgamer.superstructures;

import api.common.GameClient;
import api.config.BlockConfig;
import api.listener.Listener;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.draw.RegisterWorldDrawersEvent;
import api.listener.events.register.ManagerContainerRegisterEvent;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.utils.gui.ModGUIHandler;
import api.utils.gui.SimplePopup;
import org.schema.game.common.data.SegmentPiece;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.superstructures.elements.ElementManager;
import thederpgamer.superstructures.elements.blocks.systems.DysonSphereController;
import thederpgamer.superstructures.graphics.drawer.DysonSphereDrawer;
import thederpgamer.superstructures.graphics.gui.dysonsphere.DysonSphereControlManager;
import thederpgamer.superstructures.graphics.gui.dysonsphere.DysonSphereMenuPanel;
import thederpgamer.superstructures.manager.ConfigManager;
import thederpgamer.superstructures.manager.ResourceManager;
import thederpgamer.superstructures.systems.dysonsphere.DysonSphereManagerModule;
import thederpgamer.superstructures.utils.DataUtils;
import thederpgamer.superstructures.utils.DysonSphereUtils;
import thederpgamer.superstructures.utils.EntityUtils;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class SuperStructures extends StarMod {

    //Instance
    private static SuperStructures instance;
    public static SuperStructures getInstance() {
        return instance;
    }
    public static void main(String[] args) {

    }
    public SuperStructures() { }

    //Data
    public DysonSphereControlManager dysonSphereControlManager;

    //Drawers
    public DysonSphereDrawer dysonSphereDrawer;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.initialize(this);
        registerListeners();
    }

    @Override
    public void onResourceLoad(ResourceLoader loader) {
        ResourceManager.loadResources(this, loader);
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        ElementManager.addBlock(new DysonSphereController());

        ElementManager.initialize();
    }

    private void registerListeners() {
        StarLoader.registerListener(RegisterWorldDrawersEvent.class, new Listener<RegisterWorldDrawersEvent>() {
            @Override
            public void onEvent(RegisterWorldDrawersEvent event) {
                event.getModDrawables().add(dysonSphereDrawer = new DysonSphereDrawer());
            }
        }, this);

        StarLoader.registerListener(ManagerContainerRegisterEvent.class, new Listener<ManagerContainerRegisterEvent>() {
            @Override
            public void onEvent(ManagerContainerRegisterEvent event) {
                event.addModMCModule(new DysonSphereManagerModule(event.getSegmentController(), event.getContainer()));
            }
        }, this);

        StarLoader.registerListener(SegmentPieceActivateByPlayer.class, new Listener<SegmentPieceActivateByPlayer>() {
            @Override
            public void onEvent(SegmentPieceActivateByPlayer event) {
                SegmentPiece segmentPiece = event.getSegmentPiece();
                if(segmentPiece.getInfo().getId() == ElementManager.getBlock("Dyson Sphere Controller").getId()) {
                    if(DysonSphereUtils.isValidForDysonSphere(segmentPiece) || DataUtils.adminMode()) {
                        if(dysonSphereControlManager == null) {
                            dysonSphereControlManager = new DysonSphereControlManager();
                            ModGUIHandler.registerNewControlManager(getSkeleton(), dysonSphereControlManager);
                        }
                        dysonSphereControlManager.structureData = ((DysonSphereManagerModule) EntityUtils.getManagerContainer(segmentPiece.getSegmentController()).getModMCModule(segmentPiece.getType())).getStructureData();
                        dysonSphereControlManager.onInit();
                        ((DysonSphereMenuPanel) dysonSphereControlManager.getMenuPanel()).structureData = dysonSphereControlManager.structureData;
                        dysonSphereControlManager.setActive(true);
                    } else new SimplePopup(GameClient.getClientState(), "Invalid Location", "This is an invalid location for a Dyson Sphere structure and therefore cannot be used.").activate();
                }
            }
        }, this);
    }
}
