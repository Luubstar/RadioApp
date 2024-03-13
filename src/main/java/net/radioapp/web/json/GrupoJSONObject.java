package net.radioapp.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GrupoJSONObject {
    private int version;

    public GrupoJSONObject(int version) {
        this.version = version;
    }

    public GrupoJSONObject() {
        this.version = -1;
    }
    @JsonProperty("version")
    public int getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(int version) {
        this.version = version;
    }
}
