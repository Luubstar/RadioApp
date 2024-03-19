package net.radioapp.client;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClientActions {
    static boolean recording;
    static FileOutputStream fileStream;
    static File res;
    public static void filterAction(byte[] c) throws IOException {
        String command = new String(c, StandardCharsets.UTF_8);
        if(command.startsWith("start")){
            res = new File("test.mp3");
            fileStream = new FileOutputStream(res);
            recording = true;
            System.out.println("Recibiendo canción");
        }
        else if (command.startsWith("stop")){
            recording = false;
            System.out.println("finalizado, peso total " + res.length());
        }
        else if (recording){
            fileStream.write(c);
        }
        else{
            System.out.println(command);
        }

    }
}
