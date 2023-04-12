package tests;

import manager.HttpTaskManager;
import manager.Managers;

import org.junit.jupiter.api.*;
import servers.KVServer;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.jupiter.api.Assertions.*;

import static tasks.StatusOfTask.NEW;
import static tasks.TypeOfTask.*;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kvServer;
    @BeforeEach
     void startServer() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getHttpTaskManager();
    }
    @AfterEach
    void closeServer() {
        kvServer.stop();
    }
    @Test
    void saveAndLoadTest() throws IOException, InterruptedException {
        HttpTaskManager manager = Managers.getHttpTaskManager();
        manager.save(); //пустой список задач и пустая история
        manager.load();

        assertEquals(EMPTY_LIST, manager.getAllTasks());

        var newTask = manager.createTask(task);
        var newEpic = manager.createEpic(epic);
        var subtask = manager.createSubtask(newSubtask(newEpic));
        manager.load(); //пустая история

        assertEquals(EMPTY_LIST, manager.getHistory());
        //обычная работа
        manager.getTaskById(newTask.getId());
        manager.getEpicById(newEpic.getId());
        manager.getSubtaskById(subtask.getId());
        manager.load();

        assertEquals(List.of(subtask), manager.getAllSubtasks());
        assertEquals(List.of(newTask), manager.getAllTasks());
        assertEquals(List.of(newEpic), manager.getAllEpics());

        Task newEpicToTask = new Task(newEpic); //сделал дополнительные конструкторы, потому что
        Task subtaskToTask = new Task(subtask); // автоматическое приведение типов не работало

        assertEquals(List.of(newTask, newEpicToTask, subtaskToTask), manager.getHistory());
        //эпик без подзадач
        manager.removeSubtaskById(subtask.getId());
        newEpic.clearIds();
        manager.load();

        assertEquals(List.of(newEpic), manager.getAllEpics());
        assertEquals(EMPTY_LIST, manager.getAllSubtasks());
        //после очистки всех задач
        manager.deleteAllEpics();
        manager.deleteAllTasks();
        manager.load();

        assertEquals(EMPTY_LIST, manager.getAllTasks());
        assertEquals(EMPTY_LIST, manager.getAllEpics());
        assertEquals(EMPTY_LIST, manager.getAllSubtasks());
        assertEquals(EMPTY_LIST, manager.getHistory());
    }
}