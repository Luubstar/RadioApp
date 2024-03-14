package net.radioapp.web.UDP;

import net.radioapp.WebHandler;
import net.radioapp.web.emisor.Emisora;
import net.radioapp.web.emisor.Grupo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NetHandler implements WebHandler {
    private static final Path mainDir = Paths.get("./mainApp");
    private static List<Path> groupsPaths = new ArrayList<>();
    private static List<Path> emisorasPaths = new ArrayList<>();
    private static List<Grupo> gruposList = new ArrayList<>();
    private static List<Emisora> emisorasList = new ArrayList<>();
    private static Grupo grupoActual;
    private static UDPEmitter servicio;

    @Override
    public void initialize() throws IOException{
        checkIfHasStructure();
        Stream<Path> paths = Files.list(mainDir);
        paths.filter(Files::isDirectory).forEach(groupsPaths::add);

        for(Path p : groupsPaths){
            Stream<Path> emisoras = Files.list(p);
            emisoras.filter(Files::isDirectory).forEach(emisorasPaths::add);
            List<Emisora> temp = new ArrayList<>();

            for(Path e: emisorasPaths){
                Emisora emisor = new Emisora(e.getFileName().toString(), e);
                temp.add(emisor);
                emisorasList.add(emisor);
                emisor.readConfigFile();
            }

            Grupo g = new Grupo(p.getFileName().toString(), p, temp);
            g.readConfigFile();
            gruposList.add(g);
        }

        grupoActual = gruposList.getFirst();
        for(Grupo g: gruposList){System.out.println(g.toString());}

        servicio = new UDPEmitter();
        servicio.setFichero(grupoActual.getEmisoras().getFirst().getFicheros().getFirst());
        servicio.start();
    }

    public void checkIfHasStructure() throws  IOException{
        if (!Files.exists(mainDir)) {
            Files.createDirectory(mainDir);
            Files.createDirectory(Paths.get(mainDir+"/GrupoEjemplo"));
            Files.createDirectory(Paths.get(mainDir+"/GrupoEjemplo/EmisoraEjemplo"));
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
