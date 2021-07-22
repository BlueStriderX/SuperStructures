package thederpgamer.superstructures;

import api.common.GameClient;
import api.config.BlockConfig;
import api.listener.Listener;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.block.SegmentPieceAddEvent;
import api.listener.events.block.SegmentPieceRemoveEvent;
import api.listener.events.draw.RegisterWorldDrawersEvent;
import api.listener.events.input.KeyPressEvent;
import api.listener.events.register.ManagerContainerRegisterEvent;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.utils.StarRunnable;
import api.utils.gui.ModGUIHandler;
import api.utils.gui.SimplePopup;
import org.lwjgl.input.Keyboard;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.superstructures.data.commands.UpgradeSuperStructureCommand;
import thederpgamer.superstructures.data.shapes.Shape3D;
import thederpgamer.superstructures.elements.ElementManager;
import thederpgamer.superstructures.elements.blocks.systems.DysonSphereController;
import thederpgamer.superstructures.graphics.drawer.DysonSphereOutlineDrawer;
import thederpgamer.superstructures.graphics.gui.dysonsphere.DysonSphereControlManager;
import thederpgamer.superstructures.manager.ConfigManager;
import thederpgamer.superstructures.manager.ResourceManager;
import thederpgamer.superstructures.systems.dysonsphere.DysonSphereManagerModule;
import thederpgamer.superstructures.utils.DataUtils;
import thederpgamer.superstructures.utils.DysonSphereUtils;

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
    public SuperStructures() {

    }

    //Data
    public DysonSphereControlManager dysonSphereControlManager;
    public DysonSphereOutlineDrawer dysonSphereOutlineDrawer;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.initialize(this);
        DataUtils.initialize();
        registerListeners();
        registerCommands();
        startRunners();
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
        StarLoader.registerListener(KeyPressEvent.class, new Listener<KeyPressEvent>() {
            @Override
            public void onEvent(KeyPressEvent event) {
                if(!GameClient.getClientState().getGlobalGameControlManager().getIngameControlManager().isAnyMenuOrChatActive() && event.isKeyDown() && event.getRawEvent().getKey() == Keyboard.KEY_EQUALS && ConfigManager.getMainConfig().getBoolean("debug-mode")) {
                    int drawMode = dysonSphereOutlineDrawer.outlineShape.getDrawMode();
                    switch(drawMode) {
                        case Shape3D.NONE:
                            GameClient.getClientPlayerState().getWordTransform(dysonSphereOutlineDrawer.outlineShape.getTransform());
                            dysonSphereOutlineDrawer.outlineShape.setDrawMode(Shape3D.WIREFRAME);
                            break;
                        case Shape3D.WIREFRAME:
                            dysonSphereOutlineDrawer.outlineShape.setDrawMode(Shape3D.FILLED);
                            break;
                        case Shape3D.FILLED:
                            dysonSphereOutlineDrawer.outlineShape.setDrawMode(Shape3D.NONE);
                            break;
                    }
                }
            }
        }, this);

        StarLoader.registerListener(RegisterWorldDrawersEvent.class, new Listener<RegisterWorldDrawersEvent>() {
            @Override
            public void onEvent(RegisterWorldDrawersEvent event) {
                event.getModDrawables().add(dysonSphereOutlineDrawer = new DysonSphereOutlineDrawer());
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
                if(segmentPiece.getType() == ElementManager.getBlock("Dyson Sphere Controller").getId()) {
                    if(DysonSphereUtils.isValidForDysonSphere(segmentPiece) || (GameClient.getClientPlayerState().isAdmin()) && ConfigManager.getMainConfig().getBoolean("debug-mode")) {
                        if(dysonSphereControlManager == null) ModGUIHandler.registerNewControlManager(getSkeleton(), dysonSphereControlManager = new DysonSphereControlManager());
                        dysonSphereControlManager.structureData = DataUtils.getStructure(segmentPiece.getSegmentController().getSystem(new Vector3i()));
                        dysonSphereControlManager.setActive(true);
                    } else new SimplePopup(GameClient.getClientState(), "Invalid Location", "This is an invalid location for a Dyson Sphere structure and therefore cannot be used.").activate();
                }
            }
        }, this);

        StarLoader.registerListener(SegmentPieceAddEvent.class, new Listener<SegmentPieceAddEvent>() {
            @Override
            public void onEvent(SegmentPieceAddEvent event) {
                if(event.getNewType() == ElementManager.getBlock("Dyson Sphere Controller").getId()) {
                    SegmentPiece segmentPiece = event.getSegmentController().getSegmentBuffer().getPointUnsave(event.getAbsIndex());
                    if(DysonSphereUtils.isValidForDysonSphere(segmentPiece)) DataUtils.queueCreation(segmentPiece);
                }
            }
        }, this);

        StarLoader.registerListener(SegmentPieceRemoveEvent.class, new Listener<SegmentPieceRemoveEvent>() {
            @Override
            public void onEvent(SegmentPieceRemoveEvent event) {
                if(event.getType() == ElementManager.getBlock("Dyson Sphere Controller").getId()) {
                    if(DataUtils.hasSuperStructure(event.getSegment().getSegmentController().getSystem(new Vector3i()))) DataUtils.queueRemoval(event.getSegment().getSegmentController().getSystem(new Vector3i()));
                }
            }
        }, this);
    }

    private void registerCommands() {
        StarLoader.registerCommand(new UpgradeSuperStructureCommand());
    }

    private void startRunners() {
        new StarRunnable() {
            @Override
            public void run() {
                DataUtils.saveData();
            }
        }.runTimer(this, ConfigManager.getMainConfig().getLong("auto-save-frequency"));
    }
}
