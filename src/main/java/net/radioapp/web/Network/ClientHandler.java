package net.radioapp.web.Network;

import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.web.Client;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    public static final int MAX_LOSTED_PINGS = 100;
    private static List<Client> clientes = new ArrayList<>();
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
            return  c;
        }
        else{return getClient(address);}
    }

    public static void removeClient(Client c){
        clientes.remove(c);
    }

    public static void setClientes(List<Client> clientes) {
        ClientHandler.clientes = clientes;
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
            ActionHandler.log("Nuevo cliente conectado");
            client.setRequested(true);
            new UDPEmitter(new UDPPacket(client,"Conectado satisfactoriamente".getBytes(), PackageTypes.LOG)).start();
        }
        if (type.equals(PackageTypes.MOVER)){
            command = command.split("move: ")[0];
            try {
                client.setFrecuency(Double.parseDouble(command));
            }
            catch (NumberFormatException e){ActionHandler.log("Se ha recibido una frecuencia con un valor incorrecto por parte de un cliente " + command);}
        }
        else if (type.equals(PackageTypes.PING)){
            client.pingReceived();
        }
        else if (type.equals(PackageTypes.SOLICITAREMISION)){
            System.out.println("Pide paquete");
            client.setRequested(true);
        }
        else{
            ActionHandler.log("Algo ha fallado");
            ActionHandler.log(type.toString());
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
            c.setFrecuency(f);
        }
    }
}
