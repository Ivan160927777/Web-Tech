package org.example;

import java.io.*;
import java.net.*;

public class Main {

    public static void main(String[] args) {
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {


            String host = "github.com";//google.com


            int port = 443;

            ;
            String message = "Hello world!";

            connectToServer(host, port, message);

        } catch (Exception e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        }
    }

    private static void connectToServer(String host, int port, String message) {

        try (Socket socket = new Socket()) {

            // Таймаут подключения 5 секунд
            socket.connect(new InetSocketAddress(host, port), 5000);
            socket.setSoTimeout(5000); // Таймаут ожидания ответа

            System.out.println("Соединение установлено с " + host + ":" + port);

            // Отправка данных
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);

            // Чтение ответа как текста
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String line;
            System.out.println("Ответ сервера:");
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

        } catch (UnknownHostException e) {
            System.out.println("Ошибка: сервер не существует.");
        } catch (ConnectException e) {
            System.out.println("Ошибка: порт закрыт или протокол не поддерживается.");
        } catch (SocketTimeoutException e) {
            System.out.println("Ошибка: превышено время ожидания (timeout).");
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}

//package org.example;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.Inet4Address;
//import java.net.Socket;
//
////TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
//// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//public class Main {
//    public static void main(String[] args) throws IOException {
//        var inetAdress= Inet4Address.getByName("");
//        try(var socket = new Socket(inetAdress,80);
//            var  outputStream = new DataOutputStream(socket.getOutputStream());
//            var inputStream = new DataInputStream(socket.getInputStream())){
//            outputStream.writeUTF("Hello world!");
//            var response = inputStream.readAllBytes();
//            System.out.println(response.length);
//        }
//    }
//}

