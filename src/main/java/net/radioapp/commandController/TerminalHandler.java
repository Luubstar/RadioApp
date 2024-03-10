package net.radioapp.commandController;

import net.radioapp.InputHandler;

import java.util.Scanner;

public class TerminalHandler implements InputHandler {
    CommandController controlador;
    Scanner Input;
    private final Colors exitColor = new Colors(Colors.TYPE_TEXT, 99, 99, 99);
    private final Colors errorColor = Colors.Red;
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
    public void error(String l) {
        System.out.print(errorColor.colorize(commandPrefix+l));
    }

    @Override
    public void exit(String l){
        System.out.print(exitColor.colorize(commandPrefix + l));
        System.exit(0);
    }
}
