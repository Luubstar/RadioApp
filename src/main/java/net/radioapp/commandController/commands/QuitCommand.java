package net.radioapp.commandController.commands;

import net.radioapp.commandController.Action;
import net.radioapp.commandController.ActionType;
import net.radioapp.commandController.Command;


public class QuitCommand extends Command {

    public  QuitCommand(){name = "quit";}
    @Override
    public Action call(String[] args) {
        return new Action("Aplicación cerrada exitosamente", ActionType.QUIT);
    }
}
