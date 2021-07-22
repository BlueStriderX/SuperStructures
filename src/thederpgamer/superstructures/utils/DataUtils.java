package thederpgamer.superstructures.utils;

import api.common.GameClient;
import api.common.GameServer;
import api.mod.ModSkeleton;
import api.mod.config.PersistentObjectUtil;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.common.data.world.VoidSystem;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.data.structures.DysonSphereData;
import thederpgamer.superstructures.data.structures.SuperStructureData;
import thederpgamer.superstructures.elements.ElementManager;
import thederpgamer.superstructures.manager.ConfigManager;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DataUtils {

    public static final int CREATE = 0;
    public static final int UPDATE = 1;
    public static final int REMOVE = 2;

    private static final ModSkeleton instance = SuperStructures.getInstance().getSkeleton();
    private static final ConcurrentHashMap<Vector3i, SuperStructureData> superStructureMap = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<Integer[]> updateQueue = new ConcurrentLinkedQueue<>();

    public static void initialize() {
        for(Object obj : PersistentObjectUtil.getObjects(instance, SuperStructureData.class)) {
            SuperStructureData structureData = (SuperStructureData) obj;
            superStructureMap.put(structureData.systemPos, structureData);
        }
    }

    public static void saveData() {
        updateStructureMap();
        PersistentObjectUtil.save(instance);
    }

    public static void updateStructureMap() {
        while(!updateQueue.isEmpty()) {
            Integer[] values = updateQueue.poll();
            assert values.length == 4;
            int updateType = values[0];
            Vector3i systemPos = new Vector3i(values[1], values[2], values[3]);
            SuperStructureData structureData = superStructureMap.get(systemPos);
            assert structureData != null;
            switch(updateType) {
                case CREATE:
                    PersistentObjectUtil.removeObject(instance, structureData);
                    PersistentObjectUtil.addObject(instance, structureData);
                    break;
                case UPDATE:
                    PersistentObjectUtil.removeObject(instance, structureData);
                    PersistentObjectUtil.addObject(instance, structureData);
                    superStructureMap.remove(systemPos);
                    superStructureMap.put(systemPos, structureData);
                    break;
                case REMOVE:
                    PersistentObjectUtil.removeObject(instance, structureData);
                    superStructureMap.remove(systemPos);
                    break;
            }
        }
    }

    public static boolean hasSuperStructure(Vector3i systemPos) {
        return superStructureMap.containsKey(systemPos);
    }

    public static boolean hasSuperStructure(StellarSystem system) {
        return superStructureMap.containsKey(system.getPos());
    }

    public static boolean hasSuperStructure(Sector sector) {
        return superStructureMap.containsKey(sector._getSystem().getPos());
    }

    public static SuperStructureData getStructure(Vector3i systemPos) {
        return superStructureMap.get(systemPos);
    }

    public static SuperStructureData getStructure(StellarSystem system) {
        return superStructureMap.get(system.getPos());
    }

    public static SuperStructureData getStructure(Sector sector) {
        return superStructureMap.get(sector._getSystem().getPos());
    }

    public static void queueCreation(SegmentPiece segmentPiece) {
        Vector3i systemPos = segmentPiece.getSegmentController().getSystem(new Vector3i());
        superStructureMap.put(systemPos, Objects.requireNonNull(generateStructureData(segmentPiece)));
        updateQueue.add(new Integer[] {CREATE, systemPos.x, systemPos.y, systemPos.z});
    }

    public static void queueUpdate(SuperStructureData structureData) {
        updateQueue.add(new Integer[] {UPDATE, structureData.systemPos.x, structureData.systemPos.y, structureData.systemPos.z});
    }

    public static void queueRemoval(Vector3i systemPos) {
        updateQueue.add(new Integer[] {REMOVE, systemPos.x, systemPos.y, systemPos.z});
    }

    public static boolean adminMode() {
        return ConfigManager.getMainConfig().getBoolean("debug-mode") && GameClient.getClientPlayerState().isAdmin() && GameClient.getClientPlayerState().isCreativeModeEnabled();
    }

    private static SuperStructureData generateStructureData(SegmentPiece segmentPiece) {
        if(segmentPiece.getInfo().getId() == ElementManager.getBlock("Dyson Sphere Controller").getId()) {
            try {
                return new DysonSphereData(GameServer.getUniverse().getSector(VoidSystem.getSunSectorPosAbs(GameClient.getClientState().getCurrentGalaxy(), segmentPiece.getSegmentController().getSystem(new Vector3i()), new Vector3i())), segmentPiece);
            } catch(IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
