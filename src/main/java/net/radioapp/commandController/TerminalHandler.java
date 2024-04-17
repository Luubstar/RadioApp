package net.radioapp.commandController;

import net.radioapp.InputHandler;
import net.radioapp.Main;
import net.radioapp.commandController.actions.Action;
import net.radioapp.web.inputServer.Connectivity;

import java.util.Scanner;

public class TerminalHandler implements InputHandler {
    CommandController controlador;
    Scanner Input;
    private final Colors exitColor = new Colors(Colors.TYPE_TEXT, 99, 99, 99);
    private final Colors errorColor = Colors.Red;
    private static final String commandPrefix = ">";
    @Override
    public void initialize() {
        controlador = new CommandController();
        Input = new Scanner(System.in);
        controlador.initialize();
    }
    @Override
    public void start() {
        //TODO: Reactivar cuando sea necesario
        /*System.out.print("¿Qué modo de conectividad desea? [P]reguntar, [S]iempre, [N]unca\n"+commandPrefix);
        String res = Input.nextLine().toUpperCase();
        while (!res.equals("P") && !res.equals("S") && !res.equals("N")){
            System.out.print("¿Qué modo de conectividad desea? [P]reguntar, [S]iempre, [N]unca\n"+commandPrefix);
            res = Input.nextLine().toUpperCase();
        }

        if(res.equals("P")){ Main.setConnectivityMode(Connectivity.ASK);}
        else if(res.equals("N")){ Main.setConnectivityMode(Connectivity.NEVER);}
        else{ Main.setConnectivityMode(Connectivity.ALWAYS);}*/
        startTyping();
    }

    public void startTyping(){
        System.out.println(commandPrefix);
    }

    @Override
    public Action getAction() {
        System.out.print("\n"+commandPrefix);
        String res = Input.nextLine().toLowerCase();

        String[] temp = res.split(" ");
        res = temp[0];
        String[] args = new String[temp.length-1];
        System.arraycopy(temp, 1, args, 0, temp.length - 1);

        return controlador.call(res, args);
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
