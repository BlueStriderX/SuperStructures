package thederpgamer.superstructures.data.commands;

import api.mod.StarMod;
import api.utils.game.chat.CommandInterface;
import org.schema.game.common.data.player.PlayerState;
import thederpgamer.superstructures.SuperStructures;

import javax.annotation.Nullable;

/**
 * [Description]
 *
 * @author TheDerpGamer (MrGoose#0027)
 */
public class SSCommand implements CommandInterface {
	@Override
	public String getCommand() {
		return "super_structures";
	}

	@Override
	public String[] getAliases() {
		return new String[] {
				"super_structures",
				"ss"
		};
	}

	@Override
	public String getDescription() {
		return "Command for debugging Super Structures.\n" +
				"%COMMAND% refresh : Refreshes the nearest super structure, updating it's draw data.";
	}

	@Override
	public boolean isAdminOnly() {
		return true;
	}

	@Override
	public boolean onCommand(PlayerState sender, String[] args) {
		if(args.length == 0) return false;
		else {
			switch(args[0]) {
				case "refresh":
					SuperStructures.getInstance().refreshNearestSuperStructure(sender);
					return true;
				default:
					return false;
			}
		}
	}

	@Override
	public void serverAction(@Nullable PlayerState sender, String[] args) {

	}

	@Override
	public StarMod getMod() {
		return SuperStructures.getInstance();
	}
}
