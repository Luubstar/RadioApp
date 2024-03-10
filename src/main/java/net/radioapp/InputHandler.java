package net.radioapp;

import net.radioapp.commandController.actions.Action;

public interface InputHandler {
    void initialize();
    void start();
    Action getAction();
    void log(String l);
    void error(String l);
    void exit(String l);
}
