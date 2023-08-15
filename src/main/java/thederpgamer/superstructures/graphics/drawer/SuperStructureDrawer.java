package thederpgamer.superstructures.graphics.drawer;

import api.utils.draw.ModWorldDrawer;
import org.schema.schine.graphicsengine.core.Timer;
import thederpgamer.superstructures.data.structures.DysonSphereData;
import thederpgamer.superstructures.data.structures.SuperStructureData;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [12/06/2021]
 */
public class SuperStructureDrawer extends ModWorldDrawer {

	public static final ConcurrentHashMap<Long, SuperStructureData> drawMap = new ConcurrentHashMap<>();

	@Override
	public void onInit() {

	}

	@Override
	public void draw() {
		for(SuperStructureData data : drawMap.values()) data.draw();
	}

	@Override
	public void update(Timer timer) {
		for(SuperStructureData data : drawMap.values()) {
			if(data.updateTimer > 0) data.updateTimer -= timer.getDelta();
			else data.updateMesh();
		}
	}

	@Override
	public void cleanUp() {

	}

	@Override
	public boolean isInvisible() {
		return false;
	}

	public void addDrawData(DysonSphereData data) {
		if(drawMap.containsKey(data.segmentPiece.getAbsoluteIndex())) drawMap.replace(data.segmentPiece.getAbsoluteIndex(), data);
		else drawMap.put(data.segmentPiece.getAbsoluteIndex(), data);
	}

	public void removeDrawData(long abs) {
		if(drawMap.containsKey(abs)) {
			drawMap.get(abs).cleanUp();
			drawMap.remove(abs);
		}
	}
}
