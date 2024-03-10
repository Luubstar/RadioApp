package net.radioapp.commandController.commands;

import net.radioapp.commandController.Action;
import net.radioapp.commandController.ActionType;
import net.radioapp.commandController.Colors;
import net.radioapp.commandController.Command;


public class QuitCommand extends Command {

    public  QuitCommand(){name = "quit";}
    private final Colors exitColor = new Colors(Colors.TYPE_TEXT, 99, 99, 99);
    @Override
    public Action call(String[] args) {
        return new Action(exitColor.colorize("Aplicación cerrada exitosamente"), ActionType.QUIT);
    }
}
