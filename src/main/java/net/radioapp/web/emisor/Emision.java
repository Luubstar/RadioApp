package net.radioapp.web.emisor;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.UDP.Client;
import net.radioapp.web.UDP.ClientHandler;
import net.radioapp.web.UDP.UDPEmite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class Emision extends Thread{
    Emisora emisora;
    boolean connected = false;
    public Emision(Emisora fuente){
        this.emisora = fuente;
    }

    @Override
    public void run() {
        //TODO: Filtrar para que emita solo a los que están en su frecuencia

        int escuchas = 0;
        while (escuchas == 0){
            for(Client c : ClientHandler.getClientes()){
                if (c.getFrecuency() == emisora.getFrecuency()){escuchas++;}
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Iniciando transmisión de archivos, peso " +emisora.getFicheros().getFirst().length() );
        DatagramSocket s = null;
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        try{
            while (!connected) {
                try {
                    s = new DatagramSocket(7777);
                    connected = true;
                } catch (SocketException e) {
                    Thread.sleep(100);
                }
            }
            File f = emisora.getFicheros().getFirst();
            FileInputStream fileInputStream = new FileInputStream(f);
            byte[] buffer = new byte[2048];
            int bytesread;

            String request = "GET/";
            byte[] requestBytes = request.getBytes();
            System.arraycopy(requestBytes, 0, buffer, 0, requestBytes.length);

            bytesread = fileInputStream.read(buffer,requestBytes.length, buffer.length - requestBytes.length);
            while(bytesread != -1) {
                for (Client c : ClientHandler.getClientes()) {
                    DatagramPacket p = new DatagramPacket(buffer, bytesread, c.getAddress(), 7777);
                    s.send(p);
                    buffer = new byte[2048];
                    System.arraycopy(requestBytes, 0, buffer, 0, requestBytes.length);
                    bytesread = fileInputStream.read(buffer,requestBytes.length, buffer.length - requestBytes.length);
                }
            }
        }
        catch (IOException | InterruptedException e){
            ActionHandler.filterAction(new Action("", "Error emisor " + Arrays.toString(e.getStackTrace()), ActionType.QUIT));
        }
    }
}
