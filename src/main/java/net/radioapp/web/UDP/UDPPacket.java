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
    public static final int CHUNKSIZE = 7;
    public static final int METADATASIZE = 5;
    Client cliente;
    UDPDataArray[] buffer;
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

    public UDPDataArray[] slice(byte[] b){
        int chunks = (int) Math.ceil((double) b.length / (CHUNKSIZE-METADATASIZE));
        UDPDataArray[] result = new UDPDataArray[chunks];
        int start = 0;
        for (int i = 0; i < chunks; i++) {
            int length = Math.min(CHUNKSIZE, b.length - start + METADATASIZE);
            UDPDataArray message = new UDPDataArray(length);
            message.addData(new byte[]{type.getBytevalue()});
            message.addData(new byte[]{0,0,0,0});
            message.addData(b,start,start + (length-1-METADATASIZE));
            result[i] = message;
            start += length-METADATASIZE;
        }
        return result;
    }

    public void send(DatagramSocket s, int port) throws IOException {
        for(UDPDataArray b : buffer){
            byte[] data = b.getData(0, b.getEndIndex());
            s.send(new DatagramPacket(data, data.length, cliente.getAddress(), port));
        }
    }
}
