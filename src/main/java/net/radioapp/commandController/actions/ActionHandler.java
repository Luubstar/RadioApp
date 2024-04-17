package net.radioapp.commandController.actions;

import net.radioapp.InputHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

import java.util.Arrays;

public class ActionHandler {

    private static InputHandler manejador;

    public static void start(InputHandler m){
        manejador = m;
    }
    public static void filterAction(Action action){
        switch (action.getType()){
            case LOG:
                manejador.log(action.getRes());
                break;
            case ERROR:
                manejador.error(action.getRes());
                break;
            case QUIT:
                manejador.exit(action.getRes());
                break;
            case HELP:
                manejador.log(getHelpCommands());
                break;
            case HELPARG:
                manejador.log(getHelpCommands(action.getRes()));
                break;
        }
    }

    public static String getHelpCommands(){
        return manejador.getController().getHelpCommands();
    }
    public static String getHelpCommands(String s){
        return manejador.getController().getHelpCommands(s);
    }

    public static void handleException(Exception e, String message){
        String b = "Se ha lanzado una excepción -> " + e.getMessage() + "(" + message + ")" +
                "\n" + Arrays.toString(e.getStackTrace());
        manejador.error(b);
    }
    public static void log(String c){
        filterAction(new Action("", c, ActionType.LOG));
    }
}
