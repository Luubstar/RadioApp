package net.radioapp.commandController.commands;

import net.radioapp.Main;
import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class LockVolume extends Command {
    public LockVolume(){name = "lockvolume";}
    @Override
    public Action call(String[] args) {
        this.resultMessage = "Sistema de bloqueo de volumen alternado";
        Main.setLockedVolume(!Main.isLockedVolume());
        return new Action(resultMessage, ActionType.SET);
    }
}
