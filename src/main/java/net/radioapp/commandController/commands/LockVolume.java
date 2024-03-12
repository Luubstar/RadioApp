package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class LockVolume extends Command {
    public LockVolume(){
        name = "lockvolume";
        resumeMessage = "Bloquea que los clientes puedan cambiar manualmente su volumen";
        helpMessage = resumeMessage;
    }
    @Override
    public Action call(String[] args) {
        this.resultMessage = "Sistema de bloqueo de volumen alternado";
        return new Action(this.getName(),resultMessage, ActionType.SET);
    }
}
