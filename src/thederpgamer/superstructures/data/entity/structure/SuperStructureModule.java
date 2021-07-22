package thederpgamer.superstructures.data.entity.structure;

import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.manager.ingame.PlayerGameControlManager;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.shiphud.newhud.ColorPalette;
import org.schema.game.common.Starter;
import org.schema.game.common.controller.ManagedUsableSegmentController;
import org.schema.game.common.controller.Salvager;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.faction.FactionRelation;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.ai.stateMachines.AIConfigurationInterface;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.Sendable;
import javax.vecmath.Vector4f;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public abstract class SuperStructureModule extends ManagedUsableSegmentController<SuperStructureModule> {

    protected SpaceStation controllerStation;
    protected SuperStructureModuleManagerContainer managerContainer;
    protected SuperStructureModuleAIConfiguration aiConfiguration;

    public SuperStructureModule(StateInterface state, SpaceStation controllerStation) {
        super(state);
        this.controllerStation = controllerStation;
        this.managerContainer = new SuperStructureModuleManagerContainer(state, this);
        this.aiConfiguration = new SuperStructureModuleAIConfiguration(state, this);
    }

    public SpaceStation getControllerStation() {
        return controllerStation;
    }

    @Override
    public void onDetachPlayer(PlayerState playerState, boolean b, Vector3i vector3i) {
        if(!isOnServer()) {
            GameClientState s = (GameClientState) getState();
            if(s.getPlayer() == playerState && ((GameClientState) getState()).getPlayer() == playerState) {
                PlayerGameControlManager playerGameControlManager = s.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager();
                playerGameControlManager.getPlayerIntercationManager().getSegmentControlManager().setActive(false);
            }
        }
        Starter.modManager.onSegmentControllerPlayerDetached(this);
    }

    @Override
    public void onAttachPlayer(PlayerState playerState, Sendable detachedFrom, Vector3i where, Vector3i parameter) {
        super.onAttachPlayer(playerState, detachedFrom, where, parameter);
        if(!isOnServer() && ((GameClientState) getState()).getPlayer() == playerState) {
            GameClientState s = (GameClientState) getState();
            if(s.getPlayer() == playerState) {
                PlayerGameControlManager playerGameControlManager = s.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager();
                playerGameControlManager.getPlayerIntercationManager().getSegmentControlManager().setActive(true);
            }
        }
    }

    @Override
    protected void onCoreDestroyed(Damager damager) {

    }

    @Override
    public boolean isSalvagableFor(Salvager salvager, String[] strings, Vector3i vector3i) {
        return false;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public boolean isMoved() {
        return false;
    }

    @Override
    public void setMoved(boolean b) {

    }

    @Override
    public ManagerContainer<SuperStructureModule> getManagerContainer() {
        return managerContainer;
    }

    @Override
    public SegmentController getSegmentController() {
        return this;
    }

    @Override
    public EntityType getType() {
        return EntityType.SPACE_STATION;
    }

    @Override
    public AIConfigurationInterface getAiConfiguration() {
        return aiConfiguration;
    }

    @Override
    protected boolean affectsGravityOf(SimpleTransformableSendableObject<?> target) {
        return false;
    }

    @Override
    public void getRelationColor(FactionRelation.RType relation, boolean sameFaction, Vector4f out, float select, float pulse) {
        switch(relation) {
            case ENEMY:
                out.set(ColorPalette.enemyStation);
                break;
            case FRIEND:
                out.set(ColorPalette.allyStation);
                break;
            case NEUTRAL:
                out.set(ColorPalette.neutralStation);
                break;
        }
        if(sameFaction) out.set(ColorPalette.factionStation);
        out.x += select;
        out.y += select;
        out.z += select;
    }

    @Override
    public void initialize() {
        super.initialize();
        setMass(0);
    }

    @Override
    public boolean isGravitySource() {
        return true;
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
