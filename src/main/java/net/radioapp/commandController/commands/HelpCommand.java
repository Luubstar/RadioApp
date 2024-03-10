package net.radioapp.commandController.commands;

import net.radioapp.Main;
import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class HelpCommand extends Command {
    public HelpCommand(){name = "help"; aliases = new String[]{"h"};}
    @Override
    public Action call(String[] args) {
        this.resultMessage = "Mensaje de ayuda";
        return new Action(resultMessage, ActionType.LOG);
    }
}
