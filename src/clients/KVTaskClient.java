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

    public KVTaskClient(String url) {
        this.url = url;
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            token = (httpClient.send(request, handler)).body();
        } catch (InterruptedException | IOException e) {
            System.out.println("Ошибка при регистрации: " + e.getMessage());
        }
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
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        KVTaskClient client = new KVTaskClient("http://localhost:8078");
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        Task task = httpTaskServer.getManager().createTask(
                new tasks.Task("Первая задача", "ОБЫЧНАЯ", NEW, TASK, Instant.EPOCH,
                        Duration.ofMinutes(1)));
        Gson gson = Managers.getGson();
        String json = gson.toJson(task);
        client.put("1", json);

        String json1 = client.load("1");
        Task task1 = gson.fromJson(json1, Task.class);
        System.out.println(task.equals(task1));

        Task task2 = httpTaskServer.getManager().createTask(
                new tasks.Task("Вторая задача", "ОБЫЧНАЯ", NEW, TASK, Instant.EPOCH,
                        Duration.ofMinutes(1)));
        String json2 = gson.toJson(task2);
        client.put("1", json2);

        String json3 = client.load("1");
        Task task3 = gson.fromJson(json3, Task.class);
        System.out.println(task2.equals(task3));
    }
}
