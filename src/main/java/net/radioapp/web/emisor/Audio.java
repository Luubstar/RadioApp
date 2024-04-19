package net.radioapp.web.emisor;

import net.radioapp.web.Network.UDPDataArray;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

//SE ASUME QUE TODOS LOS ARCHIVOS TIENEN 2 CANALES, 16 BITS DE PROFUNDIDAD Y FRECUENCIA DE
//41000. ESTO ES ASÍ PARA EVITAR ENVIAR MÁS DATOS
public abstract class Audio {
    private final File archivo;
    private final int bytespersecond;
    public Audio(File a) throws UnsupportedAudioFileException, IOException {
        this.archivo = a;
        bytespersecond = Math.round((float) (16 * 2 * 44100) /8);
    }

    private static AudioExtension getExtension(File a) throws UnsupportedAudioFileException {
        if(a.getName().trim().toLowerCase().contains(".wav")){return  AudioExtension.WAV;}
        else{
            throw new UnsupportedAudioFileException();
        }
    }

    public File getArchivo() {
        return archivo;
    }

    public UDPDataArray getMetadata() throws IOException {return null;}

    public byte[] getSeconds(int start, int duration) throws IOException {
        FileInputStream stream = new FileInputStream(archivo);
        if(start * bytespersecond > 0){stream.skip((start * bytespersecond));}
        byte[] buffer = new byte[(int) (duration * bytespersecond)];
        stream.read(buffer);
        return buffer;
    }

    public int getBytespersecond() {
        return bytespersecond;
    }

    public static Audio newAudio(File a) throws UnsupportedAudioFileException, IOException {
        AudioExtension ext = getExtension(a);
        if(ext.equals(AudioExtension.WAV)){return new AudioWAV(a);}
        throw new UnsupportedAudioFileException();
    }
}
