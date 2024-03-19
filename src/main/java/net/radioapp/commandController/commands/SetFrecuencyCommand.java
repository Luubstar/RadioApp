package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class SetFrecuencyCommand extends Command {
    public SetFrecuencyCommand(){
        name = "setfrecuency";
        aliases = new String[]{"move"};
        resumeMessage = "Comando para asignar la frecuencia {arg} al clientes";
        helpMessage = resumeMessage;
    }
    @Override
    public Action call(String[] args) {
        return  new Action(this.getName(),args[0], ActionType.WEB);
    }
}
