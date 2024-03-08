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
        String res = Input.nextLine();
        //return test();
        return  new Action("Salimos ok", ActionType.QUIT);
    }

    @Override
    public void log(String l) {
        System.out.print(""+commandPrefix+l);
    }
    @Override
    public void exit(String l){
        System.out.print(commandPrefix + l);
        System.exit(0);
    }

    public Action test(){
        return controlador.call("",new String[1]);
    }
}
