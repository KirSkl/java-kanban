package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id=0;
    static final String IN_PROGRESS = "IN_PROGRESS";
    static final String DONE = "DONE";
    static final String NEW = "NEW";

    public int getId() {
        return id++;
    }

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    //методы для получения списка задач
    public ArrayList<Task> getAllTasks(){
        return new ArrayList<>(tasks.values());
    }
    public ArrayList<Epic> getAllEpics(){
        return new ArrayList<>(epics.values());
    }
    public ArrayList<Subtask> getAllSubtasks(){
        return new ArrayList<>(subtasks.values());
    }
    // методы для удаления
    public void deleteAllTasks(){
        tasks.clear();
    }
    public void deleteAllSubtasks(){
        subtasks.clear();
        for(Epic epic: epics.values()) { //удалить все айди из эпиков
            epic.clearIds();
            epic.setStatus(NEW);//точно не понял, какой должен быть статус у эпика, у которого нет подзадач,
            // но вроде так?
        }

    }
    public void deleteAllEpics(){
        epics.clear();
        subtasks.clear();
    }
    //методы для получения по айди
    public Task getTaskById(int id){
        return tasks.get(id);
    }
    public Epic getEpicById(int id){
        return epics.get(id);
    }
    public Subtask getSubtaskById(int id){
        return subtasks.get(id);
    }
    // методы для удаления по айди
    public void removeTaskById(int id) {
        tasks.remove(id);
    }
    public void removeEpicById(int id) {
        for (Integer i : epics.get(id).getIds()){ //удаляем подзадачи вместе с эпиком
            subtasks.remove(i);
        }
        epics.remove(id);
    }
    public void removeSubtaskById(int id) {
        //Integer idSubtask=id;
        changeEpicStatus(subtasks.get(id));
        epics.get(subtasks.get(id).getIdEpic()).removeFromIds(id);//удаляю айди субтаска из эпика
        subtasks.remove(id);


    }
    //методы по созданию задач
    public void createTask(Task task){
        tasks.put(task.getId(), task);
    }
    public void createEpic(Epic epic){
        epics.put(epic.getId(), epic);
    }
    public void createSubtask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getIdEpic()).setIds(subtask.getId());//сообщаю эпику айдти субтаска
    }
    //методы по обновлению задач
    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }
    public void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
    }
    public void updateSubtask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
        changeEpicStatus(subtask);
    }
    /*private void changeEpicStatus (Subtask subtask){
        if (subtask.status.equals(IN_PROGRESS)) {
            epics.get(subtask.getIdEpic()).setStatus(IN_PROGRESS);
        } else if (subtask.status.equals(DONE)){
            for (Integer iD : epics.get(subtask.getIdEpic()).ids) { //проверяем другие субтаски
                if (!subtasks.get(iD).getStatus().equals(DONE)) { //если какой-то не done, устанавливаем in progress
                    epics.get(subtask.getIdEpic()).setStatus(IN_PROGRESS);
                } else {
                    epics.get(subtask.getIdEpic()).setStatus(DONE);
                }
            }
        }
    }*/



    /*private void changeEpicStatus (Subtask subtask) {
        for (Integer iD : epics.get(subtask.getIdEpic()).getIds()) {
            if (subtasks.get(iD).getStatus().equals(IN_PROGRESS)) {
                epics.get(subtask.getIdEpic()).setStatus(IN_PROGRESS);
                return;
            } else if (subtasks.get(iD).getStatus().equals(DONE)) {
                epics.get(subtask.getIdEpic()).setStatus(DONE);
                for (Integer id1 : epics.get(subtask.getIdEpic()).getIds()) {//еще раз запускаем цикл, чтобы проверить
                    if (subtasks.get(id1).getStatus().equals(NEW)) { //другие сабтаски
                        epics.get(subtask.getIdEpic()).setStatus(IN_PROGRESS);
                        return;
                    } else return;
                }
            } else {
                epics.get(subtask.getIdEpic()).setStatus(NEW);
            }
        }
    }*/
        private void changeEpicStatus(Subtask subtask){
            for (Integer iD : epics.get(subtask.getIdEpic()).getIds()){
                if (subtasks.get(iD).getStatus().equals(NEW)) {
                    epics.get(subtask.getIdEpic()).setStatus(NEW);
                } else {
                    epics.get(subtask.getIdEpic()).setStatus(IN_PROGRESS);
                    break;
                }
            }
            if (epics.get(subtask.getIdEpic()).getStatus().equals(NEW)){
                return;
            }
            for (Integer iD : epics.get(subtask.getIdEpic()).getIds()) {
                if (subtasks.get(iD).getStatus().equals(DONE)) {
                    epics.get(subtask.getIdEpic()).setStatus(DONE);
                } else {
                    epics.get(subtask.getIdEpic()).setStatus(IN_PROGRESS);
                    break;
                }
            }
        }
    //получение подзадач
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic){ //можно также использовать айди эпика
        ArrayList<Subtask> subtasksOfEpics = new ArrayList<>();
        for (Integer iD : epic.getIds()){
            subtasksOfEpics.add(subtasks.get(iD));
        }
        return subtasksOfEpics;
    }
}
