package net.radioapp.client.Audio;


import net.radioapp.client.ClientNetHandler;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Network.UDPDataArray;

import javax.sound.sampled.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//TODO: Esto debería implementar una interfaz para hacerlo intercambiable
public class ClientPlayer extends  Thread{
    protected SourceDataLine line;
    private Queue<ByteArrayOutputStream> data = new LinkedList<>();
    protected Lock lockLectura = new ReentrantLock();
    private final TreeMap<Integer, byte[]> entrada = new TreeMap<>();
    public boolean running;
    private AudioFormat nextFormat = getAudioFormat();
    private AudioFormat oldformat = getAudioFormat();
    private boolean connected = true;
    protected byte[] readingBuffer;
    private PlayerThread p;

    public ClientPlayer(){}

    public synchronized void addToPlay(byte[] stream, int pos) throws IOException {
       lockLectura.lock();
        try {
            if (entrada.containsKey(pos)){System.out.println("ForceCollapse " + pos);collapse();}
            entrada.put(pos, stream);}
        finally {lockLectura.unlock(); }
    }

    public synchronized void collapse() throws IOException {
        lockLectura.lock();
        try {
            ByteArrayOutputStream d = new ByteArrayOutputStream();
            while(!entrada.isEmpty()){
                d.write(entrada.pollFirstEntry().getValue());
            }
            data.add(d);
        }
        finally {lockLectura.unlock(); notify();}
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
            p = new PlayerThread(this);
            p.start();

            while(connected) {
                if (!data.isEmpty() && p.isStoped()) {
                    readingBuffer = data.poll().toByteArray();

                    if(!nextFormat.matches(oldformat)){
                        line = AudioSystem.getSourceDataLine(nextFormat);
                        if(line.isActive()) {line.close();}
                        line.open(nextFormat);
                        line.start();
                    }

                    p.execute(readingBuffer);
                }
                Thread.sleep(100);
            }
            kill();

        } catch (LineUnavailableException e) {System.out.println(e.getStackTrace()); } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        line.close();
    }

    public void setAudioFormat(AudioFormat f){
        oldformat = nextFormat;
        nextFormat = f; }

    public void pedirMas(){
        ClientNetHandler.send(new UDPDataArray(new byte[]{0}), PackageTypes.SOLICITAREMISION);}

    private static AudioFormat getAudioFormat() {
        float sampleRate = 48000;  // Ejemplo: 44.1 kHz
        int sampleSizeInBits = 16;  // Ejemplo: 16 bits
        int channels = 2;           // Ejemplo: 2 canales (estéreo)
        boolean signed = true;      // Ejemplo: formato signed
        boolean bigEndian = false;  // Ejemplo: little-endian 

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void kill(){
        p.stopPlayer();
        data.clear();
        entrada.clear();
    }
}