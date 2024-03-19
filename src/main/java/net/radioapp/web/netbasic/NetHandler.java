package net.radioapp.web.netbasic;

import net.radioapp.ActionHandler;
import net.radioapp.WebHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.UDP.PackageTypes;
import net.radioapp.web.UDP.UDPEmitter;
import net.radioapp.web.UDP.UDPPacket;
import net.radioapp.web.UDP.UDPRecibe;
import net.radioapp.web.emisor.Emision;
import net.radioapp.web.emisor.Emisora;
import net.radioapp.web.emisor.Grupo;

import net.radioapp.commandController.Colors;
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
        //TODO: Configurar grupo actual y tal
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
        grupoActual = gruposList.get(0);
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
        ActionHandler.filterAction(new Action("start", Colors.Green.colorize("Sistema iniciado"), ActionType.LOG));}
        else {ActionHandler.filterAction(new Action("start error", Colors.Red.colorize("El sistema ya está iniciado"), ActionType.LOG));}

        for (Emisora e : emisorasList){
            new Emision(e).start();
        }
    }

    @Override
    public void stop() {
        if (ClientHandler.isOnline()) {
            ClientHandler.setOnline(false);
            ActionHandler.filterAction(new Action("stop", Colors.Green.colorize("Sistema parado"), ActionType.LOG));
        }
        else{ActionHandler.filterAction(new Action("stop error", Colors.Red.colorize("El sistema ya está parado"), ActionType.LOG));}
    }

    @Override
    public void restart() {
        ClientHandler.setOnline(false);
        recibidor.setCanRun(false);
        try{initialize();}
        catch (Exception e){new Action("restart error", Colors.Red.colorize("Error reiniciando el sistema " + Arrays.toString(e.getStackTrace())), ActionType.QUIT);}
        ClientHandler.setClientes(new ArrayList<>());
        ClientHandler.setOnline(true);
        ActionHandler.filterAction(new Action("restart", Colors.Green.colorize("Sistema reiniciado"), ActionType.LOG));
    }

    @Override
    public void send(PackageTypes t, String arg) {
        for(Client c: ClientHandler.getClientes()){
            new UDPEmitter(new UDPPacket(c, arg.getBytes(), t)).start();
        }
    }

    @Override
    public void filterAction(Action action) {
        if(action.getName().equalsIgnoreCase("start")){start();}
        if(action.getName().equalsIgnoreCase("stop")){stop();}
        if(action.getName().equalsIgnoreCase("restart")){restart();}
        if(action.getName().equalsIgnoreCase("setfrecuency")){
            ClientHandler.moveAll(Double.parseDouble(action.getRes()));
            send(PackageTypes.MOVER, action.getRes());
            ActionHandler.filterAction(new Action("", "Todos los clientes han sido" +
                    "cambiados a la frecuencia " + action.getRes(), ActionType.LOG));
        }
    }

    public void getState(){
        StringBuilder r = new StringBuilder("Mostrando los clientes:\n");

        for (Client c: ClientHandler.getClientes()){
            r.append("> ").append(c.toString()).append("\n");
        }

        ActionHandler.filterAction(new Action("show clients", r.toString(), ActionType.LOG));
    }
}
