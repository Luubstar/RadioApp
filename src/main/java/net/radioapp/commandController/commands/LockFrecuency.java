package net.radioapp.commandController.commands;

import net.radioapp.Main;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.commandController.Command;

public class LockFrecuency extends Command {
    public LockFrecuency(){name = "lockfrecuency";}
    @Override
    public Action call(String[] args) {
        this.resultMessage = "Sistema de bloqueo de frecuencia alternado";
        Main.setLockedFrecuency(!Main.isLockedFrecuency());
        return new Action(resultMessage, ActionType.SET);
    }
}
