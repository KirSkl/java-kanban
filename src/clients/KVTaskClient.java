package clients;

import com.google.gson.Gson;
import manager.Managers;
import servers.HttpTaskServer;
import servers.KVServer;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import static tasks.StatusOfTask.NEW;
import static tasks.TypeOfTask.TASK;

public class KVTaskClient {
    private String token;
    private HttpClient httpClient;
    private String url;

    public KVTaskClient(String url) throws InterruptedException, IOException {
        this.url = url;
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        token = (httpClient.send(request, handler)).body();
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "/?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Сохранение завершно с ошибкой: " + e.getMessage());
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "/?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        String response = null;
        try {
            response = (httpClient.send(request, handler)).body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении значения: " + e.getMessage());
        }
        return response;
    }
}
