package net.radioapp.commandController;

import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.commandController.commands.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CommandController {
    private List<Command> comandos;

    public void initialize(){
        comandos = new ArrayList<>();
        start();
    }

    private void start(){
        register(new HelpCommand());
        register(new QuitCommand());
        register(new RestartCommand());
        register(new StartCommand());
        register(new StopCommand());
        register(new StateCommand());
        register(new SayCommand());
        register(new LockFrecuency());
        register(new LockVolume());
        register(new LockOnOff());
        register(new SetFrecuencyCommand());
        register(new StationsCommand());
        register(new SkipSongCommand());
        register(new ChangeGroupCommand());
        register(new GroupsCommand());
    }

    private void register(Command c){comandos.add(c);}

    public Action call(String s, String[] args){
        Command r = findClosestCommand(s);
        if (r != null){return r.call(args);}
        else{return  new Action("error", "No se encontró el comando '" + s + "', usa 'help' para ver la ayuda", ActionType.ERROR);}
    }

    public String getHelpCommands(){
        StringBuilder res = new StringBuilder().append("\n");
        for(Command c: comandos){
            StringBuilder name = new StringBuilder().append(c.getAliasesAndName()[0]);
            for (int i = 1; i<c.getAliasesAndName().length;i++){
                name.append(", ").append(c.getAliasesAndName()[i]);
            }
            res.append(name).append(" - ").append(c.getResumeMessage()).append("\n");
        }
        return res.toString();
    }

    public String getHelpCommands(String s){
        StringBuilder res = new StringBuilder().append("\n");
        Command c = findClosestCommand(s);
        res.append(c.getName()).append(" - ").append(c.getHelpMessage()).append("\n");
        return res.toString();
    }

    public Command findClosestCommand(String s){
        return StringUtils.findClosestCommand(comandos,s);
    }

}
