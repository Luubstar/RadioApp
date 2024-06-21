package net.radioapp.web.emisor;

import net.radioapp.Main;
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
    private final List<Client> clientes = new ArrayList<>();
    public Emision(Emisora fuente){
        this.emisora = fuente;
    }
    private final Lock lockClientes = new ReentrantLock();

    public synchronized void waitForClients() throws InterruptedException {
        while(!requested){wait();}
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
        while (connected){
            synchronized (this){while (!isPlaying){
                    try {wait();} catch (InterruptedException e) {throw new RuntimeException(e);}}}
            try {waitForClients();} catch (InterruptedException e) {throw new RuntimeException(e);}

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

    public synchronized void setPlaying(boolean playing) {
        isPlaying = playing;
        notify();
    }
    public void kill(){connected = false;}
    public synchronized void addClient(Client c){
        lockClientes.lock();
        try{if(!clientes.contains(c)) {clientes.add(c); notify();}}
        finally{lockClientes.unlock();}}

    public synchronized void removeClient(Client c){
        lockClientes.lock();
        try{clientes.remove(c); notify();}
        finally{lockClientes.unlock();}
    }
    public boolean containsClient(Client c){return  clientes.contains(c);}
    public Emisora getEmisora() { return emisora; }
    public synchronized void request() {
        this.requested = true; notify();
    }
}
