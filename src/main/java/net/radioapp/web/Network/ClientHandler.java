package net.radioapp.web.Network;

import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.web.Client;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    public static final int MAX_LOSTED_PINGS = 10;
    private static final List<Client> clientes = new ArrayList<>();
    private static boolean online;

    private static boolean checkIfContains(InetAddress address){
        for(Client c:clientes){
            if (c.getAddress() == address){return true;}
        }
        return false;
    }

    public static Client addClient(InetAddress address){
        if(!checkIfContains(address)){
            Client c = new Client(address, 100);
            clientes.add(c);
            return c;
        }
        else{return getClient(address);}
    }

    public static void removeClient(Client c){
        clientes.remove(c);
    }

    private static Client getClient(InetAddress address){
        for(Client c:clientes){
            if (c.getAddress().equals(address)){return c;}
        }
        return null;
    }

    public static void filterCommand(UDPDataArray c, InetAddress address){
        PackageTypes type = c.getType();

        String command = new String(c.getContent(), StandardCharsets.UTF_8);

        command = command.trim().toLowerCase();
        Client client = getClient(address);

        if (client == null) {client = addClient(address);}

        if (type.equals(PackageTypes.HELO) || client.isNew()){
            client.turnNew();
            client.setFrequency(NetHandler.getGrupoActual().getDefaultFrequency());
            ActionHandler.log("Nuevo cliente escuchando");
            new UDPEmitter(new UDPPacket(client,"Conectado satisfactoriamente".getBytes(), PackageTypes.LOG)).start();
        }

        switch (type) {
            case MOVER:
                command = command.split("move: ")[0];
                try {
                    double val = Double.parseDouble(command);
                    if (val > NetHandler.getGrupoActual().getMaxFrequency() ||
                            val < NetHandler.getGrupoActual().getMinFrequency()){throw new ArithmeticException();}
                    client.setFrequency(val);
                    NetHandler.assingClient(client);
                    client.setRequested();
                } catch (NumberFormatException e) {
                    ActionHandler.logError("Se ha recibido una frecuencia con un valor incorrecto por parte de un cliente: " + command);
                }
                catch (ArithmeticException e){
                    ActionHandler.logError("Se ha recibido una frecuencia con un valor fuera del margen indicado por parte de un cliente: " + command);
                }
                break;
            case HELO:
            case PING:
                client.pingReceived();
                break;
            case SOLICITAREMISION:
                NetHandler.assingClient(client);
                client.setRequested();
                break;
            default:
                ActionHandler.log("Algo ha fallado");
                ActionHandler.log(type.toString());
                break;
        }
    }

    public static void reasingClients(){
        for(Client c : clientes){
            c.setFrequency(NetHandler.getGrupoActual().getDefaultFrequency());
            NetHandler.assingClient(c);
            c.setRequested();
        }
    }

    public static boolean isOnline() {
        return online;
    }

    public static void setOnline(boolean online) {
        ClientHandler.online = online;
    }

    public static List<Client> getClientes() {
        return clientes;
    }

    public static void moveAll(double f){
        for(Client c: getClientes()){
            c.setFrequency(f);
            c.setRequested();
        }
    }
}
