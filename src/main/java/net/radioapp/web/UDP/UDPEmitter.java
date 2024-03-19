package net.radioapp.web.UDP;

import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPEmitter extends Thread{
    public DatagramSocket server;
    UDPPacket paquete;
    public int port;
    public int destino;
    public UDPEmitter(UDPPacket p){
        paquete = p;
        port = UDPPacket.SERVEREMITTER;
        destino = UDPPacket.CLIENTRECIBER;
    }

    private void connect(){
        boolean connected = false;
        while(!connected){
            try {
                server = new DatagramSocket(port);
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

    public void send(UDPPacket p) throws IOException {
        connect();
        p.send(server, destino);
        close();
    }

    @Override
    public void run() {
        try{
            send(paquete);
        }
        catch (IOException e){
            ActionHandler.filterAction(new Action("", "Error emitiendo mensaje", ActionType.QUIT));
        }
    }
}
