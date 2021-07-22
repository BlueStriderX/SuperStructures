package thederpgamer.superstructures.data.entity.structure;

import org.schema.game.server.ai.SegmentControllerAIEntity;
import org.schema.schine.ai.stateMachines.FSMException;
import org.schema.schine.graphicsengine.core.Timer;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class SuperStructureModuleAIEntity extends SegmentControllerAIEntity<SuperStructureModule> {

    public SuperStructureModuleAIEntity(String name, SuperStructureModule superStructureModule) {
        super(name, superStructureModule);
    }

    @Override
    public void updateAIClient(Timer timer) {

    }

    @Override
    public void updateAIServer(Timer timer) throws FSMException {

    }
}
