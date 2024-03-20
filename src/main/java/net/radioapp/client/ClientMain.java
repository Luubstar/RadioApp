package net.radioapp.client;


import net.radioapp.web.UDP.PackageTypes;
import net.radioapp.web.UDP.UDPPacket;
import net.radioapp.web.netbasic.Client;

import java.net.InetAddress;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws Exception {
        ClientActions accion = new ClientActions();
        accion.start();

        new ClientUDPRecibe(accion).start();

        Client c = new Client(InetAddress.getByName("127.0.0.1"), 0);
        new ClientUDPEmite(new UDPPacket(c,PackageTypes.HELO)).start();

        Scanner s = new Scanner(System.in);
        while(true){
            String r = s.nextLine();
            new ClientUDPEmite(new UDPPacket(c, r.getBytes(), PackageTypes.MOVER)).start();
            System.out.println("Moviendonos a la frecuencia " + r);
        }
    }
}
