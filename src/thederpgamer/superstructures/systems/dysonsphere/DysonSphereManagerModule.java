package thederpgamer.superstructures.systems.dysonsphere;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import api.utils.game.module.ModManagerContainerModule;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.schine.graphicsengine.core.Timer;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.elements.ElementManager;
import thederpgamer.superstructures.graphics.drawer.DysonSphereDrawData;
import thederpgamer.superstructures.utils.DataUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereManagerModule extends ModManagerContainerModule {

    public DysonSphereManagerModule(SegmentController segmentController, ManagerContainer<?> managerContainer) {
        super(segmentController, managerContainer, SuperStructures.getInstance(), ElementManager.getBlock("Dyson Sphere Controller").getId());
    }

    @Override
    public void handle(Timer timer) {

    }

    @Override
    public void onTagSerialize(PacketWriteBuffer packetWriteBuffer) throws IOException {
        ArrayList<DysonSphereDrawData> drawDataList = getDrawDataList();
        packetWriteBuffer.writeInt(drawDataList.size());
        if(drawDataList.size() > 0) {
            for(DysonSphereDrawData drawData : drawDataList) drawData.serialize(packetWriteBuffer);
        }
    }

    @Override
    public void onTagDeserialize(PacketReadBuffer packetReadBuffer) throws IOException {
        int size = packetReadBuffer.readInt();
        if(size > 0) {
            for(int i = 0; i < size; i ++) {
                SuperStructures.getInstance().dysonSphereDrawer.addDrawData(new DysonSphereDrawData(packetReadBuffer));
            }
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
    public void handlePlace(long absIndex, byte orientation) {
        super.handlePlace(absIndex, orientation);
        DataUtils.queueCreation(getManagerContainer().getSegmentController().getSegmentBuffer().getPointUnsave(ElementCollection.getPosIndexFrom4(absIndex)));
        SuperStructures.getInstance().dysonSphereDrawer.addDrawData(new DysonSphereDrawData(ElementCollection.getPosIndexFrom4(absIndex), getManagerContainer().getSegmentController(), DataUtils.getStructure(segmentController.getSector(new Vector3i()))));
    }

    @Override
    public void handleRemove(long absIndex) {
        super.handleRemove(absIndex);
        SuperStructures.getInstance().dysonSphereDrawer.removeDrawData(ElementCollection.getPosIndexFrom4(absIndex));
        DataUtils.queueRemoval(segmentController.getSystem(new Vector3i()));
    }

    public ArrayList<DysonSphereDrawData> getDrawDataList() {
        ArrayList<DysonSphereDrawData> drawDataList = new ArrayList<>();
        for(DysonSphereDrawData drawData : SuperStructures.getInstance().dysonSphereDrawer.drawDataMap.values()) {
            if(blocks.containsKey(drawData.index)) drawDataList.add(drawData);
        }
        return drawDataList;
    }
}
