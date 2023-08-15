package thederpgamer.superstructures.data.modules.dysonsphere;

import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.modules.StructureModuleProcessingInterface;
import thederpgamer.superstructures.data.structures.SuperStructureData;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/22/2021
 */
public class DysonSphereFoundryModuleData extends StructureModuleData implements StructureModuleProcessingInterface {

    public DysonSphereFoundryModuleData(SuperStructureData structureData) {
        super(structureData, "Stellar Foundry Module", "Manufactures components and materials on a massive scale using the heat from it's star.", 5);
    }

    @Override
    public void process() {

    }

    @Override
    public boolean canProcess() {
        return false;
    }
}