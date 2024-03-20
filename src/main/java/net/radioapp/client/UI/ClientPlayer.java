package net.radioapp.client.UI;


import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ClientPlayer {
    public static void reset(){
        try{AudioSystem.getClip().close();}
        catch (Exception e){e.printStackTrace();}
    }

    public static void play(File stream) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream);
        Clip clip = AudioSystem.getClip();
        clip.close();

        clip = AudioSystem.getClip();
        clip.open(audioStream);

        clip.setFramePosition(0);

        clip.start();

        Thread.sleep(clip.getMicrosecondLength() / 1000);

        clip.close();
        audioStream.close();
    }
}