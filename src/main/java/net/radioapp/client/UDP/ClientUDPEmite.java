package net.radioapp.client.UDP;

import net.radioapp.web.UDP.UDPEmitter;
import net.radioapp.web.UDP.UDPPacket;

public class ClientUDPEmite extends UDPEmitter {
    public  ClientUDPEmite(UDPPacket p){
        super(p);
        port = UDPPacket.CLIENTEMITTER;
        destino = UDPPacket.SERVERRECIBER;
    }
}
