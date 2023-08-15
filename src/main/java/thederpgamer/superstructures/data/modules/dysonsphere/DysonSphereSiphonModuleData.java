package thederpgamer.superstructures.data.modules.dysonsphere;

import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.modules.StructureModuleUpdateInterface;
import thederpgamer.superstructures.data.structures.DysonSphereData;
import thederpgamer.superstructures.data.structures.SunType;
import thederpgamer.superstructures.data.structures.SuperStructureData;
import thederpgamer.superstructures.manager.ConfigManager;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/22/2021
 */
public class DysonSphereSiphonModuleData extends StructureModuleData implements StructureModuleUpdateInterface {

    public DysonSphereSiphonModuleData(SuperStructureData structureData) {
        super(structureData, "Stellar Siphon Module", "Generates resources by siphoning them from the star.", 5);
    }

    @Override
    public long getUpdateInterval() {
        return ConfigManager.getMainConfig().getLong("dyson-sphere-siphon-gen-frequency");
    }

    @Override
    public void update() {

    }

    public SunType getSunType() {
        return ((DysonSphereData) getStructureData()).getSunType();
    }
}
