package net.radioapp.web.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UPDService {
    private static final int PORT = 12345;

    public static void main(String[] args) {
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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
