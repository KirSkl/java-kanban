public class Subtask extends Task {



    private int idEpic;
    public Subtask(String title, String description, String status, int idEpic) {
        super(title, description, status);
        this.idEpic=idEpic;
    }
    public int getIdEpic() {
        return idEpic;
    }
}
