package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.Scanner;

public class DatagramClient {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter data size: ");
        int size = scanner.nextInt();

        System.out.print("Choose data type (1 - String, 2 - Bytes): ");
        int type = scanner.nextInt();

        InetAddress address = InetAddress.getByName("localhost");

        byte[] data;

        if (type == 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb.append("A");
            }
            data = sb.toString().getBytes();
        } else {
            data = new byte[size];
            new Random().nextBytes(data);
        }

        try (DatagramSocket socket = new DatagramSocket()) {

            // Отправка запроса серверу
            DatagramPacket packet = new DatagramPacket(
                    data,
                    data.length,
                    address,
                    7777
            );

            socket.send(packet);

            // Получение ответа
            byte[] buffer = new byte[1024];
            DatagramPacket responsePacket =
                    new DatagramPacket(buffer, buffer.length);

            socket.receive(responsePacket);

            String response = new String(
                    responsePacket.getData(),
                    0,
                    responsePacket.getLength()
            );

            System.out.println("Server response: " + response);
        }
    }
}

//package org.example;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.Inet4Address;
//
//public class DatagramClient
//{
//    public static void main(String[] args) throws IOException {
//        var inetAdress= Inet4Address.getByName("localhost");
//        try(var datagtramSocket = new DatagramSocket()){
//          var bytes = "Hello from UDP client".getBytes();
//            DatagramPacket packet = new DatagramPacket(bytes,bytes.length,inetAdress,7777);
//            datagtramSocket.send(packet);
//        }
//    }
//}
