package net.radioapp.client;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Network.UDPDataArray;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class ClientActions extends Thread{
    Queue<byte[]> acciones = new LinkedList<>();
    ClientPlayer p;

    public ClientActions() {
        this.p = new ClientPlayer();
        p.play();
    }

    public void addAction(byte[] e){acciones.add(e);}

    public void filterAction(byte[] c) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        try {
            UDPDataArray packet = new UDPDataArray(c);
            PackageTypes type = packet.getType();

            int pos = UDPDataArray.byteToInt(packet.getData(2,5));

            c = packet.getContent();

            String command = new String(c, StandardCharsets.UTF_8).trim();
            //TODO: Creo que se está congelando cuando llegan muchas emisiones tras un finemision y congela el ping
            System.out.println(type);
            System.out.println(pos);
            switch (type) {
                case INICIOEMISION:
                    System.out.println("Recibiendo canción");
                    break;
                case FINEMISION:
                    System.out.println("finalizado");
                    p.collapse();
                    p.play();
                    break;
                case EMISION:
                    p.addToPlay(c, pos);
                    break;
                case MOVER:
                    System.out.println("Cambiando frecuencia a " + command);
                    break;
                case LOG:
                    System.out.println(command);
                    break;
                case PING:
                    System.out.println("Pingeando");
                    ClientNetHandler.send(new UDPDataArray(), PackageTypes.PING);
                    break;
                default:
                    System.out.println("Algo ha fallado");
                    System.out.println(type);
                    break;
            }
        }
        catch (Exception e){e.printStackTrace(); System.out.println(e.getMessage()); throw new RuntimeException();}
    }

    @Override
    public void run() {
        while (true) {
            if (!acciones.isEmpty()) {
                try {
                    if(acciones.peek() != null) {filterAction(acciones.poll());}
                    else{acciones.poll();}
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
