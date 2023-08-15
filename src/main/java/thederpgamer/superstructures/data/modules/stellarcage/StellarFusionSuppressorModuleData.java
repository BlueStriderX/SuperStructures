package thederpgamer.superstructures.data.modules.stellarcage;

import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.structures.StellarCageStructureData;
import thederpgamer.superstructures.data.modules.StructureModuleActivationInterface;

/**
 * [Description]
 *
 * @author TheDerpGamer (TheDerpGamer#0027)
 */
public class StellarFusionSuppressorModuleData extends StructureModuleData implements StructureModuleActivationInterface {

	public StellarFusionSuppressorModuleData(StellarCageStructureData structureData) {
		super(structureData, "Stellar Fusion Suppressor", "Suppresses the fusion process in a star, gradually shrinking it and decreasing it's heat damage radius and resource capacity.", 5);
	}

	@Override
	public void activate() {

	}

	@Override
	public void deactivate() {

	}

	@Override
	public boolean canActivate() {
		return ((StellarCageStructureData) getStructureData()).getSunType().previousIndex != -1;
	}
}