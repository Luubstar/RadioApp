package net.radioapp.client;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.UDP.PackageTypes;
import net.radioapp.web.UDP.UDPEmitter;
import net.radioapp.web.UDP.UDPPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class ClientUDPEmite extends UDPEmitter {
    public  ClientUDPEmite(UDPPacket p){
        super(p);
        port = UDPPacket.CLIENTEMITTER;
        destino = UDPPacket.SERVERRECIBER;
    }
}
