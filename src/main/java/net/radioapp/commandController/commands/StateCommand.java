package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class StateCommand extends Command {
    public StateCommand(){
        name = "state";
        aliases = new String[]{"show", "s"};
        resumeMessage = "Muestra los clientes conectados, su dirección y en qué frecuencia escucha";
        helpMessage = resumeMessage;
    }
    @Override
    public Action call(String[] args) {
        return new Action("state", "", ActionType.STATE);
    }
}
