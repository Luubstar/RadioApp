package net.radioapp.client;


import java.io.*;

public class clientMain {

    public static void main(String[] args) throws IOException {
        new ClientUDPRecibe().start();
        new ClientUDPEmite().start();
    }
}
