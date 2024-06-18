package net.radioapp.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GrupoJSONObject {
    private final int version;
    private final double maxFrecuency;

    public GrupoJSONObject() {
        this.version = 1;
        this.maxFrecuency = 100;
    }
    @JsonProperty("version")
    public int getVersion() {
        return version;
    }

    @JsonProperty("maxFrecuency")
    public double getMaxFrecuency() {
        return maxFrecuency;
    }

}
