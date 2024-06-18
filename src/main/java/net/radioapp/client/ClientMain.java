package net.radioapp.client;


import net.radioapp.web.Network.*;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {
        ClientActions accion = new ClientActions();
        accion.start();
        ClientNetHandler.setAcciones(accion);
        new ClientUDPRecibe().start();
        ClientNetHandler.send(new UDPDataArray(1), PackageTypes.HELO);

        Scanner s = new Scanner(System.in);
        while(true){
            String r = s.nextLine();
            accion.move();
            ClientNetHandler.send(new UDPDataArray(r.getBytes()), PackageTypes.MOVER);
            System.out.println("Moviendonos a la frecuencia " + r);
        }
    }
}
