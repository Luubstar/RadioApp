package net.radioapp.client;

import net.radioapp.client.UI.ClientPlayer;
import net.radioapp.web.UDP.PackageTypes;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class ClientActions extends Thread{
    Queue<byte[]> acciones = new LinkedList<>();
    static boolean recording;
    static FileOutputStream fileStream;
    static File res;

    public void addAction(byte[] e){
        acciones.offer(e);
    }

    public void filterAction(byte[] c) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        PackageTypes type = PackageTypes.obtenerTipoPorCodigo(c[0]);

        System.arraycopy(c, 1, c, 0, c.length-1);
        String command = new String(c, StandardCharsets.UTF_8);

        switch (type){
            case INICIOEMISION:
                res = new File("temp.wav");
                fileStream = new FileOutputStream(res);
                recording = true;
                System.out.println("Recibiendo canción");
                break;
            case FINEMISION:
                recording = false;
                fileStream.close();
                System.out.println("finalizado, peso total " + res.length());
                ClientPlayer.play(new FileInputStream(res).readAllBytes());
                break;
            case EMISION:
                if (!recording) break;
                fileStream.write(c);
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

    @Override
    public void run() {
        while (true) {
            if (!acciones.isEmpty()) {
                try {
                    filterAction(acciones.poll());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
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
