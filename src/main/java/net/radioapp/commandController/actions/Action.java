package net.radioapp.commandController.actions;

public class Action {
    private final String res, name;
    private final ActionType type;

    public Action(String n, String r, ActionType t){
        name = n;
        res = r;
        type = t;
    }

    public String getRes() {return res;}

    public ActionType getType() {return type;}

    public String getName() {
        return name;
    }
}
