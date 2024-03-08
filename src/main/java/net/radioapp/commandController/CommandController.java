package net.radioapp.commandController;

import net.radioapp.commandController.commands.QuitCommand;
import net.radioapp.commandController.commands.TestCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandController {
    private List<Command> comandos;

    public void initialize(){
        comandos = new ArrayList<>();
        start();
    }

    private void start(){
        register(new TestCommand());
        register(new QuitCommand());
    }

    private void register(Command c){comandos.add(c);}

    public Action call(String s, String[] args){
        //TODO: Cambiar esto a un encontrador de comandos
        return comandos.getFirst().call(args);
    }

}
