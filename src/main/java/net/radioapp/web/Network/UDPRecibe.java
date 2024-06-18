package net.radioapp.web.Network;

import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

class UDPRecibe extends Thread{
    private boolean canRun = true;
    @Override
    public void run() {
        try(DatagramSocket s = new DatagramSocket(UDPPacket.SERVERRECIBER)) {
            byte[] buffer = new byte[UDPDataArray.CHUNKSIZE];
            DatagramPacket pq = new DatagramPacket(buffer, buffer.length);
            while (canRun) {
                try {
                    s.receive(pq);
                    ClientHandler.filterCommand(new UDPDataArray(pq.getData()), pq.getAddress());

                    buffer = new byte[UDPDataArray.CHUNKSIZE];
                    pq = new DatagramPacket(buffer, buffer.length);
                } catch (SocketTimeoutException ignored) {
                }
            }
        }
        catch (IOException e){
            ActionHandler.filterAction(new Action("Error recibidor", "Error recibiendo paquetes " + Arrays.toString(e.getStackTrace()) + " " + e.getCause(), ActionType.QUIT));
        }
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }
}
