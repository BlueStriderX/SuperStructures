package thederpgamer.superstructures.utils;

import api.common.GameClient;
import api.mod.ModSkeleton;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.manager.ConfigManager;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DataUtils {

    /*
    public static final int CREATE = 0;
    public static final int UPDATE = 1;
    public static final int REMOVE = 2;
     */

    private static final ModSkeleton instance = SuperStructures.getInstance().getSkeleton();

    /* Can't use input streams >:(
    public static BlueprintEntry getModuleBp(StructureModuleData moduleData) {
        String moduleType = "";
        if(moduleData instanceof DysonSpherePowerModuleData) moduleType = "powermodule";
        else if(moduleData instanceof DysonSphereResourceModuleData) moduleType = "resourcemodule";
        else if(moduleData instanceof DysonSphereFoundryModuleData) moduleType = "foundry";
        else if(moduleData instanceof DysonSphereShipyardModuleData) moduleType = "shipyard";
        else if(moduleData instanceof DysonSphereOffenseModuleData) moduleType = "offense";
        else if(moduleData instanceof DysonSphereDefenseModuleData) moduleType = "defense";
        else if(moduleData instanceof DysonSphereSupportModuleData) moduleType = "support";
        return BluePrintController.active.importFile(SuperStructures.getInstance().getJarResource("thederpgamer/superstructures/resources/blueprints/dysonsphere/" + moduleType + ".sment"), null).get(0);
    }
     */
    /*

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
        updateQueue.add(new Integer[] {UPDATE, structureData.system.x, structureData.system.y, structureData.system.z});
    }

    public static void queueRemoval(Vector3i systemPos) {
        updateQueue.add(new Integer[] {REMOVE, systemPos.x, systemPos.y, systemPos.z});
    }
     */

    public static boolean adminMode() {
        return ConfigManager.getMainConfig().getBoolean("debug-mode") && GameClient.getClientPlayerState().isAdmin() && GameClient.getClientPlayerState().isCreativeModeEnabled();
    }
}
