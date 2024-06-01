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
    int SEGUNDOSPORCARGA = 3;

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
        System.out.println(data.size());
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

            while (true) {
                if (data.size() > 0) {
                    if(nextFormat != oldFormat){
                    line.close();
                    line.open(nextFormat);
                    line.start();}

                    while (reading){Thread.sleep(1);}
                    reading = true;
                    //TODO: HAY QUE IR CON CUIDADO CON QUE LA ESCRITURA NO SEA MODULO 4
                    // TIENE UN PARCHE TEMPORAL
                    byte[] val = extraer(SEGUNDOSPORCARGA*3);
                    line.write(val,0, val.length);
                    if(data.size() < SEGUNDOSPORCARGA*3){pedirMas();}
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

    public void setAudioFormat(AudioFormat f){
        oldFormat = nextFormat;
       nextFormat = f;
    }

    private void pedirMas(){
        ClientNetHandler.send(new UDPDataArray(new byte[]{0}), PackageTypes.SOLICITAREMISION);
        System.out.println("Pide paquete");
    }

    private byte[] extraer(int segundos){
        int numBytesToExtract = (int) nextFormat.getSampleRate() * segundos;
        byte[] originalBytes = data.toByteArray();
        if ( numBytesToExtract > data.size()) {
            numBytesToExtract = originalBytes.length;
        }

        // Crear un nuevo array de bytes para almacenar los bytes extraídos
        byte[] extractedBytes = Arrays.copyOfRange(originalBytes, 0, numBytesToExtract);

        // Crear un nuevo array de bytes para los bytes restantes
        byte[] remainingBytes = Arrays.copyOfRange(originalBytes, numBytesToExtract, originalBytes.length);

        // Crear un nuevo ByteArrayOutputStream con los bytes restantes
        data = new ByteArrayOutputStream();
        data.write(remainingBytes, 0, remainingBytes.length);

        return extractedBytes;
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