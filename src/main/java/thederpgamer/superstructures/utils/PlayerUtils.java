package thederpgamer.superstructures.utils;

import org.schema.game.common.data.player.PlayerState;

/**
 * Utility methods for player-related stuff.
 *
 * @author TheDerpGamer
 */
public class PlayerUtils {

    public static boolean adminMode(PlayerState playerState) {
        return playerState.isAdmin() && playerState.isCreativeModeEnabled();
    }
}
