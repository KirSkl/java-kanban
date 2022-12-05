import java.util.ArrayList;

public class Epic extends Task{

        public Epic(String title, String description, String status) {
        super(title, description, status);
        this.status = "NEW";
    }

    ArrayList<Integer> ides = new ArrayList<>();

    public void setIdes(ArrayList<Integer> ides, int id) {
        this.ides.add(id);
    }
    public ArrayList<Integer> getIdes() {
        return ides;
    }


}
