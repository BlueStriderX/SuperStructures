package thederpgamer.superstructures.data.entity.structure;

import org.schema.game.common.controller.elements.DoorContainerInterface;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.StationaryManagerContainer;
import org.schema.game.common.data.player.inventory.InventoryHolder;
import org.schema.schine.network.StateInterface;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class SuperStructureModuleManagerContainer extends StationaryManagerContainer<SuperStructureModule> implements ShieldContainerInterface, InventoryHolder, DoorContainerInterface {

    public SuperStructureModuleManagerContainer(StateInterface stateInterface, SuperStructureModule superStructureModule) {
        super(stateInterface, superStructureModule);
    }
}
