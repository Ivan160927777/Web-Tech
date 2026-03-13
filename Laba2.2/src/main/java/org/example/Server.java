package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class Server {

    enum ServerState {
        READY,
        PAUSE,
        STOPPED
    }

    private static volatile ServerState state = ServerState.READY;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("Server started on port 7777");

            while (state != ServerState.STOPPED) {

                try {
                    Socket socket = serverSocket.accept();

                    if (state == ServerState.STOPPED) {
                        socket.close();
                        break;
                    }

                    new Thread(new ClientHandler(socket)).start();

                } catch (IOException e) {
                    break;
                }
            }

            System.out.println("Server stopped");

        } catch (IOException e) {
            System.out.println("Server unavailable");
        } finally {
            closeServer();
        }
    }

    private static void closeServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {}
    }

    static class ClientHandler implements Runnable {

        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try (DataInputStream inputStream =
                         new DataInputStream(socket.getInputStream());
                 DataOutputStream outputStream =
                         new DataOutputStream(socket.getOutputStream())) {

                String userName = inputStream.readUTF();
                System.out.println("User connected: " + userName);

                while (true) {

                    String request = inputStream.readUTF();

                    System.out.println("Request from " +
                            userName + ": " + request);

                    if (request.equalsIgnoreCase("exit")) {
                        outputStream.writeUTF("Connection closed");
                        break;
                    }

                    long start = System.nanoTime();

                    String response = handleCommand(request);

                    long end = System.nanoTime();
                    long durationMs = (end - start) / 1_000;

                    outputStream.writeUTF(
                            response + " | processed in: " + durationMs + " mks"
                    );

                    outputStream.flush();

                    if (state == ServerState.STOPPED) {
                        break;
                    }
                }

                System.out.println("User disconnected: " + userName);

            } catch (IOException e) {
                System.out.println("Client disconnected");
            }
        }

        private String handleCommand(String command) {

            if (state == ServerState.STOPPED) {
                return "Server is unavailable";
            }

            if (state == ServerState.PAUSE &&
                    !command.equalsIgnoreCase("ready")) {
                return "Server is paused";
            }

            switch (command.toLowerCase()) {

                case "time":
                    return "Server time: " + LocalDateTime.now();

                case "pause":
                    state = ServerState.PAUSE;
                    return "Server switched to PAUSE";

                case "ready":
                    state = ServerState.READY;
                    return "Server switched to READY";

                case "stop":
                    state = ServerState.STOPPED;
                    closeServer();
                    return "Server shutting down";

                default:
                    if (command.startsWith("echo ")) {
                        return command.substring(5);
                    }
                    return "Unknown command";
            }
        }
    }
}

//package org.example;
//
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.time.LocalDateTime;
//
//public class Server {
//
//    enum ServerState {
//        READY,
//        PAUSE,
//        STOPPED
//    }
//
//    private static volatile ServerState state = ServerState.READY;
//
//    public static void main(String[] args) throws IOException {
//
//        try (var serverSocket = new ServerSocket(7777)) {
//            System.out.println("Server started on port 7777");
//
//            while (state != ServerState.STOPPED) {
//                var socket = serverSocket.accept();
//                new Thread(new ClientHandler(socket)).start();
//            }
//        }
//    }
//
//    static class ClientHandler implements Runnable {
//
//        private final Socket socket;
//
//        ClientHandler(Socket socket) {
//            this.socket = socket;
//        }
//
//        @Override
//        public void run() {
//            try (var inputStream = new DataInputStream(socket.getInputStream());
//                 var outputStream = new DataOutputStream(socket.getOutputStream())) {
//                String userName = inputStream.readUTF();
//                System.out.println("User connected: " + userName);
//                String request;
//
//                while (!(request = inputStream.readUTF()).equalsIgnoreCase("exit")) {
//
//                    System.out.println("Request from " + userName + ": " + request);
//
//                    long start = System.nanoTime();
//
//                    String response = handleCommand(request);
//
//                    long end = System.nanoTime();
//                    long durationMs = (end - start) / 1_000_000;
//
//                    outputStream.writeUTF(response + " | processed in: " + durationMs + " ms");
//                }
//
//                System.out.println("User disconnected: " + userName);
//                outputStream.writeUTF("Connection closed");
//            } catch (IOException e) {
//                System.out.println("Client disconnected");
//            }
//        }
//
//        private String handleCommand(String command) {
//
//            if (state == ServerState.PAUSE && !command.equals("ready")) {
//                return "Server is paused";
//            }
//
//            switch (command.toLowerCase()) {
//
//                case "time":
//                    return "Server time: " + LocalDateTime.now();
//
//                case "pause":
//                    state = ServerState.PAUSE;
//                    return "Server switched to PAUSE";
//
//                case "ready":
//                    state = ServerState.READY;
//                    return "Server switched to READY";
//
//                case "stop":
//                    state = ServerState.STOPPED;
//                    return "Server stopped";
//
//                default:
//                    if (command.startsWith("echo ")) {
//                        return command.substring(5);
//                    }
//                    return "Unknown command";
//            }
//        }
//    }
//}
//
////package org.example;
////
////import java.io.DataInputStream;
////import java.io.DataOutputStream;
////import java.io.IOException;
////import java.net.ServerSocket;
////import java.util.Scanner;
////
////public class Server {
////    public static void main(String[] args) throws IOException {
////try (var serverSocket = new ServerSocket(7777);
////     var socket = serverSocket.accept();
////     var outputStream = new DataOutputStream(socket.getOutputStream());
////     var inputStream = new DataInputStream(socket.getInputStream());
////     var scanner= new Scanner(System.in)){
////    var request=inputStream.readUTF();
////    while(!"stop".equals(request)){
////        System.out.println("Client request: "+request);
////        var response = scanner.nextLine();
////        outputStream.writeUTF(response);
////        request=inputStream.readUTF();
////    }
////     }
////
////    }
////}