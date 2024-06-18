package net.radioapp.commandController;

import net.radioapp.InputHandler;
import net.radioapp.commandController.actions.Action;

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
    public void start() {
    }
    @Override
    public Action[] getAction() {
        String res = Input.nextLine().toLowerCase();
        String[] commands = StringUtils.splitCommands(res);
        Action[] accionesResultado = new Action[commands.length];
        int i = 0;
        for(String command : commands) {
            command = command.trim();
            String[] temp = command.split(" ");
            res = temp[0];
            String[] args = new String[temp.length - 1];
            System.arraycopy(temp, 1, args, 0, temp.length - 1);
            accionesResultado[i] = controlador.call(res, args);
            i++;
        }
        return accionesResultado;
    }

    @Override
    public void log(String l) {
        System.out.print(l + "\n" + commandPrefix);
    }

    @Override
    public void error(String l) {
        System.out.print(errorColor.colorize(l) + "\n" + commandPrefix);
    }

    @Override
    public void exit(String l){
        System.out.print(exitColor.colorize(commandPrefix + l));
        System.exit(0);
    }

    public CommandController getController() {
        return controlador;
    }

}
