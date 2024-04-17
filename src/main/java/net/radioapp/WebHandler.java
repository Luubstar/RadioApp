package net.radioapp;

import net.radioapp.commandController.actions.Action;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Client;

import java.io.IOException;

public interface WebHandler {
    void initialize() throws IOException;
    void start();
    void stop();
    void restart();
    void send(PackageTypes t, byte[] arg);
    void send(Client c, PackageTypes t, byte[] arg);
    void filterAction(Action action);
    void getState();
}
