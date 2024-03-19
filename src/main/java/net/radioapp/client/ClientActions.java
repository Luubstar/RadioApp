package net.radioapp.client;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClientActions {
    boolean recording;
    FileOutputStream fileStream;
    File res;
    public void filterAction(byte[] c) throws IOException {
        String command = new String(c, StandardCharsets.UTF_8);
        if(command.equals("start")){
            res = new File("test.mp3");
            fileStream = new FileOutputStream(res);
            recording = true;
            System.out.println("Recibiendo canción");
        }
        else if (command.startsWith("GET/") && recording){
            byte[] bytes = new byte[2048 - "GET/".getBytes().length];
            System.arraycopy(c, "GET/".getBytes().length, bytes, 0, 2048 - "GET/".getBytes().length);
            fileStream.write(bytes);
        }
        else if (command.equals("stop")){
            recording = false;
            System.out.println("finalizado, peso total " + res.length());
        }

    }
}
