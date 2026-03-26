package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalHTTPServer {

    private final ExecutorService pool;
    private final int port;
    private boolean stopped = false;

    public LocalHTTPServer(int port, int poolSize) {
        this.port = port;
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    public void run() {

        try (ServerSocket server = new ServerSocket(port)) {

            System.out.println("Server started on port " + port);

            while (!stopped) {

                Socket socket = server.accept();
                System.out.println("Client connected");

                pool.submit(() -> processSocket(socket));
            }

        } catch (IOException e) {
            logError(e.getMessage());
        }
    }

    private void processSocket(Socket socket) {

        try (
                socket;
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream()
        ) {

            HttpRequestData request = parseRequest(reader);

            if (request == null) {
                sendResponse(out,
                        HttpStatus.BAD_REQUEST,
                        "text/plain",
                        "Bad request".getBytes());
                return;
            }

            System.out.println("Request: " + request.method + " " + request.path);

            handleRequest(request, out);

        } catch (Exception e) {

            logError(e.getMessage());

        }
    }

    private HttpRequestData parseRequest(BufferedReader reader) throws IOException {

        HttpRequestData request = new HttpRequestData();

        String startLine = reader.readLine();

        if (startLine == null || startLine.isEmpty())
            return null;

        String[] parts = startLine.split(" ");

        if (parts.length != 3)
            return null;

        request.method = parts[0];
        request.path = parts[1];
        request.version = parts[2];

        String line;

        while (!(line = reader.readLine()).isEmpty()) {

            String[] header = line.split(": ", 2);

            if (header.length == 2) {
                request.headers.put(header[0], header[1]);
            }
        }

        return request;
    }

    private void handleRequest(HttpRequestData request,
                               OutputStream out) throws IOException {

        if (!request.version.equals("HTTP/1.0") &&
                !request.version.equals("HTTP/1.1")) {

            sendResponse(out,
                    HttpStatus.BAD_REQUEST,
                    "text/plain",
                    "Unsupported HTTP version".getBytes());
            return;
        }

        switch (request.path) {

            case "/html" -> sendFile(out,
                    Path.of("src/main/resources/example.html"),
                    "text/html");

            case "/json" -> sendFile(out,
                    Path.of("src/main/resources/example.json"),
                    "application/json");

            case "/xml" -> sendFile(out,
                    Path.of("src/main/resources/example.xml"),
                    "application/xml");
            case "/error" -> {
                throw new RuntimeException("Artificial server error");
            }
            default -> sendResponse(out,
                    HttpStatus.NOT_FOUND,
                    "text/plain",
                    "404 Not Found".getBytes());
        }
    }

    private void sendFile(OutputStream out,
                          Path path,
                          String contentType) throws IOException {
        System.out.println("Looking for file: " + path.toAbsolutePath());
        if (!Files.exists(path)) {

            sendResponse(out,
                    HttpStatus.NOT_FOUND,
                    "text/plain",
                    "File not found".getBytes());

            return;
        }

        byte[] body = Files.readAllBytes(path);

        sendResponse(out,
                HttpStatus.OK,
                contentType,
                body);
    }

    private void sendResponse(OutputStream out,
                              HttpStatus status,
                              String contentType,
                              byte[] body) throws IOException {

        String headers =
                "HTTP/1.1 " + status.code + " " + status.message + "\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + body.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n";

        out.write(headers.getBytes());
        out.write(body);
    }

    private void logError(String message) {

        try {

            Files.writeString(
                    Path.of("src/main/server.log"),
                    message + "\n",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException ignored) {
        }
    }

    public static void main(String[] args) {

        LocalHTTPServer server = new LocalHTTPServer(9000, 4);
        server.run();
    }
}
