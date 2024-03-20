package net.radioapp.web.emisor;

import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.UDP.PackageTypes;
import net.radioapp.web.UDP.UDPEmitter;
import net.radioapp.web.UDP.UDPPacket;
import net.radioapp.web.netbasic.Client;
import net.radioapp.web.netbasic.ClientHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Emision extends Thread{
    private final int SECONDSFOREMISSION = 3;
    Emisora emisora;
    public boolean connected = true;
    public Emision(Emisora fuente){
        this.emisora = fuente;
    }
    public List<Client> getclients(){
        List<Client> clientes = new ArrayList<>();
        while (clientes.isEmpty()){
            for(Client c : ClientHandler.getClientes()){
                if (c.getFrecuency() == emisora.getFrecuency()){clientes.add(c);}
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return clientes;
    }

    public void broadcast(byte[] b, List<Client> clientes, PackageTypes t){
        List<UDPEmitter> lista = new ArrayList<>();
        for (Client c: clientes) {
            UDPEmitter e;
            if (b.length != 0) {e = new UDPEmitter(new UDPPacket(c, b, t));}
            else {e = new UDPEmitter(new UDPPacket(c, t));}
            e.start();
            lista.add(e);
        }
        try{
            for(UDPEmitter e: lista){e.join();}
        }
        catch (InterruptedException e){
            ActionHandler.filterAction(new Action("", "Interrupcion en la espera del emisor", ActionType.QUIT));
        }
    }

    @Override
    public void run() {
        while (connected){
            long stime = System.nanoTime();
            List<Client> escuchas = getclients();

            broadcast(new byte[0], escuchas, PackageTypes.INICIOEMISION);
            try {Thread.sleep(1);} catch (InterruptedException e) {throw new RuntimeException(e);}

            try{
                long etime = System.nanoTime();
                int sdif = (int) (etime - stime)/1000000000;
                emisora.addSeconds(sdif);

                stime = System.nanoTime();
                byte[] buffer = emisora.getSecondsFromAudio(SECONDSFOREMISSION);

                broadcast(buffer, escuchas, PackageTypes.EMISION);

                try {Thread.sleep(1);} catch (InterruptedException e) {throw new RuntimeException(e);}
                broadcast(new byte[0], escuchas, PackageTypes.FINEMISION);
                etime = System.nanoTime();

                sdif = (int) (etime - stime)/1000000;
                int res = SECONDSFOREMISSION-sdif;
                if(res < 0){res = 0;}
                try {Thread.sleep( res);} catch (InterruptedException e){throw new RuntimeException(e);}
            }
            catch (IOException e){
                ActionHandler.filterAction(new Action("", "Error emisor " + Arrays.toString(e.getStackTrace()) + "\n" + e.getMessage(), ActionType.QUIT));
            }
        }
    }
}
