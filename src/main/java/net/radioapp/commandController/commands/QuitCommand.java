package net.radioapp.commandController.commands;

import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.commandController.Command;


public class QuitCommand extends Command {

    public  QuitCommand(){
        name = "quit";
        aliases = new String[]{"q"};
        resumeMessage = "Cierra la aplicación";
        helpMessage = resumeMessage;
    }
    @Override
    public Action call(String[] args) {
        return new Action("Aplicación cerrada exitosamente", ActionType.QUIT);
    }
}
