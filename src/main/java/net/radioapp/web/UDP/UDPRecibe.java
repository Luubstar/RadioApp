package net.radioapp.web.UDP;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.netbasic.ClientHandler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UDPRecibe extends Thread{
    private boolean canRun = true;
    @Override
    public void run() {
        try {
            DatagramSocket s = new DatagramSocket(UDPPacket.SERVERRECIBER);
            s.setSoTimeout(500);
            byte[] buffer = new byte[UDPPacket.CHUNKSIZE];
            DatagramPacket pq = new DatagramPacket(buffer, buffer.length);
            while (canRun) {
                if(!ClientHandler.isOnline()){
                    Thread.sleep(100);
                }
                else {
                    try{
                    s.receive(pq);
                    ClientHandler.filterCommand(new String(pq.getData(), StandardCharsets.UTF_8), pq.getAddress());
                    }
                    catch (SocketTimeoutException ignored){}

                }
            }
        }
        catch (SocketException ignored){}
        catch (Exception e){
            ActionHandler.filterAction(new Action("Error recibidor", "Error recibiendo paquetes " + Arrays.toString(e.getStackTrace()), ActionType.QUIT));
        }
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }
}
