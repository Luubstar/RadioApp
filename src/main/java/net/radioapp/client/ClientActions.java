package net.radioapp.client;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Network.UDPDataArray;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientActions extends Thread{
    Queue<byte[]> acciones = new ConcurrentLinkedQueue<>();
    ClientPlayer p;
    boolean running = true;

    public ClientActions() {
        this.p = new ClientPlayer();
        p.play();
    }

    public void filterAction(byte[] c) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        try {
            UDPDataArray packet = new UDPDataArray(c);
            PackageTypes type = packet.getType();
            c = packet.getContent();
            String command = new String(c, StandardCharsets.UTF_8).trim();

            switch (type) {
                case INICIOEMISION:
                    int samplerate = UDPDataArray.byteToInt(packet.getData(6,9));
                    int sampleSizeInBits = UDPDataArray.byteToInt(packet.getData(10,13));
                    int channels = UDPDataArray.byteToInt(packet.getData(14,18));
                    AudioFormat format = new AudioFormat((float) samplerate, sampleSizeInBits, channels, true, false);
                    p.setAudioFormat(format);
                    break;
                case FINEMISION:
                    p.collapse();
                    break;
                case EMISION:
                    int pos = UDPDataArray.byteToInt(packet.getData(2,5));
                    p.addToPlay(c, pos);
                    break;
                case MOVER:
                    System.out.println("Cambiando frecuencia a " + command);
                    break;
                case LOG:
                    System.out.println(command);
                    break;
                case PING:
                    ClientNetHandler.send(new UDPDataArray(), PackageTypes.PING);
                    break;
                default:
                    System.out.println("Algo ha fallado");
                    System.out.println(type);
                    break;
            }
        }
        catch (Exception e){System.out.println(e.getMessage()); throw new RuntimeException();}
    }

    public void move(){
        p.kill();
    }

    public synchronized  void addAction(byte[] e){acciones.add(e); notify();}
    @Override
    public void run() {
        while (running) {
            byte[] acc = null;
            synchronized (this) {
                if (!acciones.isEmpty() && acciones.peek() != null)  {acc = acciones.poll();}
                else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if (acc != null){
                try {
                    filterAction(acc);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
