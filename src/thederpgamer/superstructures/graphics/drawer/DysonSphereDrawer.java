package thederpgamer.superstructures.graphics.drawer;

import api.utils.draw.ModWorldDrawer;
import org.schema.schine.graphicsengine.core.Timer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [12/06/2021]
 */
public class DysonSphereDrawer extends ModWorldDrawer {

    public final ConcurrentHashMap<Long, DysonSphereDrawData> drawDataMap = new ConcurrentHashMap<>();

    @Override
    public void onInit() {

    }

    @Override
    public void draw() {
        for(DysonSphereDrawData drawData : drawDataMap.values()) drawData.draw();
    }

    @Override
    public void update(Timer timer) {
        for(DysonSphereDrawData drawData : drawDataMap.values()) {
            if(drawData.updateTimer > 0) drawData.updateTimer -= timer.getDelta();
            else drawData.updateMesh();
        }
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    public void addDrawData(DysonSphereDrawData drawData) {
        if(drawDataMap.containsKey(drawData.index)) drawDataMap.replace(drawData.index, drawData);
        else drawDataMap.put(drawData.index, drawData);
    }

    public void removeDrawData(long abs) {
        if(drawDataMap.containsKey(abs)) {
            drawDataMap.get(abs).cleanUp();
            drawDataMap.remove(abs);
        }
    }
}
