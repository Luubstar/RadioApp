package net.radioapp;

import java.io.IOException;

public interface WebHandler {
    void initialize() throws IOException;
    void start();
    void stop();
    void restart();
    void send();
}
