package thederpgamer.superstructures.data.structures;

import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.server.data.Galaxy;

/**
 * [Description]
 *
 * @author TheDerpGamer (TheDerpGamer#0027)
 */
public enum SunType {
	VOID(-1, -1), BLACK_HOLE(-1, -1), WORM_HOLE(-1, -1), DWARF(4, 0), MEDIUM(5, 3), GIANT(-1, 4), BINARY(-1, -1);
	//Todo: Go in-game and find the rest of the sun types as they arent really documented in the code.
	//NEUTRON_STAR //Would be cool, but not a priority.

	public final int nextIndex;
	public final int previousIndex;

	SunType(int nextIndex, int previousIndex) {
		this.nextIndex = nextIndex;
		this.previousIndex = previousIndex;
	}

	public static SunType getFromSystem(Galaxy galaxy, Vector3i system) {
		try {
			//Todo: Figure out how to get the sun type from the system.
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		return VOID;
	}
}
