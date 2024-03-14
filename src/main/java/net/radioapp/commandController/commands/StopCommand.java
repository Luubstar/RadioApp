package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class StopCommand extends Command {
    public StopCommand(){
        name = "stop";
        aliases = new String[]{};
        resumeMessage = "Comando para pausar la emisión";
        helpMessage = resumeMessage;
    }
    @Override
    public Action call(String[] args) {
        return  new Action(this.getName(),"", ActionType.WEB);
    }
}
