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
                ClientActions.filterAction(pq.getData());
                buffer = new byte[UDPPacket.CHUNKSIZE];
                pq = new DatagramPacket(buffer, buffer.length);
            }
        }
        catch (Exception e){
        }
    }
}
