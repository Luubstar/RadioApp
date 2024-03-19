package net.radioapp.client;

import net.radioapp.web.UDP.UDPPacket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class ClientUDPRecibe extends Thread{
    @Override
    public void run() {
        try {
            DatagramSocket s = new DatagramSocket(UDPPacket.CLIENTRECIBER);
            byte[] buffer = new byte[UDPPacket.CHUNKSIZE];
            DatagramPacket pq = new DatagramPacket(buffer, buffer.length);
            while (true) {
                s.receive(pq);
                //TODO: Filtrar entre texto y audio
                System.out.println(new String(pq.getData(), StandardCharsets.UTF_8));
            }
        }
        catch (Exception e){
        }
    }
}
