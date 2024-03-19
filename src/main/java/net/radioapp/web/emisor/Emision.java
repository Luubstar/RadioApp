package net.radioapp.web.emisor;

import net.radioapp.ActionHandler;
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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Emision extends Thread{
    Emisora emisora;
    boolean connected = false;
    public Emision(Emisora fuente){
        this.emisora = fuente;
    }
    public List<Client> getclients(){
        List<Client> clientes = new ArrayList<>();
        while (clientes.size() == 0){
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
        //TODO: Filtrar para que emita solo a los que están en su frecuencia

        List<Client> escuchas = getclients();
        System.out.println("Iniciando transmisión de archivos, peso " +emisora.getFicheros().getFirst().length() );

        broadcast(new byte[0], escuchas, PackageTypes.INICIOEMISION);
        try{
            File f = emisora.getFicheros().getFirst();
            FileInputStream fileInputStream = new FileInputStream(f);
            byte[] buffer = fileInputStream.readAllBytes();

            broadcast(buffer, escuchas, PackageTypes.EMISION);
            broadcast(new byte[0], escuchas, PackageTypes.FINEMISION);
        }
        catch (IOException e){
            ActionHandler.filterAction(new Action("", "Error emisor " + Arrays.toString(e.getStackTrace()), ActionType.QUIT));
        }
    }
}
