package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class RestartCommand extends Command {
    public RestartCommand(){
        name = "restart";
        aliases = new String[]{};
        resumeMessage = "Comando para reiniciar la emisión";
        helpMessage = resumeMessage;
    }
    @Override
    public Action call(String[] args) {
        return  new Action(this.getName(),"", ActionType.WEB);
    }
}
