package net.radioapp.client;


import javax.sound.sampled.*;
import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class ClientPlayer extends  Thread{
    SourceDataLine line;
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    TreeMap<Integer, byte[]> entrada = new TreeMap<>();
    public static boolean running;
    private boolean reading;

    public ClientPlayer(){

    }
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
            line = AudioSystem.getSourceDataLine(getAudioFormat());
            line.close();
            line.open(getAudioFormat());
            line.start();

            while (true) {
                if (data.size() > 0) {
                    while (reading){Thread.sleep(1);}
                    reading = true;
                    //TODO: HAY QUE IR CON CUIDADO CON QUE LA ESCRITURA NO SEA MODULO 4
                    // TIENE UN PARCHE TEMPORAL
                    while(data.toByteArray().length % 4 != 0){data.write(0);}
                    line.write(data.toByteArray(), 0, data.toByteArray().length);
                    data.reset();
                    line.drain();
                    reading = false;
                }
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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