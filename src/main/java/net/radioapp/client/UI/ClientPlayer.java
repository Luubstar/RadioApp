package net.radioapp.client.UI;


import javax.sound.sampled.*;
import java.io.*;

public class ClientPlayer extends  Thread{
    SourceDataLine line;
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    public static boolean running;
    private boolean reading;
    public synchronized void addToPlay(byte[] stream) throws IOException, InterruptedException {
        while (reading){Thread.sleep(1);}
        reading = true;
        data.write(stream);
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