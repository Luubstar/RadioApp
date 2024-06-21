package net.radioapp;

import net.radioapp.commandController.actions.Action;
import net.radioapp.web.Network.PackageTypes;
import net.radioapp.web.Client;
import net.radioapp.web.Network.UDPDataArray;

import java.io.IOException;

public interface WebHandler {
    void initialize() throws IOException;
    void start();
    void stop();
    void restart();
    void send(UDPDataArray arg, PackageTypes t);
    void send(UDPDataArray arg, PackageTypes t, Client c);
    void filterAction(Action action);
    void getState();
    void getStations();
    void getGroups();
    void selectGroup(int index);
    void skipSong(int index);
}
