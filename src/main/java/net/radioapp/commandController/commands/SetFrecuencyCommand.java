package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class SetFrecuencyCommand extends Command {
    public SetFrecuencyCommand(){
        name = "setfrecuency";
        aliases = new String[]{"move"};
        resumeMessage = "Comando para asignar la frecuencia {arg} al clientes";
        helpMessage = resultMessage +"\n {arg} -> Frecuencia objetivo";
    }
    @Override
    public Action call(String[] args) {
        if(args.length == 0){return  new Action("error", "Tamaño de argumentos inválido", ActionType.ERROR);}
        return  new Action(this.getName(),args[0], ActionType.WEB);
    }
}
