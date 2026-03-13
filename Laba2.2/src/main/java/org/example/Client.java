package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);

        try {
            System.out.print("Enter server address: ");
            String host = console.nextLine();

            System.out.print("Enter server port: ");
            int port = Integer.parseInt(console.nextLine());

            System.out.print("Enter your name: ");
            String userName = console.nextLine();

            try (Socket socket = new Socket(host, port);
                 DataOutputStream outputStream =
                         new DataOutputStream(socket.getOutputStream());
                 DataInputStream inputStream =
                         new DataInputStream(socket.getInputStream())) {

                outputStream.writeUTF(userName);
                outputStream.flush();

                while (true) {

                    System.out.print("Enter command: ");
                    String request = console.nextLine();

                    outputStream.writeUTF(request);
                    outputStream.flush();

                    if (request.equalsIgnoreCase("exit")) {
                        break;
                    }

                    try {
                        String response = inputStream.readUTF();
                        System.out.println("Response: " + response);

                    } catch (Exception e) {
                        System.out.println("Server unavailable");
                        break;
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("Server unavailable");
        }
    }
}


//package org.example;
//
//import java.io.*;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class Client {
//
//    public static void main(String[] args) throws IOException {
//
//        Scanner console = new Scanner(System.in);
//
//        System.out.print("Enter server address: ");
//        String host = console.nextLine();
//
//        System.out.print("Enter server port: ");
//        int port = Integer.parseInt(console.nextLine());
//
//        System.out.print("Enter your name: ");
//        String userName = console.nextLine();
//
//        try (var socket = new Socket(host, port);
//             var outputStream = new DataOutputStream(socket.getOutputStream());
//             var inputStream = new DataInputStream(socket.getInputStream())) {
//
//            outputStream.writeUTF(userName); // отправляем имя
//
//            while (true) {
//
//                System.out.print("Enter command: ");
//                String request = console.nextLine();
//
//                outputStream.writeUTF(request);
//
//                try {
//                    String response = inputStream.readUTF();
//                    System.out.println("Response: " + response);
//
//                    if (request.equalsIgnoreCase("exit")) {
//                        break;
//                    }
//
//                } catch (Exception e) {
//                    System.out.println("Server closed connection");
//                    break;
//                }
//            }
//        }
//    }
//}
//

//package org.example;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Inet4Address;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class CLient {
//    public static void main(String[] args) throws IOException {
//        var inetAdress = Inet4Address.getByName("localhost");
//        try(var socket = new Socket(inetAdress, 7777);
//            var outputStream = new DataOutputStream(socket.getOutputStream());
//            var inputStream = new DataInputStream(socket.getInputStream());
//            var scanner = new Scanner(System.in)) {
//            while (scanner.hasNextLine()) {
//                var request = scanner.nextLine();
//                outputStream.writeUTF(request);
//                System.out.println("Response: "+inputStream.readUTF());
//            }
//        }
//    }
//}
