package net.radioapp.web.Network;

import net.radioapp.client.ClientNetHandler;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientUDPRecibe extends Thread{
    public ClientUDPRecibe(){}
    @Override
    public void run() {
        try {
            try(DatagramSocket s = new DatagramSocket(UDPPacket.CLIENTRECIBER)){
                byte[] buffer = new byte[UDPDataArray.CHUNKSIZE];
                DatagramPacket pq = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    s.receive(pq);
                    if (new UDPDataArray(pq.getData()).getType() != null) {
                        ClientNetHandler.receive(pq.getData());
                    }
                    buffer = new byte[UDPDataArray.CHUNKSIZE];
                    pq = new DatagramPacket(buffer, buffer.length);
                }
            }
        }
        catch (Exception e){System.out.println(e.getMessage());}
    }
}
