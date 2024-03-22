package net.radioapp.client;

import net.radioapp.web.UDP.PackageTypes;
import net.radioapp.web.UDP.UDPPacket;

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
            PackageTypes type = PackageTypes.obtenerTipoPorCodigo(c[0]);
            System.arraycopy(c, 1, c, 0, c.length - 1);

            byte[] num = new byte[2];
            System.arraycopy(c, 1, num, 0, 2);
            short pos = UDPPacket.bytesToShort(num);

            System.arraycopy(c, 2, c, 0, c.length - 2);
            String command = new String(c, StandardCharsets.UTF_8);

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
