package net.radioapp.commandController.commands;

import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.commandController.Command;

public class LockFrecuency extends Command {
    public LockFrecuency(){
        name = "lockfrecuency";
        resumeMessage = "Bloquea que los clientes puedan cambiar manualmente su frecuencia";
        helpMessage = resumeMessage;
    }
    @Override
    public Action call(String[] args) {
        this.resultMessage = "Sistema de bloqueo de frecuencia alternado";
        return new Action(this.getName(),resultMessage, ActionType.SET);
    }
}
