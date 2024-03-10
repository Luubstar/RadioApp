package net.radioapp;

import net.radioapp.commandController.Action;
import net.radioapp.commandController.ActionType;
import net.radioapp.commandController.TerminalHandler;

public class Main {
    private static final InputHandler manejador = new TerminalHandler();
    public static void main(String[] args) {
        manejador.initialize();
        manejador.start();
        start();
    }
    
    public static void start(){
        Action lastAction;
        while(true) {
            lastAction = manejador.getAction();
            filterAction(lastAction);
        }
    }

    public static void filterAction(Action action){
        if (action.getType() == ActionType.LOG){manejador.log(action.getRes());}
        else if (action.getType() == ActionType.ERROR){manejador.error(action.getRes());}
        else if(action.getType() == ActionType.QUIT){manejador.exit(action.getRes());}
    }
}