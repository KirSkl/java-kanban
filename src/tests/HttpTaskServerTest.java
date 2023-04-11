package tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import manager.Managers;
import manager.TaskManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import servers.HttpTaskServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import static java.util.Collections.EMPTY_LIST;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static tasks.StatusOfTask.NEW;
import static tasks.TypeOfTask.*;

public class HttpTaskServerTest {
    private HttpClient client;
    private TaskManager manager;
    private HttpTaskServer server;
    private Gson gson;

    final Task task = new tasks.Task("Первая задача", "ОБЫЧНАЯ", NEW, TASK, Instant.EPOCH,
            Duration.ofMinutes(1));
    final Task task1 = new tasks.Task("Третья задача", "ОБЫЧНАЯ", NEW, TASK,
            Instant.EPOCH.plusSeconds(100), Duration.ofMinutes(1));
    final Epic epic = new tasks.Epic("Первый эпик", "обычный", NEW, EPIC, Instant.EPOCH, Duration.ZERO );
    final Epic epic1 = new tasks.Epic("Второй эпик", "обычный", NEW, EPIC, Instant.EPOCH, Duration.ZERO );
    Subtask newSubtask(Epic epic) {
        return new Subtask("Первая подзадача", "Необычная", NEW,
                epic.getId(), SUBTASK, Instant.EPOCH, Duration.ofMinutes(1));
    }
    @BeforeEach
    void startServer() throws IOException {
        server = new HttpTaskServer();
        server.start();
        manager = Managers.getInMemoryTaskManager();
        client = HttpClient.newHttpClient();
        gson = Managers.getGson();
    }
    @AfterEach
    void closeServer() {
        server.stop();
    }
    @Test
    void shouldCreateTask() throws IOException, InterruptedException {
        Task newTask = manager.createTask(task);
        String jsonNewTask = gson.toJson(newTask);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewTask);
        URI url = URI.create("http://localhost:8080/tasks/task");
        var request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }
    @Test
    void shouldCreateEpicAndSubtask() throws IOException, InterruptedException {
        Epic newEpic = manager.createEpic(epic);
        String jsonNewEpic = gson.toJson(newEpic);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewEpic);
        URI url = URI.create("http://localhost:8080/tasks/epic");
        var request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Subtask subtask = manager.createSubtask(newSubtask(newEpic));
        String jsonNewSubtask = gson.toJson(subtask);
        body = HttpRequest.BodyPublishers.ofString(jsonNewSubtask);
        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }
    @Test
    void shouldReturnErrorCodes() throws IOException, InterruptedException {
        Task newTask = manager.createTask(task);

        String jsonNewTask = gson.toJson(newTask);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewTask);
        URI url = URI.create("http://localhost:8080/tasks/");
        var request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());

    }
    @Test
    void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        Task newTask1 = manager.createTask(task1);
        Task newTask = manager.createTask(task);
        String jsonNewTask = gson.toJson(newTask1);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewTask);
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        jsonNewTask = gson.toJson(newTask);
        body = HttpRequest.BodyPublishers.ofString(jsonNewTask);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        var type = new TypeToken<List<Task>>(){}.getType();
        List<Task> priorTasks = gson.fromJson(response.body(), type);

        assertEquals(List.of(newTask, newTask1), priorTasks);
    }
    @Test
    void shouldReturnAllTasks() throws IOException, InterruptedException {
        Task newTask1 = manager.createTask(task1);
        Task newTask = manager.createTask(task);
        String jsonNewTask = gson.toJson(newTask1);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewTask);
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        jsonNewTask = gson.toJson(newTask);
        body = HttpRequest.BodyPublishers.ofString(jsonNewTask);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var type = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(response.body(), type);

        assertEquals(List.of(newTask1, newTask), tasks);
    }
    @Test
    void shouldReturnAllEpicsAnsSubtasks() throws IOException, InterruptedException {
        Epic newEpic = manager.createEpic(epic);
        String jsonNewEpic = gson.toJson(newEpic);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewEpic);
        URI url = URI.create("http://localhost:8080/tasks/epic");
        var request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var type = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> epics = gson.fromJson(response.body(), type);

        assertEquals(List.of(newEpic), epics);

        Subtask subtask = manager.createSubtask(newSubtask(newEpic));
        String jsonNewSubtask = gson.toJson(subtask);
        body = HttpRequest.BodyPublishers.ofString(jsonNewSubtask);
        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        type = new TypeToken<List<Subtask>>(){}.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), type);

        assertEquals(List.of(subtask), subtasks);
    }
    @Test
    void shouldReturnTaskAndHistory() throws IOException, InterruptedException {
        Task newTask = manager.createTask(task);
        String jsonNewTask = gson.toJson(newTask);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewTask);
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task/?id=" + newTask.getId());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var type = new TypeToken<Task>(){}.getType();
        Task returnedTask = gson.fromJson(response.body(), type);

        assertEquals(newTask, returnedTask);

        url = URI.create("http://localhost:8080/tasks/history");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        type = new TypeToken<List<Task>>(){}.getType();
        List<Task> history = gson.fromJson(response.body(), type);

        assertEquals(List.of(newTask), history);
    }
    @Test
    void shouldReturnEpicAndSubtask() throws IOException, InterruptedException {
        Epic newEpic = manager.createEpic(epic);
        String jsonNewEpic = gson.toJson(newEpic);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewEpic);
        URI url = URI.create("http://localhost:8080/tasks/epic");
        var request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/epic/?id=" + newEpic.getId());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var type = new TypeToken<Epic>(){}.getType();
        Epic returnedEpic = gson.fromJson(response.body(), type);

        assertEquals(newEpic, returnedEpic);

        Subtask subtask = manager.createSubtask(newSubtask(newEpic));
        String jsonNewSubtask = gson.toJson(subtask);
        body = HttpRequest.BodyPublishers.ofString(jsonNewSubtask);
        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtask.getId());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        type = new TypeToken<Subtask>(){}.getType();
        Subtask returnedSubtask = gson.fromJson(response.body(), type);

        assertEquals(subtask, returnedSubtask);
    }
    @Test
    void shouldDeleteTasks() throws IOException, InterruptedException {
        Task newTask1 = manager.createTask(task1);
        Task newTask = manager.createTask(task);
        String jsonNewTask = gson.toJson(newTask1);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewTask);
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        jsonNewTask = gson.toJson(newTask);
        body = HttpRequest.BodyPublishers.ofString(jsonNewTask);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task/?id=" + newTask1.getId());
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/task");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var type = new TypeToken<List<Task>>(){}.getType();
        List<Task> responseTasks = gson.fromJson(response.body(), type);

        assertEquals(List.of(newTask), responseTasks);

        url = URI.create("http://localhost:8080/tasks/task");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/task");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        type = new TypeToken<List<Task>>(){}.getType();
        List<Task> responseEmptyTasks = gson.fromJson(response.body(), type);

        assertEquals(EMPTY_LIST, responseEmptyTasks);
    }
    @Test
    void shouldDeleteEpics() throws IOException, InterruptedException {
        Epic newEpic = manager.createEpic(epic);
        Epic newEpic1 = manager.createEpic(epic1);
        String jsonNewEpic = gson.toJson(newEpic);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewEpic);
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        jsonNewEpic = gson.toJson(newEpic1);
        body = HttpRequest.BodyPublishers.ofString(jsonNewEpic);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/epic/?id=" + newEpic1.getId());
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/epic");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var type = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> responseEpics = gson.fromJson(response.body(), type);

        assertEquals(List.of(newEpic), responseEpics);

        url = URI.create("http://localhost:8080/tasks/epic");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/epic");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        type = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> responseEmptyEpics = gson.fromJson(response.body(), type);

        assertEquals(EMPTY_LIST, responseEmptyEpics);
    }
    @Test
    void shouldDeleteSubtasks() throws IOException, InterruptedException {
        Epic newEpic = manager.createEpic(epic);
        String jsonNewEpic = gson.toJson(newEpic);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewEpic);
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = newSubtask(newEpic);
        Subtask newSubtask = manager.createSubtask(subtask);
        Subtask subtask1 = newSubtask(newEpic);
        Subtask newSubtask1 = manager.createSubtask(subtask1);
        String jsonNewSubtask = gson.toJson(newSubtask);
        body = HttpRequest.BodyPublishers.ofString(jsonNewSubtask);
        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        jsonNewSubtask = gson.toJson(newSubtask1);
        body = HttpRequest.BodyPublishers.ofString(jsonNewSubtask);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask/?id=" + newSubtask1.getId());
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var type = new TypeToken<List<Subtask>>(){}.getType();
        List<Subtask> responseSubtask = gson.fromJson(response.body(), type);

        assertEquals(List.of(newSubtask), responseSubtask);

        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        type = new TypeToken<List<Subtask>>(){}.getType();
        List<Subtask> responseEmptySubtask = gson.fromJson(response.body(), type);

        assertEquals(EMPTY_LIST, responseEmptySubtask);

    }
    @Test
    void shouldReturnSubtasksOfEpic() throws IOException, InterruptedException {
        Epic newEpic = manager.createEpic(epic);
        String jsonNewEpic = gson.toJson(newEpic);
        var body = HttpRequest.BodyPublishers.ofString(jsonNewEpic);
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = newSubtask(newEpic);
        Subtask newSubtask = manager.createSubtask(subtask);
        Subtask subtask1 = newSubtask(newEpic);
        Subtask newSubtask1 = manager.createSubtask(subtask1);
        String jsonNewSubtask = gson.toJson(newSubtask);
        body = HttpRequest.BodyPublishers.ofString(jsonNewSubtask);

        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        jsonNewSubtask = gson.toJson(newSubtask1);
        body = HttpRequest.BodyPublishers.ofString(jsonNewSubtask);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + newEpic.getId());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var type = new TypeToken<List<Subtask>>(){}.getType();
        List<Subtask> responseSubtask = gson.fromJson(response.body(), type);

        assertEquals(List.of(newSubtask, newSubtask1), responseSubtask);
    }
}
