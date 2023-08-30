package thederpgamer.superstructures.elements.blocks.systems;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.graphicsengine.core.GraphicsContext;
import thederpgamer.superstructures.elements.blocks.Block;
import thederpgamer.superstructures.manager.ResourceManager;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class DysonSphereController extends Block {

    public DysonSphereController() {
        super("Dyson Sphere Controller", BlockConfig.newElementCategory(ElementKeyMap.getInfo(ElementKeyMap.LIFT_ELEMENT).getType(), "Superstellar Construction"));
    }

    @Override
    public void initialize() {
        /*
        if(GraphicsContext.initialized) {
            try {
                blockInfo.setTextureId(new short[] {
                        (short) ResourceManager.getTexture("dyson-sphere-controller-front").getTextureId(),
                        (short) ResourceManager.getTexture("dyson-sphere-controller-back").getTextureId(),
                        (short) ResourceManager.getTexture("dyson-sphere-controller-top").getTextureId(),
                        (short) ResourceManager.getTexture("dyson-sphere-controller-bottom").getTextureId(),
                        (short) ResourceManager.getTexture("dyson-sphere-controller-left").getTextureId(),
                        (short) ResourceManager.getTexture("dyson-sphere-controller-right").getTextureId()
                });
                blockInfo.setBuildIconNum((short) ResourceManager.getTexture("dyson-sphere-controller-icon").getTextureId());
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
         */

        blockInfo.setDescription("Used to construct and control a Dyson Sphere around a star.");
        blockInfo.setInRecipe(true);
        blockInfo.setShoppable(true);
        blockInfo.setCanActivate(true);
        blockInfo.setPrice(15000);
        blockInfo.setOrientatable(true);

        /*
        BlockConfig.addRecipe(blockInfo, ElementManager.FactoryType.ADVANCED_FACTORY.ordinal(), 300,
                new FactoryResource(1, ElementKeyMap.TEXT_BOX),
                new FactoryResource(50, (short) 220)
        );
         */
        BlockConfig.add(blockInfo);
    }
}
