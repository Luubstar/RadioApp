package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class GroupsCommand extends Command {
    public GroupsCommand(){
        name = "groups";
        aliases = new String[]{"gp"};
        resumeMessage = "Comando para obtener todos los grupos y su índice";
        helpMessage = resumeMessage;
    }
    @Override
    public Action call(String[] args) {return new Action(this.getName(),null, ActionType.SHOWGROUPS);}
}
