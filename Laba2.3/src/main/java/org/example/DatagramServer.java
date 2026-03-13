package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DatagramServer {

    public static void main(String[] args) throws Exception {

        try (DatagramSocket serverSocket = new DatagramSocket(7777)) {

            byte[] buffer = new byte[1024];

            System.out.println("Server started...");

            while (true) {

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // ⬇ Ждём запрос (это НЕ считается обработкой)
                serverSocket.receive(packet);

                // ✅ Начинаем измерение после получения запроса
                long startTime = System.nanoTime();

                String requestData = new String(
                        packet.getData(),
                        0,
                        packet.getLength()
                );

                System.out.println("Request: " + requestData);

                // --- Здесь может быть любая обработка ---
                Thread.sleep(10); // пример работы сервера

                long endTime = System.nanoTime();

                long processingTimeMs = (endTime - startTime) / 1_000_000;

                String response =
                        "Request received. Processing time = "
                                + processingTimeMs + " ms";

                byte[] responseBytes = response.getBytes();

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                DatagramPacket responsePacket =
                        new DatagramPacket(
                                responseBytes,
                                responseBytes.length,
                                clientAddress,
                                clientPort
                        );

                serverSocket.send(responsePacket);

                //Время
                System.out.println("Processing time: " + processingTimeMs + " ms");
            }
        }
    }
}
//package org.example;
//
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//
//public class DatagramServer{
//    public static void main(String[] args) throws IOException {
//try(var datagramServer = new DatagramSocket(7777)){
//    byte[] buffer = new byte[512];
//    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//    datagramServer.receive(packet);
//    System.out.println(new String(buffer));
//}
//    }
//}