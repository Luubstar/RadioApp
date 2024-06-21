package net.radioapp.commandController.commands;

import net.radioapp.commandController.Command;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class SkipSongCommand extends Command {
    public SkipSongCommand(){
        name = "skipsong";
        aliases = new String[]{"skip", "sk"};
        resumeMessage = "Comando para saltar una canción de una emisora {arg}";
        helpMessage =  resultMessage +"\n {arg} -> Emisora que debe cambiar la canción";
    }
    @Override
    public Action call(String[] args) {
        if(args.length == 0){return new Action("error", "Tamaño de argumentos inválido", ActionType.ERROR);}
        return new Action(this.getName(),args[0], ActionType.SKIPSONG);}
}
