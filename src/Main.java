public class Main {

    public static void main(String[] args) {

     Task task = new Task("test", "cool", "new");
     System.out.println(task.getId());
     Subtask subtask = new Subtask("subtest", "cooler", "new");
        System.out.println(subtask.getId());
    }
}
