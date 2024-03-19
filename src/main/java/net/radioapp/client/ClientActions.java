package net.radioapp.client;

import net.radioapp.web.UDP.PackageTypes;
import net.radioapp.web.UDP.UDPPacket;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClientActions {
    static boolean recording;
    static FileOutputStream fileStream;
    static File res;
    public static void filterAction(byte[] c) throws IOException {
        PackageTypes type = PackageTypes.obtenerTipoPorCodigo(c[0]);

        System.arraycopy(c, 1, c, 0, c.length-1);
        String command = new String(c, StandardCharsets.UTF_8);

        if(type == PackageTypes.INICIOEMISION){
            res = new File("test.mp3");
            fileStream = new FileOutputStream(res);
            recording = true;
            System.out.println("Recibiendo canción");
        }
        else if (type == PackageTypes.FINEMISION){
            recording = false;
            System.out.println("finalizado, peso total " + res.length());
        }
        else if (type == PackageTypes.EMISION && recording){
            fileStream.write(c);
        }
        else if (type == PackageTypes.LOG){
            System.out.println(command);
        }
        else if (type == PackageTypes.MOVER){
            System.out.println("Cambiando frecuencia a " + command);
        }
        else{
            System.out.println("Algo ha fallado");
            System.out.println(type);
        }

    }
}
