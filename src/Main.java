import manager.InMemoryTaskManager;
import tasks.Task;

import java.util.Scanner;

import static tasks.StatusOfTask.*;

public class Main {

    public static void main(String[] args) {

        //просто тесты
        /*InMemoryTaskManager manager = new InMemoryTaskManager();

        tasks.Task task = new tasks.Task("Первая задача", "ОБЫЧНАЯ", NEW, manager);
        manager.createTask(task);
        tasks.Task task1 = new tasks.Task("Вторая задача", "интересная", NEW, manager);
        manager.createTask(task1);
        tasks.Epic epic = new tasks.Epic("Первый эпик", "обычный", NEW, manager);
        manager.createEpic(epic);
        tasks.Subtask subtask = new tasks.Subtask("Первая подзадача", "Необычная", NEW,
                epic.getId(), manager);

        manager.createSubtask(subtask);
        tasks.Subtask subtask1 = new tasks.Subtask("Вторая подзадача", "Скучная",
                IN_PROGRESS, epic.getId(), manager);

        manager.createSubtask(subtask1);
        manager.getTaskById(0);
        System.out.println(manager.getEpicById(epic.getId()).getTitle());
        System.out.println(manager.getEpicById(epic.getId()).getTitle());
        System.out.println(manager.getSubtaskById(subtask1.getId()).getTitle());
        System.out.println(manager.getSubtaskById(subtask.getId()).getTitle());
        System.out.println(manager.getEpicById(epic.getId()).getTitle());




        if (!manager.getHistory().isEmpty()) {
            for (int i = 0; i < manager.getHistory().size(); i++) {


                System.out.println(manager.getHistory().get(i).getTitle());
            }
        }*/
    }
}
