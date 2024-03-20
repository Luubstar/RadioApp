package net.radioapp.web.UDP;

import net.radioapp.web.netbasic.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UDPPacket {
    public static final int SERVEREMITTER = 7778;
    public static final int SERVERRECIBER = 7777;
    public static final int CLIENTEMITTER = 7776;
    public static final int CLIENTRECIBER = 7779;
    public static final int CHUNKSIZE = 1024;

    Client cliente;
    byte[][] buffer;
    PackageTypes type;

    public UDPPacket(Client c, byte[] b, PackageTypes type){
        cliente = c;
        this.type = type;
        buffer  = slice(b);
    }
    public UDPPacket(Client c, PackageTypes type){
        cliente = c;
        this.type = type;
        buffer  = slice("  ".getBytes());
    }

    public byte[][] slice(byte[] b){
        int chunks = (int) Math.ceil((double) b.length / (CHUNKSIZE-1));
        byte[][] result = new byte[chunks][];
        int start = 0;
        for (int i = 0; i < chunks; i++) {
            int length = Math.min(CHUNKSIZE, b.length - start);
            byte[] chunk = new byte[length + 1];
            chunk[0] = type.getBytevalue();
            System.arraycopy(b, start, chunk, 1, length);
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

    public byte[][] getBuffer() {
        return buffer;
    }
}
