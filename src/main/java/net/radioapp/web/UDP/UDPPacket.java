package net.radioapp.web.UDP;

import net.radioapp.web.netbasic.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPPacket {
    public static final int SERVEREMITTER = 7778;
    public static final int SERVERRECIBER = 7777;
    public static final int CLIENTEMITTER = 7776;
    public static final int CLIENTRECIBER = 7779;
    public static final int CHUNKSIZE = 2048;

    Client cliente;
    byte[][] buffer;

    public UDPPacket(Client c, byte[] b){
        cliente = c;
        buffer  = slice(b);
    }

    public byte[][] slice(byte[] b){
        int chunks = (int) Math.ceil((double) b.length / CHUNKSIZE);
        byte[][] result = new byte[chunks][];
        int start = 0;
        for (int i = 0; i < chunks; i++) {
            int length = Math.min(CHUNKSIZE, b.length - start);
            byte[] chunk = new byte[length];
            System.arraycopy(b, start, chunk, 0, length);
            result[i] = chunk;
            start += length;
        }
        return result;
    }

    public void send(DatagramSocket s, int port) throws IOException {
        for(byte[] b : buffer){
            s.send(new DatagramPacket(b, b.length, cliente.getAddress(), port));
        }
    }
}
