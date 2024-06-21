package net.radioapp.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GrupoJSONObject {
    private final int version;
    private final double maxFrequency;
    private final double defaultFrequency;
    private final double minFrequency;

    public GrupoJSONObject() {
        this.version = 1;
        this.maxFrequency = 100;
        this.defaultFrequency = 50;
        this.minFrequency = 0;
    }
    @JsonProperty("version")
    public int getVersion() {
        return version;
    }

    @JsonProperty("maxFrequency")
    public double getMaxFrequency() {
        return maxFrequency;
    }

    @JsonProperty("defaultFrequency")
    public double getDefaultFrequency() {
        return defaultFrequency;
    }
    @JsonProperty("minFrequency")
    public double getMinFrequency() {
        return minFrequency;
    }

}
