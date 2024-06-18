package net.radioapp.client;


import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Network.UDPDataArray;

import javax.sound.sampled.*;
import java.io.*;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//TODO: Esto debería implementar una interfaz para hacerlo intercambiable
public class ClientPlayer extends  Thread{
    protected SourceDataLine line;
    protected ByteArrayOutputStream data = new ByteArrayOutputStream();
    protected Lock lockLectura = new ReentrantLock();
    private final TreeMap<Integer, byte[]> entrada = new TreeMap<>();
    public boolean running;
    private AudioFormat nextFormat = getAudioFormat();
    protected boolean isPlaying = false;
    private boolean connected = true;
    private PlayerThread p;

    public ClientPlayer(){}

    public synchronized void addToPlay(byte[] stream, int pos) throws IOException {
        lockLectura.lock();
        try {
            if (entrada.containsKey(pos)) {collapse();}
            entrada.put(pos, stream);
        }
        finally {lockLectura.unlock(); }
    }

    public synchronized void collapse() throws IOException {
        lockLectura.lock();
        try {while(!entrada.isEmpty()){data.write(entrada.pollFirstEntry().getValue());}}
        finally {lockLectura.unlock();}
    }

    public void play(){
        if(!running){running = true;connected = true; this.start();}
    }

    @Override
    public void run() {
        try {
            line = AudioSystem.getSourceDataLine(nextFormat);
            if(line.isActive()) {line.close();}
            line.open(nextFormat);
            line.start();
            isPlaying = false;

            while(connected) {
                if(data.size() > 0 && !isPlaying) {
                    isPlaying = true;
                    p = new PlayerThread(this);
                    p.start();
                }
            }
            kill();

        } catch (LineUnavailableException e) {System.out.println(e.getStackTrace()); }
        line.close();
    }

    public void setAudioFormat(AudioFormat f){nextFormat = f; }

    public void pedirMas(){ClientNetHandler.send(new UDPDataArray(new byte[]{0}), PackageTypes.SOLICITAREMISION);}

    private static AudioFormat getAudioFormat() {
        float sampleRate = 48000;  // Ejemplo: 44.1 kHz
        int sampleSizeInBits = 16;  // Ejemplo: 16 bits
        int channels = 2;           // Ejemplo: 2 canales (estéreo)
        boolean signed = true;      // Ejemplo: formato signed
        boolean bigEndian = false;  // Ejemplo: little-endian 

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void kill(){
        if(p != null) { p.stopPlayer();}
        p = null;
        isPlaying = false;
        data.reset();
        entrada.clear();
    }
}