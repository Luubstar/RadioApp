package net.radioapp;

import net.radioapp.commandController.actions.Action;

import java.io.IOException;

public interface WebHandler {
    void initialize() throws IOException;
    void start();
    void stop();
    void restart();
    void send();
    void filterAction(Action action);
}
