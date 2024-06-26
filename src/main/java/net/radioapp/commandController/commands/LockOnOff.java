package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class LockOnOff extends Command {
    public LockOnOff(){
        name = "lockonoff";
        resumeMessage = "Bloquea que los clientes puedan encender o apagar la radio manualmente";
        helpMessage = resumeMessage;
    }
    @Override
    public Action call(String[] args) {
        this.resultMessage = "Sistema de bloqueo de activado/desactivado alternado";
        return new Action(this.getName(),resultMessage, ActionType.SET);
    }
}
