package net.radioapp.web.emisor;

import net.radioapp.web.Network.UDPDataArray;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Audio {
    private final File archivo;
    private static final int CHUNCKSIZE = (1024*1024);
    private List<byte[]> data = new ArrayList<>();

    public Audio(File a) {
        this.archivo = a;
    }

    public void load(){
        FileInputStream stream;
        data.clear();
        try{
            stream = new FileInputStream(archivo);
            byte[] buffer = new byte[CHUNCKSIZE];
            int bytesRead;

            while ((bytesRead = stream.read(buffer)) != -1) {
                if (bytesRead % 4 != 0) {
                    bytesRead = (bytesRead / 4) * 4;
                }

                byte[] chunk = new byte[bytesRead];
                System.arraycopy(buffer, 0, chunk, 0, bytesRead);

                data.add(chunk);
            }
        }
        catch (Exception e){}
    }

    public void unload(){
        data.clear();
    }

    public File getArchivo() {
        return archivo;
    }

    public UDPDataArray getMetadata(){return  null;}
    public byte[] getChunk(int currentChunk) {
        if(currentChunk < data.size()){return data.get(currentChunk);}
        else{return  null;}
    }
    private static AudioExtension getExtension(File a) throws UnsupportedAudioFileException {
        if(a.getName().trim().toLowerCase().contains(".wav")){return  AudioExtension.WAV;}
        else{
            throw new UnsupportedAudioFileException();
        }
    }
    public static Audio newAudio(File a) throws UnsupportedAudioFileException, IOException {
        AudioExtension ext = getExtension(a);
        if(ext.equals(AudioExtension.WAV)){return new AudioWAV(a);}
        throw new UnsupportedAudioFileException();
    }


}
