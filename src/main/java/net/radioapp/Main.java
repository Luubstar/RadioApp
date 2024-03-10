package net.radioapp;

import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.commandController.TerminalHandler;
import net.radioapp.web.inputServer.Connectivity;

public class Main {
    private static final InputHandler manejador = new TerminalHandler();
    private static Connectivity connectivityMode = Connectivity.NEVER;

    private static boolean emitting, lockedOn, lockedVolume, lockedFrecuency;
    private static String emittingGroup;


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
        if (action.getType() == ActionType.LOG || action.getType() == ActionType.SET){manejador.log(action.getRes());}
        else if (action.getType() == ActionType.ERROR){manejador.error(action.getRes());}
        else if(action.getType() == ActionType.QUIT){manejador.exit(action.getRes());}
    }

    public static Connectivity getConnectivityMode() {
        return connectivityMode;
    }

    public static void setConnectivityMode(Connectivity cm) {connectivityMode = cm;}

    public static boolean isEmitting() {
        return emitting;
    }

    public static void setEmitting(boolean emitting) {
        Main.emitting = emitting;
    }

    public static boolean isLockedOn() {
        return lockedOn;
    }

    public static void setLockedOn(boolean lockedOn) {
        Main.lockedOn = lockedOn;
    }

    public static boolean isLockedVolume() {
        return lockedVolume;
    }

    public static void setLockedVolume(boolean lockedVolume) {
        Main.lockedVolume = lockedVolume;
    }

    public static boolean isLockedFrecuency() {
        return lockedFrecuency;
    }

    public static void setLockedFrecuency(boolean lockedFrecuency) {
        Main.lockedFrecuency = lockedFrecuency;
    }

    public static String getEmittingGroup() {
        return emittingGroup;
    }

    public static void setEmittingGroup(String emittingGroup) {
        Main.emittingGroup = emittingGroup;
    }
}