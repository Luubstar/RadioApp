package net.radioapp;

import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.commandController.TerminalHandler;
import net.radioapp.commandController.commands.LockFrecuency;
import net.radioapp.commandController.commands.LockOnOff;
import net.radioapp.commandController.commands.LockVolume;
import net.radioapp.web.UDP.NetHandler;
import net.radioapp.web.inputServer.Connectivity;

import java.io.IOException;

public class Main {
    private static final InputHandler manejador = new TerminalHandler();
    private static final WebHandler net = new NetHandler();
    private static Connectivity connectivityMode = Connectivity.NEVER;

    private static boolean emitting, lockedOn, lockedVolume, lockedFrecuency;
    private static String emittingGroup;


    public static void main(String[] args) throws IOException {
        manejador.initialize();
        net.initialize();
        manejador.start();
        net.start();
        start();
    }

    public static void start(){
        Action lastAction;
        while(true) {
            lastAction = manejador.getAction();
            filterAction(lastAction);
        }
    }

    //TODO: mover a una clase aparte
    public static void filterAction(Action action){
        if (action.getType() == ActionType.LOG){manejador.log(action.getRes());}
        else if (action.getType() == ActionType.ERROR){manejador.error(action.getRes());}
        else if (action.getType() == ActionType.QUIT){manejador.exit(action.getRes());}
        else if (action.getType() == ActionType.HELP){manejador.log(getHelpCommands());}
        else if (action.getType() == ActionType.HELPARG){manejador.log(getHelpCommands(action.getRes()));}
        else if (action.getType() == ActionType.SET){filterSetters(action); manejador.log(action.getRes());}
    }

    public static void filterSetters(Action action){
        if (action.getName().equals(new LockFrecuency().getName())){Main.setLockedFrecuency(!Main.isLockedFrecuency());}
        else if (action.getName().equals(new LockOnOff().getName())){Main.setLockedOn(!Main.isLockedOn());}
        else if (action.getName().equals(new LockVolume().getName())){Main.setLockedVolume(!Main.isLockedVolume());}
    }

    public static String getHelpCommands(){
        return manejador.getController().getHelpCommands();
    }
    public static String getHelpCommands(String s){
        return manejador.getController().getHelpCommands(s);
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