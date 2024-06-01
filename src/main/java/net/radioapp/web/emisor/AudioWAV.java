package net.radioapp.web.emisor;

import net.radioapp.web.Network.UDPDataArray;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

public class AudioWAV extends Audio{
    /* bytes:
        0 -> Formato
        1-4 -> SampleRate
        5-8 -> SampleSizeInBits
        9-12 -> Channels
     */

    public int channels, sampleSizeInBits, sampleRate;
    protected AudioWAV(File a) throws UnsupportedAudioFileException, IOException {
        super(a);
        byte[] byteArray = new FileInputStream(getArchivo()).readAllBytes();
        UDPDataArray array = new UDPDataArray(byteArray);
        channels = UDPDataArray.byteToIntLittleEndian(array.getData(22,23));
        sampleSizeInBits = UDPDataArray.byteToIntLittleEndian(array.getData(34,35));
        sampleRate = UDPDataArray.byteToIntLittleEndian(array.getData(24,27));
    }

    public UDPDataArray getMetadata(){
        UDPDataArray array = new UDPDataArray();
        array.addData(AudioExtension.WAV.getBytevalue());
        array.addData(UDPDataArray.intToByte(sampleRate));
        array.addData(UDPDataArray.intToByte(sampleSizeInBits));
        array.addData(UDPDataArray.intToByte(channels));
        return array;
    }


}
