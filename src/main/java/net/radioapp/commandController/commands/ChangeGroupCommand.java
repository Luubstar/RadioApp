package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class ChangeGroupCommand extends Command {
    public ChangeGroupCommand(){
        name = "changegroup";
        aliases = new String[]{"chgp"};
        resumeMessage = "Comando para cambiar a un grupo {arg}";
        helpMessage =  resultMessage +"\n {arg} -> Grupo al que debe cambiar la radio";
    }
    @Override
    public Action call(String[] args) {
        if(args.length == 0){return new Action("error", "Tamaño de argumentos inválido", ActionType.ERROR);}
        return new Action(this.getName(),args[0], ActionType.CHANGEGROUP);}
}
