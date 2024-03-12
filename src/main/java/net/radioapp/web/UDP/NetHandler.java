package net.radioapp.web.UDP;

import net.radioapp.WebHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NetHandler implements WebHandler {

    private static final Path mainDir = Paths.get("./mainApp");
    private static final List<Path> groupsPaths = new ArrayList<>();

    private static final List<Path> emisorasPaths = new ArrayList<>();
    @Override
    public void initialize() throws  IOException{
        if (!Files.exists(mainDir)) {Files.createDirectory(mainDir);}

        Stream<Path> paths = Files.list(mainDir);
        paths.filter(Files::isDirectory).forEach(groupsPaths::add);

        //TODO: Crear grupos y agregar emisoras a este
        //TODO: Leer JSONs para comfiguraciones
        for(Path p : groupsPaths){
            Stream<Path> emisoras = Files.list(p);
            emisoras.filter(Files::isDirectory).forEach(emisorasPaths::add);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void send() {

    }
}
