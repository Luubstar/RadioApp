package net.radioapp.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GrupoJSONObject {
    private int version;
    private double maxFrecuency;

    public GrupoJSONObject(int version, double maxFrecuency ) {
        this.version = version;
        this.maxFrecuency = maxFrecuency;
    }

    public GrupoJSONObject() {
        this.version = 1;
        this.maxFrecuency = 100;
    }
    @JsonProperty("version")
    public int getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(int version) {
        this.version = version;
    }

    @JsonProperty("maxFrecuency")
    public double getMaxFrecuency() {
        return maxFrecuency;
    }

    @JsonProperty("maxFrecuency")
    public void setMaxFrecuency(double maxFrecuency) {
        this.maxFrecuency = maxFrecuency;
    }
}
