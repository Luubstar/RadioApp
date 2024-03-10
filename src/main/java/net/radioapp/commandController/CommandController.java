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
        Command r = StringUtils.findClosestCommand(comandos, s);
        if (r != null){return r.call(args);}
        else{return  new Action("No se encontró el comando " + s, ActionType.ERROR);}
    }

}
