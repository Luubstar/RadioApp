package net.radioapp.commandController;

public abstract class Command {
    protected String name, description, helpmessage, resultMessage;
    public Action call(String[] args){return new Action("Acción no implementada", ActionType.LOG);}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getHelpmessage() {return helpmessage;}
    public String getResultMessage() {return resultMessage;}
}
