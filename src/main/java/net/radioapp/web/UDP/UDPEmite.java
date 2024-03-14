package net.radioapp.web.UDP;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UDPEmite extends Thread {
    private static final int PORT = 7778;
    private final String message;
    private final InetAddress address;

    public UDPEmite(String m, InetAddress address){
        this.message = m;
        this.address = address;
    }

    public void run() {
        try {
            DatagramSocket server = new DatagramSocket();
            DatagramPacket pq = new DatagramPacket(message.getBytes(),
                    message.getBytes().length, address, PORT);
            server.send(pq);

        } catch (Exception e) {
            ActionHandler.filterAction(new Action("Error del servidor", "Error en el servidor UDP " + Arrays.toString(e.getStackTrace()), ActionType.QUIT));
        }
    }

}
