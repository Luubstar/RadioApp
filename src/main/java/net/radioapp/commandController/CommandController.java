package net.radioapp.commandController;

import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.commandController.commands.*;

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
        register(new HelpCommand());
        register(new LockOnOff());
        register(new LockFrecuency());
        register(new LockVolume());
    }

    private void register(Command c){comandos.add(c);}

    public Action call(String s, String[] args){
        Command r = StringUtils.findClosestCommand(comandos, s);
        if (r != null){return r.call(args);}
        else{return  new Action("No se encontró el comando " + s, ActionType.ERROR);}
    }

}
