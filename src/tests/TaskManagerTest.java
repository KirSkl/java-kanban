package tests;

import exceptions.IntersectionException;
import manager.TaskManager;
import tasks.*;
import static tasks.StatusOfTask.*;
import static tasks.TypeOfTask.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.EMPTY_LIST;

abstract class TaskManagerTest <T extends TaskManager> {

    T manager;
    final Task task = new tasks.Task("Первая задача", "ОБЫЧНАЯ", NEW, TASK, Instant.EPOCH, 1);
    final Task task1 = new tasks.Task("Вторая задача", "ОБЫЧНАЯ", NEW, TASK, Instant.EPOCH, 1);
    final Task task2 = new tasks.Task("Третья задача", "ОБЫЧНАЯ", NEW, TASK,
                                                    Instant.EPOCH.plusSeconds(100), 1);
    final Epic epic = new tasks.Epic("Первый эпик", "обычный", NEW, EPIC, Instant.EPOCH, 0 );
    final Epic epic1 = new tasks.Epic("Второй эпик", "обычный", NEW, EPIC, Instant.EPOCH, 0);

    Subtask newSubtask(Epic epic) {
        return new Subtask("Первая подзадача", "Необычная", NEW,
                epic.getId(), SUBTASK, Instant.EPOCH, 1);
    }
    @Test
    void shouldReturnCorrectId() {
        manager.createTask(task);
        manager.createTask(task1);

        assertEquals(1, manager.getAllTasks().get(0).getId(), "неправильный ID");
        assertEquals(2, manager.getAllTasks().get(1).getId(), "неправильный ID");
    }
    @Test
    void returnEmptyHistoryTest() {
        assertEquals(EMPTY_LIST, manager.getHistory());
    }
    @Test
    void returnHistoryWithTasksTest() {
        Task newTask = manager.createTask(task);
        Epic newEpic = manager.createEpic(epic);
        Subtask newSubtask = manager.createSubtask(newSubtask(newEpic));

        manager.getTaskById(newTask.getId());
        manager.getEpicById(newEpic.getId());
        manager.getSubtaskById(newSubtask.getId());

        assertEquals(List.of(newTask, newEpic, newSubtask), manager.getHistory());
    }
    @Test
    void getAllTasks() {
        Task newTask = manager.createTask(task);
        Task newTask1 = manager.createTask(task1);

        assertEquals(List.of(newTask, newTask1), manager.getAllTasks());
    }
    @Test
    void getAllTasksIfListOfTasksIsEmpty(){
        assertEquals(EMPTY_LIST, manager.getAllTasks());
    }
    @Test
    void getAllEpics() {
        Epic newEpic = manager.createEpic(epic);
        Epic newEpic1 = manager.createEpic(epic);

        assertEquals(List.of(newEpic, newEpic1), manager.getAllEpics());
    }
    @Test
    void getAllEpicsIfListOfEpicsIsEmpty() {
        assertEquals(EMPTY_LIST, manager.getAllEpics());
    }
    @Test
    void getAllSubtasks() {
        Epic newEpic = manager.createEpic(epic);
        Epic newEpic1 = manager.createEpic(epic1);

        Subtask newSubtask = manager.createSubtask(newSubtask(newEpic));
        Subtask newSubtask1 = manager.createSubtask(newSubtask(newEpic1));

        assertEquals(List.of(newSubtask, newSubtask1), manager.getAllSubtasks());
    }
    @Test
    void getAllSubtaskIfListOfSubtaskIsEmpty() {
        assertEquals(EMPTY_LIST, manager.getAllSubtasks());
    }
    @Test
    void deleteAllTasks() {
        manager.createTask(task);
        manager.createTask(task1);

        manager.deleteAllTasks();

        assertEquals(EMPTY_LIST, manager.getAllTasks());
    }
    @Test
    void deleteAllTasksIfListOfTasksIsEmpty() {
        manager.deleteAllTasks();

        assertEquals(EMPTY_LIST, manager.getAllTasks());
    }
    @Test
    void deleteAllSubtasks() {
        Epic newEpic = manager.createEpic(epic);
        Epic newEpic1 = manager.createEpic(epic1);

        manager.createSubtask(newSubtask(newEpic));
        manager.createSubtask(newSubtask(newEpic1));

        manager.deleteAllSubtasks();
        assertEquals(EMPTY_LIST, manager.getAllSubtasks());
    }
    @Test
    void deleteAllSubtasksIfListOfSubtaskIsEmpty() {
        manager.deleteAllSubtasks();
        assertEquals(EMPTY_LIST, manager.getAllSubtasks());
    }
    @Test
    void deleteAllEpics() {
        manager.createEpic(epic);
        manager.createEpic(epic1);

        manager.deleteAllEpics();

        assertEquals(EMPTY_LIST, manager.getAllEpics());
    }
    @Test
    void deleteAllEpicsIfListOfEpicsIsEmpty() {
        manager.deleteAllEpics();

        assertEquals(EMPTY_LIST, manager.getAllEpics());
    }
    @Test
    void getTaskById() {
        Task newTask = manager.createTask(task);
        Task newTask1 = manager.createTask(task1);

        assertEquals(newTask, manager.getTaskById(1));
        assertEquals(newTask1, manager.getTaskById(2));
    }
    @Test
    void getTaskByIncorrectId() {
        Task newTask = manager.createTask(task);

        assertNull(manager.getTaskById(100));
    }
    @Test
    void getEpicById() {
        Epic newEpic = manager.createEpic(epic);
        Epic newEpic1 = manager.createEpic(epic1);

        assertEquals(newEpic, manager.getEpicById(1));
        assertEquals(newEpic1, manager.getEpicById(2));
    }
    @Test
    void getEpicByIncorrectId () {
        Epic newEpic = manager.createEpic(epic);

        assertNull(manager.getEpicById(100));
    }
    @Test
    void getSubtaskById() {
        Epic newEpic = manager.createEpic(epic);
        Subtask subtask = manager.createSubtask(newSubtask(newEpic));
        Subtask subtask1 = manager.createSubtask(newSubtask(newEpic));

        assertEquals(subtask, manager.getSubtaskById(2));
        assertEquals(subtask1, manager.getSubtaskById(3));
    }
    @Test
    void getSubtaskByIncorrectId() {
        Epic newEpic = manager.createEpic(epic);
        Subtask subtask = manager.createSubtask(newSubtask(newEpic));

        assertNull(manager.getSubtaskById(100));
    }
    @Test
    void removeTaskById() {
        Task newTask = manager.createTask(task);

        manager.removeTaskById(newTask.getId());

        assertNull(manager.getTaskById(newTask.getId()));
    }
    @Test
    void removeTaskByIncorrectId() {
        Task newTask = manager.createTask(task);

        manager.removeTaskById(100);

        assertEquals(1, manager.getAllTasks().size());
    }
    @Test
    void removeEpicById() {
        Epic newEpic = manager.createEpic(epic);

        manager.removeEpicById(newEpic.getId());

        assertNull(manager.getEpicById(newEpic.getId()));
    }
    @Test
    void removeEpicByIncorrectId() {
        Epic newEpic = manager.createEpic(epic);

        manager.removeEpicById(100);

        assertEquals(1, manager.getAllEpics().size());
    }
    @Test
    void removeSubtaskById() {
        Epic newEpic = manager.createEpic(epic);
        Subtask subtask = manager.createSubtask(newSubtask(newEpic));

        manager.removeSubtaskById(subtask.getId());

        assertNull(manager.getSubtaskById(subtask.getId()));
    }
    @Test
    void removeSubtaskByIncorrectId() {
        Epic newEpic = manager.createEpic(epic);
        manager.createSubtask(newSubtask(newEpic));

        manager.removeSubtaskById(100);

        assertEquals(1, manager.getAllSubtasks().size());
    }

    @Test
    void createTask() {
        manager.createTask(task);

        assertEquals(1, manager.getAllTasks().size());
    }
    @Test
    void createEpic() {
        manager.createEpic(epic);

        assertEquals(1, manager.getAllEpics().size());
    }
    @Test
    void createSubtask() {
        Epic newEpic = manager.createEpic(epic);
        Subtask subtask = manager.createSubtask(newSubtask(newEpic));

        assertEquals(1, manager.getAllSubtasks().size());
        assertEquals(epic, manager.getEpicById(manager.getSubtaskById(subtask.getId()).getIdEpic()));
    }
    @Test
    void updateTask() {
        Task newTask = manager.createTask(task);

        newTask.setStatus(DONE);
        manager.updateTask(newTask);

        assertEquals(DONE, manager.getTaskById(newTask.getId()).getStatus());
    }
    @Test
    void updateEpic() {
        Epic newEpic = manager.createEpic(epic);

        newEpic.setStatus(DONE);
        manager.updateEpic(newEpic);

        assertEquals(DONE, manager.getEpicById(newEpic.getId()).getStatus());
    }
    @Test
    void updateSubtask() {
        Epic newEpic = manager.createEpic(epic);
        Subtask subtask = manager.createSubtask(newSubtask(newEpic));

        subtask.setStatus(DONE);
        manager.updateSubtask(subtask);

        assertEquals(DONE, manager.getSubtaskById(subtask.getId()).getStatus());
    }
    @Test
    void getSubtasksOfEpic() {
        Epic newEpic = manager.createEpic(epic);
        Subtask subtask = manager.createSubtask(newSubtask(newEpic));
        Subtask subtask1 = manager.createSubtask(newSubtask(newEpic));
        List<Subtask> subtasks = new ArrayList<>();

        subtasks.add(subtask);
        subtasks.add(subtask1);

        assertEquals(subtasks, manager.getSubtasksOfEpic(epic));
    }
    @Test
    void changeEpicStatus() {
        Epic newEpic = manager.createEpic(epic);

        assertEquals(NEW, manager.getEpicById(newEpic.getId()).getStatus());

        Subtask subtask = manager.createSubtask(newSubtask(newEpic));
        Subtask subtask1 = manager.createSubtask(newSubtask(newEpic));

        assertEquals(NEW, manager.getEpicById(newEpic.getId()).getStatus());

        subtask.setStatus(DONE);
        manager.updateSubtask(subtask);

        assertEquals(IN_PROGRESS, manager.getEpicById(newEpic.getId()).getStatus());

        subtask1.setStatus(DONE);
        manager.updateSubtask(subtask1);

        assertEquals(DONE, manager.getEpicById(newEpic.getId()).getStatus());

        subtask.setStatus(IN_PROGRESS);
        subtask1.setStatus(IN_PROGRESS);
        manager.updateSubtask(subtask);
        manager.updateSubtask(subtask1);

        assertEquals(IN_PROGRESS, manager.getEpicById(newEpic.getId()).getStatus());
    }
    @Test
    void changeEpicTimeTest() {
        Epic newEpic = manager.createEpic(epic);
        Subtask subtask = manager.createSubtask(newSubtask(newEpic));

        assertEquals(subtask.getDuration(), epic.getDuration());

        Subtask subtask1 = manager.createSubtask(newSubtask(newEpic));

        subtask1.setStartTime(Instant.EPOCH.plusSeconds(1));
        manager.updateSubtask(subtask1);

        assertEquals(subtask.getDuration()+subtask1.getDuration(), epic.getDuration());
        assertEquals(subtask.getStartTime(), epic.getStartTime());
        assertEquals(subtask1.getEndTime(), epic.getEndTime());
    }
    @Test
    void getAndSetTimeFieldsTest() {
        Task newTask = manager.createTask(task);

        assertEquals(Instant.EPOCH, newTask.getStartTime());
        assertEquals(1, newTask.getDuration());
        assertEquals(Instant.EPOCH.plusSeconds(60), newTask.getEndTime());

        newTask.setDuration(2);
        newTask.setStartTime(Instant.EPOCH.plusSeconds(1));

        assertEquals(Instant.EPOCH.plusSeconds(1), newTask.getStartTime());
        assertEquals(2, newTask.getDuration());
        assertEquals(Instant.EPOCH.plusSeconds(121), newTask.getEndTime());
    }
    @Test
    void shouldReturnPrioritizedList() {
        Task newTask = manager.createTask(task2);
        Task newTask1 = manager.createTask(task);

        assertEquals(List.of(newTask1, newTask), manager.getPrioritizedTasks());
    }
    @Test
    void shouldNotToAddToPrioritizedTasks() {
        Task newTask = manager.createTask(task);
        Task newTask1 = manager.createTask(task1);

        assertEquals(List.of(newTask), manager.getPrioritizedTasks());
    }
}