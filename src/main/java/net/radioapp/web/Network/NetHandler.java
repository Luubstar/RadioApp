package net.radioapp.web.Network;

import net.radioapp.Main;
import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.WebHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class NetHandler implements WebHandler {
    private static final Path mainDir = Paths.get("./mainApp");
    private static final List<Path> emisorasPaths = new ArrayList<>();
    private static final List<Path> groupsPaths = new ArrayList<>();
    private static final List<Grupo> gruposList = new ArrayList<>();
    private static final List<Emisora> emisorasList = new ArrayList<>();
    private static final List<Emision>  emisionesActivas = new ArrayList<>();
    private static Grupo grupoActual;
    private UDPRecibe recibidor;

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
            emisorasPaths.clear();
            Stream<Path> emisoras = Files.list(p);
            emisoras.filter(Files::isDirectory).forEach(emisorasPaths::add);
            List<Emisora> temp = new ArrayList<>();

            for(Path e: emisorasPaths){
                Emisora emisora = new Emisora(e.getFileName().toString(), e);
                temp.add(emisora);
                emisorasList.add(emisora);
                emisora.readConfigFile();
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
        for(Emisora e : grupoActual.getEmisoras()){
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
        System.out.println(emisionesActivas.getFirst().getEmisora().getFrequency());
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
            if(c.getEmisora().getEmisora().getFrequency() == c.getFrequency() && c.getEmisora().containsClient(c)){return;}
            c.getEmisora().removeClient(c);
        }

        for(Emision a : emisionesActivas){if(a.getEmisora().getFrequency() == c.getFrequency()){
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
                            "cambiados a la frecuencia ");
                }
                catch(NumberFormatException e){ActionHandler.handleException(e, "La frecuencia debe de ser un número válido");}
                break;

        }
    }

    public void getState(){
        StringBuilder r = new StringBuilder("\nMostrando los clientes:\n");
        for (Client c: ClientHandler.getClientes()){r.append("> ").append(c.toString()).append("\n");}
        ActionHandler.log(r.toString());
    }
    public void getStations(){
        StringBuilder r = new StringBuilder("\nMostrando las estaciones del grupo " + grupoActual.getName() + ":\n");
        int i = 0;
        for (Emision c: emisionesActivas){
            String index = Colors.Yellow.colorize("[" + i + "]");
            r.append("> ").append(index).append(" ").append(c.toString()).append("\n");
            i++;
        }
        ActionHandler.log(r.toString());
    }
    public void getGroups(){
        StringBuilder r = new StringBuilder("\nMostrando los grupos: \n");
        int i = 0;
        for (Grupo c: gruposList){
            String index = Colors.Yellow.colorize("[" + i + "]");
            String data = c.toString();
            if(grupoActual.equals(c)){
                index = Colors.Bold.and(Colors.Italic).and(Colors.Underline).colorize(index);
                data  = Colors.Bold.and(Colors.Italic).and(Colors.Underline).colorize(data);
            }
            i++;
            r.append("> ").append(index).append(" ").append(data).append("\n");

        }
        ActionHandler.log(r.toString());
    }

    public void selectGroup(int index){
        if(index >= gruposList.size()){
            ActionHandler.filterAction(
                    new Action("error", "El grupo " + index +
                            " no existe, revisa el comando groups para ver los indices ", ActionType.ERROR));
        }
        else{
            grupoActual.reset();
            String oln = Colors.Blue.colorize(getGrupoActual().getName());
            grupoActual = gruposList.get(index);

            for(Emision e : emisionesActivas){e.kill();}
            emisionesActivas.clear();

            for(Emisora a : grupoActual.emisoras){
                System.out.println(a.getName());
                Emision em = new Emision(a);
                em.start();
                emisionesActivas.add(em);
            }
            ClientHandler.reasingClients();

            send(new UDPDataArray(), PackageTypes.CAMBIODEGRUPO);
            ActionHandler.filterAction(new Action("OK", "El grupo ha sido cambiado de forma exitosa:\n"
                    + oln + " -> " + Colors.Blue.colorize(grupoActual.getName()) +"\n",
                    ActionType.LOG));
        }
    }

    public void skipSong(int index){
        if(index >= emisionesActivas.size()){
            ActionHandler.filterAction(
                    new Action("error", "La estación " + index +
                            " no existe, revisa el comando stations para ver los indices ", ActionType.ERROR));
        }
        else{
            String oln = Colors.Blue.colorize(emisionesActivas.get(index).getEmisora().getAudioName());
            emisionesActivas.get(index).getEmisora().changeSong();
            ActionHandler.filterAction(new Action("OK", "La canción ha sido saltada de forma exitosa:\n"
                    + oln + " -> " + Colors.Blue.colorize(emisionesActivas.get(index).getEmisora().getAudioName()) +"\n",
                    ActionType.LOG));
        }
    }

    public static Grupo getGrupoActual(){return grupoActual;}
}
