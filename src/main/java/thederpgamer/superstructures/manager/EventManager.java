package thederpgamer.superstructures.manager;

import api.listener.Listener;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.draw.RegisterWorldDrawersEvent;
import api.listener.events.register.ManagerContainerRegisterEvent;
import api.mod.StarLoader;
import org.schema.game.common.data.SegmentPiece;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.graphics.drawer.SuperStructureDrawer;
import thederpgamer.superstructures.structures.dysonsphere.DysonSphereManagerModule;
import thederpgamer.superstructures.utils.PlayerUtils;
import thederpgamer.superstructures.utils.StructureUtils;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class EventManager {

	public static void initialize(final SuperStructures instance) {
		StarLoader.registerListener(RegisterWorldDrawersEvent.class, new Listener<RegisterWorldDrawersEvent>() {
			@Override
			public void onEvent(RegisterWorldDrawersEvent event) {
				event.getModDrawables().add(GraphicsManager.getInstance().superStructureDrawer = new SuperStructureDrawer());
			}
		}, instance);

		StarLoader.registerListener(ManagerContainerRegisterEvent.class, new Listener<ManagerContainerRegisterEvent>() {
			@Override
			public void onEvent(ManagerContainerRegisterEvent event) {
				event.addModMCModule(new DysonSphereManagerModule(event.getSegmentController(), event.getContainer()));
			}
		}, instance);

		StarLoader.registerListener(SegmentPieceActivateByPlayer.class, new Listener<SegmentPieceActivateByPlayer>() {
			@Override
			public void onEvent(SegmentPieceActivateByPlayer event) {
				assert event.isServer();
				SegmentPiece segmentPiece = event.getSegmentPiece();
				if(StructureUtils.isValid(segmentPiece) || PlayerUtils.adminMode(event.getPlayer())) {
					StructureUtils.openGUI(segmentPiece, event.getPlayer());
				}
				/*
				if(segmentPiece.getInfo().getId() == ElementManager.getBlock("Dyson Sphere Controller").getId()) {
					if(StructureUtils.isValid(segmentPiece) || PlayerUtils.adminMode(event.getPlayer())) {
						GraphicsManager guiManager = GraphicsManager.getInstance();
						if(guiManager.dysonSphereControlManager == null) {
								}

					} else
				}
				 */
			}
		}, instance);
	}
}
