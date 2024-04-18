package net.radioapp.web.emisor;

import net.radioapp.Main;
import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Client;
import net.radioapp.web.Network.ClientHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Emision extends Thread{
    private final int SECONDSFOREMISSION = 4;
    Emisora emisora;
    public boolean connected = true;
    public Emision(Emisora fuente){
        this.emisora = fuente;
    }
    public List<Client> getclients(){
        List<Client> clientes = new ArrayList<>();
        while (clientes.isEmpty()){
            List<Client> activos = new ArrayList<>(ClientHandler.getClientes());
            for(Client c : activos){
                if (c.getFrecuency() == emisora.getFrecuency() && !clientes.contains(c)){
                    clientes.add(c);
                    c.ping();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return clientes;
    }

    /*public void broadcast(byte[] b, List<Client> clientes, PackageTypes t) {
        List<UDPEmitter> lista = new ArrayList<>();
        for (Client c: clientes) {
            UDPEmitter e;
            if (b.length != 0) {e = new UDPEmitter(new UDPPacket(c, b, t));}
            else {e = new UDPEmitter(new UDPPacket(c, t));}
            e.start();
            lista.add(e);
        }
        for(UDPEmitter a : lista){try {a.join();}catch (InterruptedException e){}}
    }*/

    //TODO: Testea bien este método nuevo
    public void broadcast(byte[] b, List<Client> clientes, PackageTypes t) {
        for (Client c: clientes) {
            Main.send(c, t, b);
        }
    }

    @Override
    public void run() { //TODO: Lo del tiempo funciona como el culo
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
                try {Thread.sleep( res);} catch (InterruptedException e){throw new RuntimeException(e);}
                dx = (float) res /1000;
            }
            catch (Exception e){
                ActionHandler.filterAction(new Action("", "Error emisor " + Arrays.toString(e.getStackTrace()) + "\n" + e.getMessage(), ActionType.QUIT));
            }
        }
    }
}
