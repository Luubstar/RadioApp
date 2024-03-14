package net.radioapp.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class ClientUDPRecibe extends Thread{
    @Override
    public void run() {
        while (true){
            try {
                DatagramSocket s = new DatagramSocket(7778);
                byte[] buffer = new byte[2048];
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
}
