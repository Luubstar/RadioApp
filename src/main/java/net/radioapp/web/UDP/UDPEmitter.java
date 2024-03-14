package net.radioapp.web.UDP;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;

public class UDPEmitter extends Thread{
    private static final int PORT = 12345;
    private File fichero;

    public void run() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            System.out.println("Servidor UDP iniciado en el puerto " + PORT);

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Espera a que llegue un paquete de datos de un cliente
                serverSocket.receive(receivePacket);

                // Obtiene la dirección IP y el puerto del cliente
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                System.out.println("Nuevo cliente UDP conectado desde " + clientAddress + ":" + clientPort);

                String res = Files.readString(fichero.toPath());

                DatagramPacket pq =  new DatagramPacket(res.getBytes(),
                        res.getBytes().length, InetAddress.getByName("localhost"),12346);

                serverSocket.send(pq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFichero(File fichero) {
        this.fichero = fichero;
    }
}
