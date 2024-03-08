package net.radioapp;

import net.radioapp.commandController.Action;

public interface InputHandler {
    void initialize();
    void start();
    Action getAction();
    void log(String l);
    void exit(String l);
}
