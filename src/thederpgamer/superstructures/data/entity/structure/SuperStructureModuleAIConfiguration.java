package thederpgamer.superstructures.data.entity.structure;

import org.schema.game.common.controller.ai.AIGameSegmentControllerConfiguration;
import org.schema.schine.network.StateInterface;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class SuperStructureModuleAIConfiguration extends AIGameSegmentControllerConfiguration<SuperStructureModuleAIEntity, SuperStructureModule> {

    public SuperStructureModuleAIConfiguration(StateInterface stateInterface, SuperStructureModule superStructureModule) {
        super(stateInterface, superStructureModule);
    }

    @Override
    protected SuperStructureModuleAIEntity getIdleEntityState() {
        return new SuperStructureModuleAIEntity("SSMAI", getOwner());
    }

    @Override
    protected boolean isForcedHitReaction() {
        return getOwner().getFactionId() < 0;
    }

    @Override
    protected void prepareActivation() {

    }
}
