package net.radioapp.web.UDP;

import net.radioapp.WebHandler;
import net.radioapp.web.emisor.Emisora;
import net.radioapp.web.emisor.Grupo;
import net.radioapp.web.json.GrupoJSON;
import net.radioapp.web.json.GrupoJSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NetHandler implements WebHandler {

    public static final int GRUPOVERSION = 1;
    public static final int EMITTERVERSION = 1;
    private static final Path mainDir = Paths.get("./mainApp");
    private static List<Path> groupsPaths = new ArrayList<>();
    private static List<Path> emisorasPaths = new ArrayList<>();
    private static List<Grupo> gruposList = new ArrayList<>();
    private static List<Emisora> emisorasList = new ArrayList<>();

    //TODO: Try-catchear bien todo
    @Override
    public void initialize() throws  IOException{
        if (!Files.exists(mainDir)) {Files.createDirectory(mainDir);}

        Stream<Path> paths = Files.list(mainDir);
        paths.filter(Files::isDirectory).forEach(groupsPaths::add);

        for(Path p : groupsPaths){
            readGroupJson(new File(p.toString() + "/config.json"));
            Stream<Path> emisoras = Files.list(p);
            emisoras.filter(Files::isDirectory).forEach(emisorasPaths::add);
            List<Emisora> temp = new ArrayList<>();
            for(Path e: emisorasPaths){
                Emisora emisor = new Emisora(e.toString(), 100);
                temp.add(emisor);
                emisorasList.add(emisor);
            }
            gruposList.add(new Grupo(p.toString(), temp));
        }

        for(Grupo g: gruposList){System.out.println(g.toString());}
    }

    //TODO: preparar configuraciones
    private void readGroupJson(File f){
        if (!f.exists()){
            try {
                GrupoJSON.create(f.toPath(), new GrupoJSONObject(GRUPOVERSION));
            }
            catch (Exception e){System.out.println(e.getMessage() + e.getStackTrace().toString());}
        }
        try{
            GrupoJSONObject obj = GrupoJSON.read(f);
            System.out.println(obj.getVersion());}
        catch(Exception e){System.out.println(e.getMessage() + e.getCause());}
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
