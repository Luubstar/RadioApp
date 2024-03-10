package net.radioapp.commandController;

import net.radioapp.InputHandler;

import java.util.Scanner;

public class TerminalHandler implements InputHandler {
    CommandController controlador;
    Scanner Input;
    private static final String commandPrefix = "> ";
    @Override
    public void initialize() {
        controlador = new CommandController();
        Input = new Scanner(System.in);
        controlador.initialize();
    }
    @Override
    public void start() {}

    @Override
    public Action getAction() {
        System.out.print("\n"+commandPrefix);
        String res = Input.nextLine().toLowerCase();
        return controlador.call(res, new String[1]);
    }

    @Override
    public void log(String l) {
        System.out.print(commandPrefix+l);
    }
    @Override
    public void exit(String l){
        System.out.print(commandPrefix + l);
        System.exit(0);
    }
}
