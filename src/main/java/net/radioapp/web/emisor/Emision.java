package net.radioapp.web.emisor;

import net.radioapp.Main;
import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Client;
import net.radioapp.web.Network.ClientHandler;
import net.radioapp.web.Network.UDPDataArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Emision extends Thread{
    private final int SECONDSFOREMISSION = 5;
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
                c.ping();
                if (c.getFrecuency() == emisora.getFrecuency() && !clientes.contains(c) && c.isRequested()){
                    clientes.add(c);
                    c.setRequested(false);
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

    public void broadcast(byte[] b, List<Client> clientes, PackageTypes t) {
        for (Client c: clientes) {
            Main.send(new UDPDataArray(b), t, c);
        }
    }

    @Override
    public void run() {
        float dx = 0;
        while (connected){
            long stime = System.nanoTime();
            List<Client> escuchas = getclients();

            long temision = System.nanoTime();

            try{
                long etime = System.nanoTime();
                int sdif = (int) (etime - stime)/1000000000;
                emisora.addSeconds(sdif + dx);

                byte[] buffer = emisora.getSecondsFromAudio(SECONDSFOREMISSION*4);
                if(buffer.length % 4 != 0){
                    byte[] aux = new byte[buffer.length + buffer.length%4];
                    System.arraycopy(aux, 0, buffer, 0, buffer.length);
                }
                broadcast(buffer, escuchas, PackageTypes.EMISION);


                broadcast(new byte[1], escuchas, PackageTypes.FINEMISION);

                long eemision = System.nanoTime();
                sdif = (int) (eemision - temision)/1000000;
                int res = (SECONDSFOREMISSION * 4  * 1000)-sdif;
                dx = (float) res /1000;
                System.out.println("Paquete enviado");
            }
            catch (Exception e){
                ActionHandler.filterAction(new Action("", "Error emisor " + Arrays.toString(e.getStackTrace()) + "\n" + e.getMessage(), ActionType.QUIT));
            }
        }
    }
}
