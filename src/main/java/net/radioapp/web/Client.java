package net.radioapp.web;

import net.radioapp.Main;
import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.web.Network.ClientHandler;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Network.UDPDataArray;

import java.net.InetAddress;

public class Client {
    private final InetAddress address;
    private double frecuency;
    private boolean isNew;
    private boolean waitingPing;
    private int lostedPings = 0;

    public Client(InetAddress address, double frecuency) {
        this.address = address;
        this.frecuency = frecuency;
        this.isNew = true;
    }

    public boolean isNew() {
        return isNew;
    }
    public void turnNew(){isNew = false;}

    public InetAddress getAddress() {
        return address;
    }

    public double getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(double frecuency) {
        this.frecuency = frecuency;
    }

    public void pingReceived(){lostedPings = 0; waitingPing = false;}
    public void ping() {
        if(waitingPing){
            pingLost();
            if (lostedPings >= ClientHandler.MAX_LOSTED_PINGS){
                ClientHandler.removeClient(this);
                ActionHandler.log("Cliente eliminado");
            }
        }
        waitingPing = true;
        Main.send(new UDPDataArray(), PackageTypes.PING);
    }

    public void pingLost(){lostedPings++;}

    @Override
    public String toString() {
        return address + " -> " + frecuency + " MHz" + " / Ping losts -> " + lostedPings;
    }
}