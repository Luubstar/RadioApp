package net.radioapp.commandController;

public class Action {
    private final String res;
    private final ActionType type;

    public Action(String r, ActionType t){
        res = r;
        type = t;
    }

    public String getRes() {return res;}

    public ActionType getType() {return type;}
}
