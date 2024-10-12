package Classes;

public class Subtask extends Task{
    private int motherId;
    public Subtask(String title, String description) {
        super(title, description);
    }

    public void setMotherId(Epic epic){
        motherId = epic.getId();
    }

    public int getMotherId(Epic epic){
        return motherId;
    }

    @Override
    public String toString(){
        return "Subtask: " + this.getTitle() + " [id: " + this.getId() + ", status: " + this.getStatus() + "mother epic id:" + motherId + "]";
    }

}
