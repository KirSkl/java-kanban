package servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import manager.*;
import tasks.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

import static tasks.StatusOfTask.NEW;
import static tasks.TypeOfTask.TASK;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager manager;
    private final HttpServer server;
    private final Gson gson;

    public HttpTaskServer() throws IOException, InterruptedException {
        manager = Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", this::handleTasks);
        gson = Managers.getGson();
    }
    private void handleTasks(HttpExchange httpExchange) {
        try {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String params = httpExchange.getRequestURI().getQuery();
            switch (method) {
                case "GET":
                    if (Pattern.matches("^/tasks$", path)) {
                        System.out.println("Обработка запроса на получение приоритизированного списка задач...");
                        sendObject(httpExchange, manager.getPrioritizedTasks());
                    } else if (Pattern.matches("/tasks/history$", path)) {
                        System.out.println("Обработка запроса на получение истории...");
                        sendObject(httpExchange, manager.getHistory());
                    } else if (params == null) {
                        if (Pattern.matches("^/tasks/task$", path)) {
                            System.out.println("Обработка запроса на получение списка всех задач...");
                            sendObject(httpExchange, manager.getAllTasks());
                        } else if (Pattern.matches("^/tasks/epic$", path)) {
                            System.out.println("Обработка запроса на получение списка всех эпиков...");
                            sendObject(httpExchange, manager.getAllEpics());
                        } else if (Pattern.matches("^/tasks/subtask$", path)) {
                            System.out.println("Обработка запроса на получение списка всех подзадач...");
                            sendObject(httpExchange, manager.getAllSubtasks());
                        } else {
                            System.out.println("Указан неверный путь для запроса");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        switch (path) {
                            case "/tasks/task" :
                            System.out.println("Обработка запроса на получение списка всех задач...");
                            sendObject(httpExchange, manager.getAllTasks());
                            break;
                            case "/tasks/epic" :
                            System.out.println("Обработка запроса на получение списка всех эпиков...");
                            sendObject(httpExchange, manager.getAllEpics());
                            break;
                            case "/tasks/subtask" :
                            System.out.println("Обработка запроса на получение списка всех подзадач...");
                            sendObject(httpExchange, manager.getAllSubtasks());
                            break;
                            default:
                            System.out.println("Указан неверный путь для запроса");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("id=\\d+$", params)) {
                        String paramsId = params.replaceFirst("id=", "");
                        int id = parseId(paramsId);
                        if (id != -1) {
                            switch (path) {
                                case "/tasks/task/":
                                    System.out.println("Обработка запроса получения задачи по ID=" + id + "...");
                                    sendObject(httpExchange, manager.getTaskById(id));
                                    break;
                                case "/tasks/epic/":
                                    System.out.println("Обработка запроса получения эпика по ID=" + id + "...");
                                    sendObject(httpExchange, manager.getEpicById(id));
                                    break;
                                case "/tasks/subtask/":
                                    System.out.println("Обработка запроса получения подзадачи по ID=" + id + "...");
                                    sendObject(httpExchange, manager.getSubtaskById(id));
                                    break;
                                case "/tasks/subtask/epic/" :
                                    System.out.println(
                                            "Обработка запроса получения подзадач эпика по ID=" + id + "...");
                                    sendObject(httpExchange,
                                            manager.getSubtasksOfEpic(manager.getEpicByIdNoHistory(id)));
                                    break;
                                default:
                                    System.out.println("Указан неверный путь для запроса");
                                    httpExchange.sendResponseHeaders(405, 0);
                            }
                        } else {
                            System.out.println("Получен некорректный ID");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        System.out.println("Указан неверный путь для запроса или неверный ID");
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case "POST":
                    switch (path) {
                        case "/tasks/task" :
                            createOrUpdateTask(httpExchange);
                            break;
                        case "/tasks/subtask" :
                            createOrUpdateSubtask(httpExchange);
                            break;
                        case "/tasks/epic" :
                            createOrUpdateEpic(httpExchange);
                            break;
                        default:
                            System.out.println("Указан неверный путь для запроса.");
                            httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case "DELETE":
                    if (params == null) {
                        switch (path) {
                            case "/tasks/task" :
                                System.out.println("Обработка запроса на удаление списка всех задач...");
                                manager.deleteAllTasks();
                                httpExchange.sendResponseHeaders(200, 0);
                                break;
                            case "/tasks/epic" :
                                System.out.println("Обработка запроса на удаление списка всех эпиков...");
                                manager.deleteAllEpics();
                                httpExchange.sendResponseHeaders(200, 0);
                                break;
                            case "/tasks/subtask" :
                                System.out.println("Обработка запроса на удаление списка всех подзадач...");
                                manager.deleteAllSubtasks();
                                httpExchange.sendResponseHeaders(200, 0);
                                break;
                            default :
                                System.out.println("Указан неверный путь для запроса.");
                                httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("id=\\d+$", params)) {
                        String paramsId = params.replaceFirst("id=", "");
                        int id = parseId(paramsId);
                        if (id != -1) {
                            switch (path) {
                                case "/tasks/task/" :
                                    System.out.println("Обработка запроса удаления задачи по ID=" + id + "...");
                                    manager.removeTaskById(id);
                                    httpExchange.sendResponseHeaders(200, 0);
                                    break;
                                case "/tasks/epic/":
                                    System.out.println("Обработка запроса удаления эпика по ID=" + id + "...");
                                    manager.removeEpicById(id);
                                    httpExchange.sendResponseHeaders(200, 0);
                                    break;
                                case "/tasks/subtask/":
                                    System.out.println("Обработка запроса удаления подзадачи по ID=" + id + "...");
                                    manager.removeSubtaskById(id);
                                    httpExchange.sendResponseHeaders(200, 0);
                                    break;
                                default :
                                System.out.println("Указан неверный путь для запроса");
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        } else {
                            System.out.println("Получен некорректный ID");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        System.out.println("Указан неверный путь для запроса или неверный ID");
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                default:
                    System.out.println("Ожидается метод GET/POST/DELETE. Текущий метод - " + method);
                    httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }
    private int parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат идентификатора");
            return -1;
        }
    }
    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }
    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен на " + PORT + " порту.");
    }
    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
    protected void sendObject(HttpExchange h, Object object) throws IOException {
        String jsonString = gson.toJson(object);
        sendText(h, jsonString);
    }
    protected void createOrUpdateTask(HttpExchange httpExchange) throws IOException {
        var inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), UTF_8);
        Task task = gson.fromJson(body, Task.class);
        boolean isExist = false;
        for (Task oldTask : manager.getAllTasks()) {
            if (task.getId() == oldTask.getId()) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            manager.createTask(task);
            System.out.println("Задача создана");
        } else {
            manager.updateTask(task);
            System.out.println("Задача обновлена");
        }
        httpExchange.sendResponseHeaders(200, 0);
    }
    protected void createOrUpdateEpic(HttpExchange httpExchange) throws IOException {
        var inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);
        boolean isExist = false;
        for (Epic oldEpic : manager.getAllEpics()) {
            if (epic.getId() == oldEpic.getId()) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            manager.createEpic(epic);
            System.out.println("Эпик создан");
        } else {
            manager.updateEpic(epic);
            System.out.println("Эпик обновлен");
        }
        httpExchange.sendResponseHeaders(200, 0);
    }
    protected void createOrUpdateSubtask(HttpExchange httpExchange) throws IOException {
        var inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), UTF_8);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        boolean isExist = false;
        for (Subtask oldSubtask : manager.getAllSubtasks()) {
            if (subtask.getId() == oldSubtask.getId()) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            manager.createSubtask(subtask);
            System.out.println("Подзадача создана");
        } else {
            manager.updateSubtask(subtask);
            System.out.println("Подзадача обновлена");
        }
        httpExchange.sendResponseHeaders(200, 0);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //ТЕСТЫ
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        httpTaskServer.manager.createTask(
                new tasks.Task("Первая задача", "ОБЫЧНАЯ", NEW, TASK, Instant.EPOCH, Duration.ofMinutes(1)));
        httpTaskServer.manager.getTaskById(1);
    }
}
