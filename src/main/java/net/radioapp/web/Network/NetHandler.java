package net.radioapp.web.Network;

import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.WebHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.web.Client;
import net.radioapp.web.emisor.Emision;
import net.radioapp.web.emisor.Emisora;
import net.radioapp.web.emisor.Grupo;

import net.radioapp.commandController.Colors;
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
    private static final List<Grupo> gruposList = new ArrayList<>();
    private static final List<Emisora> emisorasList = new ArrayList<>();
    private static final List<Emision>  emisionesActivas = new ArrayList<>();
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
        grupoActual = gruposList.getFirst();
        recibidor = new UDPRecibe();
        recibidor.start();
        if(!ClientHandler.isOnline()){
            ClientHandler.setOnline(true);
            ActionHandler.log(Colors.Green.colorize("> Sistema iniciado"));}
        else {ActionHandler.log(Colors.Red.colorize(" El sistema ya está iniciado"));}
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
        if(!ClientHandler.isOnline()){ClientHandler.setOnline(true);}

        for(Emisora e : emisorasList){
            boolean isPlaying = false;
            for(Emision t : emisionesActivas){
                if (e.getName().equals(t.getEmisora().getName())){isPlaying = true; break;}
            }
            if (!isPlaying){
                Emision em = new Emision(e);
                em.start();
                emisionesActivas.add(em);
            }
        }

        for(Emision e : emisionesActivas){e.setPlaying(true);}
    }

    @Override
    public void stop() {
        if (ClientHandler.isOnline()) {
            ClientHandler.setOnline(false);
            for(Emision e : emisionesActivas){
                e.setPlaying(false);
            }
            ActionHandler.log(Colors.Green.colorize(" Sistema parado"));
        }
        else{ActionHandler.log(Colors.Red.colorize(" El sistema ya está parado"));}
    }

    @Override
    public void restart() {
        ClientHandler.setOnline(false);
        recibidor.setCanRun(false);

        for(Emision e : emisionesActivas){e.kill();}
        emisionesActivas.clear();

        try{initialize();}
        catch (Exception e){ActionHandler.handleException(e, " Error en el reinicio del servicio de red");}
        start();

        ActionHandler.log(Colors.Green.colorize("Sistema reiniciado"));
        System.out.println(emisionesActivas.getFirst().getEmisora().getFrecuency());
    }

    @Override
    public void send(UDPDataArray arg, PackageTypes t) {
        for(Client c: ClientHandler.getClientes()){
            new UDPEmitter(new UDPPacket(c, arg.getData(), t)).start();
        }
    }

    public void send(UDPDataArray arg, PackageTypes t, Client c){
        new UDPEmitter(new UDPPacket(c, arg.getData(), t)).start();
    }

    public static void assingClient(Client c){
        if(c.getEmisora() != null){
            if(c.getEmisora().getEmisora().getFrecuency() == c.getFrecuency() && c.getEmisora().containsClient(c)){return;}
            c.getEmisora().removeClient(c);
        }

        for(Emision a : emisionesActivas){if(a.getEmisora().getFrecuency() == c.getFrecuency()){
            a.addClient(c);
            c.setEmisora(a);
            c.setRequested();
        }}

    }

    @Override
    public void filterAction(Action action) {
        switch (action.getName().toLowerCase()) {
            case "start":
                start();
                break;
            case "stop":
                stop();
                break;
            case "restart":
                restart();
                break;
            case "say":
                send(new UDPDataArray(action.getRes().getBytes()), PackageTypes.LOG);
                break;
            case "setfrecuency":
                try{
                    ClientHandler.moveAll(Double.parseDouble(action.getRes()));
                    send(new UDPDataArray(action.getRes().getBytes()), PackageTypes.MOVER);
                    ActionHandler.log("Todos los clientes han sido" +
                            "cambiados a la frecuencia ");}
                catch(NumberFormatException e){ActionHandler.handleException(e, "La frecuencia debe de ser un número válido");}
                break;

        }
    }

    public void getState(){
        StringBuilder r = new StringBuilder("Mostrando los clientes:\n");

        for (Client c: ClientHandler.getClientes()){
            r.append("> ").append(c.toString()).append("\n");
        }

        ActionHandler.log(r.toString());
    }
}
