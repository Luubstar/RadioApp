package net.radioapp.commandController;

import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public abstract class Command {
    protected String name, description, helpMessage, resumeMessage, resultMessage;
    protected String[] aliases = new String[0];
    public Action call(String[] args){return new Action("Acción no implementada", ActionType.LOG);}
    public String getName() {return name;}
    public String[] getAliases() {return aliases;}
    public String[] getAliasesAndName(){
        String[] res = new String[getAliases().length + 1];
        res[0] = getName();
        if (getAliases().length > 0){
            System.arraycopy(aliases, 0, res, 1, aliases.length);
        }
        return res;
    }
    public String getDescription() {return description;}
    public String getHelpMessage() {return helpMessage;}
    public String getResultMessage() {return resultMessage;}
    public String getResumeMessage() {
        return resumeMessage;
    }
}
