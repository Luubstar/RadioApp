package net.radioapp.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmisorJSONObject {
    private final int version;
    private final double frequency;

    public EmisorJSONObject() {
        this.version = 1;
        this.frequency = 50;
    }
    @JsonProperty("version")
    public int getVersion() {
        return version;
    }

    @JsonProperty("frequency")
    public double getFrequency() {
        return frequency;
    }
}
