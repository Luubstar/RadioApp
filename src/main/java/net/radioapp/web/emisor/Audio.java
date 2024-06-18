package net.radioapp.web.emisor;

import net.radioapp.web.Network.UDPDataArray;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

public abstract class Audio {
    private final File archivo;
    private final int bytespersecond;

    public Audio(File a) {
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

    public UDPDataArray getMetadata(){return  null;}

    public byte[] getSeconds(int start, int duration) throws IOException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(archivo);

            // Saltar al punto de inicio
            long skipBytes = start * bytespersecond;
            if (skipBytes > 0) {
                stream.skip(skipBytes);
            }

            // Calcular la longitud correcta, redondeando hacia arriba al siguiente múltiplo de 4
            int len = duration * bytespersecond;
            if (len % 4 != 0) {
                len = ((len / 4) + 1) * 4;
            }

            // Leer los datos
            byte[] buffer = new byte[len];
            int bytesRead = stream.read(buffer);

            // Ajustar el tamaño del buffer si no se leyeron suficientes bytes
            if (bytesRead < len) {
                byte[] adjustedBuffer = new byte[bytesRead];
                System.arraycopy(buffer, 0, adjustedBuffer, 0, bytesRead);
                return adjustedBuffer;
            }

            return buffer;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
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
