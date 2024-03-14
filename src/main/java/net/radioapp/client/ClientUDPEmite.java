package net.radioapp.client;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class ClientUDPEmite extends Thread{
    public static final int PORT = 7777;
    public void run() {
        try {
            //TODO: Conectar entrada de texto con esto
            String message = "HELO";
            DatagramSocket server = new DatagramSocket();
            DatagramPacket pq = new DatagramPacket(message.getBytes(),
                    message.getBytes().length, InetAddress.getByName("localhost"), PORT);
            server.send(pq);

        } catch (Exception e) {
            ActionHandler.filterAction(new Action("Error del servidor", "Error en el servidor UDP " + Arrays.toString(e.getStackTrace()), ActionType.QUIT));
        }
    }
}
