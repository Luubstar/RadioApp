package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class HelpCommand extends Command {
    public HelpCommand(){
        name = "help";
        aliases = new String[]{"h"};
        resumeMessage = "Comando para obtener tanto ayuda general o de un comando (su argumento). ";
        helpMessage = "Comando de ayuda. Si recibe como argumento un comando, muestra un mensaje de ayuda";
    }
    @Override
    public Action call(String[] args) {
        if (args.length == 0){return new Action("", ActionType.HELP);}
        return new Action(args[0], ActionType.HELPARG);
    }
}
