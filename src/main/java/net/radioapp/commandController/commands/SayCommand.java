package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class SayCommand extends Command {
    public SayCommand(){
        name = "say";
        aliases = new String[]{"msg"};
        resumeMessage = "Envía un mensaje de texto a los clientes. Es un comando de debug";
        helpMessage = resultMessage + "\n {arg} -> Mensaje a enviar";
    }
    @Override
    public Action call(String[] args) {
        if(args.length == 0){return  new Action("error", "Tamaño de argumentos inválido", ActionType.ERROR);}
        return new Action(this.getName(),args[0], ActionType.WEB);
    }
}
