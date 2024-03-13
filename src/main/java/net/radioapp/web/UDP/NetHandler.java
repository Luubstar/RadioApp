package net.radioapp.web.UDP;

import net.radioapp.Main;
import net.radioapp.WebHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.emisor.Emisora;
import net.radioapp.web.emisor.Grupo;
import net.radioapp.web.json.EmisorJSON;
import net.radioapp.web.json.EmisorJSONObject;
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
    public static final int EMISORVERSION = 1;
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
            Stream<Path> emisoras = Files.list(p);
            emisoras.filter(Files::isDirectory).forEach(emisorasPaths::add);
            List<Emisora> temp = new ArrayList<>();

            for(Path e: emisorasPaths){
                Emisora emisor = new Emisora(e.getFileName().toString(), 100);
                temp.add(emisor);
                emisorasList.add(emisor);
                readEmitterJSON(new File(e + "/config.json"), emisor);
            }

            Grupo g = new Grupo(p.getFileName().toString(), temp);

            readGroupJson(new File(p + "/config.json"), g);
            gruposList.add(g);
        }

        for(Grupo g: gruposList){System.out.println(g.toString());}
    }

    //TODO: preparar configuraciones
    //TODO: Estos métodos son feisimos, hay que apañarlos o moverlos a otra clase
    private void readGroupJson(File f, Grupo g){
        if (!f.exists()){
            try {
                GrupoJSON.create(f.toPath(), new GrupoJSONObject());
            }
            catch (Exception e){System.out.println(e.getMessage() + e.getStackTrace().toString());}
        }
        try {
            GrupoJSONObject obj = GrupoJSON.read(f);
            if (obj.getVersion() < GRUPOVERSION) {
                Main.filterAction(new Action("Inicialización grupos",
                        "La versión de los JSON de " + g.getName() + " es inferior a la esperada (" + GRUPOVERSION +"), puede causar errores\n",
                        ActionType.ERROR));
            }
            g.setFrecuency(obj.getMaxFrecuency());
            for(Emisora e : g.getEmisoras()){
                if (e.getFrecuency() > g.getMaxFrecuency()){
                    Main.filterAction(new Action("Inicialización grupos",
                            "La emisora " + e.getName() + " está fuera de la frecuencia máxima del grupo " + g.getName(),
                            ActionType.QUIT));
                }
            }
        }
        catch(Exception e){System.out.println(e.getMessage() + e.getCause());}
    }
    private void readEmitterJSON(File f, Emisora e){
        if (!f.exists()){
            try {
                EmisorJSON.create(f.toPath(), new EmisorJSONObject());
            }
            catch (Exception ex){System.out.println(ex.getMessage() + ex.getStackTrace().toString());}
        }
        try {
            EmisorJSONObject obj = EmisorJSON.read(f);
            if (obj.getVersion() < EMISORVERSION) {
                Main.filterAction(new Action("Inicialización emisoras",
                        "La versión de los JSON de "  + e.getName() + " es inferior a la esperada (" + EMISORVERSION +"), puede causar errores\n",
                        ActionType.ERROR));
            }
            e.setFrecuency(obj.getFrecuency());
        }
        catch(Exception ex){System.out.println(ex.getMessage() + ex.getCause());}
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
