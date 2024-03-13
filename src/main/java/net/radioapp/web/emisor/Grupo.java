package net.radioapp.web.emisor;

import java.util.ArrayList;
import java.util.List;

public class Grupo {
    private final String name;
    private double maxFrecuency;
    public final List<Emisora> emisoras;

    public Grupo(String n,List<Emisora> emisoras){
        this.name = n;
        this.emisoras = emisoras;
    }

    @Override
    public String toString() {
        StringBuilder r = new StringBuilder().append(name).append(":");
        for(Emisora e : emisoras){
            r.append("\n").append(e.getName()).append(" -> ").append(e.getFrecuency()).append(" MHz");
        }
        return r.toString();
    }

    public String getName() {
        return name;
    }

    public double getMaxFrecuency() {
        return maxFrecuency;
    }

    public void setFrecuency(double maxFrecuency){
        this.maxFrecuency = maxFrecuency;
    }

    public List<Emisora> getEmisoras() {
        return emisoras;
    }
}
