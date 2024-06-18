package net.radioapp.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmisorJSONObject {
    private final int version;
    private final double frecuency;

    public EmisorJSONObject() {

        this.version = 1;
        this.frecuency = 50;
    }
    @JsonProperty("version")
    public int getVersion() {
        return version;
    }

    @JsonProperty("frecuency")
    public double getFrecuency() {
        return frecuency;
    }
}
