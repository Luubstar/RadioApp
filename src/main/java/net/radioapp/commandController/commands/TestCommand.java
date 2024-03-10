package net.radioapp.commandController.commands;

import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.commandController.Command;


public class TestCommand extends Command {
    public  TestCommand(){name = "test";}
    @Override
    public Action call(String[] args) {
        this.resultMessage = "Esto es un test";
        return new Action(resultMessage, ActionType.LOG);
    }
}
