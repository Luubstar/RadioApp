package net.radioapp.web.netbasic;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.UDP.PackageTypes;
import net.radioapp.web.UDP.UDPEmitter;
import net.radioapp.web.UDP.UDPPacket;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
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
            Client c = new Client(address, 100);
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

    //TODO: Eliminar clientes que no manden paquetes en mucho tiempo
    public static void filterCommand(byte[] c, InetAddress address){
        PackageTypes type = PackageTypes.obtenerTipoPorCodigo(c[0]);

        System.arraycopy(c, 1, c, 0, c.length-1);
        String command = new String(c, StandardCharsets.UTF_8);

        command = command.toLowerCase();
        Client client = getClient(address);
        if (client == null) {client = addClient(address);}

        if (type == PackageTypes.HELO || client.isNew()){
            client.turnNew();
            ActionHandler.filterAction(new Action("nuevo cliente", "Nuevo cliente conectado", ActionType.LOG));
            new UDPEmitter(new UDPPacket(client,"Conectado satisfactoriamente".getBytes(), PackageTypes.LOG)).start();
        }
        if (type == PackageTypes.MOVER){
            client.setFrecuency(Double.parseDouble(command));
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
