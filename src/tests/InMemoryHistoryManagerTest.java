package tests;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import static tasks.StatusOfTask.NEW;
import static tasks.TypeOfTask.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import static java.util.Collections.EMPTY_LIST;

public class InMemoryHistoryManagerTest {

    HistoryManager manager;

    final Task task = new tasks.Task(1,"Первая задача", "ОБЫЧНАЯ", NEW, TASK, Instant.EPOCH,
            Duration.ZERO);
    final Epic epic = new tasks.Epic(2,"Первый эпик", "обычный", NEW, EPIC, Instant.EPOCH,
            Duration.ZERO);
    Subtask newSubtask(Epic epic) {
        return new Subtask(3, "Первая подзадача", "Необычная", NEW,
                epic.getId(), SUBTASK, Instant.EPOCH, Duration.ofMinutes(1));
    }
    @BeforeEach
    void createNewManager() {
        manager = new InMemoryHistoryManager();
    }
    @Test
    void addTest () {
        manager.add(task);
        manager.add(epic);
        Subtask subtask  = newSubtask(epic);
        manager.add(subtask);

        assertEquals(List.of(task, epic, subtask), manager.getHistory());
    }
    @Test
    void removeTest() {
        manager.add(task);
        manager.add(epic);
        Subtask subtask  = newSubtask(epic);
        manager.add(subtask);

        manager.remove(task.getId());//удаление из начала

        assertEquals(List.of(epic, subtask), manager.getHistory());

        manager.add(task);

        manager.remove(subtask.getId());//удаление из середины

        assertEquals(List.of(epic, task), manager.getHistory());

        manager.add(subtask);

        manager.remove(subtask.getId());//удаление из конца

        assertEquals(List.of(epic, task), manager.getHistory());

        manager.remove(task.getId());
        manager.remove((epic.getId()));
        manager.remove(subtask.getId());//пустая история

        assertEquals(EMPTY_LIST, manager.getHistory());
    }
    @Test
    void historyWithoutDuplicatesTest() {
        manager.add(task);
        manager.add(epic);
        Subtask subtask  = newSubtask(epic);
        manager.add(subtask);
        manager.add(task);
        manager.add(epic);
        manager.add(subtask);
        manager.add(task);
        manager.add(epic);
        manager.add(subtask);

        assertEquals(List.of(task, epic, subtask), manager.getHistory());
    }
}
