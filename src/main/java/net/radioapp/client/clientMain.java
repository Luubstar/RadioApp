package net.radioapp.client;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class clientMain {

    public static void main(String[] args) throws IOException {
        String ms = new String("Nicolás Barona Riera\n");
        DatagramPacket dp = new DatagramPacket(ms.getBytes(),
                ms.getBytes().length, InetAddress.getByName("localhost"),12345);

        DatagramSocket s = new DatagramSocket(12346);
        s.send(dp);
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        s.receive(receivePacket);
        System.out.println(new String(receivePacket.getData(), StandardCharsets.UTF_8));
        s.close();
    }
}
