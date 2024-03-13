package net.radioapp.web.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GrupoJSON{

    public static void create(Path url, GrupoJSONObject obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(url.toFile(), obj);
    }

    public static GrupoJSONObject read(File f) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String s = Files.readString(Paths.get(f.getAbsolutePath()));
        return mapper.readValue(s, GrupoJSONObject.class);
    }

}
