import manager.FileBackedTasksManager;
import manager.InMemoryTaskManager;
import tasks.Task;

import java.io.File;
import java.util.Scanner;

import static tasks.StatusOfTask.*;
import static tasks.TypeOfTask.*;

public class Main {

    public static void main(String[] args) {

        /*//просто тесты
        InMemoryTaskManager manager = new InMemoryTaskManager();
        File file = new File("test.csv");
        FileBackedTasksManager manager1 = new FileBackedTasksManager(file);

        tasks.Task task = new tasks.Task("Первая задача", "ОБЫЧНАЯ", NEW, manager1, TASK);
        manager1.createTask(task);
        tasks.Task task1 = new tasks.Task("Вторая задача", "интересная", NEW, manager1, TASK);
        manager1.createTask(task1);
        tasks.Epic epic = new tasks.Epic("Первый эпик", "обычный", NEW, manager1, EPIC);
        manager1.createEpic(epic);
        tasks.Subtask subtask = new tasks.Subtask("Первая подзадача", "Необычная", NEW,
                epic.getId(), manager1, SUBTASK);

        manager1.createSubtask(subtask);
        tasks.Subtask subtask1 = new tasks.Subtask("Вторая подзадача", "Скучная",
                IN_PROGRESS, epic.getId(), manager1, SUBTASK);

        manager1.createSubtask(subtask1);
        manager1.getTaskById(0);
        System.out.println(manager1.getEpicById(epic.getId()).getTitle());
        System.out.println(manager1.getEpicById(epic.getId()).getTitle());
        System.out.println(manager1.getSubtaskById(subtask1.getId()).getTitle());
        System.out.println(manager1.getSubtaskById(subtask.getId()).getTitle());
        System.out.println(manager1.getEpicById(epic.getId()).getTitle());




        if (!manager1.getHistory().isEmpty()) {
            for (int i = 0; i < manager1.getHistory().size(); i++) {


                System.out.println(manager1.getHistory().get(i).getTitle());
            }
        }
        FileBackedTasksManager manager2 = Util.loadFromFile(manager1.file);*/
    }
}
