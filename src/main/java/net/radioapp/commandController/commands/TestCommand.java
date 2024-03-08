package net.radioapp.commandController.commands;

import net.radioapp.commandController.Action;
import net.radioapp.commandController.ActionType;
import net.radioapp.commandController.Command;


public class TestCommand extends Command {

    @Override
    public Action call(String[] args) {
        this.resultMessage = "Esto es un test";
        return new Action(resultMessage, ActionType.LOG);
    }
}
