package tests;

import manager.FileBackedTasksManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import static java.util.Collections.EMPTY_LIST;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void createNewManager() {
        manager = new FileBackedTasksManager();
    }
    @Test
    protected void saveAndLoadTest() {
        manager.save(); //пустой список задач и пустая история
        manager = FileBackedTasksManager.loadFromFile();
        assertEquals(EMPTY_LIST, manager.getAllTasks());

        var newTask = manager.createTask(task);
        var newEpic = manager.createEpic(epic);
        var subtask = manager.createSubtask(newSubtask(newEpic));

        manager = FileBackedTasksManager.loadFromFile(); //пустая история
        assertEquals(EMPTY_LIST, manager.getHistory());
        //обычная работа
        manager.getTaskById(newTask.getId());
        manager.getEpicById(newEpic.getId());
        manager.getSubtaskById(subtask.getId());

        manager = FileBackedTasksManager.loadFromFile();

        assertEquals(List.of(subtask), manager.getAllSubtasks());
        assertEquals(List.of(newTask), manager.getAllTasks());
        assertEquals(List.of(newEpic), manager.getAllEpics());
        assertEquals(manager.getHistory(), List.of(newTask, newEpic, subtask));
        //эпик без подзадач
        manager.removeSubtaskById(subtask.getId());
        newEpic.clearIds();

        manager = FileBackedTasksManager.loadFromFile();

        assertEquals(List.of(newEpic), manager.getAllEpics());
        assertEquals(EMPTY_LIST, manager.getAllSubtasks());
        //после очистки всех задач
        manager.deleteAllEpics();
        manager.deleteAllTasks();

        manager = FileBackedTasksManager.loadFromFile();

        assertEquals(EMPTY_LIST, manager.getAllTasks());
        assertEquals(EMPTY_LIST, manager.getAllEpics());
        assertEquals(EMPTY_LIST, manager.getAllSubtasks());
        assertEquals(EMPTY_LIST, manager.getHistory());
    }
}
