package net.radioapp.commandController.actions;

import net.radioapp.InputHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class ActionHandler {

    private static InputHandler manejador;

    public static void start(InputHandler m){
        manejador = m;
    }
    public static void filterAction(Action action){
        //TODO: Switch
        if (action.getType() == ActionType.LOG){manejador.log(action.getRes());}
        else if (action.getType() == ActionType.ERROR){manejador.error(action.getRes());}
        else if (action.getType() == ActionType.QUIT){manejador.exit(action.getRes());}
        else if (action.getType() == ActionType.HELP){manejador.log(getHelpCommands());}
        else if (action.getType() == ActionType.HELPARG){manejador.log(getHelpCommands(action.getRes()));}
    }

    public static String getHelpCommands(){
        return manejador.getController().getHelpCommands();
    }
    public static String getHelpCommands(String s){
        return manejador.getController().getHelpCommands(s);
    }

}
