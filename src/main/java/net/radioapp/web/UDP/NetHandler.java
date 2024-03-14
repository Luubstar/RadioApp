package net.radioapp.web.UDP;

import net.radioapp.ActionHandler;
import net.radioapp.WebHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.emisor.Emisora;
import net.radioapp.web.emisor.Grupo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class NetHandler implements WebHandler {
    private static final Path mainDir = Paths.get("./mainApp");
    private static List<Path> groupsPaths = new ArrayList<>();
    private static List<Path> emisorasPaths = new ArrayList<>();
    private static List<Grupo> gruposList = new ArrayList<>();
    private static List<Emisora> emisorasList = new ArrayList<>();
    private static Grupo grupoActual;
    private static UDPRecibe recibidor;

    @Override
    public void initialize() throws IOException{
        groupsPaths.clear();
        emisorasPaths.clear();
        gruposList.clear();
        emisorasList.clear();
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
        recibidor = new UDPRecibe();
        recibidor.start();
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
        if(!ClientHandler.isOnline()){
        ClientHandler.setOnline(true);
        ActionHandler.filterAction(new Action("start", "Sistema iniciado", ActionType.LOG));}
        else {ActionHandler.filterAction(new Action("start error", "El sistema ya está iniciado", ActionType.LOG));}
    }

    @Override
    public void stop() {
        if (ClientHandler.isOnline()) {
            ClientHandler.setOnline(false);
            ActionHandler.filterAction(new Action("stop", "Sistema parado", ActionType.LOG));
        }
        else{ActionHandler.filterAction(new Action("stop error", "El sistema ya está parado", ActionType.LOG));}
    }

    @Override
    public void restart() {
        ClientHandler.setOnline(false);
        recibidor.setCanRun(false);
        try{initialize();}
        catch (Exception e){new Action("restart error", "Error reiniciando el sistema " + Arrays.toString(e.getStackTrace()), ActionType.QUIT);}
        ClientHandler.setClientes(new ArrayList<>());
        ClientHandler.setOnline(true);
        ActionHandler.filterAction(new Action("restart", "Sistema reiniciado", ActionType.LOG));
    }

    @Override
    public void send() {

    }

    @Override
    public void filterAction(Action action) {
        if(action.getName().equalsIgnoreCase("start")){start();}
        if(action.getName().equalsIgnoreCase("stop")){stop();}
        if(action.getName().equalsIgnoreCase("restart")){restart();}
    }
}
