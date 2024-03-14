package net.radioapp.web.UDP;

import java.net.InetAddress;

public class Client {
    private InetAddress address;
    private double frecuency;
    private boolean isNew;

    //TODO: Aquí lo del tiempo
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

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public double getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(double frecuency) {
        this.frecuency = frecuency;
    }
}
