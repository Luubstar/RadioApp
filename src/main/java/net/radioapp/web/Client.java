package net.radioapp.web;

import net.radioapp.Main;
import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.web.Network.ClientHandler;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Network.UDPDataArray;
import net.radioapp.web.emisor.Emision;

import java.net.InetAddress;

public class Client {
    private Emision emisora;
    private final InetAddress address;
    private double frequency;
    private boolean isNew;
    private boolean waitingPing;
    private int lostedPings = 0;
    public Client(InetAddress address, double frequency) {
        this.address = address;
        this.frequency = frequency;
        this.isNew = true;
    }

    public boolean isNew() {
        return isNew;
    }
    public void turnNew(){isNew = false; emisora = null;}

    public InetAddress getAddress() {
        return address;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public Emision getEmisora() {
        return emisora;
    }

    public void setEmisora(Emision emisora) {
        this.emisora = emisora;
    }

    public void pingReceived(){lostedPings = 0; waitingPing = false;}
    public synchronized void ping() {
        if(waitingPing){
            pingLost();
            if (lostedPings >= ClientHandler.MAX_LOSTED_PINGS){
                ClientHandler.removeClient(this);
                ActionHandler.log("Cliente eliminado");
                if(emisora != null){emisora.removeClient(this);}
            }
        }
        waitingPing = true;
        Main.send(new UDPDataArray(), PackageTypes.PING);
    }

    public void pingLost(){lostedPings++;}


    public void setRequested() {
        if(emisora!=null){emisora.request();}
    }

    @Override
    public String toString() {
        return address + " -> " + frequency + " MHz" + " / Ping losts -> " + lostedPings;
    }
}
