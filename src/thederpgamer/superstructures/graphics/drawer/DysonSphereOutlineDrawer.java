package thederpgamer.superstructures.graphics.drawer;

import api.common.GameClient;
import api.common.GameServer;
import api.utils.draw.ModWorldDrawer;
import com.bulletphysics.linearmath.Transform;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.server.data.Galaxy;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.core.Timer;
import thederpgamer.superstructures.data.shapes.Shape3D;
import thederpgamer.superstructures.data.structures.DysonSphereData;
import thederpgamer.superstructures.manager.ConfigManager;
import thederpgamer.superstructures.manager.ResourceManager;
import thederpgamer.superstructures.utils.DataUtils;
import thederpgamer.superstructures.utils.DysonSphereUtils;
import javax.vecmath.Vector4f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereOutlineDrawer extends ModWorldDrawer implements Drawable {

    public Shape3D outlineShape;
    public Vector4f color;
    private float maxDistance;
    private int sectorSize;
    private DysonSphereData sphereData;

    @Override
    public void onInit() {
        sectorSize = (int) ServerConfig.SECTOR_SIZE.getCurrentState();
        outlineShape = ResourceManager.getShape("dodecahedron");
        outlineShape.setScale(sectorSize * 0.8f);
        outlineShape.setTransform(new Transform());
        outlineShape.onInit();
        maxDistance = (float) ConfigManager.getMainConfig().getDouble("max-dyson-sphere-station-distance");
        color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        ServerConfig.STAR_DAMAGE.setCurrentState(false);
    }

    @Override
    public void draw() {
        if(doDraw()) {
            if(outlineShape.getDrawMode() != Shape3D.WIREFRAME) outlineShape.setDrawMode(Shape3D.WIREFRAME);
            outlineShape.setTransform(getStarTransform());
            outlineShape.setColor(color);
            outlineShape.draw();
        } else outlineShape.setDrawMode(Shape3D.NONE);
    }

    @Override
    public void update(Timer timer) {

    }

    @Override
    public void cleanUp() {

    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    public boolean doDraw() {
        Vector3i clientSystem = GameClient.getClientPlayerState().getCurrentSystem();
        return DataUtils.hasSuperStructure(clientSystem) && DataUtils.getStructure(clientSystem) instanceof DysonSphereData && DysonSphereUtils.isClientInDrawRange(maxDistance);
    }

    private Transform getStarTransform() {
        StellarSystem system = GameServer.getUniverse().getSector(GameClient.getClientPlayerState().getSectorId())._getSystem();
        Transform transform = new Transform();
        transform.setIdentity();
        Vector3i sector = DataUtils.getStructure(system).sectorPos;
        Vector3i relativeSector = new Vector3i(sector);
        Vector3i currentSec = GameClientState.instance.getPlayer().getCurrentSector();
        relativeSector.sub(currentSec);
        Vector3i relPosInGalaxy = Galaxy.getRelPosInGalaxyFromAbsSystem(system.getPos(), new Vector3i());
        Vector3i sunPositionOffset = GameClient.getClientState().getCurrentGalaxy().getSunPositionOffset(relPosInGalaxy, new Vector3i());
        relativeSector.add(sunPositionOffset);
        relativeSector.scale(sectorSize);
        transform.origin.set(relativeSector.toVector3f());
        if(DataUtils.getStructure(system) instanceof DysonSphereData) outlineShape.setStructureData(DataUtils.getStructure(system));
        return transform;
    }
}
