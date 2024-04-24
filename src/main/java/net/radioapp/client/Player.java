package net.radioapp.client;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

public interface Player {
    void addToQueue(byte[] stream, int pos) throws IOException;
    void collapseQueue() throws IOException;
    void play();
}
