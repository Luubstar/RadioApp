package net.radioapp.web.emisor;

public class Emisora {
    private final String name;
    private final double frecuency;

    public Emisora(String name, double frecuency) {
        this.name = name;
        this.frecuency = frecuency;
    }

    public String getName() {
        return name;
    }

    public double getFrecuency() {
        return frecuency;
    }
}
