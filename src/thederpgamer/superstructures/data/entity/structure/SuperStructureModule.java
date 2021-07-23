package thederpgamer.superstructures.data.entity.structure;

import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.network.StateInterface;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class SuperStructureModule extends Ship {

    protected SpaceStation controllerStation;

    public SuperStructureModule(StateInterface state, SpaceStation controllerStation) {
        super(state);
        this.controllerStation = controllerStation;
    }

    public SpaceStation getControllerStation() {
        return controllerStation;
    }

    @Override
    protected String getSegmentControllerTypeString() {
        return "Super Structure Module";
    }

    @Override
    public boolean isHomeBase() {
        return false;
    }

    @Override
    public void destroyPersistent() {
        super.destroyPersistent();
        Sector sector = ((GameServerState) getState()).getUniverse().getSector(getSectorId());
        Vector3i sysPos = StellarSystem.getPosFromSector(new Vector3i(sector.pos), new Vector3i());
        ((GameServerState) getState()).getGameMapProvider().updateMapForAllInSystem(sysPos); //Todo: Super Structure map icon
    }
}
