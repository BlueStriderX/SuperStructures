package thederpgamer.superstructures;

import api.config.BlockConfig;
import api.mod.StarMod;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.superstructures.elements.ElementManager;
import thederpgamer.superstructures.elements.blocks.systems.DysonSphereController;
import thederpgamer.superstructures.manager.ConfigManager;
import thederpgamer.superstructures.manager.EventManager;
import thederpgamer.superstructures.manager.ResourceManager;

/**
 * Main mod class.
 *
 * @author TheDerpGamer
 */
public class SuperStructures extends StarMod {

	//Instance
	private static SuperStructures instance;
	public static SuperStructures getInstance() {
		return instance;
	}
	public static void main(String[] args) {}
	public SuperStructures() { }

	@Override
	public void onEnable() {
		instance = this;
		ConfigManager.initialize(this);
		EventManager.initialize(this);
	}

	@Override
	public void onResourceLoad(ResourceLoader loader) {
		ResourceManager.loadResources(this, loader);
	}

	@Override
	public void onBlockConfigLoad(BlockConfig config) {
		ElementManager.addBlock(new DysonSphereController());
		ElementManager.initialize();
	}
}
