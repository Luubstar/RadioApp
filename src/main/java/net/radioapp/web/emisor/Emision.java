package net.radioapp.web.emisor;

import net.radioapp.Main;
import net.radioapp.commandController.Colors;
import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Client;
import net.radioapp.web.Network.UDPDataArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Emision extends Thread{
    private final Emisora emisora;
    private boolean connected = true;
    private boolean isPlaying = true;
    private boolean requested = false;
    private boolean empty = true;
    private final List<Client> clientes = new ArrayList<>();
    public Emision(Emisora fuente){
        this.emisora = fuente;
    }
    private final Lock lockClientes = new ReentrantLock();

    public synchronized void waitForClients() throws InterruptedException {
        if(!connected){return;}
        while(!requested){wait();}
        if(!connected){return;}
        for (Client c : clientes) {
            lockClientes.lock();
            try{c.ping();}
            finally{lockClientes.unlock();}
        }
        requested = false;
    }

    public void broadcast(byte[] b, List<Client> clientes, PackageTypes t) {
        if(clientes.isEmpty()){return;}
        lockClientes.lock();
        try{for (Client c: clientes) {Main.send(new UDPDataArray(b), t, c);}}
        finally {lockClientes.unlock();}
    }

    @Override
    public void run() {
        //TODO: Cuando no hay clientes, continuar avanzando la reproducción a un ritmo fijo
        while (connected){
            long momentoInicial = System.currentTimeMillis();
            synchronized (this){while (!isPlaying){
                    try {wait();} catch (InterruptedException e) {throw new RuntimeException(e);}}}
            try {waitForClients();} catch (InterruptedException e) {throw new RuntimeException(e);}
            if(!connected){break;}

            long momentoFinal = System.currentTimeMillis();
            int segundos = (int) Math.ceil((double) (momentoFinal - momentoInicial) /1000);

            if(empty) {emisora.addSeconds(segundos); empty = false;}

            broadcast(emisora.getActualTrack().getMetadata().getData(), clientes, PackageTypes.INICIOEMISION);
            try{
                Thread.sleep(10);
                byte[] buffer = emisora.getNextChunk();

                broadcast(buffer, clientes, PackageTypes.EMISION);
                Thread.sleep(1000);
                broadcast(new byte[1], clientes, PackageTypes.FINEMISION);
            }
            catch (Exception e){
                ActionHandler.filterAction(new Action("", "Error emisor " + Arrays.toString(e.getStackTrace()) + "\n" + e.getMessage(), ActionType.QUIT));
            }
        }
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(Colors.Green.colorize(emisora.getName())).append(Colors.Blue.colorize(" (" +getEmisora().getFrequency() + "MHz)\n"));
        if(connected) {str.append(Colors.Green.colorize("Reproduciendo -> ")).append(Colors.Blue.colorize(getEmisora().getAudioName()));}
        else{str.append(Colors.Red.colorize("No reproduciendo"));}
        return str.toString();
    }

    public synchronized void setPlaying(boolean playing) {
        isPlaying = playing;
        notify();
    }
    public synchronized void kill(){connected = false; notify();}
    public synchronized void addClient(Client c){
        lockClientes.lock();
        try{if(!clientes.contains(c)) {clientes.add(c); notify();}}
        finally{lockClientes.unlock();}}

    public synchronized void removeClient(Client c){
        lockClientes.lock();
        try{
            clientes.remove(c);
            if(clientes.isEmpty()){empty = true;}
            notify();
        }
        finally{lockClientes.unlock();}
    }
    public boolean containsClient(Client c){return  clientes.contains(c);}
    public Emisora getEmisora() { return emisora; }
    public synchronized void request() {
        this.requested = true; notify();
    }
}
