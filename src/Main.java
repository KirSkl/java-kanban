import manager.*;
import tasks.Task;
import static manager.StatusOfTask.*;


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
        //System.out.println(manager.getAllTasks().get(0).getStatus());
        manager.getTaskById(0);
        System.out.println(manager.getEpicById(epic.getId()).getDescription());
        System.out.println(manager.getEpicById(epic.getId()).getStatus());
        System.out.println(manager.getSubtaskById(subtask1.getId()).getDescription());


        //System.out.println(manager.getAllTasks().get(1).getId());

        for (Task task2  : manager.) {

            if (task2!=null) {
                System.out.println(task2.getTitle());
            } else System.out.println("null");
        }





        //System.out.println(manager.getSubtasksOfEpic(epic).get(0).getId());



        //tasks.Subtask subtaskUp = new tasks.Subtask("Первая подзадача", "Необычная", StatusOfTask.DONE,
                //epic.getId(), manager);

        //manager.updateSubtask(subtaskUp);

if (!manager.getHistory().isEmpty()) {
            for (Task task2: manager.getHistory()) {

         }*/
    }
}
