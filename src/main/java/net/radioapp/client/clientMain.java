package net.radioapp.client;

import java.io.IOException;
import java.net.*;

public class clientMain {

    public static void main(String[] args) throws IOException {
        String ms = new String("Nicolás Barona Riera\n");
        DatagramPacket dp = new DatagramPacket(ms.getBytes(),
                ms.getBytes().length, InetAddress.getByName("localhost"),12345);

        DatagramSocket s = new DatagramSocket();
        s.send(dp);
        s.close();
    }
}
