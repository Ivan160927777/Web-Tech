package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SimpleHttpClient {

    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest htmlRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9000/html"))
                .GET()
                .build();

        HttpRequest jsonRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9000/json"))
                .GET()
                .build();

        HttpRequest xmlRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9000/xml"))
                .GET()
                .build();

        var response1 = client.sendAsync(htmlRequest,
                HttpResponse.BodyHandlers.ofString());

        var response2 = client.sendAsync(jsonRequest,
                HttpResponse.BodyHandlers.ofString());

        var response3 = client.sendAsync(xmlRequest,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response1.get().body());
        System.out.println(response2.get().body());
        System.out.println(response3.get().body());
    }
}
