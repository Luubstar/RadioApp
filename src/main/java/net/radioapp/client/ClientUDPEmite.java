package net.radioapp.client;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.UDP.UDPPacket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class ClientUDPEmite extends Thread{
    String msg;
    public  ClientUDPEmite(String msg){this.msg = msg;}
    public void run() {
        try {
            DatagramSocket server = new DatagramSocket();
            DatagramPacket pq = new DatagramPacket(msg.getBytes(),
                    msg.getBytes().length, InetAddress.getByName("localhost"), UDPPacket.SERVERRECIBER);
            server.send(pq);

        } catch (Exception e) {
            ActionHandler.filterAction(new Action("Error del servidor", "Error en el servidor UDP " + Arrays.toString(e.getStackTrace()), ActionType.QUIT));
        }
    }
}
