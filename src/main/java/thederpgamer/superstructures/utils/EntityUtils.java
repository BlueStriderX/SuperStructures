package thederpgamer.superstructures.utils;

import api.common.GameCommon;
import api.common.GameServer;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.schema.game.common.controller.Planet;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.schine.network.objects.Sendable;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [12/07/2021]
 */
public class EntityUtils {

    public static SegmentController getEntityFromUID(String entityUID) {
        if(GameCommon.isOnSinglePlayer() || GameCommon.isDedicatedServer()) {
            return GameServer.getServerState().getSegmentControllersByName().get(entityUID);
        } else {
            Int2ObjectOpenHashMap<Sendable> map = GameCommon.getGameState().getState().getLocalAndRemoteObjectContainer().getLocalObjects();
            for(Sendable sendable : map.values()) {
                if(sendable instanceof SegmentController) {
                    if(((SegmentController) sendable).getUniqueIdentifier().equals(entityUID)) return (SegmentController) sendable;
                }
            }
            return null;
        }
    }

    public static ManagerContainer<?> getManagerContainer(SegmentController segmentController) {
        switch(segmentController.getType()) {
            case SHIP: return ((Ship) segmentController).getManagerContainer();
            case SPACE_STATION: return ((SpaceStation) segmentController).getManagerContainer();
            case PLANET_SEGMENT: return ((Planet) segmentController).getManagerContainer();
            default: return null;
        }
    }
}
