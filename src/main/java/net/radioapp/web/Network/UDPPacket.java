package net.radioapp.web.Network;

import net.radioapp.web.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

class UDPPacket {
    public static final int SERVEREMITTER = 7778;
    public static final int SERVERRECIBER = 7777;
    public static final int CLIENTEMITTER = 7776;
    public static final int CLIENTRECIBER = 7779;

    Client cliente;
    UDPDataArray[] arrays;
    PackageTypes type;

    public UDPPacket(Client c, byte[] b, PackageTypes type){
        cliente = c;
        this.type = type;
        arrays  = slice(b);
    }
    public UDPPacket(Client c, PackageTypes type){
        cliente = c;
        this.type = type;
        arrays  = slice("  ".getBytes());
    }

    public UDPDataArray[] slice(byte[] b){
        int chunks = (int) Math.ceil((double) b.length / (UDPDataArray.CHUNKSIZE - UDPDataArray.METADATASIZE));
        UDPDataArray[] result = new UDPDataArray[chunks];

        int start = 0;
        for (int i = 0; i < chunks; i++) {
            int length = Math.min(UDPDataArray.CHUNKSIZE, b.length - start + UDPDataArray.METADATASIZE);
            UDPDataArray pack = new UDPDataArray(length);
            pack.addData(type.getBytevalue());
            pack.addData(UDPDataArray.intToByte(i));
            pack.addData(b, start, start+length-1-UDPDataArray.METADATASIZE);
            result[i] = pack;
            start += length-UDPDataArray.METADATASIZE;
        }
        return result;
    }

    public void send(DatagramSocket s, int port) throws IOException {
        for(UDPDataArray b : arrays){
            s.send(new DatagramPacket(b.getData(), b.getData().length, cliente.getAddress(), port));
        }
    }
}
