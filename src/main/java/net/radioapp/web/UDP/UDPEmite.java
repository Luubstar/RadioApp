package net.radioapp.web.UDP;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;

public class UDPEmite extends  UDPEmitterBase implements  Runnable{
    private final String message;
    private final Client cliente;

    public UDPEmite(String m, Client cliente){
        super();
        this.message = m;
        this.cliente = cliente;
    }

    @Override
    public void start() {
        new Thread()
    }

    public void run() {
        try {
            super.send(message.getBytes(), cliente);
        } catch (Exception e) {
            ActionHandler.filterAction(new Action("", "Error emisor", ActionType.QUIT));
        }
    }

}
