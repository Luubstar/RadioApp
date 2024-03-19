package net.radioapp.web.UDP;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPEmitterBase extends Thread{
    public final int PORT = 7778;
    public DatagramSocket server;

    private void connect(){
        boolean connected = false;
        while(!connected){
            try {
                server = new DatagramSocket(PORT);
                connected = true;
            }
            catch (SocketException e){
                try{
                    Thread.sleep(100);
                }catch (InterruptedException err){
                    ActionHandler.filterAction(new Action("", "Error interrupción hilos", ActionType.QUIT));
                }
            }
        }
    }
    private void close(){server.close();}

    public void send(byte[] b, Client c) throws IOException {
        connect();
        DatagramPacket p = new DatagramPacket(b, b.length, c.getAddress(), PORT);
        server.send(p);
        close();
    }
}
