package net.radioapp;

import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.commandController.TerminalHandler;
import net.radioapp.commandController.commands.LockFrecuency;
import net.radioapp.commandController.commands.LockOnOff;
import net.radioapp.commandController.commands.LockVolume;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Client;
import net.radioapp.web.Network.NetHandler;
import net.radioapp.web.Network.UDPDataArray;

import java.io.IOException;
import java.util.Objects;

public class Main {
    private static final InputHandler manejador = new TerminalHandler();
    private static final WebHandler net = new NetHandler();
    private static boolean lockedOn, lockedVolume, lockedFrecuency;
    private static String emittingGroup;

    public static void main(String[] args) throws IOException {
        manejador.initialize();
        ActionHandler.start(manejador,net);
        net.initialize();

        manejador.start();
        net.start();
        start();
    }

    public static void start(){
        Action[] lastActions;
        while(true) {
            lastActions = manejador.getAction();
            for(Action a: lastActions) {
                filterAction(a);
            }
        }
    }

    public static void filterAction(Action a){
        if (Objects.requireNonNull(a.getType()) == ActionType.SET) {
            filterSetters(a);
        } else {
            ActionHandler.filterAction(a);
        }
    }

    public static void filterSetters(Action action){
        if (action.getName().equals(new LockFrecuency().getName())){Main.setLockedFrecuency(!Main.isLockedFrecuency());}
        else if (action.getName().equals(new LockOnOff().getName())){Main.setLockedOn(!Main.isLockedOn());}
        else if (action.getName().equals(new LockVolume().getName())){Main.setLockedVolume(!Main.isLockedVolume());}
    }

    public static void send(UDPDataArray arg, PackageTypes t){net.send(arg, t);}
    public static void send(UDPDataArray arg, PackageTypes t, Client c){net.send(arg, t,c);}

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