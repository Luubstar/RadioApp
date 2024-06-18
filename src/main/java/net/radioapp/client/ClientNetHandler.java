package net.radioapp.client;

import net.radioapp.web.Client;
import net.radioapp.web.Network.ClientUDPEmite;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Network.UDPDataArray;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientNetHandler {

    private static ClientActions acciones;
    protected static InetAddress server;

    static {
        try {
            server = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void send(UDPDataArray p, PackageTypes t){
        new ClientUDPEmite(new Client(server,0), p, t).start();
    }

    public static void receive(byte[] a){
        if(acciones == null){throw  new RuntimeException("Client Actions nunca fue asignado");}
        acciones.addAction(a);
    }

    public void setServer(InetAddress server) {
        this.server = server;
    }

    public InetAddress getServer() {
        return server;
    }

    public static void setAcciones(ClientActions a) {
        acciones = a;
    }
}
