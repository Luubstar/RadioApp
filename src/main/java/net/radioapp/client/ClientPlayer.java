package net.radioapp.client;


import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Network.UDPDataArray;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Arrays;
import java.util.TreeMap;

//TODO: Esto debería implementar una interfaz para hacerlo intercambiable
public class ClientPlayer extends  Thread{
    SourceDataLine line;
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    TreeMap<Integer, byte[]> entrada = new TreeMap<>();
    public static boolean running;
    private boolean reading;
    AudioFormat nextFormat = getAudioFormat();
    AudioFormat oldFormat = getAudioFormat();
    static boolean isPlaying = false;

    public ClientPlayer(){}

    public synchronized void addToPlay(byte[] stream, int pos) throws IOException, InterruptedException {
        while (reading){Thread.sleep(1);}
        reading = true;
        if(entrada.containsKey(pos)){collapse();}
        entrada.put(pos, stream);
        reading = false;
    }
    public synchronized void collapse() throws InterruptedException, IOException {
        while (reading){Thread.sleep(1);}
        reading = true;
        while(!entrada.isEmpty()){
            data.write(entrada.pollFirstEntry().getValue());
        }
        entrada.clear();
        reading = false;
    }
    public void play(){
        if(!running){running = true; this.start();}
    }

    @Override
    public void run() {
        try {
            line = AudioSystem.getSourceDataLine(nextFormat);
            line.close();
            line.open(nextFormat);
            line.start();
            isPlaying = false;

            while(true) {
                if(data.size() > 0 && !isPlaying) {
                    isPlaying = true;
                    new Thread(() -> {

                        int bufferSize = line.getBufferSize();
                        int totalBytesRead = 0;
                        int bytesReaded;
                        boolean calledForMore = false;

                        while (reading){
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        reading = true;
                        byte[] buffer = data.toByteArray();
                        data.reset();
                        reading = false;

                        while (totalBytesRead < buffer.length) {
                            bytesReaded = line.write(buffer, totalBytesRead, Math.min(bufferSize, buffer.length - totalBytesRead));
                            totalBytesRead += bytesReaded;

                            if (totalBytesRead >= buffer.length){break;}
                            else if (totalBytesRead >= buffer.length * 0.5 && !calledForMore) {
                                calledForMore = true;
                                pedirMas();
                            }
                            else if(totalBytesRead == 0){System.exit(-1);}
                        }

                        isPlaying = false;
                        line.drain();
                    }).start();
                }
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        line.close();
    }

    public void setAudioFormat(AudioFormat f){
        oldFormat = nextFormat;
       nextFormat = f;
    }

    private void pedirMas(){
        ClientNetHandler.send(new UDPDataArray(new byte[]{0}), PackageTypes.SOLICITAREMISION);
        System.out.println("Pide paquete");
    }

    private static AudioFormat getAudioFormat() {
        float sampleRate = 48000;  // Ejemplo: 44.1 kHz
        int sampleSizeInBits = 16;  // Ejemplo: 16 bits
        int channels = 2;           // Ejemplo: 2 canales (estéreo)
        boolean signed = true;      // Ejemplo: formato signed
        boolean bigEndian = false;  // Ejemplo: little-endian 

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

}