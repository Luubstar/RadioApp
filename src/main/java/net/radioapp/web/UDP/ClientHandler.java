package net.radioapp.web.UDP;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
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
            Client c = new Client(address, 50);
            clientes.add(c);
            return  c;
        }
        else{return getClient(address);}
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

    public static void filterCommand(String c, InetAddress address){
        //TODO: Añadir comandos de los clientes
        //TODO: Eliminar clientes que no manden paquetes en mucho tiempo
        c = c.toLowerCase();
        Client client = getClient(address);
        if (client == null) {client = addClient(address);}

        if (client.isNew()){
            client.turnNew();
            ActionHandler.filterAction(new Action("nuevo cliente", "Nuevo cliente conectado", ActionType.LOG));
            new UDPEmite("Conectado satisfactoriamente", address).start();
        }
        if (c.startsWith("move:")){
            c = c.split("move:")[1];
            client.setFrecuency(Double.parseDouble(c));
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
}
