package net.radioapp.web.emisor;

import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.UDP.PackageTypes;
import net.radioapp.web.UDP.UDPEmitter;
import net.radioapp.web.UDP.UDPPacket;
import net.radioapp.web.netbasic.Client;
import net.radioapp.web.netbasic.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Emision extends Thread{
    private final int SECONDSFOREMISSION = 5;
    private final int AJUSTE = 10000;
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

    public void broadcast(byte[] b, List<Client> clientes, PackageTypes t) {
        List<UDPEmitter> lista = new ArrayList<>();
        for (Client c: clientes) {
            UDPEmitter e;
            if (b.length != 0) {e = new UDPEmitter(new UDPPacket(c, b, t));}
            else {e = new UDPEmitter(new UDPPacket(c, t));}
            e.start();
            lista.add(e);
        }
        for(UDPEmitter a : lista){try {a.join();}catch (InterruptedException e){}}
    }

    @Override
    public void run() {
        float dx = 0;
        while (connected){
            long stime = System.nanoTime();
            List<Client> escuchas = getclients();

            broadcast(new byte[0], escuchas, PackageTypes.INICIOEMISION);
            try{
                long etime = System.nanoTime();
                stime = System.nanoTime();
                int sdif = (int) (etime - stime)/1000000000;
                emisora.addSeconds(sdif + dx);

                byte[] buffer = emisora.getSecondsFromAudio(SECONDSFOREMISSION);

                broadcast(buffer, escuchas, PackageTypes.EMISION);

                broadcast(new byte[0], escuchas, PackageTypes.FINEMISION);
                etime = System.nanoTime();

                sdif = (int) (etime - stime)/1000000;
                int res = (SECONDSFOREMISSION * 1000)-sdif;
                if(res < 0){res = 0;}
                //System.out.println(res);
                try {Thread.sleep( res);} catch (InterruptedException e){throw new RuntimeException(e);}
                dx = (float) res /1000;
            }
            catch (IOException e){
                ActionHandler.filterAction(new Action("", "Error emisor " + Arrays.toString(e.getStackTrace()) + "\n" + e.getMessage(), ActionType.QUIT));
            }
        }
    }
}
