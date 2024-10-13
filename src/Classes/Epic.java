package Classes;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class Epic extends Task{
    private HashMap<Integer, Subtask> epicSubtasks;

     public Epic(String title, String description){
         super(title, description);
         epicSubtasks =  new HashMap<>();
     }

     public HashMap<Integer, Subtask> getSubMap(){
         return epicSubtasks;
     }
     public Subtask getSubTask(int id){
         return epicSubtasks.get(id);
     }

    @Override
    public String toString(){
        return "Epic: " + this.getTitle() + " [id:" + this.getId() + ", status:" + this.getStatus() + ", number of subs:" + getSubMap().size() + "]";
    }
}
