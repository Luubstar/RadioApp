package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class StationsCommand extends Command {
    public StationsCommand(){
        name = "stations";
        aliases = new String[]{"st"};
        resumeMessage = "Comando para obtener todas las estaciones y su índice";
        helpMessage = resumeMessage;
    }
    @Override
    public Action call(String[] args) {return new Action(this.getName(),null, ActionType.SHOWSTATIONS);}
}
