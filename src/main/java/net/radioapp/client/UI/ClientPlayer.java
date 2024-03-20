package net.radioapp.client.UI;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class ClientPlayer {
    public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
        File audioFile = new File("test.wav");

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

        // Create a clip
        Clip clip = AudioSystem.getClip();
        clip.close();

        clip = AudioSystem.getClip();
        // Open the clip with the audio file
        clip.open(audioStream);

        // Start playing the audio
        clip.setFramePosition(0);

        System.out.println(clip.available());
        System.out.println(clip.getMicrosecondLength());
        clip.start();

        // Wait for the clip to finish playing
        Thread.sleep(clip.getMicrosecondLength() / 1000);

        // Close the clip and audio stream
        clip.close();
        audioStream.close();
    }
}