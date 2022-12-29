package thederpgamer.superstructures.systems.dysonsphere;

import api.utils.game.module.util.SimpleDataStorageMCModule;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.schine.graphicsengine.core.Timer;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.data.structures.DysonSphereData;
import thederpgamer.superstructures.elements.ElementManager;
import thederpgamer.superstructures.graphics.drawer.DysonSphereDrawer;
import thederpgamer.superstructures.utils.DysonSphereUtils;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereManagerModule extends SimpleDataStorageMCModule {

    public DysonSphereManagerModule(SegmentController segmentController, ManagerContainer<?> managerContainer) {
        super(segmentController, managerContainer, SuperStructures.getInstance(), ElementManager.getBlock("Dyson Sphere Controller").getId());
    }

    @Override
    public void handle(Timer timer) {
        try {
            if(!DysonSphereDrawer.drawMap.containsKey(getStructureData().segmentPiece.getAbsoluteIndex()) && DysonSphereUtils.inDrawRange()) {
                SuperStructures.getInstance().dysonSphereDrawer.addDrawData(getStructureData());
            } else {
                if(DysonSphereDrawer.drawMap.containsKey(getStructureData().segmentPiece.getAbsoluteIndex()) && !DysonSphereUtils.inDrawRange()) SuperStructures.getInstance().dysonSphereDrawer.removeDrawData(getStructureData().segmentPiece.getAbsoluteIndex());
            }
        } catch(NullPointerException exception) {
            //Ignore
        }
    }

    @Override
    public double getPowerConsumedPerSecondResting() {
        return 0;
    }

    @Override
    public double getPowerConsumedPerSecondCharging() {
        return 0;
    }

    @Override
    public String getName() {
        return "Dyson Sphere Controller";
    }

    @Override
    public void handlePlace(final long absIndex, byte orientation) {
        super.handlePlace(absIndex, orientation);
    }

    @Override
    public void handleRemove(long absIndex) {
        super.handleRemove(absIndex);
        data = null;
    }

    public DysonSphereData getStructureData() {
        try {
            if(data == null && isOnServer()) data = DysonSphereUtils.generateStructureData(segmentController.getSegmentBuffer().getPointUnsave(blocks.keySet().iterator().next()));
        } catch(Exception ignored) {}
        return (DysonSphereData) data;
    }
}
