package thederpgamer.superstructures.systems.dysonsphere;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import api.utils.game.module.ModManagerContainerModule;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.schine.graphicsengine.core.Timer;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.data.structures.DysonSphereData;
import thederpgamer.superstructures.elements.ElementManager;
import thederpgamer.superstructures.graphics.drawer.DysonSphereDrawer;
import thederpgamer.superstructures.utils.DysonSphereUtils;

import java.io.IOException;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereManagerModule extends ModManagerContainerModule {

    private DysonSphereData structureData;

    public DysonSphereManagerModule(SegmentController segmentController, ManagerContainer<?> managerContainer) {
        super(segmentController, managerContainer, SuperStructures.getInstance(), ElementManager.getBlock("Dyson Sphere Controller").getId());
    }

    @Override
    public void handle(Timer timer) {
        if(!DysonSphereDrawer.drawMap.containsKey(structureData.segmentPiece.getAbsoluteIndex()) && DysonSphereUtils.inDrawRange()) {
            SuperStructures.getInstance().dysonSphereDrawer.addDrawData(structureData);
        } else {
            if(DysonSphereDrawer.drawMap.containsKey(structureData.segmentPiece.getAbsoluteIndex())) SuperStructures.getInstance().dysonSphereDrawer.removeDrawData(structureData.segmentPiece.getAbsoluteIndex());
        }
    }

    @Override
    public void onTagSerialize(PacketWriteBuffer packetWriteBuffer) throws IOException {
        if(structureData != null) structureData.serialize(packetWriteBuffer);
    }

    @Override
    public void onTagDeserialize(PacketReadBuffer packetReadBuffer) throws IOException {
       structureData = new DysonSphereData(packetReadBuffer);
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
    public void handlePlace(long absIndex, byte orientation) {
        super.handlePlace(absIndex, orientation);
        structureData = DysonSphereUtils.generateStructureData(segmentController.getSegmentBuffer().getPointUnsave(absIndex));
    }

    @Override
    public void handleRemove(long absIndex) {
        super.handleRemove(absIndex);
        structureData = null;
    }

    public DysonSphereData getStructureData() {
        if(structureData == null) structureData = DysonSphereUtils.generateStructureData(segmentController.getSegmentBuffer().getPointUnsave(blocks.keySet().iterator().nextLong()));
        return structureData;
    }
}
