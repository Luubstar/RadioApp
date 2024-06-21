package net.radioapp.commandController.actions;

import net.radioapp.InputHandler;
import net.radioapp.WebHandler;
import net.radioapp.web.Network.NetHandler;

import java.util.Arrays;

public class ActionHandler {

    private static InputHandler manejador;
    private static WebHandler net;

    public static void start(InputHandler m, WebHandler n){
        manejador = m;
        net = n;
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
            case WEB:
                net.filterAction(action);
                break;
            case STATE:
                net.getState();
                break;
            case SHOWSTATIONS:
                net.getStations();
                break;
            case SKIPSONG:
                net.skipSong(Integer.parseInt(action.getRes()));
                break;
            case SHOWGROUPS:
                net.getGroups();
                break;
            case CHANGEGROUP:
                net.selectGroup(Integer.parseInt(action.getRes()));
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
    public static void log(String c){filterAction(new Action("", c, ActionType.LOG));}
    public static void logError(String c){filterAction(new Action("", c, ActionType.ERROR));}

}
