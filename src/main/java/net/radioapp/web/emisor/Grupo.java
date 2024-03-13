package net.radioapp.web.emisor;

import java.util.ArrayList;
import java.util.List;

public class Grupo {
    private final String name;
    public final List<Emisora> emisoras;

    public Grupo(String n,List<Emisora> emisoras){
        this.name = n;
        this.emisoras = emisoras;
    }

    @Override
    public String toString() {
        StringBuilder r = new StringBuilder().append(name).append(":");
        for(Emisora e : emisoras){
            r.append("\n").append(e.getName()).append(" -> ").append(e.getFrecuency());
        }
        return r.toString();
    }
}
