package net.radioapp.web.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EmitterJSON{
    public int version;
    public void read(File f) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String s = Files.readString(Paths.get(f.getAbsolutePath()));
        EmitterJSON temp =  mapper.readValue(s, EmitterJSON.class);
        clone(temp);
    }

    public void create(Path url) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(url.toFile(), this);
    }

    private void clone(EmitterJSON t){
        this.version = t.version;
    }
}
