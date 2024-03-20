package net.radioapp.client.UI;


import javax.sound.sampled.*;
import java.io.*;

public class ClientPlayer extends  Thread{
    SourceDataLine line;
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    public static boolean running;
    public synchronized void addToPlay(byte[] stream) throws IOException {
        data.write(stream);
    }
    public void play(){
        if(!running){running = true; this.start();}
    }

    @Override
    public void run() {
        try {
            line = AudioSystem.getSourceDataLine(getAudioFormat());
            line.open(getAudioFormat());
            line.start();

            while (true) {
                line.write(data.toByteArray(), 0, data.toByteArray().length);
                data.reset();
                line.flush();
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private static AudioFormat getAudioFormat() {
        float sampleRate = 44100;  // Ejemplo: 44.1 kHz
        int sampleSizeInBits = 16;  // Ejemplo: 16 bits
        int channels = 2;           // Ejemplo: 2 canales (estéreo)
        boolean signed = true;      // Ejemplo: formato signed
        boolean bigEndian = false;  // Ejemplo: little-endian

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}