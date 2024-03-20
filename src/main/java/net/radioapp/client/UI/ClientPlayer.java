package net.radioapp.client.UI;


import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ClientPlayer {
    public static void play(byte[] stream){

        try {
            // Abrir una línea de datos de origen (source data line) con el formato de audio dado
            SourceDataLine line = AudioSystem.getSourceDataLine(getAudioFormat());
            line.open(getAudioFormat());
            line.start();

            byte[] audioData = stream;

            line.write(audioData, 0, audioData.length);

            line.drain();
            line.stop();
            line.close();
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