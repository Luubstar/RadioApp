package net.radioapp.web.UDP;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    private static List<Client> clientes = new ArrayList<>();

    private static boolean checkIfContains(InetAddress address){
        for(Client c:clientes){
            if (c.getAddress() == address){return true;}
        }
        return false;
    }

    public static void addClient(InetAddress address){
        if(!checkIfContains(address)){
            Client c = new Client(address, 50);
            clientes.add(c);
        }
    }

    private static Client getClient(InetAddress address){
        for(Client c:clientes){
            if (c.getAddress() == address){return c;}
        }
        return null;
    }

    public static void filterCommand(String c, InetAddress address){
        //TODO: Añadir comandos de los clientes
        //TODO: Eliminar clientes que no manden paquetes en mucho tiempo
        c = c.toLowerCase();
        Client client = getClient(address);
        if (client.isNew()){//TODO:Manda mensaje al cliente
            client.turnNew();
            System.out.println("Nuevo cliente conectado");//TODO: Mejorar el printeo
            new UDPEmite("Conectado satisfactoriamente", address).start();
        }
        if (c.startsWith("move:")){c = c.split("move:")[1];} //TODO: Cambia frecuencia a cliente
    }


}
