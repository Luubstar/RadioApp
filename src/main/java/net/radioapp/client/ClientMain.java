package net.radioapp.client;


import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {
        new ClientUDPRecibe().start();
        new ClientUDPEmite("HELO").start();
        Scanner s = new Scanner(System.in);
        while(true){
            String r = s.nextLine();
            new ClientUDPEmite("move:" + r).start();
            System.out.println("Moviendonos a la frecuencia " + r);
        }
    }
}
