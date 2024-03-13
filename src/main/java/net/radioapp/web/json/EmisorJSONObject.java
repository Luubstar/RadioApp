package net.radioapp.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmisorJSONObject {
    private int version;
    private double frecuency;


    public EmisorJSONObject(int version, double frecuency) {

        this.version = version;
        this.frecuency = frecuency;
    }

    public EmisorJSONObject() {

        this.version = 1;
        this.frecuency = 50;
    }
    @JsonProperty("version")
    public int getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(int version) {
        this.version = version;
    }
    @JsonProperty("frecuency")
    public double getFrecuency() {
        return frecuency;
    }

    @JsonProperty("frecuency")
    public void setFrecuency(double frecuency) {
        this.frecuency = frecuency;
    }
}
