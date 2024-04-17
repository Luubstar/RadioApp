package net.radioapp.web.Network;

import net.radioapp.web.Client;

public class ClientUDPEmite extends UDPEmitter {
    public  ClientUDPEmite(Client servidor, UDPDataArray p, PackageTypes t){
        super(new UDPPacket(servidor, p.getData(), t));
        port = UDPPacket.CLIENTEMITTER;
        destino = UDPPacket.SERVERRECIBER;
    }

}
