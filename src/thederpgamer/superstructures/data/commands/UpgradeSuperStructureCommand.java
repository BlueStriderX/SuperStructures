package thederpgamer.superstructures.data.commands;

import api.common.GameClient;
import api.common.GameCommon;
import api.mod.StarMod;
import api.utils.game.PlayerUtils;
import api.utils.game.chat.CommandInterface;
import org.schema.game.common.data.player.PlayerState;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.data.entity.structure.SuperStructureModule;
import thederpgamer.superstructures.data.modules.StructureModuleData;
import thederpgamer.superstructures.data.structures.SuperStructureData;
import thederpgamer.superstructures.utils.DataUtils;
import javax.annotation.Nullable;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 07/21/2021
 */
public class UpgradeSuperStructureCommand implements CommandInterface {

    @Override
    public String getCommand() {
        return "structure_module_upgrade";
    }

    @Override
    public String[] getAliases() {
        return new String[] {
                "smu"
        };
    }

    @Override
    public String getDescription() {
        return "Upgrades the selected super structure module to the next level.\n" +
                "- %COMMAND% : Upgrades the selected super structure module to the next level.";
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public boolean onCommand(PlayerState sender, String[] strings) {
        if(GameClient.getClientPlayerState().getSelectedEntityId() == 0 || !(GameCommon.getGameObject(GameClient.getClientPlayerState().getSelectedEntityId()) instanceof SuperStructureModule)) PlayerUtils.sendMessage(sender, "[ERROR]: You must select a super structure module to upgrade!");
        else {
            SuperStructureData structureData = DataUtils.getStructure(sender.getCurrentSystem());
            StructureModuleData moduleData = structureData.getModule(GameClient.getClientPlayerState().getSelectedEntityId());
            if(moduleData != null) {
                if(moduleData.level + 1 < moduleData.maxLevel) {
                    moduleData.level ++;
                    PlayerUtils.sendMessage(sender, "Module level increased (" + (moduleData.level - 1) + " -> " + moduleData.level + ".");
                } else PlayerUtils.sendMessage(sender, "[ERROR]: This module is already at maximum level (" + moduleData.maxLevel + ")!");
            }
        }
        return true;
    }

    @Override
    public void serverAction(@Nullable PlayerState playerState, String[] strings) {

    }

    @Override
    public StarMod getMod() {
        return SuperStructures.getInstance();
    }
}
