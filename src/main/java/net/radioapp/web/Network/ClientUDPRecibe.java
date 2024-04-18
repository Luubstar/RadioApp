package net.radioapp.web.Network;

import net.radioapp.client.ClientActions;
import net.radioapp.client.ClientNetHandler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class ClientUDPRecibe extends Thread{
    public ClientUDPRecibe(){}
    @Override
    public void run() {
        try {
            DatagramSocket s = new DatagramSocket(UDPPacket.CLIENTRECIBER);
            byte[] buffer = new byte[UDPDataArray.CHUNKSIZE];
            DatagramPacket pq = new DatagramPacket(buffer, buffer.length);
            while (true) {
                s.receive(pq);
                //System.out.println(new UDPDataArray(pq.getData()).getType());
                if (pq.getData().length > 0){
                    ClientNetHandler.receive(pq.getData());
                }
                buffer = new byte[UDPDataArray.CHUNKSIZE];
                pq = new DatagramPacket(buffer, buffer.length);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
