package thederpgamer.superstructures.data.modules.stellarcage;

import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.structures.StellarCageStructureData;
import thederpgamer.superstructures.data.modules.StructureModuleActivationInterface;

/**
 * [Description]
 *
 * @author TheDerpGamer (TheDerpGamer#0027)
 */
public class StellarLifterModuleData extends StructureModuleData implements StructureModuleActivationInterface {

	public StellarLifterModuleData(StellarCageStructureData structureData) {
		super(structureData, "Stellar Lifter", "Increases the stars mass and density, upgrading it's resource capability but also it's heat radius.", 5);
	}

	@Override
	public void activate() {

	}

	@Override
	public void deactivate() {

	}

	@Override
	public boolean canActivate() {
		return ((StellarCageStructureData) getStructureData()).getSunType().nextIndex != -1;
	}
}