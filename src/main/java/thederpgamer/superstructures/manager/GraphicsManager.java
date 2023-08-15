package thederpgamer.superstructures.manager;

import api.common.GameCommon;
import api.utils.gui.ModGUIHandler;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.PlayerState;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.graphics.drawer.SuperStructureDrawer;
import thederpgamer.superstructures.graphics.gui.dysonsphere.DysonSphereControlManager;
import thederpgamer.superstructures.graphics.gui.dysonsphere.DysonSphereMenuPanel;
import thederpgamer.superstructures.structures.dysonsphere.DysonSphereManagerModule;
import thederpgamer.superstructures.utils.EntityUtils;

import java.util.Objects;

/**
 * Manages and holds GUIs and GUI-related data.
 *
 * @author TheDerpGamer
 */
public class GraphicsManager {

	private static GraphicsManager instance;

	public static GraphicsManager getInstance() {
		if(instance == null) instance = new GraphicsManager();
		return instance;
	}

	//Data
	public DysonSphereControlManager dysonSphereControlManager;

	//Drawers
	public SuperStructureDrawer superStructureDrawer;

	public void openDysonSphereControlManager(SegmentPiece segmentPiece, PlayerState playerState) {
		if(GameCommon.isClientConnectedToServer() || (GameCommon.isOnSinglePlayer() && !segmentPiece.getSegmentController().isOnServer())) {
			if(dysonSphereControlManager == null) {
				dysonSphereControlManager = new DysonSphereControlManager();
				ModGUIHandler.registerNewControlManager(SuperStructures.getInstance().getSkeleton(), dysonSphereControlManager);;
			}
			dysonSphereControlManager.structureData = ((DysonSphereManagerModule) Objects.requireNonNull(EntityUtils.getManagerContainer(segmentPiece.getSegmentController())).getModMCModule(segmentPiece.getType())).getStructureData();
			dysonSphereControlManager.onInit();
			((DysonSphereMenuPanel) dysonSphereControlManager.getMenuPanel()).structureData = dysonSphereControlManager.structureData;
			dysonSphereControlManager.setActive(true);
		} else NetworkManager.sendOpenGUIPacket(segmentPiece, playerState);
	}
}
